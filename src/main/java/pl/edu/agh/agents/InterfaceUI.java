package pl.edu.agh.agents;

import pl.edu.agh.parameter.ProcessJson;
import pl.edu.agh.random.IDistGenerator;

import java.util.List;

public interface InterfaceUI {
    public double runProcess(double _targetMaxTemp, double _targetFlex, double _targetSurface, String generator, double _deltaTemperature, double  _volume, double _mass);
    public List<ProcessJson> startQuery();
}
