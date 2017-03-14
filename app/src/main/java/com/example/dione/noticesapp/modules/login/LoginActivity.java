package com.example.dione.noticesapp.modules.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.api.models.Currently;
import com.example.dione.noticesapp.api.models.Weather;
import com.example.dione.noticesapp.application.NoticesApplication;
import com.example.dione.noticesapp.event.GetWeatherEvent;
import com.example.dione.noticesapp.event.SendWeatherEvent;
import com.example.dione.noticesapp.event.SendWeatherEventError;
import com.example.dione.noticesapp.manager.SharedPreferenceManager;
import com.example.dione.noticesapp.modules.dashboard.DashboardActivity;
import com.example.dione.noticesapp.modules.register.SignUpActivity;
import com.example.dione.noticesapp.utilities.ApplicationConstants;
import com.example.dione.noticesapp.utilities.InputValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    NoticesApplication noticesApplication;
    private FirebaseAuth mAuth;
//    TextView forecastTextView;
    private ProgressDialog progressDialog;
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_login));
        progressDialog.setCancelable(false);
        if (!sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_EMAIL, "").isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.putExtra("from_class", "login");
            startActivity(intent);
            finish();
        }
//        forecastTextView = (TextView) findViewById(R.id.forecastTextView);
    }

    private void sendWeatherRequest(){
        noticesApplication = new NoticesApplication();
        noticesApplication.mBus.post(new GetWeatherEvent(14.599512, 120.984222));
//        forecastTextView.setText("Waiting for API Response");
    }

    @Subscribe
    public void onSendWeatherEvent(SendWeatherEvent sendWeatherEvent) {
        Weather weather = sendWeatherEvent.getWeather();
        Currently currently = weather.getCurrently();
//        forecastTextView.setText(currently.getSummary());
    }

    @Subscribe
    public void onSendWeatherEventError(SendWeatherEventError sendWeatherEventError) {
//        forecastTextView.setText(sendWeatherEventError.getmRetroFitError());
    }


    @Override
    public void onResume() {
        super.onResume();
//        noticesApplication.mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        noticesApplication.mBus.unregister(this);
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
                    progressDialog.show();
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
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
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
