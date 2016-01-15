package ws.l10n.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import ws.l10n.rest.client.MessageBundle;
import ws.l10n.rest.client.MessageRestClient;
import ws.l10n.rest.client.Response;
import ws.l10n.rest.client.impl.MessageRestClientImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class LoadMessagesTask extends DefaultTask {

    @TaskAction
    public void loadMessages() {
        getLogger().warn("LoadMessagesTask");

        L10nExtension extension = (L10nExtension) getProject().getExtensions().findByName("l10nOptions");

        MessageRestClient restClient = new MessageRestClientImpl(extension.getServiceUrl(), extension.getAccessToken());
        Response response = restClient.load(extension.getBundleKey(), extension.getVersion());
        Locale defaultLocale = response.getDefaultLocale();
        Map<Locale, MessageBundle> bundles = response.getBundles();
        for (MessageBundle messageBundle : bundles.values()) {
            writeToFile(messageBundle, messageBundle.getLocale().equals(defaultLocale), extension);
        }

        getLogger().info("[L10n] END LOADING");
    }

    private void writeToFile(MessageBundle messageBundle, boolean isDefault, L10nExtension extension) {
        String fileName = extension.getBaseName() + (isDefault ? "" : "_" + messageBundle.getLocale());
        String filePath = (extension.getRelativePath().endsWith("/") ?
                extension.getRelativePath() + fileName : extension.getRelativePath() + "/" + fileName) + ".properties";
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(filePath));
            out.write(header);

            for (Map.Entry<String, String> entry : messageBundle.getMessages().entrySet()) {
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