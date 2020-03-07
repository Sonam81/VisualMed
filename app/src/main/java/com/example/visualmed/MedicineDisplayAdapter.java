package com.example.visualmed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MedicineDisplayAdapter extends RecyclerView.Adapter<MedicineDisplayAdapter.MedicineViewHolder> {

    private Context context;
    private List<MedicineDetail> medicineDetailList;

    public MedicineDisplayAdapter(Context context, List<MedicineDetail> medicineDetailList){
        this.context = context;
        this.medicineDetailList = medicineDetailList;
    }

    @Override
    public MedicineViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.medicine_detail, null);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MedicineViewHolder holder, int position) {
        MedicineDetail medicineDetail = medicineDetailList.get(position);

        holder.textMedicineTime.setText(medicineDetail.getMedicineName());
        holder.textMedicineName.setText(medicineDetail.getMedicineTime());

    }

    @Override
    public int getItemCount() {
        return medicineDetailList.size();
    }

    class MedicineViewHolder extends RecyclerView.ViewHolder {

        TextView textMedicineName, textMedicineTime;

        public MedicineViewHolder(View itemView) {
            super(itemView);
            textMedicineName = itemView.findViewById(R.id.textMedicineName);
            textMedicineTime = itemView.findViewById(R.id.textMedicineTime);

        }
    }

}