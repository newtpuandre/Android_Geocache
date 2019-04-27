package ntnu.imt3673.android_geocache.ui.register;

import android.support.annotation.Nullable;

/**
 * Data validation state of the register form.
 */
class RegisterFormState {
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer confirmPasswordError;

    private boolean isDataValid;

    RegisterFormState(@Nullable Integer nameError, @Nullable Integer emailError, @Nullable Integer passwordError, @Nullable Integer confirmPasswordError) {
        this.nameError = nameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.confirmPasswordError = confirmPasswordError;
        this.isDataValid = nameError == null && emailError == null && passwordError == null && confirmPasswordError == null;
    }

    @Nullable
    Integer getNameError() {
        return nameError;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

    @Nullable
    Integer getConfirmPasswordError() {
        return confirmPasswordError;
    }
}
