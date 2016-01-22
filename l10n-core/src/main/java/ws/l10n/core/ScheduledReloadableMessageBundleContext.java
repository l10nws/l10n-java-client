package ws.l10n.core;

import ws.l10n.client.L10nClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Anton Mokshyn
 */
public class ScheduledReloadableMessageBundleContext extends ReloadableMessageBundleContext {

    private static final int ONE_MINUTE = 60 * 1000;
    private static final int ONE_HOUR = 60 * 60 * 1000;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private long period = ONE_HOUR;

    public ScheduledReloadableMessageBundleContext(L10nClient l10nClient, String bundleKey, String bundleVersion) {
        super(l10nClient, bundleKey, bundleVersion);
    }

    public ScheduledReloadableMessageBundleContext(L10nClient l10nClient, String bundleKey, String bundleVersion,
                                                   long period) {
        super(l10nClient, bundleKey, bundleVersion);
        if(period < ONE_MINUTE) {
            throw new IllegalArgumentException("scheduler period cannot be less that 1 minute");
        }
        this.period = period;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                reload();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }

}
