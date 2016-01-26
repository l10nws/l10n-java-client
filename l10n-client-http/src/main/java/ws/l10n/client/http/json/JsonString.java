package ws.l10n.client.http.json;

import java.io.IOException;


class JsonString extends JsonValue {

    private final String string;

    JsonString(String string) {
        if (string == null) {
            throw new NullPointerException("string is null");
        }
        this.string = string;
    }


    void write(JsonWriter writer) throws IOException {
        writer.writeString(string);
    }


    public boolean isString() {
        return true;
    }


    public String asString() {
        return string;
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
        JsonString other = (JsonString) object;
        return string.equals(other.string);
    }

}
