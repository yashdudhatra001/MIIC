package com.yash1213.miic.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yash1213.miic.Model.COs;
import com.yash1213.miic.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrentCosAdapter extends RecyclerView.Adapter<CurrentCosAdapter.ViewHolder> {

    private Context context;
    private ArrayList<COs> coList;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private FirebaseAuth mAuth;
    private String uId = null;
    private DatabaseReference dbRef;
    private View view;

    public CurrentCosAdapter(Context context, ArrayList<COs> coList) {
        this.context = context;
        this.coList = coList;
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
        view = LayoutInflater.from(context).inflate(R.layout.item_team,parent,false);
        return new CurrentCosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvName.setText(coList.get(position).getcName().concat(""));
        holder.tvEmail.setText(coList.get(position).getcEmail().concat(""));
        holder.tvContact.setText(coList.get(position).getcPhone().concat(""));
        imageLoader.displayImage(coList.get(position).getcImage(),holder.imageView,displayImageOptions,null);

        if(uId==null){
            holder.rvTeam.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final String naa = coList.get(position).getcName();
                    simpleAlert(view,naa);
                    return true;
                }
            });
        }
    }

    public void simpleAlert(View view, final String naa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure to delete ?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbRef = FirebaseDatabase.getInstance().getReference();
                        Query query = dbRef.child("team").orderByChild("cName").equalTo(naa);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                    dataSnapshot.getRef().removeValue();
                                    notifyDataSetChanged();
                                }
                                FirebaseStorage store = FirebaseStorage.getInstance();
                                StorageReference storageRef = store.getReference();
                                //gs://miic-3d891.appspot.com/Team/dean.null
                                StorageReference delRef = storageRef.child("Team/"+naa+".null");
                                delRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Toast.makeText(context, "Error :"+e, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
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
        return coList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rvTeam;
        TextView tvName, tvEmail, tvContact;
        CircleImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvTeam = itemView.findViewById(R.id.team_layout);
            tvName = itemView.findViewById(R.id.cName);
            tvEmail = itemView.findViewById(R.id.cEmail);
            tvContact = itemView.findViewById(R.id.cContact);
            imageView = itemView.findViewById(R.id.imageC);
        }
    }
}