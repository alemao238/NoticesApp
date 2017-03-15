package com.example.dione.noticesapp.modules.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.manager.SharedPreferenceManager;
import com.example.dione.noticesapp.modules.dashboard.DashboardActivity;
import com.example.dione.noticesapp.modules.register.SignUpActivity;
import com.example.dione.noticesapp.utilities.ApplicationConstants;
import com.example.dione.noticesapp.utilities.Helpers;
import com.example.dione.noticesapp.utilities.InputValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private Helpers helpers;
    private FirebaseAuth mAuth;
    private SharedPreferenceManager sharedPreferenceManager;
    @BindView(R.id.edittext_username)
    EditText username;
    @BindView(R.id.edittext_password)
    EditText password;
    @BindView(R.id.til_username)
    TextInputLayout tilUsername;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initialize();

//        sendWeatherRequest();
    }

    private void initialize(){
        sharedPreferenceManager = new SharedPreferenceManager(this);
        helpers = new Helpers(this);
        if (!sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_EMAIL, "").isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.putExtra("from_class", "login");
            startActivity(intent);
            finish();
        }
    }

    @OnClick({R.id.button_signup, R.id.button_signin})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.button_signup:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.button_signin:
                boolean isUsernameValid = InputValidator.validate(this, username, tilUsername);
                boolean isPasswordValid = InputValidator.validate(this, password, tilPassword);
                if (isUsernameValid && isPasswordValid) {
                    helpers.showProgressDialog(getString(R.string.loading_login));
                    mAuth.signInWithEmailAndPassword(username.getText().toString().trim(), password.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(LoginActivity.this,"Welcome " + task.getResult().getUser().getDisplayName(),
                                                Toast.LENGTH_SHORT).show();
                                        sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_DISPLAY_NAME, task.getResult().getUser().getDisplayName());
                                        sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_EMAIL, task.getResult().getUser().getEmail());
                                        sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_PHOTO_URL, task.getResult().getUser().getPhotoUrl().toString());
                                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                        intent.putExtra("from_class", "login");
                                        startActivity(intent);
                                        finish();
                                    }
                                    helpers.closeProgressDialog();
                                }
                            });
                }
                break;
            default:
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(this);
        }
    }
}
