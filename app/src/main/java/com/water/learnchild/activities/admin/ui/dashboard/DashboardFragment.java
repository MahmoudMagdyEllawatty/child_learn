package com.water.learnchild.activities.admin.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.water.learnchild.R;
import com.water.learnchild.activities.parent.ChildrenGoals;
import com.water.learnchild.activities.parent.ScreenTimesActivity;
import com.water.learnchild.activities.parent.SelectChildActivity;
import com.water.learnchild.callback.ChildCallBack;
import com.water.learnchild.controller.ChildrenController;
import com.water.learnchild.model.Child;
import com.water.learnchild.utils.LoadingHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    LoadingHelper loadingHelper;
    RecyclerView childrenList;
    ArrayList<Child> children;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        loadingHelper = new LoadingHelper(getActivity());
        childrenList = root.findViewById(R.id.children_list);
        childrenList.setLayoutManager(new LinearLayoutManager(getActivity()));
        childrenList.setItemAnimator(new DefaultItemAnimator());


        loadingHelper.showLoading("Loading Children");
        loadChildren();

        return root;
    }
    
    private void loadChildren(){
        new ChildrenController().getChildren(new ChildCallBack() {
            @Override
            public void onSuccess(ArrayList<Child> children1) {
                loadingHelper.dismissLoading();
                children = children1;
                childrenList.setAdapter(new ChildrenAdapter());
            }

            @Override
            public void onFail(String msg) {
                loadingHelper.dismissLoading();
            }
        });
    }


    class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ViewHolder> {

        @NonNull
        @Override
        public ChildrenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.child_admin_item,parent,false);
            return new ChildrenAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChildrenAdapter.ViewHolder holder, int position) {
            Child child = children.get(position);
            Picasso.get()
                    .load(child.getImage())
                    .into(holder.image);

            holder.name.setText(child.getName());
            holder.parent_name.setText(child.getParent().getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedData.selectedChild = child;

                    Intent intent = new Intent(getActivity(),ReportActivity.class);
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
            TextView name,parent_name;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                name = itemView.findViewById(R.id.name);
                parent_name = itemView.findViewById(R.id.parent_name);
            }
        }
    }
    

}