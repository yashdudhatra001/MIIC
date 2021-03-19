package com.yash1213.miic.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.yash1213.miic.Model.Posts;
import com.yash1213.miic.R;


public class AddUpdatesActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView imageCancel, imageUpdate;
    private TextView tvPost;
    private EditText etDesc;
    private String imageUrl;
    private Uri imageUri;
    private String time, desc;
    private DatabaseReference dbRef;
    private StorageReference store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_add_updates);

        imageCancel = findViewById(R.id.image_cancel);
        imageUpdate = findViewById(R.id.image_added);
        tvPost = findViewById(R.id.tv_post);
        etDesc = findViewById(R.id.description);
        progressBar = findViewById(R.id.progress_circular);

        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDesc.setText("");
                onBackPressed();
            }
        });

        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        CropImage.activity().start(AddUpdatesActivity.this);
    }

    private void upload() {

        progressBar.setVisibility(View.VISIBLE);
        tvPost.setVisibility(View.GONE);

        if(imageUri != null) {
            time = ""+System.currentTimeMillis();
            store = FirebaseStorage.getInstance().getReference("Posts").child(time+"."+getFileExtension(imageUri));
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
                    desc = etDesc.getText().toString().trim().concat("");
                    Posts posts = new Posts(time, imageUrl,desc);
                    dbRef = FirebaseDatabase.getInstance().getReference("Posts").child(""+time);
                    dbRef.setValue(posts);
                    Intent ii = new Intent(AddUpdatesActivity.this, CoUpdateActivity.class);
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
            imageUpdate.setImageURI(imageUri);
        } else{
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddUpdatesActivity.this,CoUpdateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.xml.activity_in,R.xml.activity_out);
            finish();
        }
    }
}