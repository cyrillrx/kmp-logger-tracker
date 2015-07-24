package com.cyrillrx.android.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.cyrillrx.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Cyril Leroux
 *         Created on 15/12/2014.
 */
public class FieldUtils {

    private static String sErrorFieldRequired;
    private static String sErrorInvalidPassword;
    private static String sErrorInvalidEmail;

    public static void init(Resources resources) {
        sErrorFieldRequired = resources.getString(R.string.error_field_required);
        sErrorInvalidPassword = resources.getString(R.string.error_invalid_password);
        sErrorInvalidEmail = resources.getString(R.string.error_invalid_email);
    }

    public static void validateFields(ValidationCallback callback, ValidationEntry... entries) {

        boolean cancel = false;
        View focusView = null;

        List<String> values = new ArrayList<>();
        String value;
        for (ValidationEntry entry : entries) {

            value = validateEditText(entry.getEditText(), entry.getPattern(), entry.getErrorMessage());
            values.add(value);

            if (value == null) {
                if (focusView == null) { focusView = entry.getEditText(); }
                cancel |= true;
            }
        }

        if (cancel) {
            // Focused the first field in error.
            focusView.requestFocus();
            callback.onFailure();

        } else {
            callback.onSuccess(values);
        }
    }

    private static String validateEditText(EditText editText, Pattern pattern, String errorMessage) {

        // Reset error
        editText.setError(null);

        // Cache the value
        final String value = editText.getText().toString();

        // Check emptiness
        if (TextUtils.isEmpty(value)) {
            editText.setError(sErrorFieldRequired);
            return null;
        }

        if (pattern.matcher(value).matches()) {
            return value;
        }

        editText.setError(errorMessage);
        return null;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public static void validateCredentials(EditText etEmail, EditText etPassword, ValidationCallback callback) {
        validateFields(callback,
                ValidationEntry.email(etEmail),
                new ValidationEntry(etPassword, Pattern.compile(".{4,}"), sErrorInvalidPassword));
    }

    public static class ValidationEntry {
        private final EditText mEditText;
        private final Pattern mPattern;
        private final String mErrorMessage;

        public static ValidationEntry email(EditText etEmail) {
            return new ValidationEntry(etEmail, Patterns.EMAIL_ADDRESS, sErrorInvalidEmail);
        }

        public ValidationEntry(EditText editText, Pattern pattern, String errorMessage) {
            mEditText = editText;
            mPattern = pattern;
            mErrorMessage = errorMessage;
        }

        public EditText getEditText() { return mEditText; }

        public Pattern getPattern() { return mPattern; }

        public String getErrorMessage() { return mErrorMessage; }
    }

    public interface ValidationCallback {
        void onSuccess(List<String> validValues);

        void onFailure();
    }
}
