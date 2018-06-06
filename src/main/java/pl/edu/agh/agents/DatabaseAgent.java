package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

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

                ACLMessage checkMsg = receive(checkState);
                if (checkMsg!=null){
                    ACLMessage reply = new ACLMessage(CHECK_AGENT);
                    reply.setContent("success");
                    reply.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
                    send(reply);
                }
                block();
            }
        });
    }
}