package com.water.learnchild.callback;

import com.water.learnchild.model.Child;
import com.water.learnchild.model.ChildLog;

import java.util.ArrayList;

public interface ChildLogCallBack {
    void onSuccess(ArrayList<ChildLog> children);
    void onFail(String msg);
}
