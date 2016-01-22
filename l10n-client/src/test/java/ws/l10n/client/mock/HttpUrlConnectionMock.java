package ws.l10n.client.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * @author Serhii Bohutskyi
 */
public class HttpUrlConnectionMock extends HttpURLConnection {

    private String content;
    private int responseCode = 200;

    public HttpUrlConnectionMock(String content, int responseCode) {
        super(null);
        this.content = content;
        this.responseCode = responseCode;
    }

    public HttpUrlConnectionMock(String content) {
        super(null);
        this.content = content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content.getBytes("UTF-8"));
    }

    @Override
    public int getResponseCode() throws IOException {
        return responseCode;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public void connect() throws IOException {

    }
}
