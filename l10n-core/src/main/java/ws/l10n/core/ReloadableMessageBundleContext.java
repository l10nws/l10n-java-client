package ws.l10n.core;

import ws.l10n.client.L10nClient;

import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Reloadable implementation of {@link MessageBundleContext}.
 *
 * @author Serhii Bohutskyi
 * @author Anton Mokshyn
 */
class ReloadableMessageBundleContext implements MessageBundleContext {

    private final L10nClient l10nClient;

    private final String bundleKey;
    private final String bundleVersion;

    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    private final SimpleMessageBundleContext messageBundleContext = new SimpleMessageBundleContext(null);

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
    }

    public void init() {
        reload();
    }

    public void reload() {
        w.lock();
        try {
            messageBundleContext.setMessageBundle(l10nClient.loadMessageBundle(bundleKey, bundleVersion));
        } finally {
            w.unlock();
        }
    }

    public String getMessage(String code, Locale locale) {
        r.lock();
        try {
            return messageBundleContext.getMessage(code, locale);
        } finally {
            r.unlock();
        }
    }

    public String getMessage(String code, Locale locale, String defaultMessage) {
        r.lock();
        try {
            return messageBundleContext.getMessage(code, locale, defaultMessage);
        } finally {
            r.unlock();
        }
    }

}
