package ntnu.imt3673.android_geocache.ui.register;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Patterns;

import java.util.Objects;

import ntnu.imt3673.android_geocache.R;
import ntnu.imt3673.android_geocache.data.LoginRepository;
import ntnu.imt3673.android_geocache.data.Result;
import ntnu.imt3673.android_geocache.data.model.LoggedInUser;
import ntnu.imt3673.android_geocache.ui.login.LoggedInUserView;
import ntnu.imt3673.android_geocache.ui.login.LoginResult;

class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    RegisterViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    void register(final String name, final String email, final String password, final String confirmPassword) {

        // TODO register
        Result<LoggedInUser> result = loginRepository.login(email, password);
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.postValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.postValue(new LoginResult(R.string.login_failed));
        }
    }

    void registerDataChanged(String name, String email, String password, String confirmPassword) {
        registerFormState.setValue(new RegisterFormState(
                isNameValid(name),
                isEmailValid(email),
                isPasswordValid(password),
                isConfirmPasswordValid(password, confirmPassword)));
    }

    boolean isValid() {
        return Objects.requireNonNull(registerFormState.getValue()).isDataValid();
    }

    private Integer isNameValid(String name) {
        if (name != null && name.trim().length() > 5)
            return null;
        else
            return R.string.invalid_name;
    }

    private Integer isEmailValid(String username) {
        if (Patterns.EMAIL_ADDRESS.matcher(username).matches())
            return null;
        else
            return R.string.invalid_email;
    }

    private Integer isPasswordValid(String password) {
        if (password != null && password.trim().length() > 5)
            return null;
        else
            return R.string.invalid_password;
    }

    private Integer isConfirmPasswordValid(String password, String confirmPassword) {
        Integer error = isPasswordValid(confirmPassword);
        if (error != null)
            return error;

        if (confirmPassword.equals(password))
            return null;
        else
            return R.string.invalid_confirm_password;
    }
}
