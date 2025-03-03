package com.water.learnchild.callback;

import com.water.learnchild.model.Parent;

import java.util.ArrayList;

public interface ParentCallBack {
    void onSuccess(ArrayList<Parent> parents);
    void onFail(String msg);
}
