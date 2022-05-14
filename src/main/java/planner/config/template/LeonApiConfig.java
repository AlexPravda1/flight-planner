package planner.config.template;

public enum LeonApiConfig {
    QUERY_URL_POSTFIX(".leon.aero/api/graphql/"),
    TOKEN_URL_POSTFIX(".leon.aero/access_token/refresh/"),
    REFRESH_TOKEN_HEADER("refresh_token="),
    TOKEN_VALIDITY_MINUTES("25");


    private final String value;

    LeonApiConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
