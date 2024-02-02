package ru.thevalidator.daivinchikmatcher2.vk.custom.transport;

import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpTransportClientWithCustomUserAgent extends HttpTransportClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpTransportClientWithCustomUserAgent.class);

    public HttpTransportClientWithCustomUserAgent(String userAgent) {
        LOG.debug("Trying to create http client with custom user agent [{}]", userAgent);
        httpClient = buildHttpClientWithUserAgent(userAgent);
        LOG.debug("Http client with custom user agent [{}] has been created", userAgent);
    }

    private org.apache.http.client.HttpClient buildHttpClientWithUserAgent(String userAgent) {
        CookieStore cookieStore = new BasicCookieStore();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS)
                .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
                .setCookieSpec(CookieSpecs.STANDARD)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_SIMULTANEOUS_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(MAX_SIMULTANEOUS_CONNECTIONS);

        return httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore)
                .setUserAgent(userAgent)
                .build();
    }

}
