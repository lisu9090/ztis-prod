package pl.edu.agh.parameter;

public class Stiffness extends Parameter<Double> {

    public Stiffness(Double minValue, Double maxValue) {
        super(minValue, maxValue);
    }

    @Override
    public Boolean isCorrectValue() {
        return getValue() <= getMaxValue() && getValue() >= getMinValue();
    }

}