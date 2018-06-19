package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.agh.db.DBManager;
import pl.edu.agh.parameter.ProcessJson;
import pl.edu.agh.random.AgentMessages;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAgent extends Agent {

    protected void setup()
    {
        final int CHECK_AGENT = 100;

        Object [] args = getArguments();

        addBehaviour(new CyclicBehaviour(this)
        {
            public void action()
            {
                MessageTemplate checkState = MessageTemplate.MatchPerformative(CHECK_AGENT);
                MessageTemplate getDatabase = MessageTemplate.MatchPerformative(AgentMessages.GET_DATABASE_INSTANCE);
                ACLMessage checkMsg = receive(checkState);
                ACLMessage dbMsg = receive(getDatabase);
                if (checkMsg!=null){
                    ACLMessage reply = new ACLMessage(CHECK_AGENT);
                    reply.setContent("success");
                    reply.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
                    send(reply);
                }
                else if(dbMsg!=null){
                    List<ProcessJson> processes = DBManager.getINSTANCE().findAllProcesses();
                    Object listProcesses = new ArrayList<ProcessJson>(processes);
                    ACLMessage reply = new ACLMessage(AgentMessages.GET_DATABASE_INSTANCE);
                }
                block();
            }
        });
    }
}