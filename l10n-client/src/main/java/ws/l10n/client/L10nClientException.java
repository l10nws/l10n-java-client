package ws.l10n.client;

/**
 * @author Serhii Bohutskyi
 */
public class L10nClientException extends RuntimeException {
    public L10nClientException(String message) {
        super(message);
    }

    public L10nClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public L10nClientException(Throwable cause) {
        super(cause);
    }
}
