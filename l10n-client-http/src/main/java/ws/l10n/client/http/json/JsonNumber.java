package ws.l10n.client.http.json;

import java.io.IOException;


class JsonNumber extends JsonValue {

    private final String string;

    JsonNumber(String string) {
        if (string == null) {
            throw new NullPointerException("string is null");
        }
        this.string = string;
    }


    public String toString() {
        return string;
    }


    void write(JsonWriter writer) throws IOException {
        writer.writeNumber(string);
    }


    public int hashCode() {
        return string.hashCode();
    }


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
