package ws.l10n.core.impl;

import ws.l10n.core.MessageBundle;
import ws.l10n.core.MessageMap;

import java.util.Locale;
import java.util.Map;

/**
 * @author Serhii Bohutskyi
 */
public class MessageBundleMock implements MessageBundle {

    private final Locale defLocale;
    private final Map<Locale, MessageMap> messages;
    private final Iterable<Locale> locales;

    public MessageBundleMock(Locale defLocale, Map<Locale, MessageMap> messages, Iterable<Locale> locales) {
        this.defLocale = defLocale;
        this.messages = messages;
        this.locales = locales;
    }

    public Locale getDefaultLocale() {
        return defLocale;
    }

    public Map<Locale, MessageMap> getMessages() {
        return messages;
    }

    public Iterable<Locale> getSupportedLocales() {
        return locales;
    }
}
