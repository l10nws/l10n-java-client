package ws.l10n.client.impl;

import ws.l10n.client.MessageItem;
import ws.l10n.client.MessageBundle;

import java.util.Locale;
import java.util.Map;

/**
 * @author Serhii Bohutskyi
 * @author Anton Mokshyn
 */
public class MessageBundleImpl implements MessageBundle {

    private final Locale defLocale;
    private final Map<Locale, MessageItem> messages;

    public MessageBundleImpl(Locale defLocale, Map<Locale, MessageItem> messages) {
        this.defLocale = defLocale;
        this.messages = messages;
    }

    @Override
    public Locale getDefaultLocale() {
        return defLocale;
    }

    @Override
    public Map<Locale, MessageItem> getMessages() {
        return messages;
    }

    /**
     * serialization
     */
    private MessageBundleImpl() {
        this.defLocale = null;
        this.messages = null;
    }

}
