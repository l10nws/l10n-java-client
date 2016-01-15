package ws.l10n.rest.client;

import java.util.Locale;
import java.util.Map;

/**
 * Interface describes API response.
 *
 * @author Serhii Bohutskyi
 */
public interface Response {

    /**
     * Gets default local of bundle.
     *
     * @return locale
     */
    Locale getDefaultLocale();

    /**
     * Gets message pack map by locale.
     *
     * @return map
     */
    Map<Locale, MessagePack> getMessagePacks();
}
