# L10n

REST client for loading messages from service L10n

        String serviceRestUrl = "http://l10n.ws/api/m";
        String userAccessToken = "****";
        
        String bundleUid = "1";
        
        MessageRestClient messageRestClient = new MessageRestClientImpl(serviceRestUrl, userAccessToken);
        Map<String, Iterable<Message>> messagesByLocales = messageRestClient.load(bundleUid, "0.0.1");
        

