package planner.config.enums;

public enum LeonApiConfig {
    QUERY_POSTFIX(".leon.aero/api/graphql/"),
    TOKEN_POSTFIX(".leon.aero/access_token/refresh/"),
    REFRESH_TOKEN("refresh_token="),
    TOKEN_VALIDITY("25"),
    EMPTY_TOKEN("");


    private final String value;

    LeonApiConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
