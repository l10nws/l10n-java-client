package ws.l10n.client;

public class Options {

    private String serviceUrl;
    private String accessToken;
    private String bundleUid;
    private String version;

    private int reloadPeriod;
    private boolean showKeyIfNotExist = true;

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

    public Options setBundleUid(String bundleUid) {
        this.bundleUid = bundleUid;
        return this;
    }

    public Options setVersion(String version) {
        this.version = version;
        return this;
    }

    public Options setShowKeyIfNotExist(boolean showKeyIfNotExist) {
        this.showKeyIfNotExist = showKeyIfNotExist;
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

    public String getBundleUid() {
        return bundleUid;
    }

    public String getVersion() {
        return version;
    }

    public boolean isShowKeyIfNotExist() {
        return showKeyIfNotExist;
    }
}