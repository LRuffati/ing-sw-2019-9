package network.socket;

public enum StatusCode {
    OK(0),
    KO(1);

    StatusCode(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }
}
