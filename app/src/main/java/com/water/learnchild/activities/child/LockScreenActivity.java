package com.water.learnchild.activities.child;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.water.learnchild.R;
import com.water.learnchild.model.ScreenTime;
import com.water.learnchild.utils.SharedData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LockScreenActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    private static final long TIMER_INTERVAL = 30000L;
    private Handler mHandler;
    private Runnable mRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        mediaPlayer = MediaPlayer.create(this, R.raw.time_up_sound);
        mediaPlayer.start();

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {

                // do your periodic task
                try {
                    checkIfNowNotAvailable();
                } catch (ParseException e) {

                }
                mHandler.postDelayed(mRunnable, TIMER_INTERVAL);
            }
        };

    }
    @Override
    public void onResume() {
        super.onResume();
        resumeHandler();
    }

    @Override
    public void onPause() {
        pauseHandler();
        super.onPause();
    }

    private void resumeHandler() {
        pauseHandler(); // To avoid multiple occurrences,
        // at first cancel existing task if any

        mHandler.post(mRunnable);
    }

    private void pauseHandler() {
        mHandler.removeCallbacks(mRunnable);
    }
    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    private void checkIfNowNotAvailable() throws ParseException {
        boolean canUse = false  ;
        if(SharedData.screenTimes == null){
            return;
        }else if(SharedData.screenTimes.isEmpty()){
            return;
        }
        long diffInMilles = 0;
        for (ScreenTime screenTime : SharedData.screenTimes){
            Calendar calendar = Calendar.getInstance();
            String currentDay = String.format(Locale.ENGLISH,"%02d", calendar.get(Calendar.DAY_OF_MONTH));
            String currentMonth = String.format(Locale.ENGLISH,"%02d", calendar.get(Calendar.MONTH)+1);
            String currentYear = String.format(Locale.ENGLISH,"%d", calendar.get(Calendar.YEAR));

            Date startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).parse(currentDay+"/"+currentMonth+"/"+currentYear+" "+ screenTime.getFrom_hour());
            Date endDate = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.ENGLISH).parse(currentDay+"/"+currentMonth+"/"+currentYear+" "+ screenTime.getTo_hour());
            Date now = Calendar.getInstance().getTime();

            if(now.before(endDate) && now.after(startDate)){
                // Child Can Use Application
                canUse = true;
                long diff = Math.abs(now.getTime() - endDate.getTime());
                if(diffInMilles == 0){
                    diffInMilles = diff;
                }else if(diffInMilles > diff){
                    diffInMilles = diff;
                }
            }
        }


        if(!canUse){

        }else{
            //
            Intent intent = new Intent(LockScreenActivity.this,ChildDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}