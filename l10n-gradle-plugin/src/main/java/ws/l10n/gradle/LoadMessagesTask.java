package ws.l10n.gradle;

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
        if (isEmpty(extension.getBaseName())) {
            getLogger().error("Parameter 'baseName' cannot be empty.");
        }
        if (isEmpty(extension.getPath())) {
            getLogger().error("Parameter 'path' cannot be empty.");
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    private void writeToFile(MessageMap messageMap, boolean isDefault, L10nExtension extension) {
        String fileName = extension.getBaseName() + (isDefault ? "" : "_" + messageMap.getLocale());
        File dir = new File(extension.getPath());
        if (dir.mkdirs()) {
            getLogger().warn("Directories created");
        }
        String filePath = (extension.getPath().endsWith("/") ?
                extension.getPath() + fileName : extension.getPath() + "/" + fileName) + ".properties";
        BufferedWriter out = null;
        try {
            getLogger().info("Write pack with locale " + messageMap.getLocale().getDisplayName() + " to file '" +
                    filePath + "'");

            out = new BufferedWriter(new FileWriter(filePath));
            out.write(header);

            for (Map.Entry<String, String> entry : messageMap.getMessages().entrySet()) {
                out.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create message file!");
        }
    }

    private String header =
            "########################################################################################################################\n" +
                    "#                                                                                                                      #\n" +
                    "#                                  This properties file created by L10n plugin                                         #\n" +
                    "#                                                                                                                      #\n" +
                    "########################################################################################################################\n\n";

}