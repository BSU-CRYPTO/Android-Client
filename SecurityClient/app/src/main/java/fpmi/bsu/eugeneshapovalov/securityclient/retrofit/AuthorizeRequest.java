package fpmi.bsu.eugeneshapovalov.securityclient.retrofit;

import com.google.gson.annotations.SerializedName;

public class AuthorizeRequest {

    @SerializedName("login")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("sessionId")
    private String sessionId;

    public AuthorizeRequest() {}

    public AuthorizeRequest(String password, String sessionId, String username) {
        this.password = password;
        this.sessionId = sessionId;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
