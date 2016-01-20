package ws.l10n.core;

import ws.l10n.rest.client.MessagePack;

import java.util.Locale;

/**
 * Interface with main methods of message context.
 *
 * @author Serhii Bohutskyi
 */
public interface ReloadableMessageContext {

    /**
     * Reload all messages.
     * Calls if period of automatically reloading presents.
     */
    void reload();

    /**
     * Gets message by code and locale, if not exists return defaultMessage.
     *
     * @param code           the code of message
     * @param defaultMessage the default message
     * @param locale         the locale
     * @return the message
     */
    String getMessage(String code, String defaultMessage, Locale locale);

    /**
     * Gets message by code and locale, if not exists and useCodeAsDefaultMessage option was set true returns the code.
     *
     * @param code   the code
     * @param locale the locale
     * @return the message
     */
    String getMessage(String code, Locale locale);

    /**
     * Gets message pack by locale.
     *
     * @param locale the locale
     * @return the message pack
     */
    MessagePack getMessagePack(Locale locale);

    /**
     * Gets supported locales.
     *
     * @return the supported locales
     */
    Iterable<Locale> getSupportedLocales();

    /**
     * Gets all message packs.
     *
     * @return the message packs
     */
    Iterable<MessagePack> getMessagePacks();

}
