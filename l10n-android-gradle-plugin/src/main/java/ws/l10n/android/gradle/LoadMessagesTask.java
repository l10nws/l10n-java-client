package ws.l10n.android.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import ws.l10n.client.http.HttpMessageBundleClient;
import ws.l10n.core.MessageBundle;
import ws.l10n.core.MessageBundleService;
import ws.l10n.core.MessageMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class LoadMessagesTask extends DefaultTask {

    @TaskAction
    public void loadMessages() {
        getLogger().info("LoadMessages task start");
        L10nExtension extension = getProject().getExtensions().findByType(L10nExtension.class);
        validate(extension);

        MessageBundleService l10nClient = new HttpMessageBundleClient(extension.getServiceUrl(), extension.getAccessToken());
        MessageBundle messageBundle = l10nClient.load(extension.getBundleKey(), extension.getVersion());
        getLogger().info("Successfully loaded packs " + messageBundle.getMessages().size());

        Locale defaultLocale = messageBundle.getDefaultLocale();
        Map<Locale, MessageMap> messages = messageBundle.getMessages();

        for (MessageMap messageMap : messages.values()) {
            writeToFile(messageMap, messageMap.getLocale().equals(defaultLocale), extension);
        }

        getLogger().info("LoadMessages task end");

    }

    private void validate(L10nExtension extension) {
        if (extension == null) {
            getLogger().error("L10n Options var 'l10nOptions' should be described in build script.");
            return;
        }

        if (isEmpty(extension.getServiceUrl())) {
            getLogger().error("Parameter 'serviceUrl' cannot be empty.");
        }
        if (isEmpty(extension.getAccessToken())) {
            getLogger().error("Parameter 'accessToken' cannot be empty.");
        }
        if (isEmpty(extension.getBundleKey())) {
            getLogger().error("Parameter 'bundleKey' cannot be empty.");
        }
        if (isEmpty(extension.getVersion())) {
            getLogger().error("Parameter 'version' cannot be empty.");
        }
        if (isEmpty(extension.getFileName())) {
            getLogger().error("Parameter 'fileName' cannot be empty.");
        }
        if (isEmpty(extension.getResourcePath())) {
            getLogger().error("Parameter 'resourcePath' cannot be empty.");
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    private void writeToFile(MessageMap messageMap, boolean isDefault, L10nExtension extension) {
        String filePath = buildFilePath(messageMap.getLocale(), isDefault, extension);
        File file = new File(filePath);

        if (!extension.isRewriteExisted() && file.exists()) {
            getLogger().error("File already exists '{}'. Can add to l10nOptions 'rewriteExisted = true'.", filePath);
            return;
        }

        BufferedWriter out = null;
        try {
            getLogger().info("Write pack with locale '{}' to path '{}'", messageMap.getLocale(), filePath);

            out = new BufferedWriter(new FileWriter(filePath));
            out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            out.write(header.replace("%", messageMap.getLocale().toString()));
            out.write("<resources>\n");

            for (Map.Entry<String, String> entry : messageMap.getMessages().entrySet()) {
                out.write("\t<string name=\"");
                out.write(entry.getKey());
                out.write("\">");
                out.write(entry.getValue());
                out.write("</string>\n");
            }

            out.write("</resources>");

            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create message file!");
        }
    }

    /**
     * Create file path resourcePath + /values + /fileName, creates directories if not exists.
     *
     * @param locale    the locale
     * @param isDefault if default put to /values, if not to /value-localeLanguage
     * @param extension options
     * @return the file path
     */
    private String buildFilePath(Locale locale, boolean isDefault, L10nExtension extension) {

        StringBuilder pathBuilder = new StringBuilder();
        if (extension.getResourcePath().endsWith("/")) {
            pathBuilder.append(extension.getResourcePath());
        } else {
            pathBuilder.append(extension.getResourcePath()).append("/");
        }
        if (isDefault) {
            pathBuilder.append("values");
        } else {
            pathBuilder.append("values-");
            if (extension.isUseRegion()) {
                pathBuilder
                        .append(locale.getLanguage())
                        .append("-r")
                        .append(locale.getCountry());
            } else {
                pathBuilder.append(locale.getLanguage());
            }
        }

        File dir = new File(pathBuilder.toString());
        if (dir.mkdirs()) {
            getLogger().warn("Directories created" + pathBuilder.toString());
        }

        pathBuilder.append("/")
                .append(extension.getFileName().replace("/", ""));

        return pathBuilder.toString();
    }

    private String header =
            "<!--#################################################################################################################-->\n" +
                    "<!--#                                         Locale %                                                          #-->\n" +
                    "<!--#                           This resource file created by L10n plugin                                           #-->\n" +
                    "<!--#                                                                                                               #-->\n" +
                    "<!--#################################################################################################################-->\n";


}