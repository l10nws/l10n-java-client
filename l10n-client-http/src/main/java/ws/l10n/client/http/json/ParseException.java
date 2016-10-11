package ws.l10n.client.http.json;

public class ParseException extends RuntimeException {

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    ParseException(String message, int line, int column) {
        super(message + " at " + line + ":" + column);
    }

}
