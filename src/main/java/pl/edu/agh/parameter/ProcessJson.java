package pl.edu.agh.parameter;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.io.Serializable;
import org.json.JSONObject;

@Document(collection = "Processes", schemaVersion = "1.0")
public class ProcessJson{
    @Id
    private Long id;
    private String data;

    public ProcessJson(){

    }
    
    public ProcessJson(Long id, String data) {
        this.id = id;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
    
    public JSONObject parseToJSONObject(){
        return new JSONObject(data);
    }
}
