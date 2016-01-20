package ws.l10n.core.impl;

import ws.l10n.core.ReloadableMessageContext;
import ws.l10n.rest.client.MessageRestClient;
import ws.l10n.rest.client.impl.MessageRestClientImpl;

/**
 * Factory for creating message context.
 *
 * @author Serhii Bohutskyi
 */
public class MessageContextFactory {

    /**
     * Create and initialize reloadable message context.
     *
     * @param options the options of context
     * @return the reloadable message context
     */
    public static ReloadableMessageContext create(Options options) {
        MessageRestClient restClient = new MessageRestClientImpl(options.getServiceUrl(), options.getAccessToken());
        ReloadableMessageContextImpl reloadableMessageContext = new ReloadableMessageContextImpl(restClient, options);
        reloadableMessageContext.init();
        return reloadableMessageContext;
    }
}
