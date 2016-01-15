package ws.l10n.spring;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.AbstractMessageSource;
import ws.l10n.core.ReloadableMessageContext;
import ws.l10n.core.impl.Options;
import ws.l10n.core.impl.ReloadableMessageContextImpl;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author Serhii Bohutskyi
 */
public class L10nBundleMessageSource extends AbstractMessageSource implements InitializingBean {

    private ReloadableMessageContext messageContext;

    private String serviceUrl;
    private String accessToken;
    private String bundleUid;
    private String version;
    private int reloadPeriod;
    private boolean useCodeAsDefaultMessage;


    public void afterPropertiesSet() throws Exception {
        messageContext = new ReloadableMessageContextImpl(generateOptions());
    }

    private Options generateOptions() {
        return new Options()
                .setBundleUid(bundleUid)
                .setAccessToken(accessToken)
                .setReloadPeriod(reloadPeriod)
                .setServiceUrl(serviceUrl)
                .setVersion(version)
                .setUseCodeAsDefaultMessage(useCodeAsDefaultMessage);
    }

    protected MessageFormat resolveCode(String code, Locale locale) {

        String message = messageContext.getMessage(code, locale);

        MessageFormat messageFormat = new MessageFormat("");
        messageFormat.setLocale(locale);
        if (message != null) {
            messageFormat.applyPattern(message);
        }
        return messageFormat;
    }

    public boolean isUseCodeAsDefaultMessage() {
        return useCodeAsDefaultMessage;
    }

    public void setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage) {
        this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
    }

    public int getReloadPeriod() {
        return reloadPeriod;
    }

    public void setReloadPeriod(int reloadPeriod) {
        this.reloadPeriod = reloadPeriod;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBundleUid() {
        return bundleUid;
    }

    public void setBundleUid(String bundleUid) {
        this.bundleUid = bundleUid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
