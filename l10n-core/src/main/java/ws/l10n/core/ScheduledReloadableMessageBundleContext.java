package ws.l10n.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Anton Mokshyn
 */
public class ScheduledReloadableMessageBundleContext extends ReloadableMessageBundleContext {

    private static final int ONE_SECOND = 1000;
    private static final int ONE_HOUR = 60 * 60 * 1000;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private long period = ONE_HOUR;

    public ScheduledReloadableMessageBundleContext(MessageBundleService messageBundleService, String bundleKey, String bundleVersion) {
        super(messageBundleService, bundleKey, bundleVersion);
    }

    public ScheduledReloadableMessageBundleContext(MessageBundleService messageBundleService, String bundleKey, String bundleVersion,
                                                   long period) {
        super(messageBundleService, bundleKey, bundleVersion);
        if(period < ONE_SECOND) {
            throw new IllegalArgumentException("Scheduler period cannot be less that 1 second");
        }
        this.period = period;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                reload();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }

}
