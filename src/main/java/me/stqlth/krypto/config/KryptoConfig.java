package me.stqlth.krypto.config;

import org.json.JSONObject;

public class KryptoConfig {
    private JSONObject _configJson;

    public KryptoConfig(JSONObject configJson) {
        _configJson = configJson;
    }

    public String getDbUrl() {
        return _configJson.getString("dbUrl");
    }

    public String getDbUser() {
        return _configJson.getString("dbUser");
    }

    public String getDbPassword() {
        return _configJson.getString("dbPassword");
    }

    public String getToken() { return _configJson.getString("token"); }

    public String getOwnerId() { return _configJson.getString("ownerId"); }

    public String getYoutubeApiKey() { return _configJson.getString("youtubekey"); }
}
