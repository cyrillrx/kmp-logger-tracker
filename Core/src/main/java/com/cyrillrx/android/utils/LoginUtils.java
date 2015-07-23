package com.cyrillrx.android.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.cyrillrx.android.R;

/**
 * @author Cyril Leroux
 *         Created on 15/12/2014.
 */
public class LoginUtils {

    private static String sErrorFieldRequired;
    private static String sErrorInvalidPassword;
    private static String sErrorInvalidEmail;

    public static void init(Resources resources) {
        sErrorFieldRequired = resources.getString(R.string.error_field_required);
        sErrorInvalidPassword = resources.getString(R.string.error_invalid_password);
        sErrorInvalidEmail = resources.getString(R.string.error_invalid_email);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public static void attemptLogin(EditText etEmail, EditText etPassword, OnCredentialChecked callback) {

        // Reset errors.
        etEmail.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(sErrorFieldRequired);
            focusView = etPassword;
            cancel = true;

        } else if (!isPasswordValid(password)) {
            etPassword.setError(sErrorInvalidPassword);
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(sErrorFieldRequired);
            focusView = etEmail;
            cancel = true;

        } else if (!isEmailValid(email)) {
            etEmail.setError(sErrorInvalidEmail);
            focusView = etEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            callback.login(email, password);
        }
    }

    /**
     * @param email The email to check
     * @return True if the email is valid.
     */
    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * @param password The password to check
     * @return True if the password is valid.
     */
    public static boolean isPasswordValid(String password) {
        return !password.isEmpty();
    }

    public interface OnCredentialChecked {
        void login(String email, String password);
    }
}
