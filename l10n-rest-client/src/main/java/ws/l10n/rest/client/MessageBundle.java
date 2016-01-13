package ws.l10n.rest.client;

import java.util.Locale;
import java.util.Map;

public interface MessageBundle {

    Map<String, String> getMessages();

    String getMessage(String code);

    Locale getLocale();
}