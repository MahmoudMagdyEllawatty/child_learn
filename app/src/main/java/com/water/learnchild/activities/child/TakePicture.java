package com.water.learnchild.activities.child;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabelerOptionsBase;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.water.learnchild.R;
import com.water.learnchild.utils.Config;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

public class TakePicture extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private ImageView imageView;
    private TextView resultText,message;
    private Bitmap selectedImage;

    String selectedColor = "";
    String[] colors = new String[]{"Red","Black","Blue","Green","Yellow","White"};
    Integer[] colorValues = new Integer[]{Color.RED,Color.BLACK,Color.BLUE,Color.GREEN,Color.YELLOW,Color.WHITE};
    TextToSpeech t1;
    Random random;
    int randomNumber;
    int selectedColorValue = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        imageView = findViewById(R.id.imageView);
        resultText = findViewById(R.id.resultText);
        message = findViewById(R.id.message);
        Button captureButton = findViewById(R.id.captureButton);
        Button galleryButton = findViewById(R.id.galleryButton);
        Button checkButton = findViewById(R.id.checkButton);

        captureButton.setOnClickListener(v -> openCamera());
        galleryButton.setOnClickListener(v -> openGallery());
        t1 = new TextToSpeech(this,this);

        random = new Random();
        randomNumber = random.nextInt(6);
        selectedColor = colors[randomNumber];
        selectedColorValue = colorValues[randomNumber];

        message.setText("Please,Take Picture for something "+selectedColor);


        (findViewById(R.id.descibeButton))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImage != null) {
                    InputImage image = InputImage.fromBitmap(selectedImage, 0);

                    ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

                    labeler.process(image)
                            .addOnSuccessListener(labels -> {
                                StringBuilder result = new StringBuilder();
                                StringBuilder spokenText = new StringBuilder();
                                for (ImageLabel label : labels) {
                                    if(label.getConfidence() > 0.6) {
                                        result.append(label.getText()).append(")\n");

                                        spokenText.append("it may be ").append(label.getText()).append(" percentage").append("\n");
                                    }
                                }

                                // Show result
                                resultText.setText(result.toString());

                                t1.speak(spokenText,TextToSpeech.QUEUE_FLUSH,null,"");
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(TakePicture.this, "Failed to label image", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(TakePicture.this, "Please select an image first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkButton.setOnClickListener(v -> {
            if (selectedImage != null) {
                boolean isColorDetected = containsColor(selectedImage, selectedColorValue);
                if (isColorDetected) {
                    Config.addChildLog("Select Picture for color: " + selectedColor +" Correctly","");
                    resultText.setText(selectedColor + " object detected! ✅");

                    random = new Random();
                    randomNumber = random.nextInt(6);
                    selectedColor = colors[randomNumber];

                    message.setText("Please,Take Picture for something "+selectedColor);
                    t1.speak("Please,Take Picture for something "+selectedColor,TextToSpeech.QUEUE_FLUSH,null,"");

                } else {
                    Config.addChildLog("Select Picture for color: " + selectedColor +" Wrong","");
                    resultText.setText("No " + selectedColor + " object detected. ❌");
                }
            } else {
                Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show();
            }
        });

        requestPermissions();

    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
                selectedImage = photo;
            } else if (requestCode == GALLERY_REQUEST) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imageView.setImageBitmap(bitmap);
                    selectedImage = bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private boolean containsColor(Bitmap bitmap, int targetColor) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        for (int x = 0; x < width; x += 10) {
            for (int y = 0; y < height; y += 10) {
                int pixel = bitmap.getPixel(x, y);
                if (isSimilarColor(pixel, targetColor)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSimilarColor(int color1, int color2) {
        int r1 = Color.red(color1);
        int g1 = Color.green(color1);
        int b1 = Color.blue(color1);

        int r2 = Color.red(color2);
        int g2 = Color.green(color2);
        int b2 = Color.blue(color2);

        int diff = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
        return diff < 100; // هامش تقارب اللون
    }
    private boolean matchesColor(int color1, String targetColor,int tolerance) {
        int red1 = Color.red(color1);
        int green1 = Color.green(color1);
        int blue1 = Color.blue(color1);

        int color2 = 0;
        switch (targetColor.toLowerCase()) {
            case "red":
                color2= Color.RED;
            case "black":
                color2 = Color.BLACK;
            case "blue":
                color2 = Color.BLUE;
            case "green":
                color2 = Color.GREEN;
            case "yellow":
                color2 = Color.YELLOW;
            case "white":
                color2 = Color.WHITE;
            default:
                color2 = Color.RED;
        }
        int red2 = Color.red(color2);
        int green2 = Color.green(color2);
        int blue2 = Color.blue(color2);

        return Math.abs(red1 - red2) <= tolerance &&
                Math.abs(green1 - green2) <= tolerance &&
                Math.abs(blue1 - blue2) <= tolerance;

    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int result = t1.setLanguage(Locale.US);

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
            }else{
                t1.speak("Please,Take Picture for something "+selectedColor,TextToSpeech.QUEUE_FLUSH,null,"");
            }

        }else{
            Toast.makeText(this, "Init Failed", Toast.LENGTH_SHORT).show();
        }
    }
}