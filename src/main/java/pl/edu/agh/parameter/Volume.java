package pl.edu.agh.parameter;

public class Volume extends Parameter<Double> {

    public Volume(Double minValue, Double maxValue) {
        super(minValue, maxValue);
    }

    @Override
    public Boolean isCorrectValue() {
        return getValue() <= getMaxValue() && getValue() >= getMinValue();
    }
}