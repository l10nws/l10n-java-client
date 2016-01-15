package ws.l10n.rest.client;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ws.l10n.rest.client.impl.MessageClientException;
import ws.l10n.rest.client.impl.MessageRestClientImpl;
import ws.l10n.rest.client.mock.HttpUrlConnectionMock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.easymock.PowerMock.*;
import static ws.l10n.rest.client.utils.LocaleUtils.toLocale;

/**
 * @author Serhii Bohutskyi
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MessageRestClientImpl.class)
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

        MessageRestClientImpl client = PowerMock.createPartialMock(MessageRestClientImpl.class,
                new String[]{"openConnection"}, "serviceUrl", "accessToken");
        expectPrivate(client, "openConnection", "bundleId", "version", null)
                .andReturn(new HttpUrlConnectionMock(okResponse));

        replay(client);

        Response response = client.load("bundleId", "version");

        assertNotNull(response);
        assertNotNull(response.getDefaultLocale());

        verify(client);
    }

    @Test
    public void successWithLocalesTest() throws Exception {
        String[] locales = {"en_US", "en_UK"};

        MessageRestClientImpl client = PowerMock.createPartialMock(MessageRestClientImpl.class,
                new String[]{"openConnection"}, "serviceUrl", "accessToken");
        expectPrivate(client, "openConnection", "bundleId", "version", locales)
                .andReturn(new HttpUrlConnectionMock(okResponse));

        replay(client);

        Response response = client.load("bundleId", "version", locales);

        assertNotNull(response);
        assertEquals(toLocale("en_US"), response.getDefaultLocale());

        Map<String, String> enUsMessages = response.getMessagePacks().get(toLocale("en_US")).getMessages();
        assertEquals("bar", enUsMessages.get("foo"));
        assertEquals("baz", enUsMessages.get("biz"));

        Map<String, String> enUkMessages = response.getMessagePacks().get(toLocale("en_UK")).getMessages();
        assertEquals("Zzyzx", enUkMessages.get("Xyzzy"));
        assertEquals("fu", enUkMessages.get("sna"));

        verify(client);
    }

    @Test(expected = MessageClientException.class)
    public void errorTest() throws Exception {
        MessageRestClientImpl client = PowerMock.createPartialMock(MessageRestClientImpl.class,
                new String[]{"openConnection"}, "serviceUrl", "accessToken");
        expectPrivate(client, "openConnection", "bundleId", "version", null)
                .andReturn(new HttpUrlConnectionMock(errorResponse, 401));
        replay(client);

        client.load("bundleId", "version");

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
