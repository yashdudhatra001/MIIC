package com.yash1213.miic.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yash1213.miic.Model.StudentDetails;
import com.yash1213.miic.Model.Students;
import com.yash1213.miic.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentSignupActivity extends Activity {

    private ImageView imageBack;
    private EditText etfname, etlname, etmail, etPRN, etph, etpass;
    private Button btsingup;
    private Spinner sp_year;
    private List<String> years;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private ArrayList<StudentDetails> detailsArrayList = new ArrayList<>();

    private String fname, lname, email, PRN, contact, password, year;
    private ProgressBar progressBar;

    //2019 0338 0012 5652

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_student_signup);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("students");

        imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //finish();
            }
        });

        etfname = findViewById(R.id.etfname);
        etlname = findViewById(R.id.etlname);
        etmail = findViewById(R.id.etmail);
        etPRN = findViewById(R.id.etPRN);
        etph = findViewById(R.id.etph);
        etpass = findViewById(R.id.etpass);
        sp_year = findViewById(R.id.sp_year);
        progressBar = findViewById(R.id.progress_circular);

        years = new ArrayList<>();
        years.add("BE-I");
        years.add("BE-II");
        years.add("BE-III");
        years.add("BE-IV");

        ArrayAdapter<String> adpt = new ArrayAdapter<>(this, R.layout.my_spinner, years);
        adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_year.setAdapter(adpt);

        btsingup = findViewById(R.id.btsignup);

        btsingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fname = etfname.getText().toString();
                lname = etlname.getText().toString();
                email = etmail.getText().toString();
                PRN = etPRN.getText().toString();
                contact = etph.getText().toString();
                password = etpass.getText().toString();
                year = sp_year.getSelectedItem().toString();

                //String expContact = "^[0-9]{10}$";
                //Pattern patContact = Pattern.compile(expContact);
                //Matcher matContact = patContact.matcher(contact);

                String expPRN = "^[0-9]{16}$";
                Pattern patPRN = Pattern.compile(expPRN);
                Matcher matPRN = patPRN.matcher(PRN);

                final String expPass = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
                Pattern patPass = Pattern.compile(expPass);
                Matcher matPass = patPass.matcher(password);


                String expMail = "^[A-Za-z0-9+_.-]+@[a-zA-Z0-9.-]+$";
                Pattern patMail = Pattern.compile(expMail);
                Matcher matMail = patMail.matcher(email);

                if (TextUtils.isEmpty(fname) ) {
                    Toast.makeText(StudentSignupActivity.this, "\"First name cannot be Empty!\"", Toast.LENGTH_SHORT).show();
                    etfname.requestFocus();

                } else if (lname.equals("")) {
                    Toast.makeText(StudentSignupActivity.this, "\"Last name cannot be Empty!\"", Toast.LENGTH_SHORT).show();
                    etlname.requestFocus();

                } else if (!matMail.matches()) {
                    Toast.makeText(StudentSignupActivity.this, "\"Enter a valid email address!\"", Toast.LENGTH_SHORT).show();
                    etmail.requestFocus();

                } else if (!matPass.matches()) {
                    Toast.makeText(StudentSignupActivity.this, "\"At least 1 digit, letter and special character with length greater than 8 is required!\"", Toast.LENGTH_SHORT).show();
                    etpass.requestFocus();

                } else if (!matPRN.matches()) {
                    Toast.makeText(StudentSignupActivity.this, "\"PRN must be of 16 numeric digits!\"", Toast.LENGTH_SHORT).show();
                    etPRN.requestFocus();

                } else if (TextUtils.isEmpty(contact) || contact.length()<10) {
                    Toast.makeText(StudentSignupActivity.this, "\"Enter a valid Contact no.!\"", Toast.LENGTH_SHORT).show();
                    etph.requestFocus();

                } else {
                    btsingup.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful() ) {

                                if( year.equals("BE-III")){
                                    dbRef = FirebaseDatabase.getInstance().getReference("students");
                                    String id = mAuth.getCurrentUser().getUid();
                                    String fn = fname + " "+ lname;
                                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                    String description = "Welcome to MIIC";
                                    StudentDetails sd = new StudentDetails(date,description,0);
                                    detailsArrayList.add(sd);
                                    Students student = new Students(id,fn,year,email,contact,PRN,0,detailsArrayList);
                                    dbRef.child(id).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Intent ii = new Intent(StudentSignupActivity.this,LoginActivity.class);
                                                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(ii);
                                                overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
                                                Toast.makeText(StudentSignupActivity.this, "SignUp Successful as BE-III", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    Intent ii = new Intent(StudentSignupActivity.this,LoginActivity.class);
                                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(ii);
                                    overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
                                    Toast.makeText(StudentSignupActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                progressBar.setVisibility(View.GONE);
                                btsingup.setVisibility(View.VISIBLE);
                                try
                                {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthUserCollisionException existEmail)
                                {
                                    //Log.d(TAG, "onComplete: exist_email");
                                    Toast.makeText(StudentSignupActivity.this, "Email already exist", Toast.LENGTH_SHORT).show();
                                    etmail.setText("");
                                    etmail.requestFocus();
                                }
                                catch (Exception e)
                                {
                                    //Log.d(TAG, "onComplete: " + e.getMessage());
                                    Toast.makeText(StudentSignupActivity.this, "Something went Wrong.", Toast.LENGTH_SHORT).show();
                                    etfname.requestFocus();
                                    etfname.setText("");
                                    etlname.setText("");
                                    etmail.setText("");
                                    etpass.setText("");
                                    etph.setText("");
                                    etPRN.setText("");
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
