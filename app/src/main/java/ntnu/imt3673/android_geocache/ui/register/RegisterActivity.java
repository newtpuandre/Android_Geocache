package ntnu.imt3673.android_geocache.ui.register;

import android.annotation.SuppressLint;
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

import ntnu.imt3673.android_geocache.MapsActivity;
import ntnu.imt3673.android_geocache.R;
import ntnu.imt3673.android_geocache.ui.login.LoginResult;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;

    private ProgressBar loadingProgressBar;
    private Button registerButton;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = ViewModelProviders.of(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        nameEditText = findViewById(R.id.txt_name);
        emailEditText = findViewById(R.id.txt_email);
        passwordEditText = findViewById(R.id.txt_password);
        confirmPasswordEditText = findViewById(R.id.txt_confirm_password);

        registerButton = findViewById(R.id.btn_register);
        loadingProgressBar = findViewById(R.id.loading);

        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                onFormChanged(registerFormState);
            }
        });

        registerViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
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
                registerViewModel.registerDataChanged(nameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());
            }
        };
        registerViewModel.registerDataChanged(nameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());

        nameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        confirmPasswordEditText.addTextChangedListener(afterTextChangedListener);
        confirmPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && registerViewModel.isValid()) {
                    new RegisterTask().execute(nameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());
                }
                return false;
            }
        });

        // Button click handlers
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(registerViewModel.isValid()) {
                    new RegisterTask().execute(nameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());
                }
            }
        });
    }

    private void onFormChanged(RegisterFormState registerFormState) {
        if (registerFormState == null) {
            return;
        }

        registerButton.setEnabled(registerFormState.isDataValid());

        if (registerFormState.getNameError() != null) {
            nameEditText.setError(getString(registerFormState.getNameError()));
        }
        if (registerFormState.getEmailError() != null) {
            emailEditText.setError(getString(registerFormState.getEmailError()));
        }
        if (registerFormState.getPasswordError() != null) {
            passwordEditText.setError(getString(registerFormState.getPasswordError()));
        }
        if (registerFormState.getConfirmPasswordError() != null) {
            confirmPasswordEditText.setError(getString(registerFormState.getConfirmPasswordError()));
        }
    }

    private void onLoginResult(LoginResult loginResult) {
        if (loginResult == null || loginResult.getError() != null) {
            if(loginResult != null)
                showRegisterFailed(loginResult.getError());

            registerButton.setEnabled(true);
            nameEditText.setEnabled(true);
            emailEditText.setEnabled(true);
            passwordEditText.setEnabled(true);
            confirmPasswordEditText.setEnabled(true);
            loadingProgressBar.setVisibility(View.GONE);
        }
        else if (loginResult.getSuccess() != null) {
            // Destroy the login and register activities and open the map activity
            Intent intent = new Intent(this, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("StaticFieldLeak")
    private class RegisterTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registerButton.setEnabled(false);
            nameEditText.setEnabled(false);
            emailEditText.setEnabled(false);
            passwordEditText.setEnabled(false);
            confirmPasswordEditText.setEnabled(false);
            loadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String[] fields) {
            registerViewModel.register(fields[0], fields[1], fields[2], fields[3]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
}
