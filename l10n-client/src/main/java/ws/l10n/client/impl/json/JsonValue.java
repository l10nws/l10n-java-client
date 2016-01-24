package ws.l10n.client.impl.json;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;


public abstract class JsonValue implements Serializable {

    JsonValue() {
    }

    public boolean isString() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public JsonObject asObject() {
        throw new UnsupportedOperationException("Not an object: " + toString());
    }

    public JsonArray asArray() {
        throw new UnsupportedOperationException("Not an array: " + toString());
    }

    public String asString() {
        throw new UnsupportedOperationException("Not a string: " + toString());
    }

    public boolean asBoolean() {
        throw new UnsupportedOperationException("Not a boolean: " + toString());
    }

    public void writeTo(Writer writer, WriterConfig config) throws IOException {
        if (writer == null) {
            throw new NullPointerException("writer is null");
        }
        if (config == null) {
            throw new NullPointerException("config is null");
        }
        WritingBuffer buffer = new WritingBuffer(writer, 128);
        write(config.createWriter(buffer));
        buffer.flush();
    }


    public String toString() {
        return toString(WriterConfig.MINIMAL);
    }

    public String toString(WriterConfig config) {
        StringWriter writer = new StringWriter();
        try {
            writeTo(writer, config);
        } catch (IOException exception) {
            // StringWriter does not throw IOExceptions
            throw new RuntimeException(exception);
        }
        return writer.toString();
    }


    public boolean equals(Object object) {
        return super.equals(object);
    }


    public int hashCode() {
        return super.hashCode();
    }

    abstract void write(JsonWriter writer) throws IOException;

}
