package ws.l10n.rest.client.impl.json;

import java.io.IOException;


class JsonNumber extends JsonValue {

    private final String string;

    JsonNumber(String string) {
        if (string == null) {
            throw new NullPointerException("string is null");
        }
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    void write(JsonWriter writer) throws IOException {
        writer.writeNumber(string);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
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
        JsonNumber other = (JsonNumber) object;
        return string.equals(other.string);
    }

}
