package pl.edu.agh.ui.controller.details;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.parameter.Parameter;
import pl.edu.agh.ui.controller.InputOutputStageRefresh;
import pl.edu.agh.ui.util.ProcessDetailsWrapper;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;


public class ProcessDetailsController  {

    private final static Logger logger = LoggerFactory.getLogger(ProcessDetailsController.class);

    @FXML
    private TabPane processStageTabPane;

    private Map<String, Object> processDetailsTabsControllerMap = new HashMap<>();
    private static ProcessDetailsWrapper tabData;

    private static Stage detailsStage;
    private static ProcessDetailsController instance;

    public static synchronized void show(Parent dbStage, ProcessDetailsWrapper data) throws IOException {
        tabData = data;
        if (detailsStage == null) {
            URL location = ProcessDetailsController.class.getResource("/fxml/details/processDetailsWindow.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            Scene scene = new Scene(loader.load(location.openStream()));
            detailsStage = new Stage();
            detailsStage.setScene(scene);
            detailsStage.setResizable(false);
            detailsStage.initModality(Modality.NONE);
            detailsStage.initOwner(dbStage.getScene().getWindow());
        }
        detailsStage.setTitle("Process " + toMillis(tabData.getPid()));
        detailsStage.toFront();
        detailsStage.show();
    }


    @FXML
    public void initialize()  {
        instance = this;
        Arrays.asList("Stage 1", "Stage 2", "Stage 3", "Result").forEach(name -> processStageTabPane.getTabs().add(new Tab(name)));
        processStageTabPane.getSelectionModel().clearSelection();
        processStageTabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab.getContent() == null) {
                logger.info("not init tab");
                FXMLLoader loader = new FXMLLoader();
                try {
                        Parent root = loader.load(ProcessDetailsController.class.getResource("/fxml/details/tabs/"+ StringUtils.replace(newTab.getText(), " ", "").toLowerCase()+"TabContent.fxml").openStream());
                        newTab.setContent(root);
                        processDetailsTabsControllerMap.put(newTab.getText(), loader.getController());
                        ((InputOutputStageRefresh) loader.getController()).refresh(toMillis(tabData.getPid()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ((InputOutputStageRefresh) processDetailsTabsControllerMap.get(newTab.getText())).refresh(toMillis(tabData.getPid()));
            }
        });
        processStageTabPane.getSelectionModel().selectFirst();
    }

    private static Long toMillis(LocalDateTime timePid) {
        return timePid.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static ProcessDetailsController getInstance() {
        return instance;
    }
}

