package fpmi.bsu.eugeneshapovalov.securityclient.retrofit.dto.restresponse;

import java.io.Serializable;

/**
 * Error dto for rest response. Contains error code and error message.
 *
 * @author Sergey Kiselev
 */
public class ErrorDto implements Serializable {
    private String code;
    private String message;

    private static final String DEFAULT_CODE = "DEFAULT";

    public ErrorDto(String message) {
        this.message = message;
        this.code = DEFAULT_CODE;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
