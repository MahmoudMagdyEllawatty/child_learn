package com.water.learnchild.callback;

import com.water.learnchild.model.Child;
import com.water.learnchild.model.Goal;

import java.util.ArrayList;

public interface GoalCallBack {
    void onSuccess(ArrayList<Goal> data);
    void onFail(String msg);
}
