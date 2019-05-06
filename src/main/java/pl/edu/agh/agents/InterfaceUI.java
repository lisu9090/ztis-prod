package pl.edu.agh.agents;

import pl.edu.agh.parameter.ProcessJson;
import pl.edu.agh.random.IDistGenerator;

import java.util.List;
import pl.edu.agh.ui.controller.DatabaseTabPageController;
import pl.edu.agh.ui.controller.SimulationTabPageController;

public interface InterfaceUI {
    public void setUIControllerRef(SimulationTabPageController instance);
    public void setUIControllerRef(DatabaseTabPageController instance);
}
