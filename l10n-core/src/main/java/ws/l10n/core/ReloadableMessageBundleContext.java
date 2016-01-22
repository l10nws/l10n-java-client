package ws.l10n.core;

import ws.l10n.client.L10nClient;
import ws.l10n.client.MessageBundle;
import ws.l10n.client.MessageItem;

import java.util.Locale;

/**
 * Implementation of {@link MessageBundleContext}.
 *
 * @author Serhii Bohutskyi
 * @author Anton Mokshyn
 */
class ReloadableMessageBundleContext implements MessageBundleContext {

    private final L10nClient l10nClient;

    private final String bundleKey;
    private final String bundleVersion;

    private MessageBundle messageBundle = null;

    private volatile Boolean initialized = false;

    public ReloadableMessageBundleContext(L10nClient l10nClient, String bundleKey, String bundleVersion) {
        if (l10nClient == null) {
            throw new IllegalArgumentException("L10n client cannot be null");
        }
        if (bundleKey == null || bundleKey.length() == 0) {
            throw new IllegalArgumentException("bundle key cannot be null or empty");
        }
        if (bundleVersion == null || bundleVersion.length() == 0) {
            throw new IllegalArgumentException("bundle version cannot be null or empty");
        }
        this.l10nClient = l10nClient;
        this.bundleKey = bundleKey;
        this.bundleVersion = bundleVersion;

        reload();
    }

    public synchronized void reload() {
        initialized = false;
        messageBundle = l10nClient.loadMessageBundle(bundleKey, bundleVersion);
        initialized = true;
    }

    private MessageBundle getMessageBundle() {
        if (!initialized) {
            synchronized (this) {
                while (initialized) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return messageBundle;
    }

    @Override
    public String getMessage(String code, Locale locale) {
        return getMessage(code, locale, null);
    }

    @Override
    public String getMessage(String code, Locale locale, String defaultMessage) {

        MessageBundle messageBundle = getMessageBundle();
        Locale defaultLocale = messageBundle.getDefaultLocale();

        MessageItem messageItem = messageBundle.getMessages().get(locale);
        if (messageItem == null) {
            messageItem = messageBundle.getMessages().get(defaultLocale);
        }

        return messageItem != null ? messageItem.getMessage(code) : defaultMessage;
    }


}
