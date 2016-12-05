package fpmi.bsu.eugeneshapovalov.securityclient.retrofit;

import com.google.gson.annotations.SerializedName;

public class AuthorizeResponse {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("code")
    private String code;

    @SerializedName("secret")
    private String secret;

    public AuthorizeResponse() {}

    public AuthorizeResponse(String code, String secret, String sessionId) {
        this.code = code;
        this.secret = secret;
        this.sessionId = sessionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        return "AuthorizeResponse{" +
                "code='" + code + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }
}
