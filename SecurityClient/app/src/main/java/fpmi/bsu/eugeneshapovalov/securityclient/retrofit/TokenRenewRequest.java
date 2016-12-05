package fpmi.bsu.eugeneshapovalov.securityclient.retrofit;

import com.google.gson.annotations.SerializedName;

public class TokenRenewRequest {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("token")
    private long token;

    public TokenRenewRequest() {}

    public TokenRenewRequest(String sessionId, long token) {
        this.sessionId = sessionId;
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }
}
