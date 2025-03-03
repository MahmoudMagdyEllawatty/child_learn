package com.water.learnchild.controller;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.water.learnchild.callback.ChildLogCallBack;
import com.water.learnchild.callback.GoalCallBack;
import com.water.learnchild.model.Child;
import com.water.learnchild.model.ChildLog;
import com.water.learnchild.model.Goal;
import com.water.learnchild.utils.Config;
import com.water.learnchild.utils.FirebaseHelper;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ChildLogController {

    private final String node = Config.CHILD_LOG_NODE;
    ArrayList<ChildLog> complaints = new ArrayList<>();
    FirebaseHelper<ChildLog> helper = new FirebaseHelper<ChildLog>();

    public void Save(final ChildLog customer, final ChildLogCallBack callback){
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



    public void getChildLog(Child child, final ChildLogCallBack callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    ArrayList<ChildLog> complaints1 = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        ChildLog customer = snap.toObject(ChildLog.class);
                        if(customer.getChild() == null)
                            continue;
                        if(customer.getChild().getKey().equals(child.getKey()))
                            complaints1.add(customer);
                    }
                    callback.onSuccess(complaints1);
                }
            }
        });
    }

}
