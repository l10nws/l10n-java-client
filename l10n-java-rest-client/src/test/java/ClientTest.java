import org.junit.Test;
import ws.l10n.rest.client.MessageRestClient;
import ws.l10n.rest.client.impl.MessageRestClientImpl;

/**
 * @author Serhii Bohutskyi
 */
public class ClientTest {

    @Test
    public void test(){
        MessageRestClient messageRestClient = new MessageRestClientImpl("http://localhost:9000/api/m","C85hg");
         messageRestClient.load("C85hg", "1 ");
        System.out.println();
    }
}
