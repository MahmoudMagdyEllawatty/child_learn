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
import com.water.learnchild.callback.ImageCallback;
import com.water.learnchild.model.Child;
import com.water.learnchild.model.Parent;
import com.water.learnchild.utils.Config;
import com.water.learnchild.utils.FirebaseHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ChildrenController {

    private final String node = Config.CHILDREN_NODE;
    ArrayList<Child> complaints = new ArrayList<>();
    FirebaseHelper<Child> helper = new FirebaseHelper<Child>();

    public void Save(final Child customer, final ChildCallBack callback){
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


    public void delete(Child customer, ChildCallBack callback){
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


    public void getChildren( final ChildCallBack callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    ArrayList<Child> complaints1 = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        Child customer = snap.toObject(Child.class);

                        complaints1.add(customer);
                    }
                    callback.onSuccess(complaints1);
                }
            }
        });
    }


    public void getParentChildren(Parent parent, final ChildCallBack callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    ArrayList<Child> complaints1 = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        Child customer = snap.toObject(Child.class);
                        if(customer.getParent().getKey().equals(parent.getKey()))
                            complaints1.add(customer);
                    }
                    callback.onSuccess(complaints1);
                }
            }
        });
    }


    public void checkLogin(String email,String password, final ChildCallBack callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    if(SharedData.currentChild != null)
                        return;
                    ArrayList<Child> complaints1 = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        Child customer = snap.toObject(Child.class);
                        if(customer.getName().equals(email) && customer.getPassword().equals(password))
                            complaints1.add(customer);
                    }
                    callback.onSuccess(complaints1);
                }
            }
        });
    }


    public void uploadImage(Uri uri, final ImageCallback callback){
        helper.uploadDoc(uri.toString(),uri)
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            callback.onSuccess(task.getResult().toString());
                        }else{
                            callback.onFail(task.getException().toString());
                        }
                    }
                });
    }
    
}
