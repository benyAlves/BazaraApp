package org.bazara.saudigitus.bazaraapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import org.bazara.saudigitus.bazaraapp.models.AccessToken;
import org.bazara.saudigitus.bazaraapp.models.ApiError;
import org.bazara.saudigitus.bazaraapp.rest.ClientAPI;
import org.bazara.saudigitus.bazaraapp.rest.InterfaceAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    @BindView(R.id.layout_telefone)
    TextInputLayout layoutTelefone;
    @BindView(R.id.layout_password)
    TextInputLayout layoutPassword;
    @BindView(R.id.btn_autenticar)
    Button btnAutenticar;

    InterfaceAPI service;
    Call<AccessToken> call;
    TokenManager tokenManager;
    private AwesomeValidation validator;
    private MaterialDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

       dialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_autenticar)
                .content(R.string.please_wait_autenticar)
                .progress(true, 0).build();


        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        service = ClientAPI.createService(InterfaceAPI.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        setupRules();

        if(tokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(LoginActivity.this, Main2Activity.class));
            finish();
        }

    }


    public void setupRules() {

        validator.addValidation(this, R.id.layout_telefone, Patterns.PHONE, R.string.erro_phone);
        validator.addValidation(this, R.id.layout_password, RegexTemplate.NOT_EMPTY, R.string.erro_password_vazio);
    }

    @OnClick(R.id.btn_criar_conta)
    void goToCriarConta(){
        startActivity(new Intent(LoginActivity.this, ConfirmarActivity.class));
    }

    @OnClick(R.id.btn_autenticar)
    void autenticar(){

        dialog.show();
        String email = layoutTelefone.getEditText().getText().toString();
        String password = layoutPassword.getEditText().getText().toString();

        layoutTelefone.setError(null);
        layoutPassword.setError(null);
        validator.clear();



        if (validator.validate()) {

            call = service.login(email, password);
            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        tokenManager.saveToken(response.body());
                        startActivity(new Intent(LoginActivity.this, Main2Activity.class));
                        finish();
                    } else {
                        if (response.code() == 422) {
                            handleErrors(response.errorBody());
                        }
                        if (response.code() == 401) {
                            ApiError apiError = Utils.convertErrors(response.errorBody());
                            Toast.makeText(LoginActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Não conseguimos conectar ao servidor", Toast.LENGTH_LONG).show();
                }
            });

        }


    }

    private void  handleErrors(ResponseBody response){
        ApiError apiError = Utils.convertErrors(response);
        Log.w(TAG, "reaponseBody: "+response);
        Log.w(TAG, "apiError: "+apiError.getErrors());
        for(Map.Entry<String, List<String>> erro: apiError.getErrors().entrySet()){
            if(erro.getKey().equals("telefone")){
                layoutTelefone.setError(erro.getValue().get(0));
            }
            if(erro.getKey().equals("password")){
                layoutPassword.setError(erro.getValue().get(0));
            }

            if (erro.getKey().equals("telefone")){

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
    }




}

