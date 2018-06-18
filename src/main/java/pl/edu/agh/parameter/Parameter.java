package pl.edu.agh.parameter;

/**
 * Details of parameter for process model.
 * Need to set min, max and optimal value
 * and also name of this parameter to store
 * it in some database or write to file.
 *
 * @param <T> T is value type, recommend to
 *            use simply types
 */
public abstract class Parameter<T> {

    private String parameterName = getClass().getSimpleName();
    private T value;
    private T minValue;
    private T maxValue;

    public abstract Boolean isCorrectValue();

    public Parameter() {
    }

    Parameter(T minValue, T maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getMinValue() {
        return this.minValue;
    }

    public T getMaxValue() {
        return this.maxValue;
    }

    public T getValue() {
        return value;
    }

    public ParameterJson<T> toJson(Long pid, Integer stage) {
        ParameterJson<T> result = new ParameterJson<>();
        result.setId(System.currentTimeMillis());
        result.setPid(pid);
        result.setStage(stage);
        result.setName(this.parameterName);
        result.setValue(this.value);
        result.setMinValue(this.minValue);
        result.setMaxValue(this.maxValue);
        return result;
    }

    public enum Size {
        SMALL, MEDIUM, LARGE;

        public Double getValue() {
            switch (this) {
                case LARGE:
                    return 1.0;
                case MEDIUM:
                    return 0.2;
                case SMALL:
                    return 0.02;
                default:
                    return 0.2;
            }
        }
    }
}