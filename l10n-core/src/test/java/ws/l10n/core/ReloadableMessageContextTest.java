package ws.l10n.core;

import org.junit.Test;
import ws.l10n.core.impl.Options;
import ws.l10n.core.impl.ReloadableMessageContextImpl;
import ws.l10n.rest.client.MessagePack;

/**
 * @author Serhii Bohutskyi
 */
public class ReloadableMessageContextTest {

    @Test
    public void test() throws InterruptedException {
        Options options = new Options()
                .setServiceUrl("http://localhost:9000/api/m")
                .setAccessToken("C85hg")
                .setBundleUid("C85hg")
                .setVersion("1 ")
                .setReloadPeriod(1000);
        ReloadableMessageContext messageContext = new ReloadableMessageContextImpl(options);
        Iterable<MessagePack> messageBundles = messageContext.getMessageBundles();
        Thread.sleep(100000000);
        System.out.println(messageBundles);
    }
}
