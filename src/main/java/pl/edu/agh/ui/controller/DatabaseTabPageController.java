package pl.edu.agh.ui.controller;


import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.ui.controller.details.ProcessDetailsController;
import pl.edu.agh.ui.util.ProcessDetailsWrapper;

import java.io.IOException;

public class DatabaseTabPageController {

    private final static Logger logger = LoggerFactory.getLogger(DatabaseTabPageController.class);

    @FXML
    private AnchorPane dbStage;
    private static DatabaseTabPageController instance;

    @FXML
    public void initialize() {
        instance = this;
        logger.info("init");
    }

    public void onTestButtonClick()  {
        try {

            ProcessDetailsController.show(dbStage, new ProcessDetailsWrapper());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseTabPageController getInstance() {
        return instance;
    }



}
