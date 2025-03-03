package com.water.learnchild.controller;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.water.learnchild.callback.ScreenTimeCallBack;
import com.water.learnchild.model.Child;
import com.water.learnchild.model.ScreenTime;
import com.water.learnchild.utils.Config;
import com.water.learnchild.utils.FirebaseHelper;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ScreenTimeController {

    private final String node = Config.SCREEN_TIME_NODE;
    ArrayList<ScreenTime> complaints = new ArrayList<>();
    FirebaseHelper<ScreenTime> helper = new FirebaseHelper<ScreenTime>();

    public void Save(final ScreenTime customer, final ScreenTimeCallBack callback){
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


    public void delete(ScreenTime customer, ScreenTimeCallBack callback){
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


    public void getChildren( final ScreenTimeCallBack callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    ArrayList<ScreenTime> complaints1 = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        ScreenTime customer = snap.toObject(ScreenTime.class);

                        complaints1.add(customer);
                    }
                    callback.onSuccess(complaints1);
                }
            }
        });
    }


    public void getChildScreenTimes(Child child, final ScreenTimeCallBack callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    ArrayList<ScreenTime> complaints1 = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        ScreenTime customer = snap.toObject(ScreenTime.class);
                        if(customer.getChild().getKey().equals(child.getKey()))
                            complaints1.add(customer);
                    }
                    callback.onSuccess(complaints1);
                }
            }
        });
    }

}
