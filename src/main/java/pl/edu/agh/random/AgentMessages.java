package pl.edu.agh.random;

import jade.core.AgentContainer;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;

public class AgentMessages {
    public static final int CHECK_AGENT = 100;
    public static final int START_PROCESS_AGENT = 101;
    public static final int START_PROCESS_AGENT_ACK = 102;
    public static final int SET_PROCESS_VALUES = 103;
    public static final int START_PROCESS = 104;
    public static final int SET_PROCESS_VALUES_ACK = 105;
    public static final int RECEIVE_RESULT = 106;
    public static final int GET_PROCESS_IDS = 107;
    public static final int GET_PROCESS_IDS_ACK = 108;
}
