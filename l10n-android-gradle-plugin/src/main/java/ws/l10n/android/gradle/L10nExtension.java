package ws.l10n.android.gradle;

/**
 * @author Serhii Bohutskyi
 */
public class L10nExtension {
    private String serviceUrl = "https://l10n.ws/api/v1";
    private String accessToken;
    private String bundleKey;
    private String version;
    private String fileName = "strings.xml";
    private String resourcePath = "./src/main/res";

    private boolean useRegion = false;
    private boolean rewriteExisted = false;

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getBundleKey() {
        return bundleKey;
    }

    public void setBundleKey(String bundleKey) {
        this.bundleKey = bundleKey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public boolean isRewriteExisted() {
        return rewriteExisted;
    }

    public void setRewriteExisted(boolean rewriteExisted) {
        this.rewriteExisted = rewriteExisted;
    }

    public boolean isUseRegion() {
        return useRegion;
    }

    public void setUseRegion(boolean useRegion) {
        this.useRegion = useRegion;
    }
}
