package ntnu.imt3673.android_geocache.ui.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
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
import ntnu.imt3673.android_geocache.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    private boolean haveLocationPerm = false;

    private LoginViewModel loginViewModel;

    private ProgressBar loadingProgressBar;
    private Button loginButton;
    private Button registerButton;

    private EditText emailEditText;
    private EditText passwordEditText;

    private TextView permHelp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        //Request permissions
        checkForPermissions();

        emailEditText = findViewById(R.id.txt_email);
        passwordEditText = findViewById(R.id.txt_password);

        registerButton = findViewById(R.id.register);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        permHelp = findViewById(R.id.enablePerm_txt);

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
        loginViewModel.loginDataChanged(emailEditText.getText().toString(), passwordEditText.getText().toString());

        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE && loginViewModel.isValid()) && haveLocationPerm) {
                    new LoginTask().execute(emailEditText.getText().toString(), passwordEditText.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "Location permissions are required!", Toast.LENGTH_SHORT).show();
                    permHelp.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        // Button click handlers
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!haveLocationPerm) {
                    Toast.makeText(LoginActivity.this, "Location permissions are required! Please enable it before you can proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(loginViewModel.isValid()) {
                    new LoginTask().execute(emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!haveLocationPerm) {
                    Toast.makeText(LoginActivity.this, "Location permissions are required! Please enable it before you can proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent registerIntent = new Intent(LoginActivity.this.getApplication(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        permHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForPermissions();
            }
        });

    }

    private void onFormChanged(LoginFormState loginFormState) {
        if (loginFormState == null) {
            return;
        }
        loginButton.setEnabled(loginFormState.isDataValid());
        if (loginFormState.getEmailError() != null) {
            emailEditText.setError(getString(loginFormState.getEmailError()));
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
            Intent intent = new Intent(this, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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

    public void checkForPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            haveLocationPerm = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permissions was granted.
                    haveLocationPerm = true;
                    permHelp.setVisibility(View.INVISIBLE);
                } else {
                    //Ask for permissions again
                    permHelp.setVisibility(View.VISIBLE);
                    haveLocationPerm = false;
                }
                return;
            }
        }
    }

    /*@Override
    public void onBackPressed() {
        //Do nothing. We dont want the user
        //to back out of the login process.
    }*/
}
