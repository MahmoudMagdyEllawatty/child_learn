package com.water.learnchild.activities.child;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import java.util.Random;

public class TakePicture extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private ImageView imageView;
    private TextView resultText,message;
    private Bitmap selectedImage;

    String selectedColor = "";
    String[] colors = new String[]{"Red","Black","Blue","Green","Yellow","White"};


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


        Random random = new Random();
        int randomNumber = random.nextInt(6);
        selectedColor = colors[randomNumber];

        message.setText("Please,Take Picture for something "+selectedColor);


        checkButton.setOnClickListener(v -> {
            if (selectedImage != null) {
                boolean isColorDetected = containsColor(selectedImage, selectedColor);
                if (isColorDetected) {
                    Config.addChildLog("Select Picture for color: " + selectedColor +" Correctly");
                    resultText.setText(selectedColor + " object detected! ✅");
                } else {
                    Config.addChildLog("Select Picture for color: " + selectedColor +" Wrong");
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


    private boolean containsColor(Bitmap bitmap, String color) {
        int colorPixelCount = 0;
        int totalPixels = bitmap.getWidth() * bitmap.getHeight();

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int pixel = bitmap.getPixel(x, y);
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Check color based on the selected option
                if (matchesColor(red, green, blue, color)) {
                    colorPixelCount++;
                }
            }
        }

        float colorPercentage = (colorPixelCount / (float) totalPixels) * 100;
        return colorPercentage > 10; // Returns true if more than 10% of pixels match
    }

    private boolean matchesColor(int red, int green, int blue, String targetColor) {
        switch (targetColor.toLowerCase()) {
            case "red":
                return red > 150 && green < 100 && blue < 100;
            case "black":
                return red < 50 && green < 50 && blue < 50;
            case "blue":
                return blue > 150 && red < 100 && green < 100;
            case "green":
                return green > 150 && red < 100 && blue < 100;
            case "yellow":
                return red > 150 && green > 150 && blue < 100;
            case "white":
                return red > 200 && green > 200 && blue > 200;
            default:
                return false; // No match
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

}