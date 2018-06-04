package pl.edu.agh.ui.controller.details.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import pl.edu.agh.ui.controller.InputOutputStageRefresh;

import java.util.UUID;

public class StageOneTabController implements InputOutputStageRefresh {

    @FXML
    private Label temperature;

    @FXML
    private Label flexibility;

    @FXML
    private Label surface;

    @FXML
    private Label mass;

    @FXML
    private Label volume;

    @FXML
    private Label amount;

    @FXML
    private Label stiffness;

    @FXML
    public void initialize() {
    }

    @Override
    public void refresh(Integer data) {
        temperature.setText("_" + UUID.randomUUID());
        surface.setText(data.toString());
    }

}
