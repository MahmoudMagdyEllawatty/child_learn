package com.water.learnchild.activities.admin.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.water.learnchild.callback.ChildLogCallBack;
import com.water.learnchild.controller.ChildLogController;
import com.water.learnchild.model.ChildLog;
import com.water.learnchild.utils.LoadingHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    LoadingHelper loadingHelper;
    TextView childName;
    RecyclerView reportList;
    ArrayList<ChildLog> logs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        loadingHelper = new LoadingHelper(this);
        reportList = findViewById(R.id.report_list);
        childName = findViewById(R.id.text);
        reportList.setLayoutManager(new LinearLayoutManager(this));
        reportList.setItemAnimator(new DefaultItemAnimator());

        childName.setText("Report for Child : "+ SharedData.selectedChild.getName());
        loadReport();
    }

    private void loadReport(){
        loadingHelper.showLoading("Loading Report");
        new ChildLogController().getChildLog(SharedData.selectedChild, new ChildLogCallBack() {
            @Override
            public void onSuccess(ArrayList<ChildLog> children) {
                loadingHelper.dismissLoading();
                logs = children;
                reportList.setAdapter(new ReportAdapter());
            }

            @Override
            public void onFail(String msg) {
                loadingHelper.dismissLoading();
            }
        });
    }


    class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ReportActivity.this)
                    .inflate(R.layout.report_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ChildLog childLog = logs.get(position);

            holder.details.setText(childLog.getDescription());
            holder.date.setText(childLog.getDate());
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            TextView details,date;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                details = itemView.findViewById(R.id.details);
                date = itemView.findViewById(R.id.date);
            }
        }
    }
}