package pl.edu.agh.parameter;

public class Mass extends Parameter<Double> {

    public Mass(Double minValue, Double maxValue) {
        super(minValue, maxValue);
    }

    @Override
    String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Boolean isCorrectValue() {
        return getValue() <= getMaxValue() && getValue() >= getMinValue();
    }
}