package com.yash1213.miic.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yash1213.miic.Model.Posts;
import com.yash1213.miic.R;

import java.util.ArrayList;

public class UpdatesAdapter extends RecyclerView.Adapter<UpdatesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Posts> postList;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private FirebaseAuth mAuth;
    private String uId = null;
    private DatabaseReference dbRef;
    private View view;

    public UpdatesAdapter(Context context, ArrayList<Posts> postList) {
        this.context = context;
        this.postList = postList;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));
        // with below method we are setting display option for our image..
        displayImageOptions = new DisplayImageOptions.Builder()
                // stub image will display when your image is loading
                .showStubImage(R.drawable.ic_image_360)
                // below image will be displayed when the image url is empty
                .showImageForEmptyUri(R.drawable.ic_image_360)
                // cachememory method will caches the image in users external storage
                .cacheInMemory()
                // cache on disc will caches the image in users internal storage
                .cacheOnDisc()
                // build will build the view for displaying image..
                .build();

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            uId = mAuth.getCurrentUser().getUid();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_updates,parent,false);
        return new UpdatesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Posts p = postList.get(position);
        imageLoader.loadImage(p.getImageUri(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                imageLoader.displayImage(String.valueOf(R.drawable.ic_image_360),holder.imageView,displayImageOptions,null);
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageLoader.displayImage(p.getImageUri(),holder.imageView,displayImageOptions,null);
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        holder.textView.setText(p.getDescription());
        Linkify.addLinks(holder.textView,Linkify.WEB_URLS);
        final String id = p.getPostId();
        if(uId==null){
            holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    simpleAlert(view,id);
                    return true;
                }
            });
        }
    }

    public void simpleAlert(View view, final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure to delete?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbRef = FirebaseDatabase.getInstance().getReference("Posts");
                        dbRef.child(id).removeValue();
                        //Posts/1615443374373.null  -- image location
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReference();
                        StorageReference delRef = storageReference.child("Posts/"+id+".null");
                        delRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Record Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(context, "Unable to delete data", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context.getApplicationContext(),
                        android.R.string.no, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.update_layout);
            imageView = itemView.findViewById(R.id.update_image);
            textView = itemView.findViewById(R.id.update_text);
        }
    }
}
