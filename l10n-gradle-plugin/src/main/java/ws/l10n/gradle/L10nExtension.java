package ws.l10n.gradle;

/**
 * @author Serhii Bohutskyi
 */
public class L10nExtension {
    private String serviceUrl = "http://l10n.ws/api/v1/m";
    private String accessToken;
    private String bundleKey;
    private String version;
    private String baseName = "messages";
    private String relativePath = "./src/main/resources";

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

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
