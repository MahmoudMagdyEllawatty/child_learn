package com.water.learnchild.controller;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.water.learnchild.callback.ChildCallBack;
import com.water.learnchild.callback.GoalCallBack;
import com.water.learnchild.callback.ImageCallback;
import com.water.learnchild.model.Child;
import com.water.learnchild.model.Goal;
import com.water.learnchild.model.Parent;
import com.water.learnchild.utils.Config;
import com.water.learnchild.utils.FirebaseHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class GoalController {

    private final String node = Config.GOALS_NODE;
    ArrayList<Goal> complaints = new ArrayList<>();
    FirebaseHelper<Goal> helper = new FirebaseHelper<Goal>();

    public void Save(final Goal customer, final GoalCallBack callback){
        if(customer.getKey().equals("")){
            customer.setKey(helper.getKey(node));
        }

        helper.save(node,customer.getKey(),customer)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            complaints.add(customer);
                            callback.onSuccess(complaints);
                        }else{
                            callback.onFail(task.getException().toString());
                        }
                    }
                });
    }


    public void delete(Goal customer, GoalCallBack callback){
        helper.delete(node, customer.getKey())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            complaints = new ArrayList<>();
                            callback.onSuccess(complaints);
                        }else{
                            callback.onFail(task.getException().toString());
                        }
                    }
                });
    }


    public void getChildren( final GoalCallBack callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    ArrayList<Goal> complaints1 = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        Goal customer = snap.toObject(Goal.class);

                        complaints1.add(customer);
                    }
                    callback.onSuccess(complaints1);
                }
            }
        });
    }


    public void getChildGoals(Child child, final GoalCallBack callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    ArrayList<Goal> complaints1 = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        Goal customer = snap.toObject(Goal.class);
                        if(customer.getChild().getKey().equals(child.getKey()))
                            complaints1.add(customer);
                    }
                    callback.onSuccess(complaints1);
                }
            }
        });
    }

}
