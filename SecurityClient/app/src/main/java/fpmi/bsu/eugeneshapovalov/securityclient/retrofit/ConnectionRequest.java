package fpmi.bsu.eugeneshapovalov.securityclient.retrofit;

import com.google.gson.annotations.SerializedName;

public class ConnectionRequest {

    @SerializedName("rsaKey")
    private String rsaKey;

    @SerializedName("encryption")
    private boolean enabledEncryption;

    @SerializedName("postCode")
    private boolean enabledPostCode;

    public ConnectionRequest() {}

    public ConnectionRequest(boolean enabledEncryption, boolean enabledPostCode, String rsaKey) {
        this.enabledEncryption = enabledEncryption;
        this.enabledPostCode = enabledPostCode;
        this.rsaKey = rsaKey;
    }

    public boolean isEnabledEncryption() {
        return enabledEncryption;
    }

    public void setEnabledEncryption(boolean enabledEncryption) {
        this.enabledEncryption = enabledEncryption;
    }

    public boolean isEnabledPostCode() {
        return enabledPostCode;
    }

    public void setEnabledPostCode(boolean enabledPostCode) {
        this.enabledPostCode = enabledPostCode;
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public void setRsaKey(String rsaKey) {
        this.rsaKey = rsaKey;
    }
}
