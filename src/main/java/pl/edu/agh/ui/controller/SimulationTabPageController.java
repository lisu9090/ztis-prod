package pl.edu.agh.ui.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.parameter.GeneratorRange;
import pl.edu.agh.productionmodel.ProductionProcess;
import pl.edu.agh.random.*;
import pl.edu.agh.ui.log.appender.StaticOutputStreamAppender;

import java.io.IOException;
import java.io.OutputStream;

public class SimulationTabPageController {

    private final static Logger logger = LoggerFactory.getLogger(SimulationTabPageController.class);

    public TextField simulationsCount;
    public ChoiceBox<String> distributionType;
    public Label paramOneName;
    public Label paramTwoName;
    public TextField paramOneValue;
    public TextField paramTwoValue;

    public TextField targetMaxTemperature;
    public TextField targetFlex;
    public TextField targetSurface;

    public TextField deltaTemperature;
    public TextField mass;
    public TextField volume;

    public TextField stiffness;
    private ToggleGroup bucketSize;
    public RadioButton bucketAuto;
    public RadioButton bucketSmall;
    public RadioButton bucketMedium;
    public RadioButton bucketHigh;

    public TextField flexibility;

    private DistributionParams distributionConstructorParams;

    @FXML
    private TextArea textAreaConsole;

    private boolean wrapText = false;

    @FXML
    public void initialize() {
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
    }

    @FXML
    private void onRunButtonClick(ActionEvent event) {

        Integer amount = Integer.parseInt(simulationsCount.getText());
        String typeName = distributionType.getSelectionModel().getSelectedItem();

        //TARGET
        Double _targetMaxTemp = Double.parseDouble(targetMaxTemperature.getText());
        Double _targetFlex = Double.parseDouble(targetFlex.getText());
        Double _targetSurface = Double.parseDouble(targetSurface.getText());

        //STEP1
        Double _deltaTemperature = Double.parseDouble(deltaTemperature.getText());
        Double _mass = Double.parseDouble(mass.getText());
        Double _volume = Double.parseDouble(volume.getText());

        //STEP2
        Double _stiffness = Double.parseDouble(stiffness.getText()); // range

        //STEP3
        Double _flexibility = Double.parseDouble(flexibility.getText()); // range


        ProductionProcess process = new ProductionProcess(_targetMaxTemp, _targetFlex, _targetSurface); //instancja processu z zainicjalizowanymi docelowymi parametrami
        //Ustawaimy zakresy przedzialow losowania
        process.setGeneratorRange(new GeneratorRange.Builder()
                .temperature(60.0)
                .volume(0.05)
                .mass(100.0)
                .stiffness(0.1)
                .amount(1.0)
                .surface(1.0)
                .build());
        process.setGenerator(resolveFromName(typeName));
        try {
            Double wjp = process.runProcess(_deltaTemperature, _volume, _mass);
            //Double wjp = process.runProcess(1900.0, 2.0, 16000.0); //uruchom caly proces z domyslnymi wartosciami i zwroc wjp
            logger.debug("Process sucessfull! Obtained WJP = " + wjp);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private IDistGenerator resolveFromName(String typeName) {
        if(typeName.equals(GenNames.NOMINAL.name())) {
            return new NomiGen();
        } else if(typeName.equals(GenNames.GAUSS.name())) {
            return new GaussGen();
        } else if(typeName.equals(GenNames.GEOMETRICAL.name())) {
            if(distributionConstructorParams.getParamOne() != null) {
                return new GeomGen(Double.parseDouble(distributionConstructorParams.getParamOne()));
            }
            return new GeomGen();
        } else if(typeName.equals(GenNames.POISSON.name())) {
            if(distributionConstructorParams.getParamOne() != null) {
                return new PoissonGen(Double.parseDouble(distributionConstructorParams.getParamOne()));
            }
            return new PoissonGen();
        } else {
            if(distributionConstructorParams.getParamOne() != null && distributionConstructorParams.getParamTwo() != null) {
                return new BetaGen(Double.parseDouble(distributionConstructorParams.getParamOne()), Double.parseDouble(distributionConstructorParams.getParamTwo()));
            }
            return new BetaGen();
        }
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
        deltaTemperature.setText("1900.0");
        mass.setText("16000.0");
        volume.setText("2.0");
        bucketAuto.setSelected(true);

        stiffness.setText("0");
        flexibility.setText("0");
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

}
