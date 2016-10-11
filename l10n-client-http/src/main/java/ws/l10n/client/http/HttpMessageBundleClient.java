package ws.l10n.client.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.l10n.client.http.json.*;
import ws.l10n.core.MessageBundle;
import ws.l10n.core.MessageBundleService;
import ws.l10n.core.MessageMap;
import ws.l10n.core.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author Serhii Bohutskyi
 */
public class HttpMessageBundleClient implements MessageBundleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpMessageBundleClient.class);

    //------------------- HTTP -------------------//
    private static final String ACCESS_TOKEN_HEADER = "access-token";
    private static final String BUNDLE_UID_PARAM = "b";
    private static final String VERSION_PARAM = "v";
    private static final String LOCALES_PARAM = "l[]";
    private static final String MESSAGES_PATH = "/m";

    //------------------- JSON NAMES -------------------//
    public static final String CONTENT = "content";
    public static final String DEFAULT_LOCALE = "defaultLocale";
    public static final String SUPPORTED_LOCALES = "supportedLocales";
    public static final String MESSAGES = "messages";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String LOCALE = "locale";
    public static final String ERROR_CODE = "errorCode";
    public static final String REASON = "reason";


    private final String serviceUrl;
    private final String accessToken;

    public HttpMessageBundleClient(String serviceUrl, String accessToken) {
        if (serviceUrl == null || serviceUrl.equals("")) {
            throw new ServiceException("Service Url should be not empty");
        }
        if (accessToken == null || accessToken.equals("")) {
            throw new ServiceException("AccessToken should be not empty");
        }
        if (serviceUrl.endsWith("/")) {
            //remove last '/'
            serviceUrl = serviceUrl.substring(0, serviceUrl.length() - 1);
        }
        this.serviceUrl = serviceUrl;
        this.accessToken = accessToken;
    }

    public MessageBundle load(String bundleKey, String version, String[] locales) {

        validate(bundleKey, version);

        try {

            HttpURLConnection conn = openConnection(bundleKey, version, locales);

            if (conn.getResponseCode() == 301) { // redirect
                String location = conn.getHeaderField("Location");
                LOGGER.debug("Http request redirected to {}", location);
                conn = openConnection(location);
            }

            if (conn.getResponseCode() != 200) {
                String reason = tryGetReason(conn);
                LOGGER.error("Failed: HTTP error code : " + conn.getResponseCode() + ", reason '" + reason + "'");
                throw new ServiceException("Failed: HTTP error code : " + conn.getResponseCode() + ", reason '" + reason + "'");
            }

            MessageBundle messageBundle = parse(conn.getInputStream());
            conn.disconnect();
            return messageBundle;
        } catch (IOException e) {
            LOGGER.error("Load messages error", e);
            throw new ServiceException(e);
        } catch (ParseException ex) {
            LOGGER.error("Parse JSON error", ex);
            throw new ServiceException(ex);
        }
    }

    private MessageBundle parse(InputStream inputStream) {
        JsonValue jsonValue = Json.parse(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        JsonObject jsonObject = jsonValue.asObject();
        List<Locale> supportedLocales = new ArrayList<Locale>();
        for (JsonValue supportedLocale : jsonObject.get(SUPPORTED_LOCALES).asArray()) {
            supportedLocales.add(toLocale(supportedLocale.asString()));
        }
        Locale defaultLocale = toLocale(jsonObject.getString(DEFAULT_LOCALE, "en_US"));
        Map<Locale, MessageMap> map = parseContent(jsonObject);

        return new MessageBundleImpl(defaultLocale, map, supportedLocales);
    }

    private void validate(String bundleKey, String version) {
        if (bundleKey == null || bundleKey.equals("")) {
            throw new ServiceException("BundleUid should be not empty");
        }
        if (version == null || version.equals("")) {
            throw new ServiceException("Version  should be not empty");
        }
    }

    public MessageBundle load(String bundleKey, String version) {
        return load(bundleKey, version, null);
    }

    private String tryGetReason(HttpURLConnection conn) throws IOException {
        try {
            JsonValue jsonValue = Json.parse(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
            return jsonValue.asObject().getString(REASON, "");
        } catch (ParseException e) {
            LOGGER.warn("Cannot parse response.", e);
        }
        return "unknown";
    }

    private HttpURLConnection openConnection(String bundleUid, String version, String[] locales) throws IOException {
        URL url = new URL(serviceUrl + MESSAGES_PATH + toQuery(bundleUid, version, locales));
        LOGGER.debug("Open Http connection {}", url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        setDefaultHeaders(httpURLConnection);
        return httpURLConnection;
    }

    private HttpURLConnection openConnection(String spec) throws IOException {
        LOGGER.debug("Open Http connection {}", spec);
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(spec).openConnection();
        setDefaultHeaders(httpURLConnection);
        return httpURLConnection;
    }

    private void setDefaultHeaders(HttpURLConnection conn) throws ProtocolException {
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty(ACCESS_TOKEN_HEADER, accessToken);
    }

    private Map<Locale, MessageMap> parseContent(JsonObject jsonObject) {
        Map<Locale, MessageMap> result = new HashMap<Locale, MessageMap>();

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

            result.put(locale, new MessageMapImpl(messagesMap, locale));
        }
        return result;
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e.getMessage(), e);
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
