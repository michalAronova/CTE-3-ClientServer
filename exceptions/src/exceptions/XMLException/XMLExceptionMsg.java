package exceptions.XMLException;

public enum XMLExceptionMsg {
    INVALIDFILE("Invalid file: "),
    INVALIDABC("Invalid abc input in XML: "),
    INVALIDREFLECTOR("Invalid reflector input in XML: "),
    INVALIDROTOR("Invalid rotor input in XML: "),
    INVALIDAGENTCOUNT("Invalid agent count in XML: ");
    private final String msg;
    private XMLExceptionMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return this.msg;
    }
    @Override
    public String toString() {
        return getMsg();
    }

}
