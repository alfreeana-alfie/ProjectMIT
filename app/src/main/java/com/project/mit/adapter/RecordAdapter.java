package com.project.mit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mit.R;
import com.project.mit.details.RecordDetails;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    Context context;
    private List<RecordDetails> recordDetailsList;
    private OnItemClickListener mListerner;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListerner = listener;
    }

    public RecordAdapter(Context context, List<RecordDetails> recordDetails){
        this.context = context;
        this.recordDetailsList = recordDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_scanned_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {
        RecordDetails recordDetails = recordDetailsList.get(position);
        String Name = recordDetails.getLocationName();
//        String Address = recordDetails.getLocationFullAddress();
        String DateTime = recordDetails.getCreatedDateTime();

        holder.LocationName.setText(Name);
//        holder.LocationFullAddress.setText(Address);
        holder.DateTime.setText(DateTime);


    }

    @Override
    public int getItemCount() {
        return recordDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView LocationName, LocationFullAddress, DateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            LocationName = itemView.findViewById(R.id.LocationName);
//            LocationFullAddress = itemView.findViewById(R.id.LocationAddress);
            DateTime = itemView.findViewById(R.id.DateTime);

            itemView.setOnClickListener(v -> {
                if (mListerner != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mListerner.onViewClick(position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onViewClick(int position);
    }
}
