package ws.l10n.core;

import java.util.Locale;
import java.util.Map;

/**
 * Interface describes bunch of messages by locale.
 */
public interface MessageMap {

    /**
     * Gets key-value map.
     *
     * @return <key,value> map
     */
    Map<String, String> getMessages();

    /**
     * Gets message by code.
     *
     * @param code of message
     * @return string message presentation
     */
    String getMessage(String code);

    /**
     * Gets locale of this pack.
     *
     * @return the locale
     */
    Locale getLocale();
}