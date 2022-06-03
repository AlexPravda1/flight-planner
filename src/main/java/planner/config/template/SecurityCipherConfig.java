package planner.config.template;

public enum SecurityCipherConfig {
    DIGEST_ALGORITHM("SHA-1"),
    DIGEST_ARRAY_SIZE("16"),
    ENCRYPT_STANDARD("AES"),
    TRANSFORMATION("AES/ECB/PKCS5Padding");

    private final String value;

    SecurityCipherConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
