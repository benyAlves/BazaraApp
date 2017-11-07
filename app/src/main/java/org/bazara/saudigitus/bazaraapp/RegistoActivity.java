package org.bazara.saudigitus.bazaraapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.bazara.saudigitus.bazaraapp.models.AccessToken;
import org.bazara.saudigitus.bazaraapp.models.ApiError;
import org.bazara.saudigitus.bazaraapp.rest.ClientAPI;
import org.bazara.saudigitus.bazaraapp.rest.InterfaceAPI;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;


public class RegistoActivity extends AppCompatActivity  {
    @BindView(R.id.nome_input_ayout)
    TextInputLayout nomeInput;
    @BindView(R.id.password_input_ayout)
    TextInputLayout passwordInput;
    @BindView(R.id.validar_numero)
    TextView validarNumero;

    private String numero;

    InterfaceAPI service;
    Call<AccessToken> call;
    private static final String TAG = "RegistoActivity";

    TokenManager tokenManager;
    private AwesomeValidation validator;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        numero = intent.getStringExtra("NUMERO");


        dialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_registar)
                .content(R.string.please_wait_registar)
                .progress(true, 0).build();



        ButterKnife.bind(this);
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        service = ClientAPI.createService(InterfaceAPI.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        setupRules();
    }


    @OnClick(R.id.btn_registar)
    void registar(){

        dialog.show();
        final String nome = nomeInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();

        nomeInput.setError(null);
        passwordInput.setError(null);
        validarNumero.setVisibility(View.GONE);
        validator.clear();

        if(validator.validate()) {

            call = service.registarUser(nome, null, numero, password);
            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        tokenManager.saveToken(response.body());
                        tokenManager.saveProfile(nome, numero);
                        startActivity(new Intent(RegistoActivity.this, LoginActivity.class));
                    } else {
                        dialog.dismiss();
                        handleErrors(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFaluire: " + t.getMessage());
                    dialog.dismiss();
                }
            });
        }
    }

    private void  handleErrors(ResponseBody response){
        ApiError apiError = Utils.convertErrors(response);
        for(Map.Entry<String, List<String>> erro: apiError.getErrors().entrySet()){
            if(erro.getKey().equals("nome")){
                nomeInput.setError(erro.getValue().get(0));
            }
            if(erro.getKey().equals("password")){
                passwordInput.setError(erro.getValue().get(0));
            }

            if (erro.getKey().equals("telefone")){
                validarNumero.setText(erro.getValue().get(0));
                validarNumero.setVisibility(View.VISIBLE);
            }

        }

    }


    public void setupRules() {

        validator.addValidation(this, R.id.nome_input_ayout, RegexTemplate.NOT_EMPTY, R.string.erro_nome);
        validator.addValidation(this, R.id.password_input_ayout, RegexTemplate.NOT_EMPTY, R.string.erro_password_vazio);
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
