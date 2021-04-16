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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yash1213.miic.Model.Posts;
import com.yash1213.miic.Notification.Client;
import com.yash1213.miic.Notification.Data;
import com.yash1213.miic.Notification.MyResponse;
import com.yash1213.miic.Notification.Sender;
import com.yash1213.miic.Notification.Token;
import com.yash1213.miic.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    private  APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_add_updates);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

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
                    sendNotification();
                    Toast.makeText(AddUpdatesActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
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

    private void sendNotification(){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        tokens.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(snapshot.getKey(), R.mipmap.ic_icon,"New Updates. Take a look." , "New Activity Update",
                            snapshot.getKey());
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            //Toast.makeText(AddUpdatesActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }else{
                                           // Toast.makeText(AddUpdatesActivity.this, "SuccessNotification", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}