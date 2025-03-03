package com.water.learnchild.activities.parent;

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.water.learnchild.R;
import com.water.learnchild.callback.ScreenTimeCallBack;
import com.water.learnchild.controller.ScreenTimeController;
import com.water.learnchild.model.ScreenTime;
import com.water.learnchild.utils.LoadingHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ScreenTimesActivity extends AppCompatActivity {

    TextView title;
    RecyclerView recordsList;
    LoadingHelper loadingHelper;
    ArrayList<ScreenTime> screenTimes = new ArrayList<>();
    ImageButton add;
    String fromDate = "",toDate = "";
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_times);

        loadingHelper = new LoadingHelper(this);
        title = findViewById(R.id.title);
        recordsList = findViewById(R.id.records_list);
        add = findViewById(R.id.add);

        title.setText(SharedData.selectedChild.getName()+" Available Screen Times");
        recordsList.setItemAnimator(new DefaultItemAnimator());
        recordsList.setLayoutManager(new LinearLayoutManager(this));

        loadingHelper.showLoading("Loading "+SharedData.selectedChild.getName()+" Records");
        new ScreenTimeController().getChildScreenTimes(SharedData.selectedChild, new ScreenTimeCallBack() {
            @Override
            public void onSuccess(ArrayList<ScreenTime> data) {
                loadingHelper.dismissLoading();
                screenTimes = data;
                recordsList.setAdapter(new ScreenTimeAdapter());
            }

            @Override
            public void onFail(String msg) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

    }


    private void showAddDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter_daily, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);

        final Button dialogBtnSubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageButton dialogBtnClose = dialogView.findViewById(R.id.btn_close);
        final EditText fromHour = dialogView.findViewById(R.id.from_date);
        final EditText toHour = dialogView.findViewById(R.id.to_date);

        fromHour.setText(fromDate);
        toHour.setText(toDate);

        fromHour.setShowSoftInputOnFocus(false);
        fromHour.setOnClickListener(v -> fromHourPicker(fromHour,fromDate,1));
        fromHour.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                fromHourPicker(fromHour,fromDate,1);
            }
        });


        toHour.setShowSoftInputOnFocus(false);
        toHour.setOnClickListener(v -> fromHourPicker(toHour,toDate,2));
        toHour.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                fromHourPicker(toHour,toDate,2);
            }
        });


        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        dialogBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromDate.isEmpty()){
                    fromHour.setError("Required");
                    return;
                }

                if(toDate.isEmpty()){
                    toHour.setError("Required");
                    return;
                }
                ScreenTime screenTime = new ScreenTime("",fromDate,toDate,SharedData.selectedChild);
                loadingHelper.showLoading("Saving Data");
                new ScreenTimeController().Save(screenTime, new ScreenTimeCallBack() {
                    @Override
                    public void onSuccess(ArrayList<ScreenTime> data) {
                        loadingHelper.dismissLoading();
                        fromDate = "";
                        toDate = "";
                        Toast.makeText(ScreenTimesActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
                alertDialog.dismiss();
            }
        });

        dialogBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void fromHourPicker(EditText fromHour,String date,int type) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int sYear = c.get(Calendar.HOUR_OF_DAY);
        int sMonth = c.get(Calendar.MINUTE);

        if(date.contains(":")) {
            sYear = Integer.parseInt(date.substring(0, date.indexOf(":")));
            date = date.substring(date.indexOf(":") + 1);
            sMonth = Integer.parseInt(date);
        }


        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                fromHour.setText(String.format(Locale.ENGLISH,"%02d:%02d", i, i1));
                if(type == 1){
                    fromDate = fromHour.getText().toString();
                }else{
                    toDate = fromHour.getText().toString();
                }
            }
        },sYear,sMonth,true);

       dialog.show();
    }


    class ScreenTimeAdapter extends RecyclerView.Adapter<ScreenTimeAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ScreenTimesActivity.this)
                    .inflate(R.layout.screen_time_row,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ScreenTime screenTime = screenTimes.get(position);
            holder.name.setText(screenTime.getFrom_hour() +" : "+screenTime.getTo_hour());
            holder.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new LoadingHelper(ScreenTimesActivity.this)
                            .showDialog("Delete", "Are you sure?", "Delete", "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new ScreenTimeController().delete(screenTime, new ScreenTimeCallBack() {
                                        @Override
                                        public void onSuccess(ArrayList<ScreenTime> data) {
                                            Toast.makeText(ScreenTimesActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFail(String msg) {

                                        }
                                    });
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                }
            });
        }

        @Override
        public int getItemCount() {
            return screenTimes.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            TextView name;
            ImageButton btnReject;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.name);
                btnReject = itemView.findViewById(R.id.btnReject);
            }
        }
    }
}