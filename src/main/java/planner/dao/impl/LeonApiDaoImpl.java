package planner.dao.impl;

import static planner.config.template.HttpConnectionConfig.APPLICATION_JSON;
import static planner.config.template.HttpConnectionConfig.AUTHORIZATION;
import static planner.config.template.HttpConnectionConfig.BEARER;
import static planner.config.template.HttpConnectionConfig.CONTENT_TYPE;
import static planner.config.template.HttpConnectionConfig.HTTP_POST;
import static planner.config.template.HttpConnectionConfig.HTTP_PREFIX;
import static planner.config.template.LeonApiConfig.QUERY_URL_POSTFIX;
import static planner.config.template.LeonApiConfig.REFRESH_TOKEN_HEADER;
import static planner.config.template.LeonApiConfig.TOKEN_URL_POSTFIX;
import static planner.config.template.LeonApiConfig.TOKEN_VALIDITY_MINUTES;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import planner.dao.LeonApiDao;
import planner.exception.LeonAccessException;
import planner.model.Airline;
import planner.model.leon.AccessToken;
import planner.util.LeonUtil;

@Repository
@RequiredArgsConstructor
public class LeonApiDaoImpl implements LeonApiDao {
    private static final Map<String, AccessToken> tokensPool = new HashMap<>();
    private final Logger log;

    @Override
    public String getAllActiveAircraft(Airline airline) {
        log.debug("Object received " + airline.toString());

        URL url = prepareQueryUrl(airline);
        log.debug("URL prepared: " + url);

        String query = LeonUtil.prepareQueryAllActiveAircraft();
        log.debug("Query prepared: " + query);

        String accessToken = getAccessToken(airline);
        log.debug("Token received: " + accessToken);
        return LeonUtil.getValidatedResponse(fetchLeonResponse(url, query, accessToken));
    }

    @Override
    public String getAllFlightsByPeriod(Airline airline, long daysRange) {
        log.debug("Object received " + airline.toString());

        URL url = prepareQueryUrl(airline);
        log.debug("URL prepared: " + url);

        String query = LeonUtil.prepareQueryAllFlightsByPeriod(daysRange);
        log.debug("Query prepared: " + query);

        String accessToken = getAccessToken(airline);
        log.debug("Token received: " + accessToken);

        return LeonUtil.getValidatedResponse(fetchLeonResponse(url, query, accessToken));
    }

    @Override
    public String getAllFlightsByPeriodAndAircraftId(
            Airline airline, long daysRange, Long aircraftId) {
        log.debug("Object received " + airline.toString());
        log.debug("AircraftId received " + aircraftId);

        URL url = prepareQueryUrl(airline);
        log.debug("URL prepared: " + url);

        String query = LeonUtil.prepareQueryAllFlightsByPeriodAndAircraftId(daysRange, aircraftId);
        log.debug("Query prepared: " + query);

        String accessToken = getAccessToken(airline);
        log.debug("Token received: " + accessToken);

        return LeonUtil.getValidatedResponse(fetchLeonResponse(url, query, accessToken));
    }

    private URL prepareQueryUrl(Airline airline) {
        try {
            return new URL(HTTP_PREFIX.value()
                    + airline.getLeonSubDomain() + QUERY_URL_POSTFIX.value());
        } catch (MalformedURLException e) {
            log.error("Failed to build Query URL for " + airline.getName(), e);
            throw new LeonAccessException("Failed to build Query URL for " + airline.getName(), e);
        }
    }

    private URL prepareTokenRefreshUrl(Airline airline) {
        try {
            return new URL(HTTP_PREFIX.value()
                    + airline.getLeonSubDomain() + TOKEN_URL_POSTFIX.value());
        } catch (MalformedURLException e) {
            log.error("Failed to build Token Refresh URL for " + airline.getName(), e);
            throw new LeonAccessException("Failed to build Token Refresh URL for "
                    + airline.getName(), e);
        }
    }

    private boolean isValidTokenByDuration(String airlineName) {
        return tokensPool.get(airlineName).getValidityTime().isAfter(LocalDateTime.now());
    }

    private String putInTokenPoolMap(String airlineName, String accessToken) {
        LocalDateTime validity = LocalDateTime.now()
                .plusMinutes(Long.parseLong(TOKEN_VALIDITY_MINUTES.value()));
        tokensPool.put(airlineName, new AccessToken(accessToken, validity));
        return accessToken;
    }

    private String getAccessToken(Airline airline) {
        if (tokensPool.containsKey(airline.getName())
                && isValidTokenByDuration(airline.getName())) {
            return tokensPool.get(airline.getName()).getToken();
        }
        URL url = prepareTokenRefreshUrl(airline);
        String query = REFRESH_TOKEN_HEADER.value() + airline.getLeonApiKey();
        String accessToken = fetchLeonResponse(url, query, StringUtils.EMPTY);
        return putInTokenPoolMap(airline.getName(), accessToken);
    }

    private HttpURLConnection prepareHttpConnection(URL url, String accessToken) {
        try {
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod(HTTP_POST.value());
            if (!accessToken.isBlank()) {
                httpConn.setRequestProperty(CONTENT_TYPE.value(), APPLICATION_JSON.value());
                httpConn.setRequestProperty(AUTHORIZATION.value(), BEARER.value() + accessToken);
            }
            httpConn.setDoOutput(true);
            return httpConn;
        } catch (IOException e) {
            log.error("Cannot prepare HTTP connection for " + url + "and token " + accessToken, e);
            throw new LeonAccessException("Cannot prepare HTTP connection for " + url, e);
        }
    }

    private void writeLeonQueryToHttpConnection(HttpURLConnection httpConn, String query) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(query);
            writer.flush();
            writer.close();
            httpConn.getOutputStream().close();
        } catch (IOException e) {
            log.error("Cannot write query to HTTP connection " + query, e);
            throw new LeonAccessException("Cannot write query to HTTP connection " + query, e);
        }
    }

    private String parseLeonQueryHttpResponse(HttpURLConnection httpConn) {
        try {
            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            return s.hasNext() ? s.next() : StringUtils.EMPTY;
        } catch (IOException e) {
            log.error("Cannot parse Leon HTTP Response", e);
            throw new LeonAccessException("Cannot parse Leon HTTP Response");
        }
    }

    private String fetchLeonResponse(URL url, String query, String accessToken) {
        HttpURLConnection httpConnection = prepareHttpConnection(url, accessToken);
        writeLeonQueryToHttpConnection(httpConnection, query);
        return parseLeonQueryHttpResponse(httpConnection);
    }
}
