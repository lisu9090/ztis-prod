package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.agh.random.AgentMessages;
import pl.edu.agh.random.IDistGenerator;

import java.io.IOException;
import java.util.ArrayList;

public class UIAgent extends Agent implements InterfaceUI{

    private Object [] args;
    private int step = 0;
    private double agentResult;
    private boolean runProcessFinished = false;
    public UIAgent(){
        registerO2AInterface(InterfaceUI.class, this);
    }

    //process run interface for GUI
    public double runProcess(double _targetMaxTemp, double _targetFlex, double _targetSurface, String generator, double _deltaTemperature, double  _volume, double _mass){
        addBehaviour(new Behaviour() {
            @Override
            public void action() {
                String msgContent = Double.toString(_targetMaxTemp)+" "+
                        Double.toString(_targetFlex)+" "+
                        Double.toString(_targetSurface)+" "+
                        generator+" "+
                        Double.toString(_deltaTemperature)+" "+
                        Double.toString(_volume)+" "+
                        Double.toString(_mass);
                MessageTemplate msgTmp;
                ACLMessage msgReceive;
                switch(step){

                    case(0):
                        ACLMessage msgProcessInit = new ACLMessage(AgentMessages.START_PROCESS_AGENT);
                        msgProcessInit.setContent("");
                        msgProcessInit.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
                        send(msgProcessInit);
                        step = 1;
                        block();

                    case(1):
                        msgTmp = MessageTemplate.MatchPerformative(AgentMessages.START_PROCESS_AGENT_ACK);
                        msgReceive = receive(msgTmp);
                        if(msgReceive!=null){
                            step = 2;
                        }
                        break;

                    case(2):
                        ACLMessage msgSetValues = new ACLMessage(AgentMessages.SET_PROCESS_VALUES);
                        msgSetValues.setContent(msgContent);
                        msgSetValues.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
                        send(msgSetValues);
                        step = 3;
                        break;

                    case(3):
                        msgTmp = MessageTemplate.MatchPerformative(AgentMessages.SET_PROCESS_VALUES_ACK);
                        msgReceive = receive(msgTmp);
                        if(msgReceive!=null){
                            step = 4;
                        }
                        break;

                    case(4):
                        ACLMessage msgStartProcess = new ACLMessage(AgentMessages.START_PROCESS);
                        msgStartProcess.setContent("");
                        msgStartProcess.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
                        send(msgStartProcess);
                        step = 5;
                        break;

                    case(5):
                        msgTmp = MessageTemplate.MatchPerformative(AgentMessages.RECEIVE_RESULT);
                        msgReceive = receive(msgTmp);
                        if(msgReceive!=null){
                            runProcessFinished = true;
                            agentResult = Double.parseDouble(msgReceive.getContent());
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public boolean done() {
                return false;
            }
        });

        runProcessFinished = false;
        step = 0;
        return agentResult;
    }


    protected void setup()
    {
        args = getArguments();

        addBehaviour(new CyclicBehaviour(this)
        {
            public void action()
            {
                ArrayList<MessageTemplate> templates = new ArrayList<>();
                templates.add(MessageTemplate.MatchPerformative(AgentMessages.CHECK_AGENT));
                templates.add(MessageTemplate.MatchPerformative(1));
                ACLMessage [] checkMsg = new ACLMessage[templates.size()];

                int counter=0;
                for(MessageTemplate checkState: templates){
                    checkMsg[counter++] = receive(checkState);
                }

                for(ACLMessage msg: checkMsg){
                    if(msg != null){
                        //confirming that agent is working
                        if (msg.getPerformative() == AgentMessages.CHECK_AGENT) {
                            ACLMessage reply = new ACLMessage(AgentMessages.CHECK_AGENT);
                            reply.setContent("success");
                            reply.addReceiver(new AID(args[0].toString(), AID.ISLOCALNAME));
                            send(reply);
                        }

                        else if (msg.getPerformative() == 1){
                            System.out.println(msg.getContent());
                        }
                    }

                block();
                }
            }
        });
    }
}
