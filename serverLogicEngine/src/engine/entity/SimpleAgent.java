package engine.entity;

import DTO.agent.SimpleAgentDTO;

public class SimpleAgent {
    private final String myAlly;
    private final SimpleAgentDTO DTO;

    public SimpleAgent(String myAlly, SimpleAgentDTO DTO) {
        this.myAlly = myAlly;
        this.DTO = DTO;
    }

    public String getMyAlly() {
        return myAlly;
    }

    public SimpleAgentDTO getDTO() {
        return DTO;
    }
}
