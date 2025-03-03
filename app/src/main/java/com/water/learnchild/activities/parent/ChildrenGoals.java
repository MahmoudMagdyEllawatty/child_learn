package com.water.learnchild.activities.parent;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.water.learnchild.R;
import com.water.learnchild.callback.GoalCallBack;
import com.water.learnchild.callback.ScreenTimeCallBack;
import com.water.learnchild.controller.GoalController;
import com.water.learnchild.controller.ScreenTimeController;
import com.water.learnchild.model.Goal;
import com.water.learnchild.model.ScreenTime;
import com.water.learnchild.utils.LoadingHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;

public class ChildrenGoals extends AppCompatActivity {

    RecyclerView goalsList;
    ArrayList<Goal> goals = new ArrayList<>();
    LoadingHelper loadingHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_goals);

        goalsList = findViewById(R.id.goals_list);
        goalsList.setLayoutManager(new LinearLayoutManager(this));
        goalsList.setItemAnimator(new DefaultItemAnimator());
        loadingHelper = new LoadingHelper(this);

        loadingHelper.showLoading("Loading "+SharedData.selectedChild.getName()+" Goals");
        new GoalController()
                .getChildGoals(SharedData.selectedChild, new GoalCallBack() {
            @Override
            public void onSuccess(ArrayList<Goal> data) {
                loadingHelper.dismissLoading();
                goals = data;
                goalsList.setAdapter(new GoalsAdapter());
            }

            @Override
            public void onFail(String msg) {

            }
        });

        (findViewById(R.id.add))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAddGoalDialog();
                    }
                });

    }

    private void showAddGoalDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this)
                .inflate(R.layout.add_goal,null);
        builder.setView(view);

        TextInputEditText goal = view.findViewById(R.id.et_goal);
        Button save = view.findViewById(R.id.save);
        Button cancel = view.findViewById(R.id.cancel);

        AlertDialog dialog = builder.create();
        dialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goal.getText() == null){
                    goal.setError("Required");
                    return;
                }else if(goal.getText().toString().isEmpty()){
                    goal.setError("Required");
                    return;
                }

                Goal goal1 = new Goal("",goal.getText().toString(),0,SharedData.selectedChild);
                new GoalController().Save(goal1, new GoalCallBack() {
                    @Override
                    public void onSuccess(ArrayList<Goal> data) {
                        Toast.makeText(ChildrenGoals.this, "Goal Added Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ChildrenGoals.this)
                    .inflate(R.layout.goal_row,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Goal goal = goals.get(position);
            holder.name.setText(goal.getTitle());
            holder.state.setChecked(goal.getState() != 0);

            holder.state.setEnabled(goal.getState() == 0);

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new LoadingHelper(ChildrenGoals.this)
                            .showDialog("Delete", "Are you sure?", "Delete", "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new GoalController().delete(goal, new GoalCallBack() {
                                        @Override
                                        public void onSuccess(ArrayList<Goal> data) {
                                            Toast.makeText(ChildrenGoals.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
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

            holder.state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        goal.setState(1);
                        new GoalController().Save(goal, new GoalCallBack() {
                            @Override
                            public void onSuccess(ArrayList<Goal> data) {

                            }

                            @Override
                            public void onFail(String msg) {

                            }
                        });
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return goals.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            CheckBox state;
            TextView name;
            ImageButton delete;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                state = itemView.findViewById(R.id.state);
                name = itemView.findViewById(R.id.name);
                delete = itemView.findViewById(R.id.btnReject);
            }
        }
    }
}