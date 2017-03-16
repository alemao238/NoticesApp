package com.example.dione.noticesapp.modules.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.bus.BusProvider;
import com.example.dione.noticesapp.event.LoginRequestEvent;
import com.example.dione.noticesapp.event.LoginResponseEvent;
import com.example.dione.noticesapp.event.RegisterRequestEvent;
import com.example.dione.noticesapp.event.RegisterResponseEvent;
import com.example.dione.noticesapp.event.ErrorRequestEvent;
import com.example.dione.noticesapp.manager.SharedPreferenceManager;
import com.example.dione.noticesapp.modules.dashboard.DashboardActivity;
import com.example.dione.noticesapp.modules.login.LoginActivity;
import com.example.dione.noticesapp.utilities.ApplicationConstants;
import com.example.dione.noticesapp.utilities.Helpers;
import com.example.dione.noticesapp.utilities.InputValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private FirebaseAuth mAuth;
    private SharedPreferenceManager sharedPreferenceManager;
    private Helpers helpers;
    @BindView(R.id.edittext_email)
    EditText email;
    @BindView(R.id.edittext_display_name)
    EditText displayName;
    @BindView(R.id.edittext_password)
    EditText password;
    @BindView(R.id.edittext_username)
    EditText username;

    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.til_display_name)
    TextInputLayout tilDisplayName;
    @BindView(R.id.til_username)
    TextInputLayout tilUsername;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        helpers = new Helpers(this);
        sharedPreferenceManager = new SharedPreferenceManager(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private void sendRegister(String email, String password, String displayName){
        helpers.showProgressDialog(getString(R.string.loading_register));
        BusProvider.getInstance().post(new RegisterRequestEvent(email, displayName, password, "http://www.example.com/12345678/photo.png", username.getText().toString()));
    }

    @Subscribe
    public void onReceiveRegisterResponse(RegisterResponseEvent registerResponseEvent) {
        helpers.showToast(getString(R.string.info_registration_success));
        BusProvider.getInstance().post(new LoginRequestEvent(ApplicationConstants.KEY_USER_TYPE, email.getText().toString(), password.getText().toString()));
    }

    @Subscribe
    public void onReceiveRegisterErrorResponse(ErrorRequestEvent errorRequestEvent) {
        helpers.showToast(errorRequestEvent.getpErrorMessage());
        helpers.closeProgressDialog();
    }

    @OnClick({R.id.button_register})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.button_register:
                boolean isEmailValid = InputValidator.validate(this, email, tilEmail);
                boolean isPasswordValid = InputValidator.validate(this, password, tilPassword);
                boolean isDisplayNameValid = InputValidator.validate(this, displayName, tilDisplayName);
                boolean isUsernameValid = InputValidator.validate(this, username, tilUsername);
                if (isEmailValid && isPasswordValid && isDisplayNameValid && isUsernameValid) {
                    sendRegister(email.getText().toString(), password.getText().toString(), displayName.getText().toString());
                }
                break;
        }
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

    @Subscribe
    public void onReceiveLoginResponseEvent(final LoginResponseEvent loginResponseEvent) {
        sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_DISPLAY_NAME, loginResponseEvent.getDisplayName());
        sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_EMAIL, loginResponseEvent.getEmail());
        sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_PHOTO_URL, "http://i1.wp.com/www.techrepublic.com/bundles/techrepubliccore/images/icons/standard/icon-user-default.png");
        sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_USERNAME, loginResponseEvent.getUsername());
        helpers.closeProgressDialog();
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            helpers.showToast(task.getException().getMessage());
                        } else {

                            helpers.showToast("Welcome " + task.getResult().getUser().getDisplayName());
                            sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_UID, task.getResult().getUser().getUid());
                            sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_DISPLAY_NAME, loginResponseEvent.getDisplayName());
                            sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_EMAIL, loginResponseEvent.getEmail());
                            sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_PHOTO_URL, "http://i1.wp.com/www.techrepublic.com/bundles/techrepubliccore/images/icons/standard/icon-user-default.png");
                            sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_USERNAME, loginResponseEvent.getUsername());
                            Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
                            intent.putExtra(ApplicationConstants.KEY_BUNDLE_FROM_CLASS, "login");
                            startActivity(intent);
                            helpers.closeProgressDialog();
                            finish();
                        }

                    }
                });

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }
}
