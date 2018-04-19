/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.parameter;

/**
 *
 * @author Tomek
 */
public class UnivParam extends Parameter<Double> {
    private final ParamType parameterName;
    private Double minValue;
    private Double maxValue;
    private Double generatorRange; //szerokosc przedzialu generowanych danych
    
    //constructors
    public UnivParam(ParamType parameterName ){
        this.parameterName = parameterName;
        setDefaultMinMaxValsForType();
        this.generatorRange = new Double(0);
    }

    public UnivParam(ParamType parameterName, Double generatorRange ){
        this.parameterName = parameterName;
        setDefaultMinMaxValsForType();
        this.generatorRange = generatorRange;
    }
    
    public UnivParam(ParamType parameterName, Double maxValue, Double minValue, Double generatorRange ){
        this.parameterName = parameterName;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.generatorRange = generatorRange;
    }
    
    //gethers
    @Override
    Double getMinValue() {
        return this.minValue;
    }

    @Override
    Double getMaxValue() {
        return this.minValue;
    }

    public Double getGeneratorRange() {
        return generatorRange;
    }

    //setters
    public void setMinValue(Double minValue) {
        if(minValue<0)
            this.minValue = new Double(0);
        else
            this.minValue = minValue;
    }
    
    public void setMaxValue(Double maxValue) {
        if(maxValue<0)
            this.maxValue = new Double(0);
        else
            this.maxValue = maxValue;
    }

    public void setGeneratorRange(Double generatorRange) {
        this.generatorRange = generatorRange;
    }
    
    //domyslne parametry
    public final void setDefaultMinMaxValsForType(){
        switch (parameterName){
            case temperature:
                    setMinValue(0.0);
                    setMaxValue(5000.0);
                break;
                case volume:
                    setMinValue(0.5);
                    setMaxValue(20.0);
                break;
                case mass:
                    setMinValue(0.0);
                    setMaxValue(1000000.0);                    
                break;
                case stiffness:
                    setMinValue(0.0);
                    setMaxValue(10.0);
                break;
                case amount:
                    setMinValue(0.0);
                    setMaxValue(10000.0);
                break;
                case surface:
                    setMinValue(0.0);
                    setMaxValue(1000000.0);
                break;
                case flexibility:
                    setMinValue(0.0);
                    setMaxValue(1000000.0);
                break;
        }
    }
    
    public boolean isCorrectValue(){
        if(getValue() >= minValue && getValue() <= maxValue)
            return true;
        
        return false;
    }
    @Override
    Double getOptimalValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    String getParameterName() {
        return this.parameterName.name();
    }
    
}
