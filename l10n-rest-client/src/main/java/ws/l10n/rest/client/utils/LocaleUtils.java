package ws.l10n.rest.client.utils;

import java.util.Locale;

/**
 * @author Serhii Bohutskyi
 */
public class LocaleUtils {

    public static Locale toLocale(String display) {
        String[] lc = display.split("_");
        return new Locale(lc[0], lc[1]);
    }

    public static String toString(Locale locale) {
        return locale.getDisplayName();
    }

}
