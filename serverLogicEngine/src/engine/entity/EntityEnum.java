package engine.entity;

public enum EntityEnum {
    UBOAT("uBoat"),
    ALLIES("allies"),
    AGENT("agent");
    private String type;

    EntityEnum(String type){
        this.type = type;
    }
}
