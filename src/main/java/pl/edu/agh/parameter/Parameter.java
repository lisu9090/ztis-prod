package pl.edu.agh.parameter;

/**
 * Details of parameter for process model.
 * Need to set min, max and optimal value
 * and also name of this parameter to store
 * it in some database or write to file.
 *
 * @param <T> T is value type, recommend to
 *           use simply types
 */
public abstract class Parameter<T> {

    private T value;

    abstract T getMinValue();

    abstract T getMaxValue();

    abstract T getOptimalValue();

    abstract String getParameterName();

    public T getValue(){
        return value;
    }
    public void setValue(T value){
        this.value = value;
    }
}