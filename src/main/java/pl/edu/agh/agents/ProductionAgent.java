package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import pl.edu.agh.random.AgentMessages;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class ProductionAgent extends Agent {
    private ArrayList<AID> processAgents = new ArrayList<>();
    private ArrayList<AID> systemAgents = new ArrayList<>();
   
    private AID createProcess(){
        ContainerController cc = getContainerController();
        String processNickname = "Process-agent"+Integer.toString(processAgents.size());
        Object [] args = new Object[1];
        args[0] = getLocalName();

        try {
            AgentController process = cc.createNewAgent(processNickname,
                    "pl.edu.agh.agents.ProcessAgent", args);
            process.start();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return new AID(processNickname, AID.ISLOCALNAME);
    }

    private boolean checkAgent(AID agentAddress){
        ACLMessage msg = new ACLMessage(AgentMessages.CHECK_AGENT);
        msg.setContent("success");
        msg.addReceiver(agentAddress);
        send(msg);
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MessageTemplate messageTmpl = MessageTemplate.and(MessageTemplate.MatchSender(agentAddress),
                MessageTemplate.MatchPerformative(AgentMessages.CHECK_AGENT));
        ACLMessage reply = receive(messageTmpl);
        return reply != null;
    }

    protected void setup()
    {
        Object [] args = new Object[1];
        args[0]=getLocalName();
        ContainerController cc = getContainerController();

        //initialize UI agent
        try {

            AgentController ui = cc.createNewAgent("UI-agent",
                    "pl.edu.agh.agents.UIAgent", args);
            ui.start();
            systemAgents.add(new AID("UI-agent", AID.ISLOCALNAME));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        //initialize database agent
        try {
            AgentController database = cc.createNewAgent("Database-agent",
                    "pl.edu.agh.agents.DatabaseAgent", args);
            database.start();
            systemAgents.add(new AID("Database-agent", AID.ISLOCALNAME));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println("Agents initialized");

        addBehaviour(new CyclicBehaviour(this)
        {
            public void action()
            {

                ACLMessage msg;

                ArrayList<MessageTemplate> templates = new ArrayList<>();
                templates.add(MessageTemplate.and(MessageTemplate.MatchSender(
                        systemAgents.get(0)),MessageTemplate.MatchPerformative(AgentMessages.START_PROCESS_AGENT)));
                templates.add(MessageTemplate.and(MessageTemplate.MatchSender(
                        systemAgents.get(0)),MessageTemplate.MatchPerformative(AgentMessages.SET_PROCESS_VALUES)));
                templates.add(MessageTemplate.MatchPerformative(AgentMessages.SET_PROCESS_VALUES_ACK));
                templates.add(MessageTemplate.MatchPerformative(AgentMessages.START_PROCESS));
                templates.add(MessageTemplate.MatchPerformative(AgentMessages.RECEIVE_RESULT));
                templates.add(MessageTemplate.MatchPerformative(AgentMessages.GET_PROCESS_IDS));
                templates.add(MessageTemplate.MatchPerformative(AgentMessages.GET_PROCESS_IDS_ACK));

                ACLMessage [] checkMsg = new ACLMessage[templates.size()];
                int counter = 0;
                for(MessageTemplate checkState: templates){
                    checkMsg[counter++] = receive(checkState);
                }
                for(ACLMessage check: checkMsg) {
                    if (check != null) {
                        //confirming that agent is working
                        switch(check.getPerformative()){
                            case(AgentMessages.START_PROCESS_AGENT):
                                AID processTag = createProcess();
                                processAgents.add(processTag);
                                boolean processConfirmation = checkAgent(processTag);
                                if(processConfirmation == true){
                                    msg = new ACLMessage(AgentMessages.START_PROCESS_AGENT_ACK);
                                    msg.setContent("Process Agent created.");
                                    msg.addReceiver(systemAgents.get(0));
                                    send(msg);
                                }
                                break;
                            case(AgentMessages.SET_PROCESS_VALUES):
                                String parameters = check.getContent();
                                msg = new ACLMessage(AgentMessages.SET_PROCESS_VALUES);
                                msg.addReceiver(processAgents.get(processAgents.size()-1));
                                msg.setContent(parameters);
                                send(msg);
                                break;
                            case(AgentMessages.SET_PROCESS_VALUES_ACK):
                                msg = new ACLMessage(AgentMessages.SET_PROCESS_VALUES_ACK);
                                msg.setContent("Values set.");
                                msg.addReceiver(systemAgents.get(0));
                                send(msg);
                                break;
                            case(AgentMessages.START_PROCESS):
                                msg = new ACLMessage(AgentMessages.START_PROCESS);
                                msg.addReceiver(processAgents.get(processAgents.size()-1));
                                msg.setContent("");
                                send(msg);
                                break;
                            case(AgentMessages.RECEIVE_RESULT):
                                String finish = check.getContent();
                                msg = new ACLMessage(AgentMessages.RECEIVE_RESULT);
                                msg.addReceiver(systemAgents.get(0));
                                msg.setContent(finish);
                                send(msg);
                                break;
                            case(AgentMessages.GET_PROCESS_IDS):
                                msg = new ACLMessage(AgentMessages.GET_PROCESS_IDS);
                                msg.addReceiver(systemAgents.get(1));
                                msg.setContent("");
                                send(msg);
                                break;
                            case(AgentMessages.GET_PROCESS_IDS_ACK):
                                msg = new ACLMessage(AgentMessages.GET_PROCESS_IDS_ACK);
                                String pids = check.getContent();
                                msg.addReceiver(systemAgents.get(0));
                                msg.setContent(pids);
                                send(msg);
                                System.out.println("Whatever");
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        });


    }
}
