package com.water.learnchild.activities.admin.ui.parents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.water.learnchild.R;
import com.water.learnchild.callback.ParentCallBack;
import com.water.learnchild.controller.ParentController;
import com.water.learnchild.model.Parent;
import com.water.learnchild.utils.LoadingHelper;

import java.util.ArrayList;


public class ParentsFragment extends Fragment {


    LoadingHelper loadingHelper;
    RecyclerView parentsList;
    ArrayList<Parent> parents;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_parents, container, false);

        loadingHelper = new LoadingHelper(getActivity());
        parentsList = root.findViewById(R.id.parents_list);
        parentsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        parentsList.setItemAnimator(new DefaultItemAnimator());

        loadParents();

        return root;
    }

    private void loadParents(){
        loadingHelper.showLoading("Loading Data");
        new ParentController()
                .getParents(new ParentCallBack() {
                    @Override
                    public void onSuccess(ArrayList<Parent> parents1) {
                        loadingHelper.dismissLoading();
                        parents = parents1;
                        parentsList.setAdapter(new ParentsAdapter());
                    }

                    @Override
                    public void onFail(String msg) {
                        loadingHelper.dismissLoading();
                    }
                });
    }

    class ParentsAdapter extends RecyclerView.Adapter<ParentsAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.parent_row,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Parent parent = parents.get(position);

            holder.name.setText(parent.getName());
            if(parent.getState() == 0){
                holder.state.setText("Waiting For Approval");
                holder.actionsArea.setVisibility(View.VISIBLE);
            }else if(parent.getState() == 1){
                holder.state.setText("Account Accepted");
                holder.actionsArea.setVisibility(View.GONE);
            }else if(parent.getState() == -1){
                holder.state.setText("Account Rejected");
                holder.actionsArea.setVisibility(View.GONE);
            }

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parent.setState(1);
                    new ParentController().Save(parent, new ParentCallBack() {
                        @Override
                        public void onSuccess(ArrayList<Parent> parents) {
                            Toast.makeText(getActivity(), "Account Accepted Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });
                }
            });


            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parent.setState(-1);
                    new ParentController().Save(parent, new ParentCallBack() {
                        @Override
                        public void onSuccess(ArrayList<Parent> parents) {
                            Toast.makeText(getActivity(), "Account Rejected Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return parents.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            TextView name,state;
            ImageButton accept,reject;
            RelativeLayout actionsArea;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.name);
                state = itemView.findViewById(R.id.state);

                accept = itemView.findViewById(R.id.btnAccept);
                reject = itemView.findViewById(R.id.btnReject);
                actionsArea = itemView.findViewById(R.id.actions_area);
            }
        }
    }

}