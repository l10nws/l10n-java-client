package ws.l10n.spring;

import org.springframework.context.support.AbstractMessageSource;
import ws.l10n.core.MessageBundleContext;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * Implementation of the {@link org.springframework.context.MessageSource} interface
 *
 * @author Serhii Bohutskyi
 * @author Anton Mokshyn
 */
public class L10nBundleMessageSource extends AbstractMessageSource {

    private final MessageBundleContext messageContext;

    public L10nBundleMessageSource(MessageBundleContext messageContext) {
        this.messageContext = messageContext;
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

        MessageFormat messageFormat = createMessageFormat(code, locale);
        if (message != null) {
            messageFormat.applyPattern(message);
        }

        return messageFormat;
    }

}
