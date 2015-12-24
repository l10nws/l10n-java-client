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

import static org.junit.Assert.assertNotNull;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * @author Serhii Bohutskyi
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MessageRestClientImpl.class)
public class ClientTest {


    private static String okContent = "";
    private static String errorContent = "";

    @BeforeClass
    public static void setup() throws IOException {
        okContent = readFile("/messages.json");
        errorContent = readFile("/error.json");
    }

    @Test
    public void testSuccess() throws Exception {

        MessageRestClientImpl client = PowerMock.createPartialMock(MessageRestClientImpl.class,
                new String[]{"openConnection"}, "serviceUrl", "accessToken");
        expectPrivate(client, "openConnection", "bundleId", "version")
                .andReturn(new HttpUrlConnectionMock(okContent));

        replay(client);

        Response response = client.load("bundleId", "version");

        assertNotNull(response);
        assertNotNull(response.getDefaultLocale());


        verify(client);
    }

    @Test(expected = MessageClientException.class)
    public void testError() throws Exception {
        MessageRestClientImpl client = PowerMock.createPartialMock(MessageRestClientImpl.class,
                new String[]{"openConnection"}, "serviceUrl", "accessToken");
        expectPrivate(client, "openConnection", "bundleId", "version")
                .andReturn(new HttpUrlConnectionMock(errorContent, 401));
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
