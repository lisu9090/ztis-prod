package pl.edu.agh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.log.appender.StaticOutputStreamAppender;
import pl.edu.agh.parameter.GeneratorRange;
import pl.edu.agh.productionmodel.ProductionProcess;
import pl.edu.agh.random.NomiGen;

import java.io.IOException;
import java.io.OutputStream;

public class SimulationTabPageController {

    private final static Logger logger = LoggerFactory.getLogger(SimulationTabPageController.class);

    @FXML
    private TextArea textAreaConsole;

    private boolean wrapText = false;

    @FXML
    public void initialize() {
        OutputStream os = new TextAreaOutputStream(textAreaConsole);
        StaticOutputStreamAppender.setStaticOutputStream(os);
    }


    @FXML
    private void onRunButtonClick(ActionEvent event) {
        //PoissonGen p = new PoissonGen(6666.0);
        ProductionProcess process = new ProductionProcess(400.0, 1000.0, 270.0); //instancja processu z zainicjalizowanymi docelowymi parametrami
        //if (args.length == 3)
        //    process.setTargetParams(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));

        //Ustawaimy zakresy przedzialow losowania
        process.setGeneratorRange(new GeneratorRange.Builder()
                .temperature(60.0)
                .volume(0.05)
                .mass(100.0)
                .stiffness(0.1)
                .amount(1.0)
                .surface(1.0)
                .build());
        process.setGenerator(new NomiGen());
        try {
            Double wjp = process.runProcess(1900.0, 2.0, 16000.0); //uruchom caly proces z domyslnymi wartosciami i zwroc wjp
            logger.debug("Process sucessfull! Obtained WJP = " + wjp);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
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

    private static class TextAreaOutputStream extends OutputStream {

        private TextArea textArea;

        public TextAreaOutputStream(TextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            textArea.appendText(String.valueOf((char) b));
        }
    }
}
