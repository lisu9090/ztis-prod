package pl.edu.agh.ui.util;

import javafx.beans.property.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ProcessDetailsWrapper {

    private ObjectProperty<LocalDateTime> pid = new SimpleObjectProperty<>(null);
    private BooleanProperty stageOne = new SimpleBooleanProperty(false);
    private BooleanProperty stageTwo = new SimpleBooleanProperty(false);
    private BooleanProperty stageThree = new SimpleBooleanProperty(false);
    private DoubleProperty wjp = new SimpleDoubleProperty(0);
    private DoubleProperty temperature = new SimpleDoubleProperty(0);
    private DoubleProperty flexibility = new SimpleDoubleProperty(0);
    private DoubleProperty surface = new SimpleDoubleProperty(0);


    public ProcessDetailsWrapper(Long pid, boolean stageOne, boolean stageTwo, boolean stageThree, Double temperature, Double flexibility, Double surface, Double wjp) {
        this.pid.setValue(LocalDateTime.ofInstant(Instant.ofEpochMilli(pid), ZoneId.systemDefault()));
        this.stageOne.setValue(stageOne);
        this.stageTwo.setValue(stageTwo);
        this.stageThree.setValue(stageThree);
        this.wjp.setValue(wjp);
        this.temperature.setValue(temperature);
        this.flexibility.setValue(flexibility);
        this.surface.setValue(surface);
    }

    public LocalDateTime getPid() {
        return pid.get();
    }

    public ObjectProperty<LocalDateTime> pidProperty() {
        return pid;
    }

    public void setPid(LocalDateTime pid) {
        this.pid.set(pid);
    }

    public boolean isStageOne() {
        return stageOne.get();
    }

    public BooleanProperty stageOneProperty() {
        return stageOne;
    }

    public void setStageOne(boolean stageOne) {
        this.stageOne.set(stageOne);
    }

    public boolean isStageTwo() {
        return stageTwo.get();
    }

    public BooleanProperty stageTwoProperty() {
        return stageTwo;
    }

    public void setStageTwo(boolean stageTwo) {
        this.stageTwo.set(stageTwo);
    }

    public boolean isStageThree() {
        return stageThree.get();
    }

    public BooleanProperty stageThreeProperty() {
        return stageThree;
    }

    public void setStageThree(boolean stageThree) {
        this.stageThree.set(stageThree);
    }

    public double getTemperature() {
        return temperature.get();
    }

    public DoubleProperty temperatureProperty() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature.set(temperature);
    }

    public double getFlexibility() {
        return flexibility.get();
    }

    public DoubleProperty flexibilityProperty() {
        return flexibility;
    }

    public void setFlexibility(double flexibility) {
        this.flexibility.set(flexibility);
    }

    public double getSurface() {
        return surface.get();
    }

    public DoubleProperty surfaceProperty() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface.set(surface);
    }

    public double getWjp() {
        return wjp.get();
    }

    public DoubleProperty wjpProperty() {
        return wjp;
    }

    public void setWjp(double wjp) {
        this.wjp.set(wjp);
    }
}
