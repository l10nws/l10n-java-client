package ws.l10n.client.impl;

import ws.l10n.client.MessageMap;

import java.util.Locale;
import java.util.Map;

/**
 * @author Serhii Bohutskyi
 * @author Anton Mokshyn
 */
public class MessageMapImpl implements MessageMap {

    private final Map<String, String> messages;
    private final Locale locale;

    public MessageMapImpl(Map<String, String> messages, Locale locale) {
        this.messages = messages;
        this.locale = locale;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public String getMessage(String code) {
        return messages.get(code);
    }

    public Locale getLocale() {
        return locale;
    }

    /**
     *  serialization
     */
    private MessageMapImpl() {
        this.messages = null;
        this.locale = null;
    }
}
