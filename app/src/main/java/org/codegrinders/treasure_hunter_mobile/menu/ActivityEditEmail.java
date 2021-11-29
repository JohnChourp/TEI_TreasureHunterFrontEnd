package org.codegrinders.treasure_hunter_mobile.menu;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.codegrinders.treasure_hunter_mobile.R;
import org.codegrinders.treasure_hunter_mobile.retrofit.RetroCallBack;
import org.codegrinders.treasure_hunter_mobile.retrofit.UsersCall;

public class ActivityEditEmail extends AppCompatActivity {

    EditText et_currentEmail, et_newEmail, et_password;
    Button bt_changeEmail;

    UsersCall usersCall = new UsersCall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editemail);

        et_currentEmail = findViewById(R.id.et_currentEmail);
        et_newEmail = findViewById(R.id.et_newEmail);
        et_password = findViewById(R.id.et_password2);
        bt_changeEmail = findViewById(R.id.bt_submit2);

        bt_changeEmail.setOnClickListener(v -> {
            RetroCallBack retroCallBack = new RetroCallBack() {
                @Override
                public void onCallFinished(String callType) {
                    if (validate()) {
                        usersCall.oneUserGetEmail(UsersCall.user.getId(), et_newEmail.getText().toString());
                        Toast.makeText(ActivityEditEmail.this, "Email has been changed successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Changing email has failed", Toast.LENGTH_SHORT).show();
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
        String currentEmail = et_currentEmail.getText().toString();
        String password = et_password.getText().toString();

        if (!currentEmail.equals(UsersCall.user.getEmail())) {
            et_currentEmail.setError("The email given doesn't match with the account's email");
            return false;
        } else if (!password.equals(UsersCall.user.getPassword())) {
            et_password.setError("The password given is incorrect");
            return false;
        } else
            return true;
    }
}
