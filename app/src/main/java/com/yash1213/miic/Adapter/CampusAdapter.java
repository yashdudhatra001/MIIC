package com.yash1213.miic.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yash1213.miic.Model.Campus;
import com.yash1213.miic.R;

import java.util.ArrayList;

public class CampusAdapter extends RecyclerView.Adapter<CampusAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Campus> campuses;
    private String uId = null;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private View view;

    public CampusAdapter(Context context, ArrayList<Campus> campuses) {
        this.context = context;
        this.campuses = campuses;
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            uId = mAuth.getCurrentUser().getUid();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.item_campus,parent,false);
        return new CampusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.pt.setText(campuses.get(position).getTitleText());
        holder.st.setText(campuses.get(position).getSecondaryText());
        Linkify.addLinks(holder.st,Linkify.WEB_URLS);
        holder.rm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.rm.getText().toString().equals("Read More")){
                    holder.st.setMaxLines(15);
                    holder.st.setEllipsize(null);
                    holder.rm.setText("Read Less");
                }else{
                    holder.st.setMaxLines(2);
                    holder.st.setEllipsize(TextUtils.TruncateAt.END);
                    holder.rm.setText("Read More");
                }
            }
        });
        if(uId==null){
            final String teamTitle = campuses.get(position).getTitleText();
            holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    simpleAlert(view,teamTitle);
                    return true;
                }
            });
        }
    }

    public void simpleAlert(View view, final String teamTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure to delete?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbRef = FirebaseDatabase.getInstance().getReference();
                        Query query = dbRef.child("campus").orderByChild("titleText").equalTo(teamTitle);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    dataSnapshot.getRef().removeValue();
                                    notifyDataSetChanged();
                                }
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
        return campuses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView pt, st, rm;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pt = itemView.findViewById(R.id.tvFText);
            st = itemView.findViewById(R.id.tvSText);
            rm = itemView.findViewById(R.id.tvReadMore);
            layout = itemView.findViewById(R.id.camous_layout);
        }
    }
}
