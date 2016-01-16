package ws.l10n.core.impl;

public class Options {

    private String serviceUrl;
    private String accessToken;
    private String bundleKey;
    private String version;

    private int reloadPeriod;
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