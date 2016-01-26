package ws.l10n.client;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ws.l10n.client.http.HttpMessageBundleClient;
import ws.l10n.client.mock.HttpUrlConnectionMock;
import ws.l10n.core.ServiceException;
import ws.l10n.core.MessageBundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.easymock.PowerMock.expectPrivate;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

/**
 * @author Serhii Bohutskyi
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpMessageBundleClient.class)
public class ClientTest {


    private static String okResponse = "";
    private static String errorResponse = "";

    @BeforeClass
    public static void setup() throws IOException {
        okResponse = readFile("/messages.json");
        errorResponse = readFile("/error.json");
    }

    @Test
    public void successTest() throws Exception {

        HttpMessageBundleClient client = PowerMock.createPartialMock(HttpMessageBundleClient.class,
                new String[]{"openConnection"}, "serviceUrl", "accessToken");
        expectPrivate(client, "openConnection", "bundleId", "version", null)
                .andReturn(new HttpUrlConnectionMock(okResponse));

        replay(client);

        MessageBundle messageBundle = client.loadMessageBundle("bundleId", "version");

        assertNotNull(messageBundle);
        assertNotNull(messageBundle.getDefaultLocale());

        verify(client);
    }

    @Test
    public void successWithLocalesTest() throws Exception {
        String[] locales = {"en_US", "en_UK"};

        HttpMessageBundleClient client = PowerMock.createPartialMock(HttpMessageBundleClient.class,
                new String[]{"openConnection"}, "serviceUrl", "accessToken");
        expectPrivate(client, "openConnection", "bundleId", "version", locales)
                .andReturn(new HttpUrlConnectionMock(okResponse));

        replay(client);

        MessageBundle messageBundle = client.loadMessageBundle("bundleId", "version", locales);

        assertNotNull(messageBundle);
        assertEquals(new Locale("en", "US"), messageBundle.getDefaultLocale());

        Map<String, String> enUsMessages = messageBundle.getMessages().get(new Locale("en", "US")).getMessages();
        assertEquals("bar", enUsMessages.get("foo"));
        assertEquals("baz", enUsMessages.get("biz"));

        Map<String, String> enUkMessages = messageBundle.getMessages().get(new Locale("en", "US")).getMessages();
        assertEquals("Zzyzx", enUkMessages.get("Xyzzy"));
        assertEquals("fu", enUkMessages.get("sna"));

        verify(client);
    }

    @Test(expected = ServiceException.class)
    public void errorTest() throws Exception {
        HttpMessageBundleClient client = PowerMock.createPartialMock(HttpMessageBundleClient.class,
                new String[]{"openConnection"}, "serviceUrl", "accessToken");
        expectPrivate(client, "openConnection", "bundleId", "version", null)
                .andReturn(new HttpUrlConnectionMock(errorResponse, 401));
        replay(client);

        client.loadMessageBundle("bundleId", "version");

        verify(client);
    }


    private static String readFile(String filename) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(ClientTest.class
                .getResourceAsStream(filename)));
        String result = "";
        String line;
        while ((line = reader.readLine()) != null) {
            result += line;
        }
        reader.close();
        return result;
    }
}
