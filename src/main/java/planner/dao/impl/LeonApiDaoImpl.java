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
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import planner.config.template.LeonApiConfig;
import planner.dao.LeonApiDao;
import planner.exception.LeonAccessException;
import planner.model.Airline;
import planner.model.leon.AccessToken;
import planner.util.LeonUtil;

@Repository
@Log4j2
public class LeonApiDaoImpl implements LeonApiDao {
    private static final Map<String, AccessToken> tokensPool = new HashMap<>();

    @Override
    public String getAllAircraft(Airline airline) {
        URL url = generateUrl(QUERY_URL_POSTFIX, airline);
        String query = LeonUtil.prepareQueryAllAircraft();
        String accessToken = getAccessToken(airline);
        log.info(String.format("Processing %s, %s", url, query));
        return fetchLeonResponse(url, query, accessToken);
    }

    @Override
    public String getAllFlightsByPeriod(Airline airline, long daysRange) {
        URL url = generateUrl(QUERY_URL_POSTFIX, airline);
        String query = LeonUtil.prepareQueryAllFlightsByPeriod(daysRange);
        String accessToken = getAccessToken(airline);
        log.info(String.format("Processing %s, %s", url, query));
        return fetchLeonResponse(url, query, accessToken);
    }

    @Override
    public String getAllFlightsByPeriodAndAircraftId(
            Airline airline, long daysRange, Long aircraftId) {
        URL url = generateUrl(QUERY_URL_POSTFIX, airline);
        String query = LeonUtil.prepareQueryAllFlightsByPeriodAndAircraftId(daysRange, aircraftId);
        String accessToken = getAccessToken(airline);
        log.info(String.format("Processing %s, %s", url, query));
        return fetchLeonResponse(url, query, accessToken);
    }

    private URL generateUrl(LeonApiConfig urlType, Airline airline) {
        try {
            return new URL(HTTP_PREFIX.value()
                    + airline.getLeonSubDomain() + urlType.value());
        } catch (MalformedURLException e) {
            String message = String.format("Failed to build %s URL for %s",
                    urlType.name(), airline.getName());
            log.error(message, e);
            throw new LeonAccessException(message, e);
        }
    }

    private boolean isValidTokenByDuration(String airlineName) {
        return tokensPool.containsKey(airlineName)
                && tokensPool.get(airlineName).getValidityTime().isAfter(LocalDateTime.now());
    }

    private String putInTokenPoolMap(String airlineName, String fetchedToken) {
        LocalDateTime validity = LocalDateTime.now()
                .plusMinutes(Long.parseLong(TOKEN_VALIDITY_MINUTES.value()));
        tokensPool.put(airlineName, new AccessToken(fetchedToken, validity));
        return fetchedToken;
    }

    private String getAccessToken(Airline airline) {
        if (isValidTokenByDuration(airline.getName())) {
            log.info(String.format("Returned existing Token for: %s", airline.getName()));
            return tokensPool.get(airline.getName()).getToken();
        }
        URL url = generateUrl(TOKEN_URL_POSTFIX, airline);
        String query = REFRESH_TOKEN_HEADER.value() + airline.getLeonApiKey();
        String fetchedToken = fetchLeonResponse(url, query, StringUtils.EMPTY);
        log.info(String.format("Generated new Token %s for: %s", fetchedToken, airline.getName()));
        return putInTokenPoolMap(airline.getName(), fetchedToken);
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
        try (OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream())) {
            writer.write(query);
            writer.flush();
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