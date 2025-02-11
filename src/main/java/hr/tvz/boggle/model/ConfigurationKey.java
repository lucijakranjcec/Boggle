package hr.tvz.boggle.model;

import lombok.Getter;

@Getter
public enum ConfigurationKey {

    RMI_HOST("rmi.host"), RMI_PORT("rmi.port"), CHAT_HOST("chat.host");

    private String key;

    ConfigurationKey(String key) {
        this.key = key;
    }

}
