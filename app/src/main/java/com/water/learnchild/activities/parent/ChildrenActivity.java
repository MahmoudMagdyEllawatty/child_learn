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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.water.learnchild.R;
import com.water.learnchild.callback.ChildCallBack;
import com.water.learnchild.controller.ChildrenController;
import com.water.learnchild.model.Child;
import com.water.learnchild.utils.LoadingHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;

public class ChildrenActivity extends AppCompatActivity {

    RecyclerView childrenList;
    ArrayList<Child> children = new ArrayList<>();
    LoadingHelper loadingHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        loadingHelper = new LoadingHelper(this);
        childrenList = findViewById(R.id.children_list);
        childrenList.setItemAnimator(new DefaultItemAnimator());
        childrenList.setLayoutManager(new LinearLayoutManager(this));

        (findViewById(R.id.add))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedData.currentChild  = null;
                        Intent intent = new Intent(ChildrenActivity.this,ChildDataActivity.class);
                        startActivity(intent);
                    }
                });

        loadingHelper.showLoading("Loading Data");
        new ChildrenController().getParentChildren(SharedData.currentParent, new ChildCallBack() {
            @Override
            public void onSuccess(ArrayList<Child> children1) {
                loadingHelper.dismissLoading();
                children= children1;
                childrenList.setAdapter(new ChildrenAdapter());
            }

            @Override
            public void onFail(String msg) {

            }
        });

    }


    private class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ChildrenActivity.this)
                    .inflate(R.layout.child_row,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Child child = children.get(position);
            Picasso.get()
                    .load(child.getImage())
                    .into(holder.image);

            holder.name.setText(child.getName());
            holder.age.setText(String.format("%s years", child.getAge()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedData.currentChild = child;
                    Intent intent = new Intent(ChildrenActivity.this,ChildDataActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return children.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            ImageView image;
            TextView name,age;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                name = itemView.findViewById(R.id.name);
                age = itemView.findViewById(R.id.state);
            }
        }
    }
}