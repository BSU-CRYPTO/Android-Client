package fpmi.bsu.eugeneshapovalov.securityclient.retrofit;

import com.google.gson.annotations.SerializedName;

public class VerifyResponse {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("secret")
    private String secret;

    public VerifyResponse() {}

    public VerifyResponse(String code, String secret, String sessionId) {
        this.secret = secret;
        this.sessionId = sessionId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "VerifyResponse{" +
                "secret='" + secret + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
