package pl.edu.agh.ui.util;

import javafx.beans.property.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ProcessDetailsWrapper {

    private ObjectProperty<LocalDateTime> pid = new SimpleObjectProperty<>(null);
    private DoubleProperty inputTemp = new SimpleDoubleProperty(0);
    private DoubleProperty inputMass = new SimpleDoubleProperty(0);
    private DoubleProperty inputVol = new SimpleDoubleProperty(0);
    private DoubleProperty outputTemp = new SimpleDoubleProperty(0);
    private DoubleProperty outputFlex = new SimpleDoubleProperty(0);
    private DoubleProperty outputSurf = new SimpleDoubleProperty(0);
    private DoubleProperty targetTemp = new SimpleDoubleProperty(0);
    private DoubleProperty targetFlex = new SimpleDoubleProperty(0);
    private DoubleProperty targetSurf = new SimpleDoubleProperty(0);
    private DoubleProperty wjp = new SimpleDoubleProperty(0);

    public ProcessDetailsWrapper() {
    }
    
    public ProcessDetailsWrapper(Long pid, Double inputTemp, Double inputMass, Double inputVol, Double outputTemp, 
            Double outputFlex, Double outputSurf, Double targetTemp, Double targetFlex, Double targetSurf, Double wjp) {
        this.pid.setValue(LocalDateTime.ofInstant(Instant.ofEpochMilli(pid), ZoneId.systemDefault()));
        this.inputMass.setValue(inputMass);
        this.inputTemp.setValue(inputTemp);
        this.inputMass.setValue(inputMass);
        this.inputVol.setValue(inputVol);
        this.outputTemp.setValue(outputTemp);
        this.outputFlex.setValue(outputFlex);
        this.outputSurf.setValue(outputSurf);
        this.targetTemp.setValue(targetTemp);
        this.targetFlex.setValue(targetFlex);
        this.targetSurf.setValue(targetSurf);
        this.wjp.setValue(wjp);
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

    public Double getInputTemp() {
        return inputTemp.getValue();
    }

    public void setInputTemp(Double inputTemp) {
        this.inputTemp.setValue(inputTemp);
    }

    public Double getInputMass() {
        return inputMass.getValue();
    }

    public void setInputMass(Double inputMass) {
        this.inputMass.setValue(inputMass);
    }

    public Double getInputVol() {
        return inputVol.getValue();
    }

    public void setInputVol(Double inputVol) {
        this.inputVol.setValue(inputVol);
    }

    public Double getOutputTemp() {
        return outputTemp.getValue();
    }

    public void setOutputTemp(Double outputTemp) {
        this.outputTemp.setValue(outputTemp);
    }

    public Double getOutputFlex() {
        return outputFlex.getValue();
    }

    public void setOutputFlex(Double outputFlex) {
        this.outputFlex.setValue(outputFlex);
    }

    public Double getOutputSurf() {
        return outputSurf.getValue();
    }

    public void setOutputSurf(Double outputSurf) {
        this.outputSurf.setValue(outputSurf);
    }

    public Double getTargetTemp() {
        return targetTemp.getValue();
    }

    public void setTargetTemp(Double targetTemp) {
        this.targetTemp.setValue(targetTemp);
    }

    public Double getTargetFlex() {
        return targetFlex.getValue();
    }

    public void setTargetFlex(Double targetFlex) {
        this.targetFlex.setValue(targetFlex);
    }

    public Double getTargetSurf() {
        return targetSurf.getValue();
    }

    public void setTargetSurf(Double targetSurf) {
        this.targetSurf.setValue(targetSurf);
    }

    public Double getWjp() {
        return wjp.getValue();
    }

    public void setWjp(Double wjp) {
        this.wjp.setValue(wjp);
    }
}
