package ws.l10n.rest.client;

import java.util.Locale;

public interface MessageBundle {

    String getMessage(String key);

    Locale getLocale();
}