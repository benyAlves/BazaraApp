package org.bazara.saudigitus.bazaraapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class ConfirmarActivity extends AppCompatActivity {


    private Button btnVerificar;
    private Button btnReenviar;

    @BindView(R.id.numero_layout)
    EditText txtNumero;

    private PinEntryView txtCodigoVerificar;
    private static final String TAG = "ConfirmLogin";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_verificar)
                .content(R.string.please_wait_verificar)
                .progress(true, 0).build();

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                mVerificationInProgress = false;
                Toast.makeText(ConfirmarActivity.this, "Verificação concluida", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (dialog != null && dialog.isShowing())
                         dialog.dismiss();
                Toast.makeText(ConfirmarActivity.this, "Verificação falhou", Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(ConfirmarActivity.this, "Número inválido", Toast.LENGTH_SHORT).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(ConfirmarActivity.this, "Um código de verificação foi enviado para "+txtNumero.getText().toString(), Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                startActivity(new Intent(ConfirmarActivity.this, NumeroActivity.class)
                        .putExtra("VERIFICATION_ID", mVerificationId)
                        .putExtra("NUMERO", txtNumero.getText().toString()));
            }
        };

    }

    @OnClick(R.id.btn_verificar)
    void onVerificar(){
       dialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                txtNumero.getText().toString(),
                60,
                java.util.concurrent.TimeUnit.SECONDS,
                ConfirmarActivity.this,
                mCallbacks);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (dialog != null && dialog.isShowing())
                                dialog.dismiss();
                            Toast.makeText(ConfirmarActivity.this,"Verificação concluída",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ConfirmarActivity.this, RegistoActivity.class)
                                    .putExtra("NUMERO", txtNumero.getText().toString()));
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                if (dialog != null && dialog.isShowing())
                                    dialog.dismiss();
                                Toast.makeText(ConfirmarActivity.this,"Vefiricação inválida",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}
