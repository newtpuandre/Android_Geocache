package ntnu.imt3673.android_geocache.ui.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ntnu.imt3673.android_geocache.AddMessageActivity;
import ntnu.imt3673.android_geocache.MapsActivity;
import ntnu.imt3673.android_geocache.R;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private ProgressBar loadingProgressBar;
    private Button loginButton;
    private Button registerButton;

    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        emailEditText = findViewById(R.id.txt_email);
        passwordEditText = findViewById(R.id.txt_password);

        registerButton = findViewById(R.id.register);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                onFormChanged(loginFormState);
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                onLoginResult(loginResult);
            }
        });

        // Text field watchers
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && loginViewModel.isValid()) {
                    new LoginTask().execute(emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return false;
            }
        });

        // Button click handlers
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginViewModel.isValid()) {
                    new LoginTask().execute(emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this.getApplication(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    private void onFormChanged(LoginFormState loginFormState) {
        if (loginFormState == null) {
            return;
        }
        loginButton.setEnabled(loginFormState.isDataValid());
        if (loginFormState.getUsernameError() != null) {
            emailEditText.setError(getString(loginFormState.getUsernameError()));
        }
        if (loginFormState.getPasswordError() != null) {
            passwordEditText.setError(getString(loginFormState.getPasswordError()));
        }
    }

    private void onLoginResult(LoginResult loginResult) {
        if (loginResult == null) {
            return;
        }
        if (loginResult.getError() != null) {
            showLoginFailed(loginResult.getError());
        }
        if (loginResult.getSuccess() != null) {
            // Destroy the login activity and open the map activity
            finish();
            Intent addMessageIntent = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(addMessageIntent);
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
            emailEditText.setEnabled(false);
            passwordEditText.setEnabled(false);
            loadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String[] fields) {
            loginViewModel.login(fields[0], fields[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            loginButton.setEnabled(true);
            registerButton.setEnabled(true);
            emailEditText.setEnabled(true);
            passwordEditText.setEnabled(true);
            loadingProgressBar.setVisibility(View.GONE);
        }
    }

    /*@Override
    public void onBackPressed() {
        //Do nothing. We dont want the user
        //to back out of the login process.
    }*/
}
