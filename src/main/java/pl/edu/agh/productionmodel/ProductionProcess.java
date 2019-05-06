package pl.edu.agh.productionmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.db.DBManager;
import pl.edu.agh.parameter.*;
import pl.edu.agh.random.IDistGenerator;
import pl.edu.agh.random.NomiGen;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONObject;

public class ProductionProcess {

    private final static Logger logger = LoggerFactory.getLogger(ProductionProcess.class);

    private Temperature temperature = new Temperature(0.0, 5000.0);
    private Volume volume = new Volume(0.5, 20.0);
    private Mass mass = new Mass(0.0, 200000.0);
    private Stiffness stiffness = new Stiffness(0.0, 10.0);
    private Amount amount = new Amount(0.0, 10000.0);
    private Surface surface = new Surface(0.0, 1000000.0);
    private Flexibility flexibility = new Flexibility(0.0, 1000000.0);

    private Temperature outputTemperature = new Temperature(0.0, 5000.0);
    private Surface outputSurface = new Surface(0.0, 1000000.0);
    private Flexibility outputFlexibility = new Flexibility(0.0, 1000000.0);

    private Double size;
    private GeneratorRange generatorRange = new GeneratorRange.Builder().build();

    private IDistGenerator generator;
    private Long pid;
    
    private boolean firstStepCopleated = false;
    private boolean secondStepCopleated = false;
    private boolean thirdStepCopleated = false;
    
    private JSONObject stateJson = new JSONObject();

    private ProductionProcess() {
        pid = System.currentTimeMillis();
        stateJson.put("pid", pid);
//        DBManager.getINSTANCE().saveProcess(new ProcessJson(pid));
    }

    public ProductionProcess(Double targetTemp, Double targetFlex, Double targetSurf) {
        this();
        outputTemperature.setValue(targetTemp);
        outputSurface.setValue(targetSurf);
        outputFlexibility.setValue(targetFlex);
        generator = new NomiGen();
        saveTargetParams();
    }

    public void setTargetParams(Double temp, Double flex, Double surf) {
        outputTemperature.setValue(temp);
        outputSurface.setValue(surf);
        outputFlexibility.setValue(flex);
        saveTargetParams();
    }

    public void setGeneratorRange(GeneratorRange generatorRange) {
        this.generatorRange = generatorRange;
    }

    public void setGenerator(IDistGenerator generator) {
        this.generator = generator;
    }
    
    private void saveTargetParams(){
        stateJson.put("target_temperature", outputTemperature.getValue());
        stateJson.put("target_surface", outputSurface.getValue());
        stateJson.put("target_flexibility", outputFlexibility.getValue());
    }
    
    public JSONObject paramsToJson(){  
        String wjp;
        try{
            wjp = computeWJP().toString();
        }
        catch(Exception e){
            wjp = "null";
        }
        stateJson.put("wjp", wjp);
        
        return stateJson;
    }

    public Double computeWJP() throws Exception{
        if(!firstStepCopleated || !secondStepCopleated || !thirdStepCopleated)
            throw new Exception("Process has not been compleated!");
        
        if (temperature.getValue() <= outputTemperature.getValue()) {
            return (Math.abs(outputSurface.getValue() - surface.getValue())
                    + Math.abs(outputFlexibility.getValue() - flexibility.getValue())) * amount.getValue();
        }

        return (Math.abs(outputTemperature.getValue() - temperature.getValue()) + Math.abs(outputSurface.getValue() - surface.getValue())
                + Math.abs(outputFlexibility.getValue() - flexibility.getValue())) * amount.getValue();
    }

    public void firstStep(Double temp, Double vol, Double mass) throws Exception {
        temperature.setValue(temp);
        volume.setValue(vol);
        this.mass.setValue(mass);
        
        JSONObject params = new JSONObject();
        params.put("in_temperature", temp);
        params.put("in_volume", vol);
        params.put("in_mass", mass);

        if (generator == null) {
            throw new Exception("Generator has not been initialized! Process hes been stopped.");
        }

        temperature.setValue(generator.generate(temp, generatorRange.getTemperatureRange()));
        if (!temperature.isCorrectValue()) {
            throw new Exception("Temperature is out of range! Process hes been stopped.");
        }

        volume.setValue(generator.generate(vol, generatorRange.getVolumeRange()));
        if (!volume.isCorrectValue()) {
            throw new Exception("Volume is out of range! Process hes been stopped.");
        }

        this.mass.setValue(generator.generate(mass, generatorRange.getMassRange()));
        if (!this.mass.isCorrectValue()) {
            throw new Exception("Mass is out of range! Process hes been stopped.");
        }
        
        params.put("out_temperature", temperature.getValue());
        params.put("out_volume", volume.getValue());
        params.put("out_mass", this.mass.getValue());
        stateJson.put("stage_1", params);

        firstStepCopleated = true;
    }

