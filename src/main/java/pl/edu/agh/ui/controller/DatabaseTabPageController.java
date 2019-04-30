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

public class DatabaseTabPageController {

    private final static Logger logger = LoggerFactory.getLogger(DatabaseTabPageController.class);

    @FXML
    private AnchorPane dbStage;

    @FXML
    private TableView<ProcessDetailsWrapper> databaseView;

    private static DatabaseTabPageController instance;

    private ObservableList<ProcessDetailsWrapper> observableList;

    @FXML
    public void initialize() {
        instance = this;
        logger.info("init");
        TableColumn<ProcessDetailsWrapper, LocalDateTime> pidColumn = new TableColumn<>("PID");
        TableColumn<ProcessDetailsWrapper, String> stageMultiColumn = new TableColumn<>("STAGES");
        TableColumn<ProcessDetailsWrapper, Boolean> stageOneColumn = new TableColumn<>("I");
        TableColumn<ProcessDetailsWrapper, Boolean> stageTwoColumn = new TableColumn<>("II");
        TableColumn<ProcessDetailsWrapper, Boolean> stageThreeColumn = new TableColumn<>("III");
        TableColumn<ProcessDetailsWrapper, String> outputMultiColumn = new TableColumn<>("RESULT");
        TableColumn<ProcessDetailsWrapper, Double> temperatureColumn = new TableColumn<>("TEMPERATURE");
        TableColumn<ProcessDetailsWrapper, Double> flexibilityColumn = new TableColumn<>("FLEXIBILITY");
        TableColumn<ProcessDetailsWrapper, Double> surfaceColumn = new TableColumn<>("SURFACE");
        TableColumn<ProcessDetailsWrapper, Double> wjpColumn = new TableColumn<>("WJP");

        stageMultiColumn.getColumns().addAll(stageOneColumn, stageTwoColumn, stageThreeColumn);
        outputMultiColumn.getColumns().addAll(temperatureColumn, flexibilityColumn, surfaceColumn, wjpColumn);

        pidColumn.setCellValueFactory(new PropertyValueFactory<>("pid"));
        stageOneColumn.setCellValueFactory(new PropertyValueFactory<>("stageOne"));
        stageTwoColumn.setCellValueFactory(new PropertyValueFactory<>("stageTwo"));
        stageThreeColumn.setCellValueFactory(new PropertyValueFactory<>("stageThree"));
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        flexibilityColumn.setCellValueFactory(new PropertyValueFactory<>("flexibility"));
        surfaceColumn.setCellValueFactory(new PropertyValueFactory<>("surface"));
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

//        observableList = FXCollections.observableArrayList(prepareData());
        databaseView.setItems(observableList);
        databaseView.getColumns().addAll(pidColumn, stageMultiColumn, outputMultiColumn);
        
        try{
            AgentController ac = MainContainer.cc.getAgent("UI-agent");
            InterfaceUI uiObj = ac.getO2AInterface(InterfaceUI.class);
            uiObj.setUIControllerRef(this);
        }
        catch(Exception e){
            System.out.println("UI reference passing failure" + e);
        }

    }

//    private List<ProcessDetailsWrapper> prepareData(){
//        List<ProcessDetailsWrapper> tableElements = new ArrayList<>();
//
//        AgentController ac = null;
//        InterfaceUI uiObj = null;
//        try {
//            ac = MainContainer.cc.getAgent("UI-agent");
//            uiObj = ac.getO2AInterface(InterfaceUI.class);
//        } catch (ControllerException e) {
//            e.printStackTrace();
//        }
//
//        List<ProcessJson> processes = DBManager.getINSTANCE().findAllProcesses();
//
//        for (ProcessJson p : processes) {
//            System.out.println("Process ID: " + p.getId());
//            Long pid = p.getId();
//            Double wjp = Double.NaN, temperature = Double.NaN, flexibility = Double.NaN, surface = Double.NaN;
//            boolean[] stages = new boolean[]{false, false, false};
//
//            List<ProductionInput> inputs = DBManager.getINSTANCE().findAllInputForPid(p.getId());
//            for(int i=0; i<inputs.size(); i++) {
//                stages[i] = inputs.get(i).getStage() != null;
//            }
//
//            List<ProductionOutput> outputs = DBManager.getINSTANCE().findAllOutputForPid(p.getId());
//            for(ProductionOutput po : outputs) {
//                wjp = po.getWjp();
//                System.out.println(wjp);
//                temperature = (Double) po.getTemperature().toObject().getValue();
//                flexibility = (Double) po.getFlexibility().toObject().getValue();
//                surface = (Double) po.getSurface().toObject().getValue();
//            }
//            tableElements.add(new ProcessDetailsWrapper(pid, stages[0], stages[1], stages[2], temperature, flexibility, surface, wjp));
//        }
//        return tableElements;
//    }



    private void showDetails(ProcessDetailsWrapper wrapper) {
        try {
            ProcessDetailsController.show(dbStage, wrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onTestButtonClick() {
        showDetails(null);
    }

    public void onRefreshButtonClick() {
        databaseView.getItems().removeAll(observableList);
        observableList.removeAll();
//        observableList.addAll(prepareData());
        databaseView.setItems(observableList);
    }

    public static DatabaseTabPageController getInstance() {
        return instance;
    }


}
