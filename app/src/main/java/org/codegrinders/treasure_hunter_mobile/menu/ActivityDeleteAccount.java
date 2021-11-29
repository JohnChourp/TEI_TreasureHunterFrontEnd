package org.codegrinders.treasure_hunter_mobile.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.codegrinders.treasure_hunter_mobile.R;
import org.codegrinders.treasure_hunter_mobile.retrofit.RetroCallBack;
import org.codegrinders.treasure_hunter_mobile.retrofit.UsersCall;
import org.codegrinders.treasure_hunter_mobile.ui.ActivityStart;

public class ActivityDeleteAccount extends AppCompatActivity {

    EditText et_email, et_password, et_confirmPassword;
    Button bt_delete;

    UsersCall usersCall = new UsersCall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteaccount);

        et_email = findViewById(R.id.et_emailForDel);
        et_password = findViewById(R.id.et_currentPasswordDel);
        et_confirmPassword = findViewById(R.id.et_confirmPasswordDel);
        bt_delete = findViewById(R.id.bt_submitDel);

        RetroCallBack retroCallBack = new RetroCallBack() {
            @Override
            public void onCallFinished(String callType) {
                if(validate()) {
                    Toast.makeText(ActivityDeleteAccount.this, "Account has been deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityDeleteAccount.this, "Deleting account has failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCallFailed(String errorMessage) {

            }
        };
        usersCall.setCallBack(retroCallBack);
            bt_delete.setOnClickListener(v -> {
                usersCall.oneUserDelete(UsersCall.user.getId());
                if(validate()) {
                    Toast.makeText(this, "Account has been deleted successfully", Toast.LENGTH_SHORT).show();
                    openActivityStart();
                }
            });

    }

    public void openActivityStart() {
        Intent intent = new Intent(this, ActivityStart.class);
        startActivity(intent);
    }

    public boolean validate() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();

        if(!email.equals(UsersCall.user.getEmail())) {
            et_email.setError("The email given doesn't match with the account's email");
            return false;
        } else if(!password.equals(UsersCall.user.getPassword())) {
            et_password.setError("The password given is incorrect");
            return false;
        } else if(!confirmPassword.equals(password)) {
            et_confirmPassword.setError("Passwords do not match");
            return false;
        } else {
            return true;
        }
    }
}