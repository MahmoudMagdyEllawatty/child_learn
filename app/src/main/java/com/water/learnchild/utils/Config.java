package com.water.learnchild.utils;

import com.water.learnchild.callback.ChildLogCallBack;
import com.water.learnchild.controller.ChildLogController;
import com.water.learnchild.model.ChildLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Config {

    public static final String PARENTS_NODE = "parents";
    public static final String CHILDREN_NODE = "children";
    public static final String GOALS_NODE = "goals";

    public static final String SCREEN_TIME_NODE = "screen_times";
    public static final String CHILD_LOG_NODE = "logs";

    public static void addChildLog(String details,String imageURL){
        ChildLog childLog = new ChildLog(
                "",
                new SimpleDateFormat("dd/MM/yyyy H:m:s", Locale.ENGLISH).format(Calendar.getInstance().getTime()),
                details,
                SharedData.currentChild,
                imageURL
        );

        new ChildLogController().Save(childLog, new ChildLogCallBack() {
            @Override
            public void onSuccess(ArrayList<ChildLog> children) {

            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
}
