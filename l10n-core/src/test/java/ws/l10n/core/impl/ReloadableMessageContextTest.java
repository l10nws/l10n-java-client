package ws.l10n.core.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ws.l10n.client.L10nClient;
import ws.l10n.client.MessageBundle;
import ws.l10n.client.MessageMap;
import ws.l10n.client.impl.MessageBundleImpl;
import ws.l10n.client.impl.MessageMapImpl;
import ws.l10n.core.ScheduledReloadableMessageBundleContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.easymock.EasyMock.createMock;

/**
 * @author Serhii Bohutskyi
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ScheduledReloadableMessageBundleContext.class)
public class ReloadableMessageContextTest {

    @Test
    public void contextTest() throws InterruptedException {
//        Options options = new Options()
//                .setServiceUrl("serviceUrl")
//                .setAccessToken("accessToken")
//                .setBundleKey("bundleKey")
//                .setVersion("1.0.0")
//                .setReloadPeriod(70 * 1000)
//                .setUseCodeAsDefaultMessage(false);
//
//        MessageBundleContext messageContext = MessageContextFactory.create(options);

        L10nClient restL10nClient = createMock(L10nClient.class);

//        ReloadableMessageBundleContextImpl context = new ReloadableMessageBundleContextImpl(restL10nClient, createOptions());
//        expect(restL10nClient.loadMessageBundle("bundleKey", "1.0.0")).andReturn(createResponse()).anyTimes();
//        replay(restL10nClient);
//        context.init();

//        Assert.assertNotNull(context.getMessage("foo", Locale.CHINA));

    }

    private MessageBundle createResponse() {
        Map<Locale, MessageMap> content = new HashMap<Locale, MessageMap>();
        content.put(Locale.ENGLISH, new MessageMapImpl(createRandomMessages(), Locale.ENGLISH));
        content.put(Locale.CANADA, new MessageMapImpl(createRandomMessages(), Locale.CANADA));
        content.put(Locale.CHINA, new MessageMapImpl(createRandomMessages(), Locale.CHINA));
        return new MessageBundleImpl(Locale.ENGLISH, content);
    }

    private Map<String, String> createRandomMessages() {
        Map<String, String> messages = new HashMap<String, String>();
        messages.put("foo", UUID.randomUUID().toString());
        messages.put("biz", UUID.randomUUID().toString());
        return messages;
    }

//    private Options createOptions() {
//        return new Options()
//                .setServiceUrl("serviceUrl")
//                .setAccessToken("accessToken")
//                .setBundleKey("bundleKey")
//                .setVersion("1.0.0")
//                .setReloadPeriod(70 * 1000)
//                .setUseCodeAsDefaultMessage(false);
//    }
}
