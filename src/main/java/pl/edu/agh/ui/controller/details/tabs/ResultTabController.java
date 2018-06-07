package pl.edu.agh.ui.controller.details.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import pl.edu.agh.db.DBManager;
import pl.edu.agh.parameter.ProductionOutput;
import pl.edu.agh.ui.controller.InputOutputStageRefresh;

import java.util.List;

public class ResultTabController implements InputOutputStageRefresh {

    @FXML
    private Label wjp;

    @FXML
    private Label temperature;

    @FXML
    private Label flexibility;

    @FXML Label surface;

    @Override
    public void refresh(Long pid) {
        List<ProductionOutput> list = DBManager.getINSTANCE().findAllOutputForPid(pid);
        if(!list.isEmpty()) {
            ProductionOutput po = list.get(0);
            wjp.setText(po.getWjp() == null ? Double.toString(Double.NaN) : po.getWjp().toString());
            temperature.setText(po.getTemperature() == null ? Double.toString(Double.NaN) : po.getTemperature().getValue().toString());
            surface.setText(po.getSurface() == null ? Double.toString(Double.NaN) : po.getSurface().getValue().toString());
            flexibility.setText(po.getFlexibility() == null ? Double.toString(Double.NaN) : po.getFlexibility().getValue().toString());
        } else {
            wjp.setText(Double.toString(Double.NaN));
            temperature.setText(Double.toString(Double.NaN));
            flexibility.setText(Double.toString(Double.NaN));
            surface.setText(Double.toString(Double.NaN));
        }
    }
}
