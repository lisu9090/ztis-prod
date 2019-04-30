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

//        addBehaviour(new CyclicBehaviour(this)
//        {
//            public void action()
//            {
//                ArrayList<MessageTemplate> templates = new ArrayList<>();
//                templates.add(MessageTemplate.MatchPerformative(AgentMessages.CHECK_AGENT));
//                templates.add(MessageTemplate.MatchPerformative(1));
//                ACLMessage [] checkMsg = new ACLMessage[templates.size()];
//
//                int counter=0;
//                for(MessageTemplate checkState: templates){
//                    checkMsg[counter++] = receive(checkState);
//                }
//
//                for(ACLMessage msg: checkMsg){
//                    if(msg != null){
//                        //confirming that agent is working
//                        if (msg.getPerformative() == AgentMessages.CHECK_AGENT) {
//                            ACLMessage reply = new ACLMessage(AgentMessages.CHECK_AGENT);
//                            reply.setContent("success");
//                            reply.addReceiver(new AID(args[0].toString(), AID.ISLOCALNAME));
//                            send(reply);
//                        }
//
//                        else if (msg.getPerformative() == 1){
//                            System.out.println(msg.getContent());
//                        }
//                    }
//
//                block();
//                }
//            }
//        });
        
        addBehaviour(new CyclicBehaviour(this)
        {
            public void action(){
                MessageTemplate resultTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
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
    }

    //db access start for GUI
//    public List<ProcessJson> startQuery() {
//        while(!AgentQuery());
//        dbQueryFinished = false;
//        return processes;
//    }
//
//    public boolean AgentQuery(){
//        addBehaviour(new Behaviour() {
//            @Override
//            public void action() {
//                switch(DBQueryStartStep) {
//                    case (0):
//                        ACLMessage msgQueryInit = new ACLMessage(AgentMessages.GET_PROCESS_IDS);
//                        msgQueryInit.setContent("");
//                        msgQueryInit.addReceiver(new AID(args[1].toString(), AID.ISLOCALNAME));
//                        send(msgQueryInit);
//                        DBQueryStartStep = 1;
//                    case (1):
//                        MessageTemplate msgTmp = MessageTemplate.MatchPerformative(AgentMessages.GET_PROCESS_IDS_ACK);
//                        ACLMessage msgReceive = receive(msgTmp);
//                        processes = new ArrayList<>();
//                        if(msgReceive!=null){
//                            System.out.println("Hmm");
//                            String [] stringPIDs = msgReceive.getContent().split(" ");
//                            long []longPIDs = new long[stringPIDs.length];
//                            for(int i = 0; i<stringPIDs.length;i++){
//                                ProcessJson process = new ProcessJson();
//                                process.setId(Long.parseLong(stringPIDs[i]));
//                                processes.add(process);
//                            }
//                            dbQueryFinished = true;
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                block();
//            }
//
//            @Override
//            public boolean done() {
//                return dbQueryFinished;
//            }
//        });
//
//        return dbQueryFinished;
//    }
    
    private void sendRunSimReq(){
        try{
            JSONObject simParams = new JSONObject();
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver((AID)args[1]);

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

            //STEP1 add value control
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
    
    private void sendStopSimReq(){
        System.out.println("Sending Stop request..");
    }
    
    private void sendPauseSimReq(){
        System.out.println("Sending Pause request..");
    }

//    //process run interface for GUI
//    public double runProcess(double _targetMaxTemp, double _targetFlex, double _targetSurface, String generator, double _deltaTemperature, double  _volume, double _mass){
//        addBehaviour(new Behaviour() {
//            @Override
//            public void action() {
//                String msgContent = Double.toString(_targetMaxTemp)+" "+
//                        Double.toString(_targetFlex)+" "+
//                        Double.toString(_targetSurface)+" "+
//                        generator+" "+
//                        Double.toString(_deltaTemperature)+" "+
//                        Double.toString(_volume)+" "+
//                        Double.toString(_mass);
//                MessageTemplate msgTmp;
//                ACLMessage msgReceive;
//                switch(runProcessStep){
//
//                    case(0):
//                        ACLMessage msgProcessInit = new ACLMessage(AgentMessages.START_PROCESS_AGENT);
//                        msgProcessInit.setContent("");
//                        msgProcessInit.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
//                        send(msgProcessInit);
//                        runProcessStep = 1;
//                        block();
//
//                    case(1):
//                        msgTmp = MessageTemplate.MatchPerformative(AgentMessages.START_PROCESS_AGENT_ACK);
//                        msgReceive = receive(msgTmp);
//                        if(msgReceive!=null){
//                            runProcessStep = 2;
//                        }
//                        break;
//
//                    case(2):
//                        ACLMessage msgSetValues = new ACLMessage(AgentMessages.SET_PROCESS_VALUES);
//                        msgSetValues.setContent(msgContent);
//                        msgSetValues.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
//                        send(msgSetValues);
//                        runProcessStep = 3;
//                        break;
//
//                    case(3):
//                        msgTmp = MessageTemplate.MatchPerformative(AgentMessages.SET_PROCESS_VALUES_ACK);
//                        msgReceive = receive(msgTmp);
//                        if(msgReceive!=null){
//                            runProcessStep = 4;
//                        }
//                        break;
//
//                    case(4):
//                        ACLMessage msgStartProcess = new ACLMessage(AgentMessages.START_PROCESS);
//                        msgStartProcess.setContent("");
//                        msgStartProcess.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
//                        send(msgStartProcess);
//                        runProcessStep = 5;
//                        break;
//
//                    case(5):
//                        msgTmp = MessageTemplate.MatchPerformative(AgentMessages.RECEIVE_RESULT);
//                        msgReceive = receive(msgTmp);
//                        if(msgReceive!=null){
//                            runProcessFinished = true;
//                            agentResult = Double.parseDouble(msgReceive.getContent());
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public boolean done() {
//                return runProcessFinished;
//            }
//        });

//        runProcessFinished = false;
//        runProcessStep = 0;
//        return agentResult;
//    }
}
