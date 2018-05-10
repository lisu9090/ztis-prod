package pl.edu.agh.parameter;

public class UnivParam extends Parameter<Double> {
    private final ParamType parameterName;
    private Double minValue;
    private Double maxValue;
    private Double generatorRange; //szerokosc przedzialu generowanych danych

    public UnivParam(ParamType parameterName ){
        this.parameterName = parameterName;
        setDefaultMinMaxValuesForType();
        this.generatorRange = 0.0d;
    }

    public UnivParam(ParamType parameterName, Double generatorRange ){
        this.parameterName = parameterName;
        setDefaultMinMaxValuesForType();
        this.generatorRange = generatorRange;
    }
    
    public UnivParam(ParamType parameterName, Double maxValue, Double minValue, Double generatorRange ){
        this.parameterName = parameterName;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.generatorRange = generatorRange;
    }

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

    private void setMinValue(Double minValue) {
        if(minValue<0)
            this.minValue = 0.0d;
        else
            this.minValue = minValue;
    }
    
     private void setMaxValue(Double maxValue) {
        if(maxValue<0)
            this.maxValue = 0.0d;
        else
            this.maxValue = maxValue;
    }

    public void setGeneratorRange(Double generatorRange) {
        this.generatorRange = generatorRange;
    }

    private void setDefaultMinMaxValuesForType(){
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
                    setMaxValue(200000.0);                    
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
        return getValue() >= minValue && getValue() <= maxValue;
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
