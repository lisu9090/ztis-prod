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
public class SizeParam extends Parameter<Double>{

    public SizeParam(){
        setValue(SizeType.small);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
