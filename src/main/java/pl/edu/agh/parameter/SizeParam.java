package pl.edu.agh.parameter;

public class SizeParam extends Parameter<Double>{

    public SizeParam(){
        this(SizeType.small);
    }
    
    public SizeParam(SizeType size){
        setValue(size);
    }
    
    @Override
    Double getMinValue() {
        return 0.02;
    }

    @Override
    Double getMaxValue() {
            return 1.0;
    }

    @Override
    Double getOptimalValue() {
        return 0.2d;
    }

    @Override
    String getParameterName() {
        return "SizeParam";
    }

    public final void setValue(SizeType size) {
        switch (size){
            case small:
                setValue(getMinValue());
                break;
            case medium:
                setValue(0.2);
                break;
            case large:
                setValue(getMaxValue());
                break;
        } 
    }
}
