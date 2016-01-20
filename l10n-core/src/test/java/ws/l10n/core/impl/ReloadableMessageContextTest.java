package ws.l10n.core.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ws.l10n.core.ReloadableMessageContext;
import ws.l10n.rest.client.MessagePack;
import ws.l10n.rest.client.MessageRestClient;
import ws.l10n.rest.client.Response;
import ws.l10n.rest.client.impl.MessagePackImpl;
import ws.l10n.rest.client.impl.ResponseImpl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.easymock.EasyMock.*;

/**
 * @author Serhii Bohutskyi
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ReloadableMessageContextImpl.class)
public class ReloadableMessageContextTest {

    @Test
    public void contextTest() throws InterruptedException {
        Options options = new Options()
                .setServiceUrl("serviceUrl")
                .setAccessToken("accessToken")
                .setBundleKey("bundleKey")
                .setVersion("1.0.0")
                .setReloadPeriod(70 * 1000)
                .setUseCodeAsDefaultMessage(false);

        ReloadableMessageContext messageContext = MessageContextFactory.create(options);

        MessageRestClient restClient = createMock(MessageRestClient.class);

        ReloadableMessageContextImpl context = new ReloadableMessageContextImpl(restClient, createOptions());
        expect(restClient.load("bundleKey", "1.0.0")).andReturn(createResponse()).anyTimes();
        replay(restClient);
        context.init();

        Assert.assertNotNull(context.getMessage("foo", Locale.CHINA));

    }

    private Response createResponse() {
        Map<Locale, MessagePack> content = new HashMap<Locale, MessagePack>();
        content.put(Locale.ENGLISH, new MessagePackImpl(createRandomMessages(), Locale.ENGLISH));
        content.put(Locale.CANADA, new MessagePackImpl(createRandomMessages(), Locale.CANADA));
        content.put(Locale.CHINA, new MessagePackImpl(createRandomMessages(), Locale.CHINA));
        return new ResponseImpl(Locale.ENGLISH, content);
    }

    private Map<String, String> createRandomMessages() {
        Map<String, String> messages = new HashMap<String, String>();
        messages.put("foo", UUID.randomUUID().toString());
        messages.put("biz", UUID.randomUUID().toString());
        return messages;
    }

    private Options createOptions() {
        return new Options()
                .setServiceUrl("serviceUrl")
                .setAccessToken("accessToken")
                .setBundleKey("bundleKey")
                .setVersion("1.0.0")
                .setReloadPeriod(70 * 1000)
                .setUseCodeAsDefaultMessage(false);
    }
}
