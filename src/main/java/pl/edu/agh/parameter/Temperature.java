package pl.edu.agh.parameter;

public class Temperature extends Parameter<Double> {
    public Temperature(Double minValue, Double maxValue) {
        super(minValue, maxValue);
    }

    @Override
    public Boolean isCorrectValue() {
        return getValue() >= getMaxValue() && getValue() <= getMinValue();
    }

    @Override
    String getName() {
        return getClass().getSimpleName();
    }
}