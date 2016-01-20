package ws.l10n.spring;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.AbstractMessageSource;
import ws.l10n.core.ReloadableMessageContext;
import ws.l10n.core.impl.MessageContextFactory;
import ws.l10n.core.impl.Options;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * Implementation of the {@link org.springframework.context.MessageSource} interface,
 * implementing loading messages from L10n service with reload period or once.
 *
 * @author Serhii Bohutskyi
 */
public class L10nBundleMessageSource extends AbstractMessageSource implements InitializingBean {

    private ReloadableMessageContext messageContext;

    private String serviceUrl;
    private String accessToken;
    private String bundleKey;
    private String version;
    private int reloadPeriod;
    private boolean useCodeAsDefaultMessage;


    /**
     * Initialize L10n message context.
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        messageContext = MessageContextFactory.create(generateOptions());
    }

    private Options generateOptions() {
        return new Options()
                .setBundleKey(bundleKey)
                .setAccessToken(accessToken)
                .setReloadPeriod(reloadPeriod)
                .setServiceUrl(serviceUrl)
                .setVersion(version)
                .setUseCodeAsDefaultMessage(useCodeAsDefaultMessage);
    }

    /**
     * Getting message from l10n context and apply format.
     *
     * @param code   message key
     * @param locale the locale
     * @return message format
     */
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

    public String getBundleKey() {
        return bundleKey;
    }

    public void setBundleKey(String bundleKey) {
        this.bundleKey = bundleKey;
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
