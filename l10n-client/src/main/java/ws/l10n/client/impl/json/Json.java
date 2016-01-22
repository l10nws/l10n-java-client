package ws.l10n.client.impl.json;

import java.io.IOException;
import java.io.Reader;

public final class Json {

    private Json() {
    }

    public static final JsonValue NULL = new JsonLiteral("null");

    public static final JsonValue TRUE = new JsonLiteral("true");

    public static final JsonValue FALSE = new JsonLiteral("false");

    public static JsonValue parse(Reader reader) throws IOException {
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        return new JsonParser(reader).parse();
    }

}
