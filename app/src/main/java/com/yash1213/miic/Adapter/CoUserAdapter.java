package com.yash1213.miic.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yash1213.miic.Activity.CoProfileActivity;
import com.yash1213.miic.R;
import com.yash1213.miic.Model.Students;

import java.util.ArrayList;

public class CoUserAdapter extends RecyclerView.Adapter<CoUserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Students> studentList;
    private FirebaseAuth mAuth;
    private String uId = null;
    private DatabaseReference dbRef;
    private View view;

    public CoUserAdapter(Context context, ArrayList<Students> studentList){
        this.context = context;
        this.studentList = studentList;
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            uId = mAuth.getCurrentUser().getUid();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.student_item,parent,false);
        return new CoUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.idName.setText(studentList.get(position).getStudentName());
        holder.tvProfile.setText(studentList.get(position).getStudentName().substring(0,1));
        holder.idRating.setText(""+studentList.get(position).getTotalPoint());
        holder.itemStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(context, CoProfileActivity.class);
                ii.putExtra("sId",studentList.get(position).getStudentId());
                ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ii);
            }
        });
        if(uId!=null){
            holder.itemStudent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    simpleAlert(view);
                    return true;
                }
            });
        }
    }

    public void simpleAlert(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure to delete?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbRef = FirebaseDatabase.getInstance().getReference("students");
                        dbRef.child(uId).removeValue();
                        notifyDataSetChanged();
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
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemStudent;
        TextView idName, idRating;
        TextView tvProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idName = itemView.findViewById(R.id.idTVName);
            idRating = itemView.findViewById(R.id.idTVRating);
            itemStudent = itemView.findViewById(R.id.student_layout);
            tvProfile = itemView.findViewById(R.id.textFirstChar);
        }
    }
}
