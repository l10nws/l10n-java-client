package ws.l10n.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import ws.l10n.rest.client.MessagePack;
import ws.l10n.rest.client.MessageRestClient;
import ws.l10n.rest.client.Response;
import ws.l10n.rest.client.impl.MessageRestClientImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * @author Serhii Bohutskyi
 */
@Mojo(name = "load")
public class MessageMojo extends AbstractMojo {

    @Parameter(property = "load.serviceUrl", defaultValue = "https://l10n.ws/api/v1")
    private String serviceUrl;
    @Parameter(property = "load.accessToken", required = true)
    private String accessToken;
    @Parameter(property = "load.bundleKey", required = true)
    private String bundleKey;
    @Parameter(property = "load.version", required = true)
    private String version;

    @Parameter(property = "load.baseName", defaultValue = "messages")
    private String baseName;
    @Parameter(property = "load.path", defaultValue = "./src/main/resources")
    private String path;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("[L10n] START LOADING");

        MessageRestClient restClient = new MessageRestClientImpl(serviceUrl, accessToken);
        Response response = restClient.load(bundleKey, version);
        Locale defaultLocale = response.getDefaultLocale();
        Map<Locale, MessagePack> packs = response.getMessagePacks();
        for (MessagePack messagePack : packs.values()) {
            writeToFile(messagePack, messagePack.getLocale().equals(defaultLocale));
        }

        getLog().info("[L10n] END LOADING");
    }

    private void writeToFile(MessagePack messagePack, boolean isDefault) {
        String fileName = baseName + (isDefault ? "" : "_" + messagePack.getLocale());
        String filePath = (path.endsWith("/") ? path + fileName : path + "/" + fileName) + ".properties";
        BufferedWriter out = null;
        try {

            getLog().info("Write pack with locale " + messagePack.getLocale().getDisplayName() + " to file '" +
                    filePath + "'");

            out = new BufferedWriter(new FileWriter(filePath));
            out.write(header);

            for (Map.Entry<String, String> entry : messagePack.getMessages().entrySet()) {
                out.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
            out.close();
        } catch (IOException e) {
            getLog().info(e.getMessage());
            throw new RuntimeException("Cannot create message file!");
        }
    }

    private String header = "########################################################################################################################\n" +
            "#                                                                                                                      #\n" +
            "#                                  This properties file created by L10n plugin                                         #\n" +
            "#                                                                                                                      #\n" +
            "########################################################################################################################\n\n";
}
