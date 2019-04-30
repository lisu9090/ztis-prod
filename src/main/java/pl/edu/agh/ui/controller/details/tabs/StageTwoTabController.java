package pl.edu.agh.ui.controller.details.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import pl.edu.agh.db.DBManager;
import pl.edu.agh.parameter.ParameterJson;
import pl.edu.agh.parameter.ProductionInput;
import pl.edu.agh.ui.controller.InputOutputStageRefresh;

import java.util.List;
import java.util.UUID;

public class StageTwoTabController implements InputOutputStageRefresh {

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
    public void refresh(Long pid) {
//        List<ProductionInput> pi = DBManager.getINSTANCE().findAllInputForPid(pid);
//        if(pi.size() >= 2) {
//            ParameterJson[] parameterJsons = pi.get(1).getParameters();
//            temperature.setText(parameterJsons[0].getValue() == null ? Double.toString(Double.NaN) : parameterJsons[0].getValue().toString());
//            flexibility.setText(parameterJsons[1].getValue() == null ? Double.toString(Double.NaN) : parameterJsons[1].getValue().toString());
//            surface.setText(parameterJsons[2].getValue() == null ? Double.toString(Double.NaN) : parameterJsons[2].getValue().toString());
//            stiffness.setText(parameterJsons[3].getValue() == null ? Double.toString(Double.NaN) : parameterJsons[3].getValue().toString());
//            amount.setText(parameterJsons[4].getValue() == null ? Double.toString(Double.NaN) : parameterJsons[4].getValue().toString());
//            mass.setText(parameterJsons[5].getValue() == null ? Double.toString(Double.NaN) : parameterJsons[5].getValue().toString());
//            volume.setText(parameterJsons[6].getValue() == null ? Double.toString(Double.NaN) : parameterJsons[6].getValue().toString());
//        } else {
//            temperature.setText(Double.toString(Double.NaN));
//            flexibility.setText(Double.toString(Double.NaN));
//            surface.setText(Double.toString(Double.NaN));
//            stiffness.setText(Double.toString(Double.NaN));
//            amount.setText(Double.toString(Double.NaN));
//            mass.setText(Double.toString(Double.NaN));
//            volume.setText(Double.toString(Double.NaN));
//        }
    }

}
