package ws.l10n.rest.client;

/**
 * @author Serhii Bohutskyi
 */
public interface MessageRestClient {

    Response load(String bundleKey, String version);

}
