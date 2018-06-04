package pl.edu.agh.db;

import io.jsondb.JsonDBTemplate;
import pl.edu.agh.parameter.ParameterJson;
import pl.edu.agh.parameter.ProcessJson;
import pl.edu.agh.parameter.ProductionInput;
import pl.edu.agh.parameter.ProductionOutput;

import java.io.File;
import java.util.List;

public class DBManager {
    private static DBManager INSTANCE;
    private JsonDBTemplate jsonDb;

    private DBManager() {
        String packageDir = "pl.edu.agh.parameter";
        jsonDb = new JsonDBTemplate(new File("src/main/resources/json").getAbsolutePath(), packageDir);
        if (!jsonDb.collectionExists(ProductionInput.class)) {
            jsonDb.createCollection(ProductionInput.class);
        }
        if (!jsonDb.collectionExists(ProductionOutput.class)) {
            jsonDb.createCollection(ProductionOutput.class);
        }
        if (!jsonDb.collectionExists(ParameterJson.class)) {
            jsonDb.createCollection(ParameterJson.class);
        }
        if (!jsonDb.collectionExists(ProcessJson.class)) {
            jsonDb.createCollection(ProcessJson.class);
        }
    }

    public static DBManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DBManager();
        }
        return INSTANCE;
    }

    public void saveProductionInput(ProductionInput input) {
        jsonDb.insert(input);
    }

    public void saveProductionOutput(ProductionOutput output) {
        jsonDb.insert(output);
    }

    public void saveProcess(ProcessJson processJson) {
        jsonDb.insert(processJson);
    }

    public void saveParameter(ParameterJson parameterJson) {
        jsonDb.insert(parameterJson);
    }

    public List<ProcessJson> findAllProcesses() {
        return jsonDb.findAll(ProcessJson.class);
    }

    public List<ParameterJson> findAllParametersForPid(Long pid) {
        String pidEquality = String.format("/.[pid='%d']", pid);
        return jsonDb.find(pidEquality, ParameterJson.class);
    }

    public List<ProductionInput> findAllInputForPid(Long pid) {
        String pidEquality = String.format("/.[pid='%d']", pid);
        return jsonDb.find(pidEquality, ProductionInput.class);
    }

    public List<ProductionOutput> findAllOutputForPid(Long pid) {
        String pidEquality = String.format("/.[pid='%d']", pid);
        return jsonDb.find(pidEquality, ProductionOutput.class);
    }




}