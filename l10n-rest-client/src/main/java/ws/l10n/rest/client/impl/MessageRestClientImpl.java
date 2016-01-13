package ws.l10n.rest.client.impl;

import ws.l10n.rest.client.MessageBundle;
import ws.l10n.rest.client.MessageRestClient;
import ws.l10n.rest.client.Response;
import ws.l10n.rest.client.impl.json.*;

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
public class MessageRestClientImpl implements MessageRestClient {

    //------------------- HTTP -------------------//
    private static final String ACCESS_TOKEN_HEADER = "access-token";
    private static final String BUNDLE_UID_PARAM = "b";
    private static final String VERSION_PARAM = "v";
    private static final String LOCALES_PARAM = "l";

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

    public MessageRestClientImpl(String serviceUrl, String accessToken) {
        if (serviceUrl == null || serviceUrl.equals("")) {
            throw new MessageClientException("Service Url should be not empty");
        }
        if (accessToken == null || accessToken.equals("")) {
            throw new MessageClientException("AccessToken  should be not empty");
        }
        this.serviceUrl = serviceUrl;
        this.accessToken = accessToken;
    }

    public Response load(String bundleKey, String version) {
        if (bundleKey == null || bundleKey.equals("")) {
            throw new MessageClientException("BundleUid should be not empty");
        }
        if (version == null || version.equals("")) {
            throw new MessageClientException("Version  should be not empty");
        }

        try {

            HttpURLConnection conn = openConnection(bundleKey, version);

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty(ACCESS_TOKEN_HEADER, accessToken);

            if (conn.getResponseCode() != 200) {
                String reason = tryGetReason(conn);
                throw new MessageClientException("Failed: HTTP error code : " + conn.getResponseCode()
                        + ", reason '" + reason + "'");
            }

            JsonValue jsonValue = Json.parse(new InputStreamReader(conn.getInputStream()));
            conn.disconnect();
            JsonObject jsonObject = jsonValue.asObject();

            Locale defaultLocale = parseLocale(jsonObject.getString(DEFAULT_LOCALE, "en_US"));
            Map<Locale, MessageBundle> map = parseContent(jsonObject);
            return new ResponseImpl(defaultLocale, map);

        } catch (IOException | ParseException e) {
            throw new MessageClientException(e);
        }
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

    private HttpURLConnection openConnection(String bundleUid, String version) throws IOException {
        URL url = new URL(serviceUrl + toQuery(bundleUid, version));
        return (HttpURLConnection) url.openConnection();
    }

    private Locale parseLocale(String display) {
        String[] lc = display.split("_");
        return new Locale(lc[0], lc[1]);
    }

    private Map<Locale, MessageBundle> parseContent(JsonObject jsonObject) {
        Map<Locale, MessageBundle> result = new HashMap<Locale, MessageBundle>();

        JsonArray locales = jsonObject.get(CONTENT).asArray();
        for (JsonValue localeMessages : locales) {
            Map<String, String> messagesMap = new HashMap<String, String>();
            JsonArray messages = localeMessages.asObject().get(MESSAGES).asArray();
            for (JsonValue message : messages) {
                String key = message.asObject().getString(KEY, null);
                String value = message.asObject().getString(VALUE, "");
                messagesMap.put(key, value);
            }
            Locale locale = parseLocale(localeMessages.asObject().get(LOCALE).asString());

            result.put(locale, new MessageBundleImpl(messagesMap, locale));
        }
        return result;
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new MessageClientException(e.getMessage(), e);
        }
    }

    private String toQuery(String bundleUid, String version) {
        StringBuilder builder = new StringBuilder("?")
                .append(BUNDLE_UID_PARAM)
                .append("=")
                .append(encode(bundleUid))
                .append("&")
                .append(VERSION_PARAM)
                .append("=")
                .append(encode(version));
        return builder.toString();
    }
}
