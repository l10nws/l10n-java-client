package ws.l10n.rest.client.impl;

/**
 * @author Serhii Bohutskyi
 */
public class MessageClientException extends RuntimeException {
    public MessageClientException(String message) {
        super(message);
    }

    public MessageClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageClientException(Throwable cause) {
        super(cause);
    }
}
