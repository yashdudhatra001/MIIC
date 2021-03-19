package com.yash1213.miic.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yash1213.miic.Model.COs;
import com.yash1213.miic.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddTeamActivity extends AppCompatActivity {

    private CircleImageView imageProfile;
    private EditText etName, etEmail, etContact;
    private Button addMember;
    private ProgressBar progressBar;
    private String imageUrl;
    private Uri imageUri;
    private DatabaseReference dbRef;
    private StorageReference store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_add_team);

        imageProfile = findViewById(R.id.profile_image);
        etName = findViewById(R.id.cNameAdd);
        etContact = findViewById(R.id.cContactAdd);
        etEmail = findViewById(R.id.cEmailAdd);
        addMember = findViewById(R.id.cMemberAdd);
        progressBar = findViewById(R.id.progress_circular);

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        CropImage.activity().start(AddTeamActivity.this);
    }

    private void upload() {
        addMember.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if(imageUri!= null){
            store = FirebaseStorage.getInstance().getReference("Team")
                    .child(etName.getText().toString().trim().concat("")+"."+getFileExtension(imageUri));
            StorageTask storageTask = store.putFile(imageUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return store.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Uri downloadUri = (Uri) task.getResult();
                    imageUrl = downloadUri.toString();
                    String name = etName.getText().toString().trim().concat("");
                    String email = etEmail.getText().toString().trim().concat("");
                    String phn = etContact.getText().toString().trim().concat("");
                    COs member =new COs(name,email,phn,imageUrl);
                    dbRef = FirebaseDatabase.getInstance().getReference("team");
                    dbRef.push().setValue(member);
                    Intent ii = new Intent(AddTeamActivity.this, CoCurrentActivity.class);
                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(ii);
                    overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
                    finish();
                }
            });
        }else{
            Toast.makeText(this, "Not any image is selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri imageUri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            imageProfile.setImageURI(imageUri);
        } else{
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddTeamActivity.this,CoCurrentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
            finish();
        }
    }

}