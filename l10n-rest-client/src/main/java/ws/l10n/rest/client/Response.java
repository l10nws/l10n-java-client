package ws.l10n.rest.client;

import java.util.Locale;
import java.util.Map;

/**
 * @author Serhii Bohutskyi
 */
public interface Response {

    Locale getDefaultLocale();

    Map<Locale, MessageBundle> getBundles();
}
