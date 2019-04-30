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

    private ProductionProcess() {
        pid = System.currentTimeMillis();
        DBManager.getINSTANCE().saveProcess(new ProcessJson(pid));
    }

    public ProductionProcess(Double targetTemp, Double targetFlex, Double targetSurf) {
        this();
        outputTemperature.setValue(targetTemp);
        outputSurface.setValue(targetSurf);
        outputFlexibility.setValue(targetFlex);
        generator = new NomiGen();
    }

    public void setTargetParams(Double temp, Double flex, Double surf) {
        outputTemperature.setValue(temp);
        outputSurface.setValue(surf);
        outputFlexibility.setValue(flex);
    }

    public void setGeneratorRange(GeneratorRange generatorRange) {
        this.generatorRange = generatorRange;
    }

    public void setGenerator(IDistGenerator generator) {
        this.generator = generator;
    }
    
    public JSONObject paramsToJson(){
        JSONObject ret = new JSONObject();
        ret.put("timestamp", System.currentTimeMillis());
        ret.put("pid", pid);
        ret.put("temperature", temperature);
        ret.put("volume", volume);
        ret.put("mass", mass);
        ret.put("stiffness", stiffness);
        ret.put("amount", amount);
        ret.put("size", size);
        ret.put("surface", surface);
        ret.put("flexibility", flexibility);
        ret.put("outputTemperature", outputTemperature);
        ret.put("outputSurface", outputSurface);
        ret.put("outputFlexibility", outputFlexibility);
        String wjp;
        try{
            wjp = computeWJP().toString();
        }
        catch(Exception e){
            wjp = "null";
        }
        ret.put("wjp", wjp);
        
        return ret;
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
    
//        private void saveCurrentStage(int stageNum) {
//        DBManager.getINSTANCE()
//                .saveProductionInput(new ProductionInput(
//                        System.currentTimeMillis(), stageNum, pid,
//                        Stream.of(temperature, flexibility, surface, stiffness, amount, mass, volume)
//                                .map(it -> it.toJson(pid, stageNum))
//                                .collect(Collectors.toList())
//                                .toArray(new ParameterJson[7])));
//    }

    public void firstStep(Double temp, Double vol, Double mass) throws Exception {
        temperature.setValue(temp);
        volume.setValue(vol);
        this.mass.setValue(mass);
//        saveCurrentStage(1);
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

        firstStepCopleated = true;
    }

    public void secondStep(Double deltaTemp) throws Exception {
//        saveCurrentStage(2);
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

        double temporary = temperature.getValue();
        if (temporary < 400)
            size = Parameter.Size.SMALL.getValue();
        else if (temporary < 600)
            size = Parameter.Size.MEDIUM.getValue();
        else
            size = Parameter.Size.LARGE.getValue();

        amount.setValue(Math.floor(volume.getValue() / size));
        //amount.setValue(generator.generate(Math.floor(volume.getValue() / size.getValue()) , amount.getGeneratorRange()));
        if (!amount.isCorrectValue())
            throw new Exception("Amount is out of range! Process hes been stopped.");
        
        secondStepCopleated = true; 
    }

    public void thirdStep() throws Exception {
//        saveCurrentStage(3);

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
        
        thirdStepCopleated = true;
    }

    public Double runProcess(Double temp, Double vol, Double mass) throws Exception {
        //1600.0, 2.0, 16000.0
        logger.debug("Production process started! Target parameters: max temperature = " + outputTemperature.getValue()
                + ", targetSurface = " + outputSurface.getValue() + ", flexibility = " + outputFlexibility.getValue());
        firstStep(temp, vol, mass);
        logger.debug("First step successfull! Obtained parameters: temperature = " + temperature.getValue()
                + ", volume = " + volume.getValue() + ", mass = " + this.mass.getValue());
        secondStep(1200.0);
        logger.debug("Second step successfull! Obtained parameters: temperature = " + temperature.getValue()
                + ", stiffness = " + stiffness.getValue() + ", amount = " + amount.getValue());
        thirdStep();
        logger.debug("Third step successfull! Obtained parameters: temperature = " + temperature.getValue()
                + ", targetSurface = " + surface.getValue() + ", flexibility = " + flexibility.getValue());

        Double wjp = computeWJP();
        DBManager.getINSTANCE().saveProductionOutput(new ProductionOutput(System.currentTimeMillis(), pid, wjp, outputTemperature.toJson(pid, 3), outputSurface.toJson(pid, 3), outputFlexibility.toJson(pid, 3)));
        return wjp;
    }
}
