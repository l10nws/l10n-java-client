package ws.l10n.core.impl;

import ws.l10n.core.ReloadableMessageContext;
import ws.l10n.rest.client.MessagePack;
import ws.l10n.rest.client.MessageRestClient;
import ws.l10n.rest.client.Response;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Serhii Bohutskyi
 */
class ReloadableMessageContextImpl implements ReloadableMessageContext {

    private final Options options;
    private MessageRestClient restClient;
    private final AtomicReference<Map<Locale, MessagePack>> lookupMessagePacks =
            new AtomicReference<Map<Locale, MessagePack>>();
    private final AtomicReference<MessagePack> lookupDefaultMessagePack = new AtomicReference<MessagePack>();
    private ReloadableThread reloadableThread;
    private final ReentrantLock lock = new ReentrantLock();
    private final AtomicBoolean initialized = new AtomicBoolean();

    public ReloadableMessageContextImpl(MessageRestClient restClient, Options options) {
        validate(restClient, options);
        this.restClient = restClient;
        this.options = options;
    }

    private void validate(MessageRestClient restClient, Options options) {
        if (restClient == null) {
            throw new IllegalArgumentException("MessageRestClient cannot be empty");
        }
        if (options.getServiceUrl() == null || options.getServiceUrl().equals("")) {
            throw new IllegalArgumentException("ServiceURL cannot be empty");
        }
        if (options.getAccessToken() == null || options.getAccessToken().equals("")) {
            throw new IllegalArgumentException("Access-Token cannot be empty");
        }
        if (options.getBundleKey() == null || options.getBundleKey().equals("")) {
            throw new IllegalArgumentException("BundleKey cannot be empty");
        }
        if (options.getVersion() == null || options.getVersion().equals("")) {
            throw new IllegalArgumentException("Version cannot be empty");
        }
        if (options.getReloadPeriod() < 60 * 1000) {
            throw new IllegalArgumentException("Reload period cannot be less then 1 minute");
        }
    }

    protected void init() {

        if (options.getReloadPeriod() > 0) {
            this.reloadableThread = new ReloadableThread();
            this.reloadableThread.start();
        } else {
            this.reloadableThread = null;
        }

        initialized.set(true);

        reload();
    }

    public void reload() {
        synchronized (lock) {
            Response response = restClient.load(options.getBundleKey(), options.getVersion());
            lookupDefaultMessagePack.set(response.getMessagePacks().get(response.getDefaultLocale()));
            lookupMessagePacks.set(response.getMessagePacks());
            lock.notifyAll();
        }
    }

    public String getMessage(String code, Locale locale) {
        return getMessage(code, null, locale);
    }

    public String getMessage(String code, String defaultMessage, Locale locale) {
        waitIfNotLoaded();

        MessagePack messagePack = getMessagePack(locale);
        if (messagePack == null) {//no such pack for locale
            messagePack = lookupDefaultMessagePack.get();
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

    public MessagePack getMessagePack(Locale locale) {
        waitIfNotLoaded();

        return getLookup().get(locale);
    }

    public Iterable<MessagePack> getMessagePacks() {
        waitIfNotLoaded();

        return lookupMessagePacks.get().values();
    }

    public Iterable<Locale> getSupportedLocales() {
        waitIfNotLoaded();

        return lookupMessagePacks.get().keySet();
    }


    private boolean isNotLoaded() {
        return !isLoaded();
    }

    private boolean isLoaded() {
        return lookupMessagePacks.get() != null;
    }

    private Map<Locale, MessagePack> getLookup() {
        return lookupMessagePacks.get();
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

    private class ReloadableThread extends Thread {

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
