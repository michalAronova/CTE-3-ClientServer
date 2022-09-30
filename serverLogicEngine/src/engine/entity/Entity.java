package engine.entity;

import engine.Engine;

public interface Entity {
    String getUsername();
    Engine getEngine();

    EntityEnum getEntity();
}
