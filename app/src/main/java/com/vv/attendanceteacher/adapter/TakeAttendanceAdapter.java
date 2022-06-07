package com.vv.attendanceteacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vv.attendanceteacher.R;
import com.vv.attendanceteacher.models.ShowAttendanceModel;

import java.util.List;

public class TakeAttendanceAdapter extends RecyclerView.Adapter<TakeAttendanceAdapter.ViewHolder> {

    private List<ShowAttendanceModel> listdata;

    // RecyclerView recyclerView;
    public TakeAttendanceAdapter(List<ShowAttendanceModel> listdata) {
        this.listdata = listdata;
    }

    public void clearAll(){
        listdata.clear();
        notifyDataSetChanged();
    }
    
    public List<ShowAttendanceModel> getList(){
        return listdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View photoView = inflater.inflate(R.layout.student_list,parent, false);

        TakeAttendanceAdapter.ViewHolder viewHolder = new TakeAttendanceAdapter.ViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rollNo.setText(listdata.get(position).getAdmissionNumber());
        holder.sName.setText(listdata.get(position).getFirstName() +" "+listdata.get(position).getLastName());
        holder.checkBox.setChecked(listdata.get(position).getStatus().equals("1"));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listdata.get(holder.getAdapterPosition()).setStatus(holder.checkBox.isChecked()?"1":"0");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rollNo,sName;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rollNo = itemView.findViewById(R.id.rollNo);
            sName = itemView.findViewById(R.id.sName);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
