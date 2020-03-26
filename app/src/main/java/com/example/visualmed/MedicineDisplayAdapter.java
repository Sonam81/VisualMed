package com.example.visualmed;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class MedicineDisplayAdapter extends RecyclerView.Adapter<MedicineDisplayAdapter.MedicineViewHolder> {

    private Context context;
    private List<MedicineDetail> medicineDetailList;
    MedicineDAO medicineDAO;
    TextToSpeech textToSpeech;

    public MedicineDisplayAdapter(Context context, List<MedicineDetail> medicineDetailList){
        this.context = context;
        this.medicineDetailList = medicineDetailList;
    }

    @Override
    public MedicineViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.medicine_detail, null);
        initializeTextToSpeech();
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

    public void initializeTextToSpeech(){
        textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(context.getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    class MedicineViewHolder extends RecyclerView.ViewHolder {

        TextView textMedicineName, textMedicineTime;

        public MedicineViewHolder(final View itemView) {
            super(itemView);
            textMedicineName = itemView.findViewById(R.id.textMedicineName);
            textMedicineTime = itemView.findViewById(R.id.textMedicineTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    List<MedicineWithTime> medicineWithTimes;
                    medicineWithTimes = ManageMedicineActivity.myAppDatabase.medicineDAO().getAll();

//                    Medicine medicine = ManageMedicineActivity.myAppDatabase.medicineDAO().getAll().get(position).getMedicine();
                    Medicine medicine = medicineWithTimes.get(position).getMedicine();
                    String medicineName = medicine.getMedicineName();
                    String medicineTime = "";
                    for (int i = 0; i < medicineWithTimes.get(position).getMedicineTimes().size(); i++){
                        medicineTime = medicineTime + medicineWithTimes.get(position).getMedicineTimes().get(i).getMedicineTime()+" . ";
                    }
                    speak("Your medicine Name is "+medicineName+ ". And the time "+ medicineTime);
                }
            });

        }
    }

    public void speak(String message){
        if(Build.VERSION.SDK_INT >= 21){
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null, null);
        }
        else{
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }


}