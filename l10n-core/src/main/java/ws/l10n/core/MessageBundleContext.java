package ws.l10n.core;

import java.util.Locale;

/**
 * Interface with main methods of message context.
 *
 * @author Serhii Bohutskyi
 */
public interface MessageBundleContext {

    /**
     * Gets message by code and locale, if not exists return defaultMessage.
     *
     * @param code           the code of message
     * @param locale         the locale
     * @param defaultMessage the default message
     * @return the message
     */
    String getMessage(String code, Locale locale, String defaultMessage);

    /**
     * Gets message by code and locale, if not exists and useCodeAsDefaultMessage option was set true returns the code.
     *
     * @param code   the code
     * @param locale the locale
     * @return the message
     */
    String getMessage(String code, Locale locale);

}
