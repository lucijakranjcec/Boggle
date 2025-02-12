package hr.tvz.boggle.model;

public enum ConfigurationKey {
    RMI_HOST("rmi.host"), RMI_PORT("rmi.port"), CHAT_HOST("chat.host");

    private final String key;

    ConfigurationKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
