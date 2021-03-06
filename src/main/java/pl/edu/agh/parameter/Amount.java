package pl.edu.agh.parameter;

public class Amount extends Parameter<Double> {

    public Amount(Double minValue, Double maxValue) {
        super(minValue, maxValue);
    }

    @Override
    public Boolean isCorrectValue() {
        return getValue() <= getMaxValue() && getValue() >= getMinValue();
    }
}