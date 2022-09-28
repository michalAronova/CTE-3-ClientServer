package DTO.machineAnatomy.componentAnatomy;

public class ComponentAnatomy {
    private ComponentType type;
    private String leftPermutation;
    private String rightPermutation;
    private String ID;

    public ComponentAnatomy(ComponentType type, String leftPermutation,
                            String rightPermutation, String ID) {
        this.type = type;
        this.leftPermutation = leftPermutation;
        this.rightPermutation = rightPermutation;
        this.ID = ID;
    }

    public ComponentType getType() {
        return type;
    }

    public String getLeftPermutation() {
        return leftPermutation;
    }

    public String getRightPermutation() {
        return rightPermutation;
    }

    public String getID() {
        return ID;
    }
}
