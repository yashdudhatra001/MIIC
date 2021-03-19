package com.yash1213.miic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yash1213.miic.R;
import com.yash1213.miic.Model.Students;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Students> studentList;

    public UsersAdapter(Context context, ArrayList<Students> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_item,parent,false);
        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.idName.setText(studentList.get(position).getStudentName());
        holder.tvProfile.setText(studentList.get(position).getStudentName().substring(0,1));
        holder.idRating.setText(""+studentList.get(position).getTotalPoint());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView idName,idRating;
        TextView tvProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idName = itemView.findViewById(R.id.idTVName);
            idRating = itemView.findViewById(R.id.idTVRating);
            tvProfile = itemView.findViewById(R.id.textFirstChar);
        }
    }
}
