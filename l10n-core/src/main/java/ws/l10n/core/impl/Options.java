package ws.l10n.core.impl;

/**
 * Options of message context.
 */
public class Options {

    /**
     * Url of the service
     */
    private String serviceUrl;
    /**
     * User access token.
     */
    private String accessToken;
    /**
     * Bundle key.
     */
    private String bundleKey;
    /**
     * Version of bundle.
     */
    private String version;

    /**
     * Reload period in milliseconds. Can't be less then one minute.
     */
    private int reloadPeriod;
    /**
     * Use code as default message if message with such code not exists.
     */
    private boolean useCodeAsDefaultMessage = true;



    public Options() {
    }

    public Options setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
        return this;
    }

    public Options setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Options setReloadPeriod(int reloadPeriod) {
        this.reloadPeriod = reloadPeriod;
        return this;
    }

    public Options setBundleKey(String bundleKey) {
        this.bundleKey = bundleKey;
        return this;
    }

    public Options setVersion(String version) {
        this.version = version;
        return this;
    }

    public Options setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage) {
        this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
        return this;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getReloadPeriod() {
        return reloadPeriod;
    }

    public String getBundleKey() {
        return bundleKey;
    }

    public String getVersion() {
        return version;
    }

    public boolean isUseCodeAsDefaultMessage() {
        return useCodeAsDefaultMessage;
    }
}