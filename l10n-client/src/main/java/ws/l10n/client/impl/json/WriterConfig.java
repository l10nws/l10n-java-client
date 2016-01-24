package ws.l10n.client.impl.json;

import java.io.Writer;

public abstract class WriterConfig {

    public static WriterConfig MINIMAL = new WriterConfig() {

        JsonWriter createWriter(Writer writer) {
            return new JsonWriter(writer);
        }
    };

    abstract JsonWriter createWriter(Writer writer);

}
