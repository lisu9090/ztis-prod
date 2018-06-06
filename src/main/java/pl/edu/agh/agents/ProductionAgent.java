package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class ProductionAgent extends Agent {

    private ArrayList<AID> processAgents = new ArrayList<>();
    private ArrayList<AID> systemAgents = new ArrayList<>();

    private void createProcess(){
        ContainerController cc = getContainerController();
        String processNickname = "Process-agent"+Integer.toString(processAgents.size());
        Object [] args = new Object[1];
        args[0] = getLocalName();

        try {
            AgentController process = cc.createNewAgent(processNickname,
                    "pl.edu.agh.agents.ProcessAgent", args);
            process.start();
            processAgents.add(new AID(processNickname, AID.ISLOCALNAME));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private boolean checkAgent(AID agentAddress){
        final int CHECK_AGENT = 100;

        ACLMessage msg = new ACLMessage(CHECK_AGENT);
        msg.setContent("success");
        msg.addReceiver(agentAddress);
        send(msg);
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MessageTemplate messageTmpl = MessageTemplate.and(MessageTemplate.MatchSender(agentAddress),
                MessageTemplate.MatchPerformative(CHECK_AGENT));
        ACLMessage reply = receive(messageTmpl);
        return reply != null;
    }

    protected void setup()
    {
        final int START_PROCESS = 101;
        Object [] args = new Object[1];
        args[0]=getLocalName();
        ContainerController cc = getContainerController();

        try {

            AgentController ui = cc.createNewAgent("UI-agent",
                    "pl.edu.agh.agents.UIAgent", args);
            ui.start();
            systemAgents.add(new AID("UI-agent", AID.ISLOCALNAME));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }




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

        createProcess();

       if(checkAgent(processAgents.get(processAgents.size()-1)))
           System.out.println("Process agent working!");

        addBehaviour(new CyclicBehaviour(this)
        {
            public void action()
            {

                MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchSender(
                        systemAgents.get(0)),MessageTemplate.MatchPerformative(START_PROCESS));
                ACLMessage orderProcessStart = receive(template);

                if (orderProcessStart!=null){
                    int processNumber = Integer.parseInt(orderProcessStart.getContent());
                    ACLMessage msgProcessStart = new ACLMessage(START_PROCESS);
                    msgProcessStart.setContent("success");
                    msgProcessStart.addReceiver(processAgents.get(processNumber-1));
                    send(msgProcessStart);
                }

                block();
            }
        });


    }
}
