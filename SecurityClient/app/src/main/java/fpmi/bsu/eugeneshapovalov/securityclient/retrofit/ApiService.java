package fpmi.bsu.eugeneshapovalov.securityclient.retrofit;

import fpmi.bsu.eugeneshapovalov.securityclient.retrofit.dto.restresponse.RestResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("rsakey")
    Call<RestResponse<ConnectionResponse>> connectToServer(@Body ConnectionRequest request);

    @POST("login")
    Call<RestResponse<AuthorizeResponse>> authorize(@Body AuthorizeRequest request);

    @POST("verify")
    Call<RestResponse<VerifyResponse>> verify(@Body VerifyRequest request);

    @POST("token")
    Call<RestResponse<String>> renewToken(@Body TokenRenewRequest request);
}
