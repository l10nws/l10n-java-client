package ws.l10n.core.impl;

import ws.l10n.core.ReloadableMessageContext;
import ws.l10n.rest.client.MessageRestClient;
import ws.l10n.rest.client.impl.MessageRestClientImpl;

/**
 * @author Serhii Bohutskyi
 */
public class MessageContextFactory {

    public static ReloadableMessageContext create(Options options) {
        MessageRestClient restClient = new MessageRestClientImpl(options.getServiceUrl(), options.getAccessToken());
        ReloadableMessageContextImpl reloadableMessageContext = new ReloadableMessageContextImpl(restClient, options);
        reloadableMessageContext.init();
        return reloadableMessageContext;
    }
}
