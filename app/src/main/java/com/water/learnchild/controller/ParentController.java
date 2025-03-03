package com.water.learnchild.controller;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.water.learnchild.callback.ChildCallBack;
import com.water.learnchild.callback.ParentCallBack;
import com.water.learnchild.model.Child;
import com.water.learnchild.model.Parent;
import com.water.learnchild.utils.Config;
import com.water.learnchild.utils.FirebaseHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ParentController {

    private final String node = Config.PARENTS_NODE;
    ArrayList<Parent> complaints = new ArrayList<>();
    FirebaseHelper<Parent> helper = new FirebaseHelper<Parent>();

    public void Save(final Parent customer, final ParentCallBack callback){
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


    public void delete(Parent customer, ParentCallBack callback){
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


    public void getParents( final ParentCallBack callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    ArrayList<Parent> complaints1 = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        Parent customer = snap.toObject(Parent.class);

                        complaints1.add(customer);
                    }
                    callback.onSuccess(complaints1);
                }
            }
        });
    }


    public void checkLogin(String email,String password, final ParentCallBack callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    if(SharedData.currentParent != null)
                        return;
                    ArrayList<Parent> complaints1 = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        Parent customer = snap.toObject(Parent.class);
                        if(customer.getEmail().equals(email) && customer.getPassword().equals(password))
                            complaints1.add(customer);
                    }
                    callback.onSuccess(complaints1);
                }
            }
        });
    }

    
}
