package ws.l10n.core.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ws.l10n.core.*;

import java.util.*;

import static org.easymock.EasyMock.*;

/**
 * @author Serhii Bohutskyi
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ScheduledReloadableMessageBundleContext.class)
public class ReloadableMessageContextTest {

    @Test
    public void contextTest() throws InterruptedException {

        MessageBundleService messageBundleService = createMock(MessageBundleService.class);

        ReloadableMessageBundleContext context = new ReloadableMessageBundleContext(messageBundleService, "bundleKey", "bundleVersion");
        expect(messageBundleService.load("bundleKey", "bundleVersion")).andReturn(createResponse()).anyTimes();
        replay(messageBundleService);
        context.init();

        Assert.assertNotNull(context.getMessage("foo", Locale.CHINA));

    }

    private MessageBundle createResponse() {
        Map<Locale, MessageMap> content = new HashMap<Locale, MessageMap>();
        content.put(Locale.ENGLISH, new MessageMapMock(createRandomMessages(), Locale.ENGLISH));
        content.put(Locale.CANADA, new MessageMapMock(createRandomMessages(), Locale.CANADA));
        content.put(Locale.CHINA, new MessageMapMock(createRandomMessages(), Locale.CHINA));
        return new MessageBundleMock(Locale.ENGLISH, content, Arrays.asList(Locale.ENGLISH, Locale.CANADA, Locale.CHINA));
    }

    private Map<String, String> createRandomMessages() {
        Map<String, String> messages = new HashMap<String, String>();
        messages.put("foo", UUID.randomUUID().toString());
        messages.put("biz", UUID.randomUUID().toString());
        return messages;
    }
}

