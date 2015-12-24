package ws.l10n.rest.client.impl.json;

import java.io.IOException;


class JsonLiteral extends JsonValue {

    private final String value;
    private final boolean isNull;
    private final boolean isTrue;
    private final boolean isFalse;

    JsonLiteral(String value) {
        this.value = value;
        isNull = "null".equals(value);
        isTrue = "true".equals(value);
        isFalse = "false".equals(value);
    }

    @Override
    void write(JsonWriter writer) throws IOException {
        writer.writeLiteral(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean isNull() {
        return isNull;
    }

    @Override
    public boolean asBoolean() {
        return isNull ? super.asBoolean() : isTrue;
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
        JsonLiteral other = (JsonLiteral) object;
        return value.equals(other.value);
    }

}
