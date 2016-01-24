package ws.l10n.core;

import ws.l10n.client.MessageBundle;
import ws.l10n.client.MessageItem;

import java.util.Locale;

/**
 * @author Anton Mokshyn
 */
public class SimpleMessageBundleContext implements MessageBundleContext {

    private MessageBundle messageBundle;

    public SimpleMessageBundleContext(MessageBundle messageBundle) {
        this.messageBundle = messageBundle;
    }

    public void setMessageBundle(MessageBundle messageBundle) {
        this.messageBundle = messageBundle;
    }

    public String getMessage(String code, Locale locale) {
        return getMessage(code, locale, null);
    }

    public String getMessage(String code, Locale locale, String defaultMessage) {

        Locale defaultLocale = messageBundle.getDefaultLocale();

        MessageItem messageItem = messageBundle.getMessages().get(locale);
        if (messageItem == null) {
            messageItem = messageBundle.getMessages().get(defaultLocale);
        }

        return messageItem != null ? messageItem.getMessage(code) : defaultMessage;

    }
}
