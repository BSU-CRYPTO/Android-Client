package fpmi.bsu.eugeneshapovalov.securityclient.retrofit.dto.restresponse;

import java.io.Serializable;

/**
 * Common rest response class for all rest services.
 * @param <T> type of data inside the response
 *
 * @author Sergey Kiselev
 */
public class RestResponse<T> implements Serializable {

    private T data;
    private ErrorDto errorDto;

    public RestResponse(T data) {
        this.data = data;
        this.errorDto = new ErrorDto("   ");
    }

    public RestResponse(T data, ErrorDto errorDto) {
        this.data = data;
        this.errorDto = errorDto;
    }

    public RestResponse(ErrorDto errorDto) {
        this.errorDto = errorDto;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorDto getErrorDto() {
        return errorDto;
    }

    public void setErrorDto(ErrorDto errorDto) {
        this.errorDto = errorDto;
    }
}
