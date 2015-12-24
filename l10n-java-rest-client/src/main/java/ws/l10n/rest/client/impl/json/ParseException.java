package ws.l10n.rest.client.impl.json;

public class ParseException extends RuntimeException {

    ParseException(String message, int line, int column) {
        super(message + " at " + line + ":" + column);
    }

}
