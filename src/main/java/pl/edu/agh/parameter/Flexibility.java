package pl.edu.agh.parameter;

public class Flexibility extends Parameter<Double> {

    public Flexibility(Double minValue, Double maxValue) {
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