package j3_l3.server;

public class ChatServerException extends RuntimeException {
    public ChatServerException(String message) {
        super(message);
    }

    public ChatServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
