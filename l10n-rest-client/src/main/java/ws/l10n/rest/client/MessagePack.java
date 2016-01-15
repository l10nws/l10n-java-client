package ws.l10n.rest.client;

import java.util.Locale;
import java.util.Map;

/**
 * Interface describes bunch of messages by locale.
 */
public interface MessagePack {

    /**
     * Gets code-message map.
     *
     * @return <code,message> map
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