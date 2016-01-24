package ws.l10n.client.impl.json;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonObject extends JsonValue implements Iterable<JsonObject.Member> {

    private final List<String> names;
    private final List<JsonValue> values;
    private transient HashIndexTable table;

    public JsonObject() {
        names = new ArrayList<String>();
        values = new ArrayList<JsonValue>();
        table = new HashIndexTable();
    }


    public JsonObject add(String name, JsonValue value) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        table.add(name, names.size());
        names.add(name);
        values.add(value);
        return this;
    }

    public JsonValue get(String name) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        int index = indexOf(name);
        return index != -1 ? values.get(index) : null;
    }

    public String getString(String name, String defaultValue) {
        JsonValue value = get(name);
        return value != null ? value.asString() : defaultValue;
    }

    public Iterator<Member> iterator() {
        final Iterator<String> namesIterator = names.iterator();
        final Iterator<JsonValue> valuesIterator = values.iterator();
        return new Iterator<Member>() {

            public boolean hasNext() {
                return namesIterator.hasNext();
            }

            public Member next() {
                String name = namesIterator.next();
                JsonValue value = valuesIterator.next();
                return new Member(name, value);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }


    void write(JsonWriter writer) throws IOException {
        writer.writeObjectOpen();
        Iterator<String> namesIterator = names.iterator();
        Iterator<JsonValue> valuesIterator = values.iterator();
        boolean first = true;
        while (namesIterator.hasNext()) {
            if (!first) {
                writer.writeObjectSeparator();
            }
            writer.writeMemberName(namesIterator.next());
            writer.writeMemberSeparator();
            valuesIterator.next().write(writer);
            first = false;
        }
        writer.writeObjectClose();
    }


    public JsonObject asObject() {
        return this;
    }


    public int hashCode() {
        int result = 1;
        result = 31 * result + names.hashCode();
        result = 31 * result + values.hashCode();
        return result;
    }


    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JsonObject other = (JsonObject) obj;
        return names.equals(other.names) && values.equals(other.values);
    }

    int indexOf(String name) {
        int index = table.get(name);
        if (index != -1 && name.equals(names.get(index))) {
            return index;
        }
        return names.lastIndexOf(name);
    }

    private synchronized void readObject(ObjectInputStream inputStream)
            throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        table = new HashIndexTable();
        updateHashIndex();
    }

    private void updateHashIndex() {
        int size = names.size();
        for (int i = 0; i < size; i++) {
            table.add(names.get(i), i);
        }
    }

    /**
     * Represents a member of a JSON object, a pair of a name and a value.
     */
    public static class Member {

        private final String name;
        private final JsonValue value;

        Member(String name, JsonValue value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Returns the name of this member.
         *
         * @return the name of this member, never <code>null</code>
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the value of this member.
         *
         * @return the value of this member, never <code>null</code>
         */
        public JsonValue getValue() {
            return value;
        }


        public int hashCode() {
            int result = 1;
            result = 31 * result + name.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }


        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Member other = (Member) obj;
            return name.equals(other.name) && value.equals(other.value);
        }

    }

    static class HashIndexTable {

        private final byte[] hashTable = new byte[32]; // must be a power of two

        public HashIndexTable() {
        }

        public HashIndexTable(HashIndexTable original) {
            System.arraycopy(original.hashTable, 0, hashTable, 0, hashTable.length);
        }

        void add(String name, int index) {
            int slot = hashSlotFor(name);
            if (index < 0xff) {
                // increment by 1, 0 stands for empty
                hashTable[slot] = (byte) (index + 1);
            } else {
                hashTable[slot] = 0;
            }
        }

        void remove(int index) {
            for (int i = 0; i < hashTable.length; i++) {
                if (hashTable[i] == index + 1) {
                    hashTable[i] = 0;
                } else if (hashTable[i] > index + 1) {
                    hashTable[i]--;
                }
            }
        }

        int get(Object name) {
            int slot = hashSlotFor(name);
            // subtract 1, 0 stands for empty
            return (hashTable[slot] & 0xff) - 1;
        }

        private int hashSlotFor(Object element) {
            return element.hashCode() & hashTable.length - 1;
        }

    }

}