    public void secondStep(Double deltaTemp, Parameter.Size size) throws Exception {
        JSONObject params = new JSONObject();
        params.put("in_deltaTemp", deltaTemp);
        params.put("in_size", size.getValue());

        if (generator == null)
            throw new Exception("Generator has not been initialized! Process hes been stopped.");

        temperature.setValue(generator.generate((temperature.getValue() - deltaTemp), generatorRange.getTemperatureRange()));
        if (!temperature.isCorrectValue())
            throw new Exception("Temperature is out of range! Process hes been stopped.");

        stiffness.setValue(generator.generate(
                ((mass.getValue() / volume.getValue()) / 8000.0) * (1 - (temperature.getValue() / 2000.0)),
                generatorRange.getStiffnessRange()));
        if (!stiffness.isCorrectValue())
            throw new Exception("Stiffness is out of range! Process hes been stopped.");
        
        this.size = size.getValue();

        amount.setValue(Math.floor(volume.getValue() / this.size));
        //amount.setValue(generator.generate(Math.floor(volume.getValue() / size.getValue()) , amount.getGeneratorRange()));
        if (!amount.isCorrectValue())
            throw new Exception("Amount is out of range! Process hes been stopped.");
        
        params.put("out_temperature", temperature.getValue());
        params.put("out_stiffness", stiffness.getValue());
        params.put("out_amount", amount.getValue());
        stateJson.put("stage_2", params);
        
        secondStepCopleated = true; 
    }

    public void thirdStep() throws Exception {
        if (generator == null)
            throw new Exception("Generator has not been initialized! Process hes been stopped.");

        temperature.setValue(generator.generate((temperature.getValue() - 300), generatorRange.getTemperatureRange()));
        if (!temperature.isCorrectValue())
            throw new Exception("Temperature is out of range! Process hes been stopped.");

        surface.setValue(generator.generate((amount.getValue() * (size / 0.002)), generatorRange.getSurfaceRange()));
        if (!surface.isCorrectValue())
            throw new Exception("Surface is out of range! Process hes been stopped.");

        flexibility.setValue(generator.generate((stiffness.getValue() * temperature.getValue()), generatorRange.getFlexibilityRange()));
        if (!flexibility.isCorrectValue())
            throw new Exception("Flexibility is out of range! Process hes been stopped.");
        
        JSONObject params = new JSONObject();
        params.put("out_temperature", temperature.getValue());
        params.put("out_surface", surface.getValue());
        params.put("out_flexibility", flexibility.getValue());
        stateJson.put("stage_3", params);
        
        thirdStepCopleated = true;
    }

    public Double runProcess(Double temp, Double vol, Double mass) throws Exception {
        logger.debug("Production process started! Target parameters: max temperature = " + outputTemperature.getValue()
                + ", targetSurface = " + outputSurface.getValue() + ", flexibility = " + outputFlexibility.getValue());
        firstStep(temp, vol, mass);
        logger.debug("First step successfull! Obtained parameters: temperature = " + temperature.getValue()
                + ", volume = " + volume.getValue() + ", mass = " + this.mass.getValue());
        secondStep(1200.0, Parameter.Size.SMALL);
        logger.debug("Second step successfull! Obtained parameters: temperature = " + temperature.getValue()
                + ", stiffness = " + stiffness.getValue() + ", amount = " + amount.getValue());
        thirdStep();
        logger.debug("Third step successfull! Obtained parameters: temperature = " + temperature.getValue()
                + ", targetSurface = " + surface.getValue() + ", flexibility = " + flexibility.getValue());
        
        return computeWJP();
    }
}
