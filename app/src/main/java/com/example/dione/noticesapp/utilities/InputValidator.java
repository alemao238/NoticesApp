package com.example.dione.noticesapp.utilities;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.example.dione.noticesapp.R;

/**
 * Created by Donds on 3/14/2017.
 */

public class InputValidator {
    public static boolean validate(Context context, EditText editText, TextInputLayout textInputLayout) {
        boolean isValid = true;
        if (editText.getText().toString().trim().isEmpty()) {
            isValid = false;
            textInputLayout.setError(context.getString(R.string.error_field_required));
        } else {
            textInputLayout.setError(null);
        }
        return isValid;
    }
}
