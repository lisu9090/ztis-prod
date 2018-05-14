package pl.edu.agh.parameter;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Document(collection = "ProductionInput", schemaVersion = "1.0")
public class ProductionInput implements Serializable{
    @Id
    private Long id;
    private Integer stage;
    private Long pid;
    private List<Parameter> parameters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public ProductionInput(){

    }

    public ProductionInput(Long id, Integer stage, Long pid, List<Parameter> parameters) {
        this.id = id;
        this.stage = stage;
        this.pid = pid;
        this.parameters = parameters;
    }
}