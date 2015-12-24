package ws.l10n.core;

import ws.l10n.rest.client.MessageBundle;

import java.util.Locale;

/**
 * @author Serhii Bohutskyi
 */
public interface ReloadableMessageContext {

    void reload();

    String getMessage(String code, String defaultMessage, Locale locale);

    String getMessage(String code, Locale locale);

    MessageBundle getMessageBundle(Locale locale);

    Iterable<Locale> getSupportedLocales();

    Iterable<MessageBundle> getMessageBundles();

}
