package com.yash1213.miic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yash1213.miic.R;
import com.yash1213.miic.Model.StudentDetails;

import java.util.ArrayList;

public class CoCommentsAdapter extends RecyclerView.Adapter<CoCommentsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<StudentDetails> sdList;

    public CoCommentsAdapter(Context context, ArrayList<StudentDetails> sdList) {
        this.context = context;
        this.sdList = sdList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comments,parent,false);
        return new CoCommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDescription.setText(sdList.get(position).getDescription());
        holder.tvPoints.setText(""+sdList.get(position).getPoint());
        holder.tvDate.setText(sdList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return sdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription, tvDate, tvPoints;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPoints = itemView.findViewById(R.id.tvPoint);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
