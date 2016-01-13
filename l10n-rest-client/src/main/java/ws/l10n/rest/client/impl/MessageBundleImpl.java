package ws.l10n.rest.client.impl;

import ws.l10n.rest.client.MessageBundle;

import java.util.Locale;
import java.util.Map;

/**
 * @author Serhii Bohutskyi
 */
public class MessageBundleImpl implements MessageBundle {
    private final Map<String, String> messages;
    private final Locale locale;

    MessageBundleImpl(Map<String, String> messages, Locale locale) {
        this.messages = messages;
        this.locale = locale;
    }

    @Override
    public Map<String, String> getMessages() {
        return messages;
    }

    @Override
    public String getMessage(String code) {
        return messages.get(code);
    }

    @Override
    public Locale getLocale() {
        return locale;
    }
}
