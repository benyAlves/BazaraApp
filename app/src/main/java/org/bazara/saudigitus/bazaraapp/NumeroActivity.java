package org.bazara.saudigitus.bazaraapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.philio.pinentry.PinEntryView;

public class NumeroActivity extends AppCompatActivity {

    @BindView(R.id.codigo_verificar)
    PinEntryView txtCodigoVerificar;

    private String numero;
    private String verificationID;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean mVerificationInProgress;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numero);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        dialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_verificar)
                .content(R.string.please_wait_verificar)
                .progress(true, 0).build();


        Intent intent = getIntent();
        numero = intent.getStringExtra("NUMERO");
        verificationID = intent.getStringExtra("VERIFICATION_ID");
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(NumeroActivity.this, "Verificação completa", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(NumeroActivity.this, "A verificação falhou", Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(NumeroActivity.this, "Número inválido", Toast.LENGTH_SHORT).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(NumeroActivity.this, "O código de verificação foi enviado para o número "+numero, Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    @OnClick(R.id.btn_verificar)
    void verificar(){
        dialog.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, txtCodigoVerificar.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }

    @OnClick(R.id.btn_reenviar)
    void reenviar(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                numero,
                60,
                java.util.concurrent.TimeUnit.SECONDS,
                this,
                mCallbacks, mResendToken);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (dialog != null && dialog.isShowing())
                                dialog.dismiss();
                            startActivity(new Intent(NumeroActivity.this, RegistoActivity.class));

                            Toast.makeText(NumeroActivity.this,"Verificação efectuada",Toast.LENGTH_SHORT).show();
                        } else {
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                if (dialog != null && dialog.isShowing())
                                    dialog.dismiss();
                                Toast.makeText(NumeroActivity.this,"Verificação inválida",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}
