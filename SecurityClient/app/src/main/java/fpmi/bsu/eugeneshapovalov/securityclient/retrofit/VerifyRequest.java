package fpmi.bsu.eugeneshapovalov.securityclient.retrofit;

import com.google.gson.annotations.SerializedName;

public class VerifyRequest {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("code")
    private String code;

    public VerifyRequest() {}

    public VerifyRequest(String code, String sessionId) {
        this.code = code;
        this.sessionId = sessionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
