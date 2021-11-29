package org.codegrinders.treasure_hunter_mobile.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.codegrinders.treasure_hunter_mobile.R;
import org.codegrinders.treasure_hunter_mobile.retrofit.RetroCallBack;
import org.codegrinders.treasure_hunter_mobile.retrofit.UsersCall;

import java.util.regex.Pattern;

public class ActivityEditPassword extends AppCompatActivity {

    EditText et_email, et_currentPassword, et_newPassword, et_confirmPassword;
    Button bt_changePass;

    UsersCall usersCall = new UsersCall();

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,99}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassword);

        et_email = findViewById(R.id.et_emailForPass);
        et_currentPassword = findViewById(R.id.et_currentPassword);
        et_newPassword = findViewById(R.id.et_newPassword);
        et_confirmPassword = findViewById(R.id.et_confirmEditPassword);
        bt_changePass = findViewById(R.id.bt_submit3);

        bt_changePass.setOnClickListener(v -> {
            RetroCallBack retroCallBack = new RetroCallBack() {
                @Override
                public void onCallFinished(String callType) {
                    if (validate()) {
                        usersCall.oneUserGetPassword(UsersCall.user.getId(), et_newPassword.getText().toString());
                        Toast.makeText(ActivityEditPassword.this, "Password has been changed successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ActivityEditPassword.this, "Changing password has failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCallFailed(String errorMessage) {
                }
            };
            usersCall.oneUserGetRequest(UsersCall.user.getId());
            usersCall.setCallBack(retroCallBack);
            });
        }

        public boolean validate() {
            String email = et_email.getText().toString();
            String currentPassword = et_currentPassword.getText().toString();
            String newPassword = et_newPassword.getText().toString();
            String confirmedPassword = et_confirmPassword.getText().toString();

            if (!email.equals(UsersCall.user.getEmail())) {
                et_email.setError("The email given doesn't match with the account's email");
                return false;
            } else if (!currentPassword.equals(UsersCall.user.getPassword())) {
                et_currentPassword.setError("The password given is incorrect");
                return false;
            } else if (!PASSWORD_PATTERN.matcher(newPassword).matches() || newPassword.contains(" ")) {
                et_newPassword.setError("Password must be 8–99 characters long and must have at least 1 digit, 1 upper case letter and 1 special character without spaces");
                return false;
            } else if (!confirmedPassword.equals(newPassword)) {
                et_confirmPassword.setError("Passwords do not match");
                return false;
            } else
                return true;
        }
    }