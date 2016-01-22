package ws.l10n.client;

import java.util.Locale;
import java.util.Map;

/**
 * Interface describes API response.
 *
 * @author Serhii Bohutskyi
 * @author Anton Mokshyn
 */
public interface MessageBundle {

    /**
     * Gets default local of message bundle.
     *
     * @return locale default locale
     */
    Locale getDefaultLocale();

    /**
     * Gets message map by locale.
     *
     * @return map messages
     */
    Map<Locale, MessageItem> getMessages();


}
