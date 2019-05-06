package pl.edu.agh.ui.controller;


import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.agents.InterfaceUI;
import pl.edu.agh.db.DBManager;
import pl.edu.agh.parameter.ProcessJson;
import pl.edu.agh.parameter.ProductionInput;
import pl.edu.agh.parameter.ProductionOutput;
import pl.edu.agh.random.MainContainer;
import pl.edu.agh.ui.controller.details.ProcessDetailsController;
import pl.edu.agh.ui.util.ProcessDetailsWrapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.Button;
import org.json.JSONObject;

public class DatabaseTabPageController {

    private final static Logger logger = LoggerFactory.getLogger(DatabaseTabPageController.class);

    @FXML
    private AnchorPane dbStage;

    @FXML
    private TableView<ProcessDetailsWrapper> databaseView;

    private static DatabaseTabPageController instance;

    private ObservableList<ProcessDetailsWrapper> observableList;
    
    @FXML
    public Button refreshDataButton;

    @FXML
    public void initialize() {
        instance = this;
        logger.info("init");
        TableColumn<ProcessDetailsWrapper, LocalDateTime> pidColumn = new TableColumn<>("PID");
        TableColumn<ProcessDetailsWrapper, Double> inputMultiColumn = new TableColumn<>("INPUT");
        TableColumn<ProcessDetailsWrapper, Double> inputTempMultiColumn = new TableColumn<>("TEMP");
        TableColumn<ProcessDetailsWrapper, Double> inputMassMultiColumn = new TableColumn<>("MASS");
        TableColumn<ProcessDetailsWrapper, Double> inputVolMultiColumn = new TableColumn<>("VOLUME");
        TableColumn<ProcessDetailsWrapper, Double> outputMultiColumn = new TableColumn<>("OUTPUT");
        TableColumn<ProcessDetailsWrapper, Double> outputTempMulitColumn = new TableColumn<>("TEMP");
        TableColumn<ProcessDetailsWrapper, Double> outputFlexMultiColumn = new TableColumn<>("FLEX");
        TableColumn<ProcessDetailsWrapper, Double> outputSurfMultiColumn = new TableColumn<>("SURF");
        TableColumn<ProcessDetailsWrapper, Double> targetMutiColumn = new TableColumn<>("TARGET");
        TableColumn<ProcessDetailsWrapper, Double> targetTempMulitColumn = new TableColumn<>("TEMP");
        TableColumn<ProcessDetailsWrapper, Double> targetFlexMultiColumn = new TableColumn<>("FLEX");
        TableColumn<ProcessDetailsWrapper, Double> targetSurfMultiColumn = new TableColumn<>("SURF");
        TableColumn<ProcessDetailsWrapper, Double> wjpColumn = new TableColumn<>("WJP");

        inputMultiColumn.getColumns().addAll(inputTempMultiColumn, inputMassMultiColumn, inputVolMultiColumn);              
        outputMultiColumn.getColumns().addAll(outputTempMulitColumn, outputFlexMultiColumn, outputSurfMultiColumn);
        targetMutiColumn.getColumns().addAll(targetTempMulitColumn, targetFlexMultiColumn, targetSurfMultiColumn);

        pidColumn.setCellValueFactory(new PropertyValueFactory<>("pid"));
        inputTempMultiColumn.setCellValueFactory(new PropertyValueFactory<>("inputTemp"));
        inputMassMultiColumn.setCellValueFactory(new PropertyValueFactory<>("inputMass"));
        inputVolMultiColumn.setCellValueFactory(new PropertyValueFactory<>("inputVol"));
        outputTempMulitColumn.setCellValueFactory(new PropertyValueFactory<>("outputTemp"));
        outputFlexMultiColumn.setCellValueFactory(new PropertyValueFactory<>("outputFlex"));
        outputSurfMultiColumn.setCellValueFactory(new PropertyValueFactory<>("outputSurf"));
        targetTempMulitColumn.setCellValueFactory(new PropertyValueFactory<>("targetTemp"));
        targetFlexMultiColumn.setCellValueFactory(new PropertyValueFactory<>("targetFlex"));
        targetSurfMultiColumn.setCellValueFactory(new PropertyValueFactory<>("targetSurf"));
        wjpColumn.setCellValueFactory(new PropertyValueFactory<>("wjp"));

        databaseView.setRowFactory( tv -> {
            TableRow<ProcessDetailsWrapper> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ProcessDetailsWrapper rowData = row.getItem();
                    showDetails(rowData);
                }
            });
            return row ;
        });

        pidColumn.setSortType(TableColumn.SortType.DESCENDING);
        databaseView.getColumns().addAll(pidColumn, inputMultiColumn, outputMultiColumn, targetMutiColumn, wjpColumn);
        
        try{
            AgentController ac = MainContainer.cc.getAgent("UI-agent");
            InterfaceUI uiObj = ac.getO2AInterface(InterfaceUI.class);
            uiObj.setUIControllerRef(this);
        }
        catch(Exception e){
            System.out.println("UI reference passing failure" + e);
        }

    }

    public void setTableValuesFromJson(JSONObject data){
        Runnable updateTable = () -> {
            List<ProcessDetailsWrapper> tableElements = new ArrayList<>();

            for(String pid : data.keySet()){
                JSONObject process = data.getJSONObject(pid);
                JSONObject firstStage = process.getJSONObject("stage_1");
                JSONObject lastStage = process.getJSONObject("stage_3");
                tableElements.add(new ProcessDetailsWrapper(
                    Long.parseLong(pid),
                    firstStage.getDouble("in_temperature"), firstStage.getDouble("in_mass"), firstStage.getDouble("in_volume"),
                    lastStage.getDouble("out_temperature"), lastStage.getDouble("out_flexibility"), lastStage.getDouble("out_surface"),
                    process.getDouble("target_temperature"), process.getDouble("target_flexibility"), process.getDouble("target_surface"),
                    process.getString("wjp").equals("null") ? Double.NaN : process.getDouble("wjp")
                ));
            }

            observableList = FXCollections.observableArrayList(tableElements);
            databaseView.setItems(observableList);
        };
        Platform.runLater(updateTable);
    }

    private void showDetails(ProcessDetailsWrapper wrapper) {
        try {
            ProcessDetailsController.show(dbStage, wrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
