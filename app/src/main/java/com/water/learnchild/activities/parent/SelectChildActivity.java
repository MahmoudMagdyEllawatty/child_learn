package com.water.learnchild.activities.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.water.learnchild.R;
import com.water.learnchild.activities.admin.ui.dashboard.ReportActivity;
import com.water.learnchild.callback.ChildCallBack;
import com.water.learnchild.controller.ChildrenController;
import com.water.learnchild.model.Child;
import com.water.learnchild.utils.LoadingHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;

public class SelectChildActivity extends AppCompatActivity {

    RecyclerView childrenList;
    ArrayList<Child> children = new ArrayList<>();
    LoadingHelper loadingHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_child);

        loadingHelper = new LoadingHelper(this);
        childrenList = findViewById(R.id.children_list);
        childrenList.setItemAnimator(new DefaultItemAnimator());
        childrenList.setLayoutManager(new GridLayoutManager(this,2));

        loadingHelper.showLoading("Loading Children");
        new ChildrenController().getParentChildren(SharedData.currentParent, new ChildCallBack() {
            @Override
            public void onSuccess(ArrayList<Child> children1) {
                loadingHelper.dismissLoading();
                children = children1;
                childrenList.setAdapter(new ChildrenAdapter());
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }


    class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SelectChildActivity.this)
                    .inflate(R.layout.child_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Child child = children.get(position);
            Picasso.get()
                    .load(child.getImage())
                    .into(holder.image);

            holder.name.setText(child.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedData.selectedChild = child;
                    if(SharedData.which == 1){
                        Intent intent = new Intent(SelectChildActivity.this,ChildrenGoals.class);
                        startActivity(intent);
                    }else if(SharedData.which == 2){
                        Intent intent = new Intent(SelectChildActivity.this,ScreenTimesActivity.class);
                        startActivity(intent);
                    }else if(SharedData.which == 3){
                        Intent intent = new Intent(SelectChildActivity.this, ReportActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return children.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            ImageView image;
            TextView name;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                name = itemView.findViewById(R.id.name);
            }
        }
    }
}