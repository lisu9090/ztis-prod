/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.productionmodel;

import java.util.ArrayList;
import java.util.List;
import pl.edu.agh.parameter.ParamType;
import pl.edu.agh.parameter.SizeParam;
import pl.edu.agh.parameter.SizeType;
import pl.edu.agh.parameter.UnivParam;
import pl.edu.agh.random.IDistGenerator;
import pl.edu.agh.random.NomiGen;


/**
 *
 * @author Tomek
 */
public class ProductionProcess {
    public List<UnivParam> parameters = new ArrayList<>();
    public List<UnivParam> targetParams = new ArrayList<>();
    public SizeParam size;
    private IDistGenerator generator;

    public ProductionProcess(){
        System.out.println("Konstruktor bezparametrowy do testow. \nNalezy recznie zainicjalizowac wszystkie parametry");
    }
    
    public ProductionProcess(Double targetTemp, Double targetSurf, Double targetFlex){
        parameters.add(new UnivParam(ParamType.temperature)); //0
        parameters.add(new UnivParam(ParamType.volume)); //1
        parameters.add(new UnivParam(ParamType.mass)); //2 
        parameters.add(new UnivParam(ParamType.stiffness)); //3
        parameters.add(new UnivParam(ParamType.amount)); //4
        parameters.add(new UnivParam(ParamType.surface)); //5
        parameters.add(new UnivParam(ParamType.flexibility)); //6        
        size = new SizeParam();
        targetParams.add(new UnivParam(ParamType.temperature)); //0
        targetParams.add(new UnivParam(ParamType.surface)); //1
        targetParams.add(new UnivParam(ParamType.flexibility)); //2
        targetParams.get(0).setValue(targetTemp);
        targetParams.get(1).setValue(targetSurf);
        targetParams.get(2).setValue(targetFlex);
        generator = new NomiGen();
    }
    
    public void setTargetParams(Double temp, Double surf, Double flex){
        targetParams.get(0).setValue(temp);
        targetParams.get(1).setValue(surf);
        targetParams.get(2).setValue(flex);        
    }
    
    public void setGenerator(IDistGenerator generator){
        this.generator = generator;
    }
    
    public void disableGenerator(){
        for(int i=0; i<parameters.size(); i++){
            parameters.get(i).setGeneratorRange(0.0);
        }
    }
    
    private Double computeWJP(){
        if(parameters.get(0).getValue() <= targetParams.get(0).getValue()){
            return (Math.abs(targetParams.get(1).getValue() - parameters.get(5).getValue())
                    + Math.abs(targetParams.get(2).getValue() - parameters.get(6).getValue())) * parameters.get(4).getValue();
        }
        
        return (Math.abs(targetParams.get(0).getValue() - parameters.get(0).getValue()) + Math.abs(targetParams.get(1).getValue() - parameters.get(5).getValue())
                    + Math.abs(targetParams.get(2).getValue() - parameters.get(6).getValue())) * parameters.get(4).getValue();
    }
    
    public void firstStep(Double temp, Double vol, Double mass) throws Exception{
        if(generator == null)
            throw new Exception("Generator has not been initialized! Process hes been stopped.");           
        
        parameters.get(0).setValue(generator.generate(temp, parameters.get(0).getGeneratorRange()));
        if(!parameters.get(0).isCorrectValue())
            throw new Exception("Temperature is out of range! Process hes been stopped.");
        
        parameters.get(1).setValue(generator.generate(vol, parameters.get(1).getGeneratorRange()));
        if(!parameters.get(1).isCorrectValue())
            throw new Exception("Volume is out of range! Process hes been stopped.");
        
        parameters.get(2).setValue(generator.generate(mass , parameters.get(2).getGeneratorRange()));
        if(!parameters.get(2).isCorrectValue())
            throw new Exception("Mass is out of range! Process hes been stopped.");
        
    }
    
    public void secondStep(Double deltaTemp) throws Exception{
        if(generator == null)
            throw new Exception("Generator has not been initialized! Process hes been stopped.");
        
        parameters.get(0).setValue(generator.generate((parameters.get(0).getValue() - deltaTemp), parameters.get(0).getGeneratorRange()));
        if(!parameters.get(0).isCorrectValue())
            throw new Exception("Temperature is out of range! Process hes been stopped.");
        
        parameters.get(3).setValue(generator.generate(
                ((parameters.get(2).getValue() / parameters.get(1).getValue()) / 8000.0) * (1 - (parameters.get(0).getValue() / 2000.0)),
                parameters.get(3).getGeneratorRange()));
        if(!parameters.get(3).isCorrectValue())
            throw new Exception("Stiffness is out of range! Process hes been stopped.");
        
        double temporary = parameters.get(0).getValue();
        if(temporary < 400)
            size.setValue(SizeType.small);
        else if (temporary < 600)
            size.setValue(SizeType.medium);
        else
            size.setValue(SizeType.large);
        
        parameters.get(4).setValue(Math.floor(parameters.get(1).getValue() / size.getValue()));
        //parameters.get(4).setValue(generator.generate(Math.floor(parameters.get(1).getValue() / size.getValue()) , parameters.get(4).getGeneratorRange()));
        if(!parameters.get(4).isCorrectValue())
            throw new Exception("Amount is out of range! Process hes been stopped.");  
    }
    
    public void thirdStep() throws Exception{
        if(generator == null)
            throw new Exception("Generator has not been initialized! Process hes been stopped.");
        
        parameters.get(0).setValue(generator.generate((parameters.get(0).getValue() - 300), parameters.get(0).getGeneratorRange()) );
        if(!parameters.get(0).isCorrectValue())
            throw new Exception("Temperature is out of range! Process hes been stopped.");
        
        parameters.get(5).setValue(generator.generate((parameters.get(4).getValue() * (size.getValue()/0.002)), parameters.get(5).getGeneratorRange()));
        if(!parameters.get(5).isCorrectValue())
            throw new Exception("Surface is out of range! Process hes been stopped.");
        
        parameters.get(6).setValue(generator.generate((parameters.get(3).getValue() * parameters.get(0).getValue()), parameters.get(6).getGeneratorRange()));
        if(!parameters.get(6).isCorrectValue())
            throw new Exception("Flexibility is out of range! Process hes been stopped.");
    }
    
    public Double runProcess(Double temp, Double vol, Double mass) throws Exception{
        //1600.0, 2.0, 16000.0 
        System.out.println("Production process started! Target parameters: max temperature = " + targetParams.get(0).getValue() 
                + ", surface = " + targetParams.get(1).getValue() + ", flexibility = " + targetParams.get(2).getValue());
        firstStep(temp, vol, mass);
        System.out.println("First step successfull! Obtained parameters: temperature = " + parameters.get(0).getValue() 
                + ", volume = " + parameters.get(1).getValue() + ", mass = " + parameters.get(2).getValue());
        secondStep(1200.0);
        System.out.println("Second step successfull! Obtained parameters: temperature = " + parameters.get(0).getValue() 
                + ", stiffness = " + parameters.get(3).getValue() + ", amount = " + parameters.get(4).getValue());
        thirdStep();
        System.out.println("Third step successfull! Obtained parameters: temperature = " + parameters.get(0).getValue() 
                + ", surface = " + parameters.get(5).getValue() + ", flexibility = " + parameters.get(6).getValue());
        
        return computeWJP();
    }
}
