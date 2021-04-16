package com.yash1213.miic.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yash1213.miic.R;

public class CoLoginActivity extends Activity {

    private EditText etconame, etcopass;
    private Button btlogin;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_login);

        etconame = findViewById(R.id.et_co_uname);
        etcopass = (EditText) findViewById(R.id.et_co_upass);

        btlogin = findViewById(R.id.bt_co_login);

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = etconame.getText().toString();
                password = etcopass.getText().toString();

                if (username.equals("")) {
                    etconame.setError("Username cannot be Empty!");
                    etconame.requestFocus();
                } else if (password.equals("")) {
                    etcopass.setError("Password cannot be Empty!");
                    etcopass.requestFocus();
                } else {
                    if(username.equals("Miic.@possible") && password.equals("mIIc#24redefine")){
                        Intent ii = new Intent(getApplicationContext(), CoActivity.class);
                        startActivity(ii);
                    }else{
                        Toast.makeText(CoLoginActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    // check for Authentication
                }
            }
        });

    }
}