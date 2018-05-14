package pl.edu.agh.db;

import io.jsondb.JsonDBTemplate;
import pl.edu.agh.parameter.*;

import java.util.function.Consumer;

public class DBManager {
    private static DBManager INSTANCE;
    private JsonDBTemplate jsonDb;

    private DBManager() {
        String packageDir = "pl.edu.agh.parameter";
        jsonDb = new JsonDBTemplate(System.getProperty("user.dir"), packageDir);
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
}