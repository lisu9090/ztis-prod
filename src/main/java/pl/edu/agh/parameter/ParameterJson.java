package pl.edu.agh.parameter;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Document(collection = "Parameters", schemaVersion = "1.0")
public class ParameterJson<T> implements Serializable {
    @Id
    private Long id;
    private Long pid;
    private Integer stage;
    private String name;
    private T value;
    private T minValue;
    private T maxValue;

    public ParameterJson(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getMinValue() {
        return minValue;
    }

    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }

    public Parameter<T> toObject()  {
        Object object = null;
        try {
            Class<Parameter<T>> clazz = (Class<Parameter<T>>) Class.forName("pl.edu.agh.parameter."+this.name);
            Constructor<?> cons = clazz.getConstructor(this.minValue.getClass(), this.maxValue.getClass()); // nie wiem czy da sie jakos obejsc parametrized type tutaj
            object = cons.newInstance(this.minValue, this.maxValue);
            Method m = object.getClass().getSuperclass().getMethod("setValue", Object.class);
            m.invoke(object, this.value);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException  | InvocationTargetException e) {
            e.printStackTrace();
        }
        return (Parameter<T>) object;
    }
}