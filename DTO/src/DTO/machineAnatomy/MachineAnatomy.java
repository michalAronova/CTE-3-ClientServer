package DTO.machineAnatomy;

import DTO.machineAnatomy.componentAnatomy.ComponentAnatomy;

import java.util.List;

public class MachineAnatomy {
    ComponentAnatomy reflectorAnatomy;
    List<ComponentAnatomy> rotorsAnatomy;

    public MachineAnatomy(ComponentAnatomy reflectorAnatomy, List<ComponentAnatomy> rotorsAnatomy) {
        this.reflectorAnatomy = reflectorAnatomy;
        this.rotorsAnatomy = rotorsAnatomy;
    }

    public ComponentAnatomy getReflectorAnatomy() {
        return reflectorAnatomy;
    }

    public List<ComponentAnatomy> getRotorsAnatomy() {
        return rotorsAnatomy;
    }
}
