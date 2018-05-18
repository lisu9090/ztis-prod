package pl.edu.agh.parameter;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.io.Serializable;

@Document(collection = "ProductionResult", schemaVersion = "1.0")
public class ProductionOutput implements Serializable {
    @Id
    private Long id;
    private Long pid;
    private Double result;
    private ParameterJson temperature;
    private ParameterJson surface;
    private ParameterJson flexibility;

    public ProductionOutput() {

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

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public ParameterJson getTemperature() {
        return temperature;
    }

    public void setTemperature(ParameterJson temperature) {
        this.temperature = temperature;
    }

    public ParameterJson getSurface() {
        return surface;
    }

    public void setSurface(ParameterJson surface) {
        this.surface = surface;
    }

    public ParameterJson getFlexibility() {
        return flexibility;
    }

    public void setFlexibility(ParameterJson flexibility) {
        this.flexibility = flexibility;
    }

    public ProductionOutput(Long id, Long pid, Double result, ParameterJson temperature, ParameterJson surface, ParameterJson flexibility) {
        this.id = id;
        this.pid = pid;
        this.result = result;
        this.temperature = temperature;
        this.surface = surface;
        this.flexibility = flexibility;
    }
}
