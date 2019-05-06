package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.agh.parameter.ProcessJson;
import pl.edu.agh.random.AgentMessages;

import java.util.*;
import javafx.event.ActionEvent;
import org.json.JSONObject;
import pl.edu.agh.ui.controller.DatabaseTabPageController;
import pl.edu.agh.ui.controller.SimulationTabPageController;

public class UIAgent extends Agent implements InterfaceUI{
    private static final String numberRegexp = "^[0-9]*(\\.[0-9]*)?$";
    private Object [] args;
    private int runProcessStep = 0;
    private int DBQueryStartStep = 0;
    private double agentResult;
    private boolean runProcessFinished = false;
    private boolean dbQueryFinished = false;
    private SimulationTabPageController simulationTabPageController;
    private DatabaseTabPageController databaseTabPageController;

    private List<ProcessJson> processes = null;
    
    public UIAgent(){
        registerO2AInterface(InterfaceUI.class, this);
    }
    
        protected void setup()
    {
        args = getArguments();

        addBehaviour(new CyclicBehaviour(this)
        {
            public void action(){
                MessageTemplate startAck = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchContent("START ACK"));
                MessageTemplate stopAck = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchContent("STOP ACK"));
                MessageTemplate pauseAck = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchContent("PAUSE ACK"));
                MessageTemplate done = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchContent("SIMULATION DONE"));
                MessageTemplate tariningStarted = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchContent("TRAINING STARTED"));
                MessageTemplate trainingCompleated = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchContent("TRAINING COMPLEATED"));
                
                ACLMessage msg = receive(startAck);
                if(msg != null){
                    simulationTabPageController.printToConsole("Simulation started.");
                }
                msg = receive(stopAck);
                if(msg != null){
                    
                }
                msg = receive(pauseAck);
                if(msg != null){
                    
                }
                msg = receive(done);
                if(msg != null){
                    simulationTabPageController.printToConsole("Simulation compleated.");
                }
                msg = receive(tariningStarted);
                if(msg != null){
                    simulationTabPageController.printToConsole(msg.getSender().getName() + " - training started.");
                }
                msg = receive(trainingCompleated);
                if(msg != null){
                    simulationTabPageController.printToConsole(msg.getSender().getName() + " - training compleated.");
                }
                block();
            }
        });
        
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                MessageTemplate resultTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchSender((AID)args[2]));
                ACLMessage msg = receive(resultTemplate);
                if(msg != null)
                {
                    JSONObject data = new JSONObject(msg.getContent());
                    databaseTabPageController.setTableValuesFromJson(data);
                }
                else
                    block();
            }
        });
        
        addBehaviour(new CyclicBehaviour(this)
        {
            public void action(){
                MessageTemplate resultTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchSender((AID)args[1]));
                
                ACLMessage msg = receive(resultTemplate);
                if(msg != null)
                {
                    simulationTabPageController.printToConsole(msg.getContent());
                }
                else
                    block();
            }
        });
    }
        
    @Override
    public void setUIControllerRef(SimulationTabPageController instance){
        simulationTabPageController = instance;
        
        simulationTabPageController.runButton.setOnAction((ActionEvent t) -> {
                addBehaviour(new OneShotBehaviour(this) {
                    @Override
                    public void action() {
                       sendRunSimReq();
                    }
                });
        });
        
        simulationTabPageController.learnButton.setOnAction((ActionEvent event) -> {
            addBehaviour(new OneShotBehaviour(this) {
                    @Override
                    public void action() {
                       sendLearnReq();
                    }
                });
        });
        
        simulationTabPageController.stopButton.setOnAction((ActionEvent event) -> {
            addBehaviour(new OneShotBehaviour(this) {
                    @Override
                    public void action() {
                       sendStopSimReq();
                    }
                });
        });
        
        simulationTabPageController.pauseButton.setOnAction((ActionEvent event) -> {
            addBehaviour(new OneShotBehaviour(this) {
                    @Override
                    public void action() {
                       sendPauseSimReq();
                    }
                });
        });
    }
    
    @Override
    public void setUIControllerRef(DatabaseTabPageController instance){
        databaseTabPageController = instance;
        
        databaseTabPageController.refreshDataButton.setOnAction(event -> {
            addBehaviour(new OneShotBehaviour(this) {
                @Override
                public void action() {
                    sendReqForData(null);
                }
            });
        });
        
        sendReqForData(null);
    }
    
    private void sendRunSimReq(){
        try{
            JSONObject simParams = new JSONObject();
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver((AID)args[1]);

            //TODO dodac walidacje wymaganych pol!
            
            if(!simulationTabPageController.simulationsCount.getText().matches(numberRegexp) || simulationTabPageController.simulationsCount.getText().length()<1)
                return;
            simParams.put("noSim", simulationTabPageController.simulationsCount.getText());
            simParams.put("generator", simulationTabPageController.distributionType.getSelectionModel().getSelectedItem());

            simParams.put("param1", simulationTabPageController.paramOneValue.getText());
            simParams.put("param2", simulationTabPageController.paramTwoValue.getText());

            //TARGET
            if(!simulationTabPageController.targetMaxTemperature.getText().matches(numberRegexp) || simulationTabPageController.targetMaxTemperature.getText().length()<1)
                return;
            simParams.put("targetMaxTemperature", simulationTabPageController.targetMaxTemperature.getText());
            if(!simulationTabPageController.targetFlex.getText().matches(numberRegexp) || simulationTabPageController.targetFlex.getText().length()<1)
                return;
            simParams.put("targetFlex", simulationTabPageController.targetFlex.getText());
            if(!simulationTabPageController.targetSurface.getText().matches(numberRegexp) || simulationTabPageController.targetSurface.getText().length()<1)
                return;
            simParams.put("targetSurface", simulationTabPageController.targetSurface.getText());

            //STEP1
            simParams.put("temperature", simulationTabPageController.temperature.getText());
            simParams.put("mass", simulationTabPageController.mass.getText());
            simParams.put("volume", simulationTabPageController.volume.getText());

            //STEP2
            simParams.put("deltaTemperature", simulationTabPageController.deltaTemperature.getText());
            simParams.put("bucketSize", simulationTabPageController.getBucketSize());

            msg.setContent(simParams.toString());
            send(msg);
        }
        catch(NumberFormatException e){
            System.out.println("Sending run process req err " + e);
        }
    }
    
    private void sendLearnReq(){
        ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
        req.addReceiver((AID)args[3]);
        req.addReceiver((AID)args[4]);
        req.setContent("START TRAINING");
        send(req);
    }
    
    private void sendStopSimReq(){
        System.out.println("Sending Stop request..");
    }
    
    private void sendPauseSimReq(){
        System.out.println("Sending Pause request..");
    }
    
    private void sendReqForData(Long id){
        ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
        req.addReceiver((AID)args[2]);
        req.setContent(id != null ? id.toString() : "ALL");
        send(req);
    }
}
