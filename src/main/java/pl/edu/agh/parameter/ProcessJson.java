package pl.edu.agh.parameter;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.io.Serializable;

@Document(collection = "Processes", schemaVersion = "1.0")
public class ProcessJson implements Serializable{
    @Id
    private Long id;

    public ProcessJson(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessJson(Long id) {
        this.id = id;
    }
}
