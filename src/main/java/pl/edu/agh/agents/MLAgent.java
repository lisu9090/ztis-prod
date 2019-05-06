/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONObject;

/**
 *
 * @author Tomek
 */
public abstract class MLAgent extends Agent{
    private Object[] args;

    protected void setup()
    {
        args = getArguments();
        
        loadRecentConfig(); //load recent configuration if exists

        addBehaviour(new CyclicBehaviour(this)
        {
            public void action() //request for help from process agent
            {
                MessageTemplate helpTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP); 
                ACLMessage helpRequest = receive(helpTemplate);
                if(helpRequest != null){
                    ACLMessage res = helpRequest.createReply();
                    res.setPerformative(ACLMessage.PROPOSE);
                    res.setContent(decision(helpRequest.getContent()).toString());
                    send(res);
                }
                else
                    block();
            }
        });
        
        addBehaviour(new CyclicBehaviour(this)
        {
            public void action()  //Receive data form database agent
            {
                MessageTemplate dataResTemplate =  MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchSender((AID)args[2]));
                ACLMessage dataResponse = receive(dataResTemplate);
                if(dataResponse != null){
                    receiveData(dataResponse.getContent());
                }
                else
                    block();
            }
        });
        
        addBehaviour(new CyclicBehaviour(this)
        {
            public void action() //Request for learn action from ui agent
            {
                MessageTemplate learningTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchContent("START LEARN"));
                ACLMessage learningRequest = receive(learningTemplate);
                if(learningRequest != null){
                    ACLMessage res = learningRequest.createReply();
                    res.setContent("TRAINING STARTED");
                    send(res);
                    learn();
                }
                else
                    block();
            }
        });
    }
    
    private void sendRequestForData(Long id){
        ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
        req.addReceiver((AID)args[2]);
        req.setContent(id != null ? id.toString() : "ALL");
        send(req);
    }
    
    private void blockingSendReceiveForData(Long id){ //blocking
        sendRequestForData(id);
        MessageTemplate dataResTemplate =  MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchSender((AID)args[2]));
        ACLMessage dataResponse = blockingReceive(dataResTemplate);
        receiveData(dataResponse.getContent());
    }
    
    protected abstract JSONObject decision(String params);
    
    protected abstract void receiveData(String data);
    
    protected abstract void learn();
    
    protected abstract void loadRecentConfig();
}
