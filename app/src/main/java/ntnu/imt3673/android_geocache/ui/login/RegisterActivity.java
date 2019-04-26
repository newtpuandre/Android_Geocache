package ntnu.imt3673.android_geocache.ui.login;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ntnu.imt3673.android_geocache.R;

public class RegisterActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private ProgressBar loadingProgressBar;
    private Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText nameEditText = findViewById(R.id.txt_name);
        final EditText emailEditText = findViewById(R.id.txt_email);
        final EditText passwordEditText = findViewById(R.id.txt_password);
        final EditText confirmPassfordEditText = findViewById(R.id.txt_confirm_password);

        registerButton = findViewById(R.id.btn_register);
        loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showRegistrationFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    /*
                    Register user and allow them to log in

                    updateUiWithUser(loginResult.getSuccess());
                    */
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start register process
            }
        });


    }

    private void showRegistrationFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
