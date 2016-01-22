package ws.l10n.client.impl;

import ws.l10n.client.L10nClientException;
import ws.l10n.client.MessageItem;
import ws.l10n.client.L10nClient;
import ws.l10n.client.MessageBundle;
import ws.l10n.client.impl.json.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Serhii Bohutskyi
 */
public class DefaultHttpClientImpl implements L10nClient {

    //------------------- HTTP -------------------//
    private static final String ACCESS_TOKEN_HEADER = "access-token";
    private static final String BUNDLE_UID_PARAM = "b";
    private static final String VERSION_PARAM = "v";
    private static final String LOCALES_PARAM = "l[]";
    private static final String MESSAGES_PATH = "/m";

    //------------------- JSON NAMES -------------------//
    public static final String CONTENT = "content";
    public static final String DEFAULT_LOCALE = "defaultLocale";
    public static final String MESSAGES = "messages";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String LOCALE = "locale";
    public static final String ERROR_CODE = "errorCode";
    public static final String REASON = "reason";


    private final String serviceUrl;
    private final String accessToken;

    public DefaultHttpClientImpl(String serviceUrl, String accessToken) {
        if (serviceUrl == null || serviceUrl.equals("")) {
            throw new L10nClientException("Service Url should be not empty");
        }
        if (accessToken == null || accessToken.equals("")) {
            throw new L10nClientException("AccessToken  should be not empty");
        }
        if (serviceUrl.endsWith("/")) {
            //remove last '/'
            serviceUrl = serviceUrl.substring(0, serviceUrl.length() - 1);
        }
        this.serviceUrl = serviceUrl;
        this.accessToken = accessToken;
    }

    public MessageBundle loadMessageBundle(String bundleKey, String version, String[] locales) {

        validate(bundleKey, version);

        try {

            HttpURLConnection conn = openConnection(bundleKey, version, locales);

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty(ACCESS_TOKEN_HEADER, accessToken);

            if (conn.getResponseCode() != 200) {
                String reason = tryGetReason(conn);
                throw new L10nClientException("Failed: HTTP error code : " + conn.getResponseCode()
                        + ", reason '" + reason + "'");
            }

            JsonValue jsonValue = Json.parse(new InputStreamReader(conn.getInputStream()));
            conn.disconnect();
            JsonObject jsonObject = jsonValue.asObject();

            Locale defaultLocale = toLocale(jsonObject.getString(DEFAULT_LOCALE, "en_US"));
            Map<Locale, MessageItem> map = parseContent(jsonObject);
            return new MessageBundleImpl(defaultLocale, map);

        } catch (IOException e) {
            throw new L10nClientException(e);
        } catch (ParseException ex) {
            throw new L10nClientException(ex);
        }
    }

    private void validate(String bundleKey, String version) {
        if (bundleKey == null || bundleKey.equals("")) {
            throw new L10nClientException("BundleUid should be not empty");
        }
        if (version == null || version.equals("")) {
            throw new L10nClientException("Version  should be not empty");
        }
    }

    public MessageBundle loadMessageBundle(String bundleKey, String version) {
        return loadMessageBundle(bundleKey, version, null);
    }

    private String tryGetReason(HttpURLConnection conn) {
        try {
            JsonValue jsonValue = Json.parse(new InputStreamReader(conn.getInputStream()));

            return jsonValue.asObject().getString(REASON, "");
        } catch (IOException e) {
            //skip
        }
        return "";
    }

    private HttpURLConnection openConnection(String bundleUid, String version, String[] locales) throws IOException {
        URL url = new URL(serviceUrl + MESSAGES_PATH + toQuery(bundleUid, version, locales));
        return (HttpURLConnection) url.openConnection();
    }

    private Map<Locale, MessageItem> parseContent(JsonObject jsonObject) {
        Map<Locale, MessageItem> result = new HashMap<Locale, MessageItem>();

        JsonArray locales = jsonObject.get(CONTENT).asArray();
        for (JsonValue localeMessages : locales) {
            Map<String, String> messagesMap = new HashMap<String, String>();
            JsonArray messages = localeMessages.asObject().get(MESSAGES).asArray();
            for (JsonValue message : messages) {
                String key = message.asObject().getString(KEY, null);
                String value = message.asObject().getString(VALUE, "");
                messagesMap.put(key, value);
            }
            Locale locale = toLocale(localeMessages.asObject().get(LOCALE).asString());

            result.put(locale, new MessageItemImpl(messagesMap, locale));
        }
        return result;
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new L10nClientException(e.getMessage(), e);
        }
    }

    private String toQuery(String bundleUid, String version, String[] locales) {

        StringBuilder builder = new StringBuilder("?")
                .append(BUNDLE_UID_PARAM)
                .append("=")
                .append(encode(bundleUid))
                .append("&")
                .append(VERSION_PARAM)
                .append("=")
                .append(encode(version));

        if (locales != null) {
            for (String locale : locales) {
                builder.append("&")
                        .append(LOCALES_PARAM)
                        .append("=")
                        .append(locale);
            }
        }

        return builder.toString();
    }

    private Locale toLocale(String display) {
        String[] lc = display.split("_");
        return new Locale(lc[0], lc[1]);
    }
}
