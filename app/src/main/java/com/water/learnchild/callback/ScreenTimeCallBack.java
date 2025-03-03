package com.water.learnchild.callback;

import com.water.learnchild.model.Child;
import com.water.learnchild.model.ScreenTime;

import java.util.ArrayList;

public interface ScreenTimeCallBack {
    void onSuccess(ArrayList<ScreenTime> data);
    void onFail(String msg);
}
