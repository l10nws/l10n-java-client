package ws.l10n.rest.client.impl;

import ws.l10n.rest.client.MessagePack;
import ws.l10n.rest.client.Response;

import java.util.Locale;
import java.util.Map;

/**
 * @author Serhii Bohutskyi
 */
public class ResponseImpl implements Response {

    private Locale defLocale;
    private Map<Locale, MessagePack> bundleMap;

    public ResponseImpl(Locale defLocale, Map<Locale, MessagePack> bundleMap) {
        this.defLocale = defLocale;
        this.bundleMap = bundleMap;
    }

    public Locale getDefaultLocale() {
        return defLocale;
    }

    public Map<Locale, MessagePack> getMessagePacks() {
        return bundleMap;
    }
}
