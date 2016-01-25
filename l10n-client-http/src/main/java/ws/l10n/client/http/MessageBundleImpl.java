package ws.l10n.client.http;

import ws.l10n.core.MessageBundle;
import ws.l10n.core.MessageMap;

import java.util.Locale;
import java.util.Map;

/**
 * @author Serhii Bohutskyi
 * @author Anton Mokshyn
 */
public class MessageBundleImpl implements MessageBundle {

    private final Locale defLocale;
    private final Map<Locale, MessageMap> messages;

    public MessageBundleImpl(Locale defLocale, Map<Locale, MessageMap> messages) {
        this.defLocale = defLocale;
        this.messages = messages;
    }

    public Locale getDefaultLocale() {
        return defLocale;
    }

    public Map<Locale, MessageMap> getMessages() {
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
