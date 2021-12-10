package j3_l6.task_1;

public class ClientException extends RuntimeException {
    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
