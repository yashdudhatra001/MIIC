package com.yash1213.miic.Activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.yash1213.miic.R;


public class LoginActivity extends Activity {

    private EditText etuname, etupass;
    private Button btlogin;
    private CheckBox chkpass;
    private ProgressBar progressBar;

    private String username,password;

    private SharedPreferences shPref;
    private SharedPreferences.Editor shPrefEditor;
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_login);

        etuname = findViewById(R.id.et_st_uname);
        etupass = findViewById(R.id.et_st_upass);

        btlogin = findViewById(R.id.bt_st_login);

        chkpass = findViewById(R.id.chkpass);
        progressBar = findViewById(R.id.progress_circular);

        mAuth = FirebaseAuth.getInstance();
        shPref = getSharedPreferences("UserLogIn", Context.MODE_PRIVATE);                 // Shred Pref file  - UserLogIn.xml
        shPrefEditor = shPref.edit();                                                           // shPref editor

        if(!shPref.contains("username")) {              // if userdata isn't already saved then clear every field.
            chkpass.setChecked(false);
            shPrefEditor.clear();
            shPrefEditor.apply();
        }
        else {                                                                              // else fetch data into Edit Texts and tick chkboc
            chkpass.setChecked(true);

            username = shPref.getString("username","");
            password = shPref.getString("password", "");
            etuname.setText(username);
            etupass.setText(password);
        }

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                  // onCLick Login

                username = etuname.getText().toString();
                password = etupass.getText().toString();

                if (username.equals("")) {                                                        // Fields cant be empty
                    etuname.setError("Username cannot be Empty!");
                    etuname.requestFocus();
                } else if (password.equals("")) {
                    etupass.setError("Password cannot be Empty!");
                    etupass.requestFocus();
                } else {                                                                          // else save / not save-> username / password into shpref.
                    btlogin.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    username = etuname.getText().toString();
                    password = etupass.getText().toString();

                    if (chkpass.isChecked()) {
                        shPrefEditor.putString("username", username);
                        shPrefEditor.putString("password", password);
                        shPrefEditor.commit();
                    } else {
                        shPrefEditor.clear();
                        shPrefEditor.apply();
                    }
                    mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkIfEmailVerified();
                            }else{
                                btlogin.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                try
                                {
                                    throw task.getException();
                                }
                                // if user enters wrong email.
                                catch (FirebaseAuthInvalidUserException invalidEmail)
                                {
                                    //Log.d(TAG, "onComplete: invalid_email");
                                    Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                                    etuname.requestFocus();
                                    etuname.setText("");
                                }
                                // if user enters wrong password.
                                catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                {
                                    //Log.d(TAG, "onComplete: wrong_password");
                                    Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                    etupass.setText("");
                                    etupass.requestFocus();
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    etuname.setText("");
                                    etupass.setText("");
                                    etuname.requestFocus();
                                    //Log.d(TAG, "onComplete: " + e.getMessage());
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            Intent ii = new Intent(getApplicationContext(),MainActivity.class);
            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(ii);
            overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
            finish();
            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();
            progressBar.setVisibility(View.GONE);
            btlogin.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Verify your Email.", Toast.LENGTH_SHORT).show();
            //restart this activity
        }
    }

    public void coordinatorLogin(View view) {                                                   // when user wants to login as coordinator

        TextView tv = findViewById(view.getId());
        //tv.setTextColor(Color.BLUE);
        Intent ii = new Intent(LoginActivity.this, CoLoginActivity.class);
        startActivity(ii);
        overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
    }

    public void studentSignUp(View view) {                                                      // when new user wants to signup

        TextView tv = findViewById(view.getId());
        //tv.setTextColor(Color.BLUE);
        Intent ii = new Intent(LoginActivity.this,StudentSignupActivity.class);
        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ii);
        overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
    }

    public void forgotPassword(View view){
        Intent ii = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
        startActivity(ii);
        overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (user != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
            finish();
        }
        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){


            Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            } catch (Exception e) {
            }
        }
    }
}