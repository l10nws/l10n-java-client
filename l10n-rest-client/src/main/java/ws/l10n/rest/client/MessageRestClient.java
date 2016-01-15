package ws.l10n.rest.client;

/**
 * Interface describes main L10n APIs functionality
 * <p/>
 * In case of error or exceptions throws runtime {@link ws.l10n.rest.client.impl.MessageClientException}.
 *
 * @author Serhii Bohutskyi
 */
public interface MessageRestClient {

    /**
     * Loads messages and put them to {@link Response} object.
     *
     * @param bundleKey key of bundle
     * @param version   version string
     * @return {@link Response}
     */
    Response load(String bundleKey, String version);

    /**
     * The same as above only loads requested locales.
     *
     * @param bundleKey key of bundle
     * @param version   version string
     * @param locales   requested locales
     * @return {@link Response}
     */
    Response load(String bundleKey, String version, String[] locales);

}
