package planner.dao.impl;

import static planner.config.enums.HttpConnectionConfig.APPLICATION_JSON;
import static planner.config.enums.HttpConnectionConfig.AUTHORIZATION;
import static planner.config.enums.HttpConnectionConfig.BEARER;
import static planner.config.enums.HttpConnectionConfig.CONTENT_TYPE;
import static planner.config.enums.HttpConnectionConfig.HTTP_POST;
import static planner.config.enums.HttpConnectionConfig.HTTP_PREFIX;
import static planner.config.enums.LeonApiConfig.EMPTY_TOKEN;
import static planner.config.enums.LeonApiConfig.QUERY_POSTFIX;
import static planner.config.enums.LeonApiConfig.REFRESH_TOKEN;
import static planner.config.enums.LeonApiConfig.TOKEN_POSTFIX;
import static planner.config.enums.LeonApiConfig.TOKEN_VALIDITY;
import static planner.config.enums.LeonQueryTemplate.ALL_ACTIVE_AIRCRAFT;
import static planner.config.enums.LeonQueryTemplate.ALL_FLIGHTS_BY_PERIOD;
import static planner.config.enums.LeonQueryTemplate.TEMPLATE_PATH;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.springframework.stereotype.Repository;
import planner.dao.LeonApiDao;
import planner.model.Airline;

@Repository
public class LeonApiDaoImpl implements LeonApiDao {
    private static final Map<String, LocalDateTime> tokenValidity = new HashMap<>();
    private static final Map<String, String> tokenPool = new HashMap<>();

    @Override
    public String getAllByPeriod(Airline airline, long daysRange) {
        String accessToken = getAccessToken(airline);
        try {
            Path queryPath = Path.of(TEMPLATE_PATH.value().concat(ALL_FLIGHTS_BY_PERIOD.value()));
            String query = removeLineBreaks(Files.readString(queryPath));
            query = query.replaceAll("START_DATE",
                    LocalDate.now().minusDays(daysRange).toString());
            query = query.replaceAll("END_DATE",
                    LocalDate.now().plusDays(++daysRange).toString());
            URL url = new URL(HTTP_PREFIX.value()
                    + airline.getLeonSubDomain() + QUERY_POSTFIX.value());
            return fetchLeonResponse(url, query, accessToken);
        } catch (IOException e) {
            throw new RuntimeException(ALL_FLIGHTS_BY_PERIOD.value() + " Query response is "
                    + "failed for " + airline.getName() + " airline", e);
        }
    }

    @Override
    public String getAllActiveAircraft(Airline airline) {
        String accessToken = getAccessToken(airline);
        try {
            Path queryPath = Path.of(TEMPLATE_PATH.value().concat(ALL_ACTIVE_AIRCRAFT.value()));
            String query = removeLineBreaks(Files.readString(queryPath));
            URL url = new URL(HTTP_PREFIX.value()
                    + airline.getLeonSubDomain() + QUERY_POSTFIX.value());
            return fetchLeonResponse(url, query, accessToken);
        } catch (IOException e) {
            throw new RuntimeException(ALL_ACTIVE_AIRCRAFT.value() + " Query response is "
                    + "failed for " + airline.getName() + " airline", e);
        }
    }

    private static String removeLineBreaks(String linesFromFile) {
        return linesFromFile
                .replaceAll("[\\r\\n]+", " ")
                .replaceAll(System.lineSeparator(), " ");
    }

    private String getAccessToken(Airline airline) {
        String airlineName = airline.getName();
        try {
            if (!tokenValidity.containsKey(airlineName)
                    || tokenValidity.get(airlineName)
                    .plusMinutes(Long.parseLong(TOKEN_VALIDITY.value()))
                    .isBefore(LocalDateTime.now())) {
                URL url = new URL(HTTP_PREFIX.value()
                        + airline.getLeonSubDomain() + TOKEN_POSTFIX.value());
                String query = REFRESH_TOKEN.value() + airline.getLeonApiKey();
                String accessToken = fetchLeonResponse(url, query, EMPTY_TOKEN.value());
                tokenValidity.put(airlineName, LocalDateTime.now());
                tokenPool.put(airlineName, accessToken);
                return accessToken;
            }
        } catch (IOException e) {
            throw new RuntimeException("Access token cannot be taken. Query = "
                    + "refresh_token=" + airline.getLeonApiKey() + "URL = "
                    + HTTP_PREFIX.value() + airline.getLeonSubDomain() + TOKEN_POSTFIX.value(), e);
        }
        return tokenPool.get(airlineName);
    }

    private String fetchLeonResponse(URL url, String query, String accessToken)
            throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod(HTTP_POST.value());
        if (!accessToken.equals(EMPTY_TOKEN.value())) {
            httpConn.setRequestProperty(CONTENT_TYPE.value(), APPLICATION_JSON.value());
            httpConn.setRequestProperty(AUTHORIZATION.value(), BEARER.value() + accessToken);
        }
        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write(query);
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();
        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
