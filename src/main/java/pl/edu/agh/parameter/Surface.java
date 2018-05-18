package pl.edu.agh.parameter;

public class Surface extends Parameter<Double> {
    public Surface(Double minValue, Double maxValue) {
        super(minValue, maxValue);
    }

    @Override
    public Boolean isCorrectValue() {
        return getValue() <= getMaxValue() && getValue() >= getMinValue();
    }

    @Override
    String getName() {
        return getClass().getSimpleName();
    }
}