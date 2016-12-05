package fpmi.bsu.eugeneshapovalov.securityclient.retrofit;

import com.google.gson.annotations.SerializedName;

public class ConnectionResponse {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("aesKey")
    private String aesKey;

    @SerializedName("ivector")
    private String initVector;

    public ConnectionResponse() {}

    public ConnectionResponse(String aesKey, String initVector, String sessionId) {
        this.aesKey = aesKey;
        this.initVector = initVector;
        this.sessionId = sessionId;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getInitVector() {
        return initVector;
    }

    public void setInitVector(String initVector) {
        this.initVector = initVector;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "ConnectionResponse{" +
                "aesKey='" + aesKey + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", initVector='" + initVector + '\'' +
                '}';
    }
}
