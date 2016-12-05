package fpmi.bsu.eugeneshapovalov.securityclient;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import fpmi.bsu.eugeneshapovalov.securityclient.cypher.AES;
import fpmi.bsu.eugeneshapovalov.securityclient.cypher.RSA;
import fpmi.bsu.eugeneshapovalov.securityclient.retrofit.ApiService;
import fpmi.bsu.eugeneshapovalov.securityclient.retrofit.AuthorizeRequest;
import fpmi.bsu.eugeneshapovalov.securityclient.retrofit.AuthorizeResponse;
import fpmi.bsu.eugeneshapovalov.securityclient.retrofit.ConnectionRequest;
import fpmi.bsu.eugeneshapovalov.securityclient.retrofit.ConnectionResponse;
import fpmi.bsu.eugeneshapovalov.securityclient.retrofit.TOTPService;
import fpmi.bsu.eugeneshapovalov.securityclient.retrofit.TokenRenewRequest;
import fpmi.bsu.eugeneshapovalov.securityclient.retrofit.VerifyRequest;
import fpmi.bsu.eugeneshapovalov.securityclient.retrofit.VerifyResponse;
import fpmi.bsu.eugeneshapovalov.securityclient.retrofit.dto.restresponse.RestResponse;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String MSG_EMPTY_FIELD = "Empty field";

    public static final String BASE_URL = "https://seorgy-15283.herokuapp.com/";

    private byte[] mInitVector;
    private byte[] mSecret;
    private int mTokenRenewTries;
    private String mSessionId;
    private SecretKey mSecretKey;
    private KeyPair mKeyPair;

    private BASE64Encoder mEncoder = new BASE64Encoder();
    private BASE64Decoder mDecoder = new BASE64Decoder();

    private Retrofit mRetrofit;

    private Handler mHlRenewToken = new Handler();

    private TextView mTvNewHash;
    private Button mBtnAuthorize;
    private Button mBtnConnectToServer;
    private Button mBtnSendEmailCodeToServer;
    private CheckBox mCbEncryption;
    private CheckBox mCbPostCode;
    private TextInputEditText mTieLUsername;
    private TextInputEditText mTieLPassword;
    private TextInputEditText mTieLEmailCode;

    private LinearLayout mllConnectionContainer;
    private LinearLayout mllAuthorizeContainer;
    private LinearLayout mllEmailCodeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvNewHash = (TextView) findViewById(R.id.tv_login_new_hash);
        mBtnAuthorize = (Button) findViewById(R.id.btn_login_authorize);
        mBtnConnectToServer = (Button) findViewById(R.id.btn_login_connect_to_server);
        mBtnSendEmailCodeToServer = (Button) findViewById(R.id.btn_login_send_email_code_to_server);
        mCbEncryption = (CheckBox) findViewById(R.id.cb_login_ecnryption);
        mCbPostCode = (CheckBox) findViewById(R.id.cb_login_post_code);
        mTieLUsername = (TextInputEditText) findViewById(R.id.tie_login_username);
        mTieLPassword = (TextInputEditText) findViewById(R.id.tie_login_password);
        mTieLEmailCode = (TextInputEditText) findViewById(R.id.tie_login_email_code);

        mllConnectionContainer = (LinearLayout) findViewById(R.id.ll_login_connection_container);
        mllAuthorizeContainer = (LinearLayout) findViewById(R.id.ll_login_authorize_connection);
        mllEmailCodeContainer = (LinearLayout) findViewById(R.id.ll_login_email_code_container);

        mBtnAuthorize.setOnClickListener(this);
        mBtnConnectToServer.setOnClickListener(this);
        mBtnSendEmailCodeToServer.setOnClickListener(this);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        enableChildren(mllAuthorizeContainer, false);
        enableChildren(mllEmailCodeContainer, false);

        // TODO: remove test credentials
        mTieLUsername.setText("eugeneshapovalov94@gmail.com");
        mTieLPassword.setText("password");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_authorize:
                String username = mTieLUsername.getText().toString();
                String password = mTieLPassword.getText().toString();

                boolean isEmptyField = false;
                View firstViewToFocus = null;

                if (TextUtils.isEmpty(username)) {
                    isEmptyField = true;
                    mTieLUsername.setError(MSG_EMPTY_FIELD);
                    firstViewToFocus = mTieLUsername;
                }
                if (TextUtils.isEmpty(password)) {
                    isEmptyField = true;
                    mTieLPassword.setError(MSG_EMPTY_FIELD);
                    if (firstViewToFocus == null) {
                        firstViewToFocus = mTieLPassword;
                    }
                }

                if (!isEmptyField) {
                    if (mustUseEncryption()) {
                        try {
                            username = mEncoder.encode(
                                    AES.encrypt(username.getBytes(), mSecretKey, new IvParameterSpec(mInitVector)));
                            password = mEncoder.encode(
                                    AES.encrypt(password.getBytes(), mSecretKey, new IvParameterSpec(mInitVector)));
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidAlgorithmParameterException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        }
                    } else {
                        username = mEncoder.encode(username.getBytes());
                        password = mEncoder.encode(password.getBytes());
                    }
                    AuthorizeRequest request = new AuthorizeRequest();
                    request.setSessionId(mSessionId);
                    request.setUsername(username);
                    request.setPassword(password);

                    executeAuthorizeRequest(request);
                } else {
                    firstViewToFocus.requestFocus();
                }
                break;
            case R.id.btn_login_connect_to_server:
                ConnectionRequest request = new ConnectionRequest();
                request.setEnabledEncryption(mCbEncryption.isChecked());
                request.setEnabledPostCode(mCbPostCode.isChecked());
                if (mustUseEncryption()) {
                    mKeyPair = RSA.generateKeyPair();
                    request.setRsaKey(mEncoder.encode(mKeyPair.getPublic().getEncoded()));
                }

                executeConnectionRequest(request);
                break;
            case R.id.btn_login_send_email_code_to_server:
                String emailCode = mTieLEmailCode.getText().toString();

                isEmptyField = false;
                if (TextUtils.isEmpty(emailCode)) {
                    isEmptyField = true;
                    mTieLEmailCode.setError(MSG_EMPTY_FIELD);
                }

                if (!isEmptyField) {
                    if (mustUseEncryption()) {
                        try {
                            emailCode = mEncoder.encode(
                                    AES.encrypt(emailCode.getBytes(), mSecretKey, new IvParameterSpec(mInitVector)));
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidAlgorithmParameterException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        }
                    } else {
                        emailCode = mEncoder.encode(emailCode.getBytes());
                    }
                    VerifyRequest verifyRequest = new VerifyRequest();
                    verifyRequest.setCode(emailCode);
                    verifyRequest.setSessionId(mSessionId);

                    executeVerifyEmailCodeRequest(verifyRequest);
                }
                break;
        }
    }

    private void executeConnectionRequest(ConnectionRequest request) {
        ApiService apiService = mRetrofit.create(ApiService.class);
        Call<RestResponse<ConnectionResponse>> call = apiService.connectToServer(request);
        call.enqueue(new Callback<RestResponse<ConnectionResponse>>() {
            @Override
            public void onResponse(Call<RestResponse<ConnectionResponse>> call, Response<RestResponse<ConnectionResponse>> response) {
                RestResponse<ConnectionResponse> body = response.body();
                if (body.getData() != null) {
                    mSessionId = body.getData().getSessionId();
                    if (mustUseEncryption()) {
                        initCryptoValues(body);
                    }

                    enableChildren(mllConnectionContainer, false);
                    enableChildren(mllAuthorizeContainer, true);
                } else {
                    showError(body.getErrorDto().getMessage());
                }
            }

            @Override
            public void onFailure(Call<RestResponse<ConnectionResponse>> call, Throwable t) {

            }
        });
    }

    private void executeAuthorizeRequest(AuthorizeRequest request) {
        ApiService apiService = mRetrofit.create(ApiService.class);
        Call<RestResponse<AuthorizeResponse>> call = apiService.authorize(request);
        call.enqueue(new Callback<RestResponse<AuthorizeResponse>>() {
            @Override
            public void onResponse(Call<RestResponse<AuthorizeResponse>> call, Response<RestResponse<AuthorizeResponse>> response) {
                RestResponse<AuthorizeResponse> body = response.body();
                if (body.getData() != null) {
                    if (mustUsePostCode()) {
                        enableChildren(mllAuthorizeContainer, false);
                        enableChildren(mllEmailCodeContainer, true);
                    } else {
                        Log.d(TAG, body.getData().toString());
                        initSecret(body.getData().getSecret());
                        enableChildren(mllAuthorizeContainer, false);
                        Snackbar.make(getWindow().getDecorView(), "Renew token will start in 10 seconds", Snackbar.LENGTH_SHORT)
                                .show();
                        mHlRenewToken.postDelayed(mRenewToken, 5000);
                    }
                } else {
                    showError(body.getErrorDto().getMessage());
                }
            }

            @Override
            public void onFailure(Call<RestResponse<AuthorizeResponse>> call, Throwable t) {

            }
        });
    }

    private void executeVerifyEmailCodeRequest(VerifyRequest request) {
        ApiService apiService = mRetrofit.create(ApiService.class);
        Call<RestResponse<VerifyResponse>> call = apiService.verify(request);
        call.enqueue(new Callback<RestResponse<VerifyResponse>>() {
            @Override
            public void onResponse(Call<RestResponse<VerifyResponse>> call, Response<RestResponse<VerifyResponse>> response) {
                RestResponse<VerifyResponse> body = response.body();
                if (body.getData() != null) {
                    Log.d(TAG, body.getData().toString());
                    initSecret(body.getData().getSecret());
                    enableChildren(mllEmailCodeContainer, false);
                    Snackbar.make(getWindow().getDecorView(), "Renew token will start in 10 seconds", Snackbar.LENGTH_SHORT)
                            .show();
                    mHlRenewToken.postDelayed(mRenewToken, 10000);
                } else {
                    showError(body.getErrorDto().getMessage());
                }
            }

            @Override
            public void onFailure(Call<RestResponse<VerifyResponse>> call, Throwable t) {

            }
        });
    }

    private void executeRenewTokenRequest(TokenRenewRequest request) {
        ApiService apiService = mRetrofit.create(ApiService.class);
        Call<RestResponse<String>> call = apiService.renewToken(request);
        call.enqueue(new Callback<RestResponse<String>>() {
            @Override
            public void onResponse(Call<RestResponse<String>> call, Response<RestResponse<String>> response) {
                RestResponse<String> body = response.body();
                if (body.getData() != null) {
                    ++mTokenRenewTries;
                    mTvNewHash.setText(mTvNewHash.getText() + "-" + body.getData());
                    mHlRenewToken.postDelayed(mRenewToken, 10000);
                } else {
                    showError(body.getErrorDto().getMessage());
                }
            }

            @Override
            public void onFailure(Call<RestResponse<String>> call, Throwable t) {

            }
        });
    }

    private boolean mustUseEncryption() {
        return mCbEncryption.isChecked();
    }

    private boolean mustUsePostCode() {
        return mCbPostCode.isChecked();
    }

    private void enableChildren(ViewGroup viewGroup, boolean enable) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            child.setEnabled(enable);
        }
    }

    private void showError(String message) {
        Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void initCryptoValues(RestResponse<ConnectionResponse> body) {
        try {
            byte[] rsaEncryptedSecretKey = mDecoder.decodeBuffer(body.getData().getAesKey());
            byte[] encryptedInitVector = mDecoder.decodeBuffer(body.getData().getInitVector());
            byte[] secretKeyBytes = RSA.decrypt(rsaEncryptedSecretKey, mKeyPair.getPrivate());
            mInitVector = RSA.decrypt(encryptedInitVector, mKeyPair.getPrivate());
            mSecretKey = new SecretKeySpec(secretKeyBytes, "AES");
        } catch (IOException e) {
            // empty
        }
    }

    private void initSecret(String secret) {
        try {
            if (mustUseEncryption()) {
                mSecret = AES.decrypt(mDecoder.decodeBuffer(secret), mSecretKey, new IvParameterSpec(mInitVector));
            } else {
                mSecret = mDecoder.decodeBuffer(secret);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable mRenewToken = new Runnable() {
        @Override
        public void run() {
            if (mTokenRenewTries < 10) {
                long totpCode = 0;
                try {
                    long timeIndex = System.currentTimeMillis() / 1000 / 30;
                    TOTPService totpService = new TOTPService();
                    totpCode = totpService.getCode(mSecret, timeIndex);
                    mTvNewHash.setText(String.valueOf(totpCode));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
                TokenRenewRequest request = new TokenRenewRequest();
                request.setToken(totpCode);
                request.setSessionId(mSessionId);

                executeRenewTokenRequest(request);
            }
        }
    };
}
