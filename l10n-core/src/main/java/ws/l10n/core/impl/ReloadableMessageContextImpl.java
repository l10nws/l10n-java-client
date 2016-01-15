package ws.l10n.core.impl;

import ws.l10n.core.ReloadableMessageContext;
import ws.l10n.rest.client.MessagePack;
import ws.l10n.rest.client.MessageRestClient;
import ws.l10n.rest.client.Response;
import ws.l10n.rest.client.impl.MessageRestClientImpl;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Serhii Bohutskyi
 */
public class ReloadableMessageContextImpl implements ReloadableMessageContext {

    private final Options options;
    private final MessageRestClient restClient;
    private final AtomicReference<Map<Locale, MessagePack>> lookupMessageBundles =
            new AtomicReference<Map<Locale, MessagePack>>();
    private final AtomicReference<MessagePack> lookupDefaultMessageBundle = new AtomicReference<MessagePack>();
    private final ReloadableThread reloadableThread;
    private final ReentrantLock lock = new ReentrantLock();

    public ReloadableMessageContextImpl(Options options) {

        validate(options);

        this.options = options;
        this.restClient = new MessageRestClientImpl(options.getServiceUrl(), options.getAccessToken());

        if (options.getReloadPeriod() > 0) {
            this.reloadableThread = new ReloadableThread();
            this.reloadableThread.start();
        } else {
            this.reloadableThread = null;
        }

        reload();
    }

    private void validate(Options options) {
        if (options.getServiceUrl() == null || options.getServiceUrl().equals("")) {
            throw new IllegalArgumentException("ServiceURL cannot be empty");
        }
        if (options.getAccessToken() == null || options.getAccessToken().equals("")) {
            throw new IllegalArgumentException("Access-Token cannot be empty");
        }
        if (options.getBundleUid() == null || options.getBundleUid().equals("")) {
            throw new IllegalArgumentException("BundleUid cannot be empty");
        }
        if (options.getVersion() == null || options.getVersion().equals("")) {
            throw new IllegalArgumentException("Version cannot be empty");
        }
        if (options.getReloadPeriod() < 60 * 1000) {
            throw new IllegalArgumentException("Reload period cannot be less then 1 minute");
        }
    }

    private void waitIfNotLoaded() {
        if (isNotLoaded()) {
            synchronized (lock) {
                if (isLoaded()) {
                    return;
                }

                try {
                    lock.wait(30 * 1000);
                } catch (InterruptedException e) {
                }
            }

        }
    }

    public void reload() {
        synchronized (lock) {
            Response response = restClient.load(options.getBundleUid(), options.getVersion());
            lookupDefaultMessageBundle.set(response.getMessagePacks().get(response.getDefaultLocale()));
            lookupMessageBundles.set(response.getMessagePacks());
            lock.notifyAll();
        }
    }

    public String getMessage(String code, Locale locale) {
        return getMessage(code, null, locale);
    }

    public String getMessage(String code, String defaultMessage, Locale locale) {
        waitIfNotLoaded();

        MessagePack messagePack = getMessageBundle(locale);
        if (messagePack == null) {
            messagePack = lookupDefaultMessageBundle.get();
        }
        String message = messagePack != null ? messagePack.getMessage(code) : null;
        if (message == null) {
            if (defaultMessage != null) {
                return defaultMessage;
            }
            if (options.isUseCodeAsDefaultMessage()) {
                return code;
            } else {
                throw new IllegalArgumentException("No such message found with code '"
                        + code + "' and locale " + locale.getDisplayName());
            }
        }
        return message;
    }

    public MessagePack getMessageBundle(Locale locale) {
        waitIfNotLoaded();

        return getLookup().get(locale);
    }

    public Iterable<MessagePack> getMessageBundles() {
        waitIfNotLoaded();

        return lookupMessageBundles.get().values();
    }

    public Iterable<Locale> getSupportedLocales() {
        waitIfNotLoaded();

        return lookupMessageBundles.get().keySet();
    }


    private boolean isNotLoaded() {
        return !isLoaded();
    }

    private boolean isLoaded() {
        return lookupMessageBundles.get() != null;
    }

    private Map<Locale, MessagePack> getLookup() {
        return lookupMessageBundles.get();
    }

    private class ReloadableThread extends Thread {
        public ReloadableThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(options.getReloadPeriod());
                } catch (InterruptedException e) {
                }
                reload();
            }
        }
    }

}
