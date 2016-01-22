package ws.l10n.client.impl.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonArray extends JsonValue implements Iterable<JsonValue> {

    private final List<JsonValue> values;

    public JsonArray() {
        values = new ArrayList<JsonValue>();
    }

    public JsonArray add(JsonValue value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        values.add(value);
        return this;
    }

    public Iterator<JsonValue> iterator() {
        final Iterator<JsonValue> iterator = values.iterator();
        return new Iterator<JsonValue>() {

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public JsonValue next() {
                return iterator.next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    void write(JsonWriter writer) throws IOException {
        writer.writeArrayOpen();
        Iterator<JsonValue> iterator = iterator();
        boolean first = true;
        while (iterator.hasNext()) {
            if (!first) {
                writer.writeArraySeparator();
            }
            iterator.next().write(writer);
            first = false;
        }
        writer.writeArrayClose();
    }

    @Override
    public JsonArray asArray() {
        return this;
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        JsonArray other = (JsonArray) object;
        return values.equals(other.values);
    }

}
