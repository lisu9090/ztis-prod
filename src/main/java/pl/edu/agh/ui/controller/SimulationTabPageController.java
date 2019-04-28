package pl.edu.agh.ui.controller;


import jade.core.*;
import jade.core.Runtime;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.agents.InterfaceUI;
import pl.edu.agh.parameter.GeneratorRange;
import pl.edu.agh.productionmodel.ProductionProcess;
import pl.edu.agh.random.*;
import pl.edu.agh.random.MainContainer;
import pl.edu.agh.ui.log.appender.StaticOutputStreamAppender;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class SimulationTabPageController{

    private final static Logger logger = LoggerFactory.getLogger(SimulationTabPageController.class);
    private static final String numberRegexp = "^[0-9]*(\\.[0-9]*)?$";
    
    public TextField simulationsCount;
    public ChoiceBox<String> distributionType;
    public Label paramOneName;
    public Label paramTwoName;
    public TextField paramOneValue;
    public TextField paramTwoValue;

    public TextField targetMaxTemperature;
    public TextField targetFlex;
    public TextField targetSurface;

    public TextField temperature;
    public TextField mass;
    public TextField volume;

    public TextField deltaTemperature;
    private ToggleGroup bucketSize;
    public RadioButton bucketAuto;
    public RadioButton bucketSmall;
    public RadioButton bucketMedium;
    public RadioButton bucketHigh;
    
    public Button stopButton;
    public Button pauseButton;
    public Button resetButton;
    public Button runButton;

//    public TextField flexibility;

    private DistributionParams distributionConstructorParams;

    @FXML
    private TextArea textAreaConsole;

    private boolean wrapText = false;

    @FXML
    public void initialize() {
        logger.info("init");
        bucketSize = new ToggleGroup();
        bucketAuto.setToggleGroup(bucketSize);
        bucketAuto.setSelected(true);
        bucketSmall.setToggleGroup(bucketSize);
        bucketMedium.setToggleGroup(bucketSize);
        bucketHigh.setToggleGroup(bucketSize);

        distributionConstructorParams = new DistributionParams("Param 1:", "Param 2:");
        paramOneName.textProperty().bind(distributionConstructorParams.paramOneProperty());
        paramTwoName.textProperty().bind(distributionConstructorParams.paramTwoProperty());
        distributionType.setItems(FXCollections.observableArrayList(GenNames.NOMINAL.name(), GenNames.BETA.name(), GenNames.GAUSS.name(), GenNames.GEOMETRICAL.name(), GenNames.POISSON.name()));
        distributionType.getSelectionModel().selectFirst();
        maintainDistributionChoice();
        
        OutputStream os = new TextAreaOutputStream(textAreaConsole);
        StaticOutputStreamAppender.setStaticOutputStream(os);

        setDefaultProcessValues();
        
        try{
            AgentController ac = MainContainer.cc.getAgent("UI-agent");
            InterfaceUI uiObj = ac.getO2AInterface(InterfaceUI.class);
            uiObj.setUIControllerRef(this);
        }
        catch(Exception e){
            System.out.println("UI reference passing failure" + e);
        }
    }
    
    public String getBucketSize(){
        if(bucketSmall.isSelected())
            return "0.02";
        else if(bucketMedium.isSelected())
            return "0.2";
        else if(bucketSmall.isSelected())
            return "1.0";
        return "0"; //auto
    }

    @FXML
    private void onCopyButtonClick() {
        textAreaConsole.selectAll();
        textAreaConsole.copy();
    }

    @FXML
    private void onResetButtonClick() {
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }

    @FXML
    public void onClearButtonClick() {
        textAreaConsole.clear();
    }

    @FXML
    public void onWrapTextButtonClick() {
        textAreaConsole.setWrapText(wrapText = !wrapText);
    }
    
    private void maintainDistributionChoice() {
        distributionType.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            paramOneValue.clear();
            paramTwoValue.clear();
            if(newValue.intValue() == 0 || newValue.intValue() == 2) {
                distributionConstructorParams.setParamOne("Param 1:");
                distributionConstructorParams.setParamTwo("Param 2:");
                paramOneValue.setDisable(true);
                paramTwoValue.setDisable(true);
            } else if(newValue.intValue() == 1) {
                distributionConstructorParams.setParamOne("Alpha:");
                distributionConstructorParams.setParamTwo("Beta:");
                paramOneValue.setDisable(false);
                paramTwoValue.setDisable(false);
            } else if(newValue.intValue() == 3) {
                distributionConstructorParams.setParamOne("Probability:");
                distributionConstructorParams.setParamTwo("Parameter 2:");
                paramOneValue.setDisable(false);
                paramTwoValue.setDisable(true);
            } else if(newValue.intValue() == 4) {
                distributionConstructorParams.setParamOne("Mean:");
                distributionConstructorParams.setParamTwo("Parameter 2:");
                paramOneValue.setDisable(false);
                paramTwoValue.setDisable(true);
            }
        });
    }

    private void setDefaultProcessValues() {
        simulationsCount.setText("1");
        distributionType.getSelectionModel().select(0);
        targetMaxTemperature.setText("400");
        targetFlex.setText("270");
        targetSurface.setText("1000");
        temperature.setText("1900.0");
        mass.setText("16000.0");
        volume.setText("2.0");
        bucketAuto.setSelected(true);

        deltaTemperature.setText("0");
//        flexibility.setText("0");
    }

    private static class TextAreaOutputStream extends OutputStream {

        private TextArea textArea;

        TextAreaOutputStream(TextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            textArea.appendText(String.valueOf((char) b));
        }
    }

    public void printToConsole(String msg){
        textAreaConsole.appendText("\n" + msg);
    }
}
