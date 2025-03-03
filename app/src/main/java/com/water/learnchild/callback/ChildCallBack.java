package com.water.learnchild.callback;

import com.water.learnchild.model.Child;
import com.water.learnchild.model.Parent;

import java.util.ArrayList;

public interface ChildCallBack {
    void onSuccess(ArrayList<Child> children);
    void onFail(String msg);
}
