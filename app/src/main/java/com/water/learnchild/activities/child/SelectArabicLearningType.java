package com.water.learnchild.activities.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.water.learnchild.R;
import com.water.learnchild.utils.SharedData;

public class SelectArabicLearningType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_arabic_learning_type);

        Animation buttonClick = AnimationUtils.loadAnimation(this, R.anim.button_click);

        CardView btnEnglish = findViewById(R.id.letters_card);
        btnEnglish.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
            SharedData.learnLetters = true;
            startActivity(new Intent(SelectArabicLearningType.this, LearnArabicActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out); // Apply transition animation
        });
        ImageButton btnEnglish1 = findViewById(R.id.btnEnglish);
        btnEnglish1.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
            SharedData.learnLetters = true;
            startActivity(new Intent(SelectArabicLearningType.this, LearnArabicActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out); // Apply transition animation
        });

        CardView btnEnglishWords = findViewById(R.id.words_card);
        btnEnglishWords.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
            startActivity(new Intent(SelectArabicLearningType.this, LearnArabicWordsActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out); // Apply transition animation
        });
        ImageButton btnEnglishWords1 = findViewById(R.id.btnEnglishWord);
        btnEnglishWords1.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
            startActivity(new Intent(SelectArabicLearningType.this, LearnArabicWordsActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out); // Apply transition animation
        });

        // Arabic Learning Button
        CardView btnArabic = findViewById(R.id.numbers_card);
        btnArabic.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
            SharedData.learnLetters = false;
            startActivity(new Intent(SelectArabicLearningType.this, LearnArabicActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        });
        ImageView btnArabic1 = findViewById(R.id.btnArabic);
        btnArabic1.setOnClickListener(v -> {
            v.startAnimation(buttonClick); // Apply animation
            SharedData.learnLetters = false;
            startActivity(new Intent(SelectArabicLearningType.this, LearnArabicActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        });

        View rootView = findViewById(android.R.id.content);
        rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }
}