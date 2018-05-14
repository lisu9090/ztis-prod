package pl.edu.agh.parameter;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.io.Serializable;

@Document(collection = "ProductionResult", schemaVersion = "1.0")
public class ProductionOutput implements Serializable{
    @Id
    private Long id;
    private Long pid;
    private Double result;
    private Temperature temperature;
    private Surface surface;
    private Flexibility flexibility;

    public ProductionOutput(){

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

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Surface getSurface() {
        return surface;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    public Flexibility getFlexibility() {
        return flexibility;
    }

    public void setFlexibility(Flexibility flexibility) {
        this.flexibility = flexibility;
    }

    public ProductionOutput(Long id, Long pid, Double result, Temperature temperature, Surface surface, Flexibility flexibility) {
        this.id = id;
        this.pid = pid;
        this.result = result;
        this.temperature = temperature;
        this.surface = surface;
        this.flexibility = flexibility;
    }
}
