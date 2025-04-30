package com.water.learnchild.activities.child;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.water.learnchild.R;
import com.water.learnchild.utils.Config;
import com.water.learnchild.utils.LetterHelper;
import com.water.learnchild.utils.LoadingHelper;
import com.water.learnchild.utils.SharedData;

import java.util.ArrayList;
import java.util.Locale;

public class LearnEnglishActivity extends AppCompatActivity
        implements android.view.View.OnTouchListener, TextToSpeech.OnInitListener {

    ImageView imageView;
    Button next,previous,play;
    ArrayList<LetterHelper> letters;
    int index = 0;

    Canvas canvas;
    Paint paint;
    Matrix matrix;
    float downx = 0;
    float downy = 0;
    float upx = 0;
    float upy = 0;

    private Bitmap DrawBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint DrawBitmapPaint;
    RelativeLayout Rl;
    CustomView View;
    Paint mPaint;

    private String baseSpeakURL = "https://ttsmp3.com/created_mp3/";

    private MediaPlayer mediaPlayer;

    Button speak;
    TextToSpeech t1;
    String tts = "";
    LoadingHelper loadingHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_english);
        Rl = findViewById(R.id.rel);

//        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
//        if(SharedData.learnLetters){
//            toolbar.setTitle("Learn English Letters");
//        }else{
//            toolbar.setTitle("Learn English Numbers");
//        }

        loadingHelper = new LoadingHelper(this);



        imageView = findViewById(R.id.image);


        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        speak = findViewById(R.id.speak);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                try {
                    Config.addChildLog("Play Word "+letters.get(index).getCorrectPronounce()+" Correctly","");
                    playAudio(letters.get(index).getUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LearnEnglishActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(t1.isSpeaking()){
                    t1.stop();
                }
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL
                        ,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent,11);

//                if(arabicLetters.isChecked()){
//                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL
//                            ,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ar-JO");
//                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"tk.oryx.voice");
//                    startActivityForResult(intent,11);
//                }else if(englishLetters.isChecked()){
//                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL
//                            ,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                    startActivityForResult(intent,11);
//                }

            }
        });

        letters = new ArrayList<>();
        if(SharedData.learnLetters)
            fillEnglishLetters();
        else
            fillEnglishNumbers();

        index = 0;
        setData();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index < (letters.size()-1))
                    index++;
                setData();
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index > 0)
                    index--;
                setData();
            }
        });


    }

    private void playAudio(String url) throws Exception
    {
        loadingHelper.showLoading("Loading...");
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        tts = url;
        t1 = new TextToSpeech(this,this);
        tts = url;

    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onPause() {
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
    }

    private void setData(){
        imageView.setImageResource(letters.get(index).getSource());
        if(index == 0){
            previous.setVisibility(View.GONE);
        }else{
            previous.setVisibility(View.VISIBLE);
        }

        if(index == letters.size() -1){
            next.setVisibility(View.GONE);
        }else{
            next.setVisibility(View.VISIBLE);
        }

//        if(View != null){
//            Rl.removeView(View);
//        }
//        View = new CustomView(this);

        //Rl.addView(View);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getResources()
                .getColor(android.R.color.holo_green_dark));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(100);



    }


    private void fillEnglishLetters(){
        letters.add(new LetterHelper(R.mipmap.a1,"a ant","ant"));
        letters.add(new LetterHelper(R.mipmap.b1,"b Bear","bear"));
        letters.add(new LetterHelper(R.mipmap.c1,"c Camel","camel"));
        letters.add(new LetterHelper(R.mipmap.d1,"d Doe","doe"));
        letters.add(new LetterHelper(R.mipmap.e1,"e Elephant","elephant"));
        letters.add(new LetterHelper(R.mipmap.f1,"f Fox","fox"));
        letters.add(new LetterHelper(R.mipmap.g1,"g Giraffe","giraffe"));
        letters.add(new LetterHelper(R.mipmap.h1,"h Hog","hog"));
        letters.add(new LetterHelper(R.mipmap.i1,"i iguana","iguana"));
        letters.add(new LetterHelper(R.mipmap.j1,"j Jellyfish","jellyfish"));
        letters.add(new LetterHelper(R.mipmap.k1,"k Koala","koala"));
        letters.add(new LetterHelper(R.mipmap.l1,"l Lion","lion"));
        letters.add(new LetterHelper(R.mipmap.m1,"m Mouse","mouse"));
        letters.add(new LetterHelper(R.mipmap.n1,"n narwhal","narwhal"));
        letters.add(new LetterHelper(R.mipmap.o1,"o otter","otter"));
        letters.add(new LetterHelper(R.mipmap.p1,"p Panda","panda"));
        letters.add(new LetterHelper(R.mipmap.q1,"q Quail","quail"));
        letters.add(new LetterHelper(R.mipmap.r1,"r rhinocerous","rhinocerous"));
        letters.add(new LetterHelper(R.mipmap.s1,"s Sheep","sheep"));
        letters.add(new LetterHelper(R.mipmap.t1,"t Tiger","tiger"));
        letters.add(new LetterHelper(R.mipmap.u1,"u Uakai monkey","uakai monkey"));
        letters.add(new LetterHelper(R.mipmap.v1,"v Volcano rabbit","volcano rabbit"));
        letters.add(new LetterHelper(R.mipmap.w1,"w Whale","whale"));
        letters.add(new LetterHelper(R.mipmap.x1,"x x-ray tetra fish","xray tetra fish"));
        letters.add(new LetterHelper(R.mipmap.y1,"y yellow bellied weasal","yellow bellied weasal"));
        letters.add(new LetterHelper(R.mipmap.z1,"z Zebra","zebra"));
    }

    private void fillEnglishNumbers(){
        letters.add(new LetterHelper(R.mipmap.one,"One","one"));
        letters.add(new LetterHelper(R.mipmap.two,"Two","two"));
        letters.add(new LetterHelper(R.mipmap.three,"Three","three"));
        letters.add(new LetterHelper(R.mipmap.four,"Four","four"));
        letters.add(new LetterHelper(R.mipmap.five,"Five","five"));
        letters.add(new LetterHelper(R.mipmap.six,"Six","six"));
        letters.add(new LetterHelper(R.mipmap.seven,"Seven","seven"));
        letters.add(new LetterHelper(R.mipmap.eight,"Eight","eight"));
        letters.add(new LetterHelper(R.mipmap.nine,"Nine","nine"));
        letters.add(new LetterHelper(R.mipmap.ten,"Ten","ten"));
    }





    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downx = event.getX();
                downy = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                upx = event.getX();
                upy = event.getY();
                canvas.drawCircle(downx, downy, 20, paint);
                imageView.invalidate();
                downx = upx;
                downy = upy;
                break;
            case MotionEvent.ACTION_UP:
                upx = event.getX() ;
                upy = event.getY() ;
                canvas.drawCircle(downx, downy, 20, paint);
                imageView.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onInit(int status) {
        loadingHelper.dismissLoading();
        if(status == TextToSpeech.SUCCESS){
            int result = t1.setLanguage(Locale.US);

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
            }else{
                t1.speak(tts,TextToSpeech.QUEUE_FLUSH,null,"");
            }

        }else{
            Toast.makeText(this, "Init Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public class CustomView extends View {

        @SuppressWarnings("deprecation")
        public CustomView(Context c) {

            super(c);
            Display Disp = getWindowManager().getDefaultDisplay();
            DrawBitmap = Bitmap.createBitmap(Disp.getWidth(), Disp.getHeight(),
                    Bitmap.Config.ARGB_4444);

            mCanvas = new Canvas(DrawBitmap);

            mPath = new Path();
            DrawBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            setDrawingCacheEnabled(true);
            canvas.drawBitmap(DrawBitmap, 0, 0, DrawBitmapPaint);
            canvas.drawPath(mPath, mPaint);
            canvas.drawRect(mY, 0, mY, 0, DrawBitmapPaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);

            mCanvas.drawPath(mPath, mPaint);

            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            float[] eventXY = new float[] {eventX, eventY};

            Matrix invertMatrix = new Matrix();
            ((ImageView)imageView).getImageMatrix().invert(invertMatrix);

            invertMatrix.mapPoints(eventXY);
            int x1 = Integer.valueOf((int)eventXY[0]);
            int y1 = Integer.valueOf((int)eventXY[1]);

            Drawable imgDrawable = ((ImageView)imageView).getDrawable();
            Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();

            //Limit x, y range within bitmap
            if(x1 < 0){
                x1 = 0;
            }else if(x1 > bitmap.getWidth()-1){
                x1 = bitmap.getWidth()-1;
            }

            if(y1 < 0){
                y1 = 0;
            }else if(y1 > bitmap.getHeight()-1){
                y1 = bitmap.getHeight()-1;
            }

            int touchedRGB = bitmap.getPixel(x1, y1);

            //   Toast.makeText(StudentLettersLearningActivity.this, "touched color: " + "#" + Integer.toHexString(touchedRGB), Toast.LENGTH_SHORT).show();


            if (!Integer.toHexString(touchedRGB).equals("ffffffff")){
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(LearnEnglishActivity.this, "Please,Draw in white area", Toast.LENGTH_SHORT).show();
                setData();
                return true;
            }

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 11 && resultCode == RESULT_OK){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(result.get(0).contains(letters.get(index).getCorrectPronounce())){
                Config.addChildLog("Speak Word "+letters.get(index).getCorrectPronounce()+" Correctly","");
                Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
//                try {
//                    playAudio("31b99ffa0d41e1ebbc4854d74e7bf52d.mp3");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }else{
                Config.addChildLog("Speak Word "+letters.get(index).getCorrectPronounce()+" Wrong","");
                Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
//                try {
//                    playAudio("83f8ca27e0f8363939778cab01be576b.mp3");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}