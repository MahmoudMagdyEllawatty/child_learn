package com.water.learnchild.activities.child;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.Query;
import com.water.learnchild.R;
import com.water.learnchild.callback.ImageCallback;
import com.water.learnchild.controller.ChildrenController;
import com.water.learnchild.utils.Config;
import com.water.learnchild.utils.SharedData;
import com.water.learnchild.utils.TracingView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class LearnEnglishWordsActivity extends AppCompatActivity {

    private TracingView tracingView;

    private KonfettiView konfettiView;

    private Button clearButton, evaluateButton;
    TextView guideText;
    int index = 0;
    ArrayList<Integer> imagesList = new ArrayList<>();
    ArrayList<String> wordsList = new ArrayList<>();
    ImageView guideImage;
;    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_english_words);

        tracingView = findViewById(R.id.drawingView);
     //   konfettiView = findViewById(R.id.confettiView);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        clearButton = findViewById(R.id.clearButton);
        evaluateButton = findViewById(R.id.evaluateButton);
        guideText = findViewById(R.id.guideText);
        guideImage = findViewById(R.id.guideImage);


        imagesList.add(R.drawable.lion_picture);
        imagesList.add(R.drawable.applr);
        imagesList.add(R.drawable.bee);
        imagesList.add(R.drawable.boy);
        imagesList.add(R.drawable.doctor);
        imagesList.add(R.drawable.elephant);
        imagesList.add(R.drawable.frog);
        imagesList.add(R.drawable.house);
        imagesList.add(R.drawable.monkey);
        imagesList.add(R.drawable.orange);


        wordsList.add("Lion");
        wordsList.add("Apple");
        wordsList.add("Bee");
        wordsList.add("Boy");
        wordsList.add("Doctor");
        wordsList.add("Elephant");
        wordsList.add("Frog");
        wordsList.add("House");
        wordsList.add("Monkey");
        wordsList.add("Orange");

        guideImage.setBackgroundResource(imagesList.get(index));
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracingView.clear();
            }
        });

        evaluateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Optionally capture the drawing view as a bitmap
                Bitmap userBitmap = getBitmapFromView(tracingView);


                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                         ContentValues values3 = new ContentValues();
                        values3.put(MediaStore.Images.Media.DISPLAY_NAME, "user.png");
                        values3.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                        values3.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);  // Saves in "Pictures" directory

                        Uri uri3 = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values3);

                        new ChildrenController().uploadImage(uri3, new ImageCallback() {

                            @Override
                            public void onSuccess(String URL) {
                                Config.addChildLog("Child Tries to write Word "+guideText.getText().toString(),URL);
                            }

                            @Override
                            public void onFail(String msg) {

                            }


                        });
//                        try (OutputStream outputStream = getContentResolver().openOutputStream(uri3)) {
//                            userBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }else {
                        File cachePath = new File(getCacheDir(), "images");
                        cachePath.mkdirs(); // Make sure the directory exists

                        File imageFile = new File(cachePath, "image.png");
                        FileOutputStream out = new FileOutputStream(imageFile);
                        userBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.close();

                        Uri uri3 = FileProvider.getUriForFile(LearnEnglishWordsActivity.this,
                                getPackageName() + ".fileprovider", imageFile);
                        new ChildrenController().uploadImage(uri3, new ImageCallback() {

                            @Override
                            public void onSuccess(String URL) {
                                Config.addChildLog("Child Tries to write Word "+guideText.getText().toString(),URL);
                            }

                            @Override
                            public void onFail(String msg) {

                            }


                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });


        (findViewById(R.id.nextButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                if(index >9){
                    index = 0;
                }
                guideText.setText(wordsList.get(index));
                guideImage.setBackgroundResource(imagesList.get(index));
                tracingView.clear();
            }
        });
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        view.draw(canvas);
        return returnedBitmap;
    }
    private void showConfetti() {
        konfettiView.setVisibility(View.VISIBLE);
        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);

        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(270)
                        .spread(90)
                        .setSpeedBetween(1f, 5f)
                        .timeToLive(2000L)
                        .shapes(Shape.Circle.INSTANCE, Shape.Square.INSTANCE)
                        .position(0.0, 0.0, 1.0, 0.0)
                        .build()

        );

        // Hide confetti after 3 seconds
        new Handler().postDelayed(() -> konfettiView.setVisibility(View.GONE), 3000);
    }
}