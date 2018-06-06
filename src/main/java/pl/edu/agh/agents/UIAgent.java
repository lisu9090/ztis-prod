package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UIAgent extends Agent {

    protected void setup()
    {
        final int CHECK_AGENT = 100;
        final int START_PROCESS = 101;
        Object [] args = getArguments();
        ACLMessage msgProcessStart = new ACLMessage(START_PROCESS);
        msgProcessStart.setContent("1");
        msgProcessStart.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
        send(msgProcessStart);


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
