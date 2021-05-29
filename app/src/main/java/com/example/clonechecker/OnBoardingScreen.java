package com.example.clonechecker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class OnBoardingScreen extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);

        fragmentManager = getSupportFragmentManager();
        btnGetStarted = findViewById(R.id.btn_get_started);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoardingScreen.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        final PaperOnboardingFragment paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataForOnBoardingScreen());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, paperOnboardingFragment);
        fragmentTransaction.commit();
    }

    private ArrayList<PaperOnboardingPage> getDataForOnBoardingScreen() {
        PaperOnboardingPage scr1 = new  PaperOnboardingPage("Information", "Get Information About your Device",
                Color.parseColor("#11AFF6"), R.drawable.ic_baseline_info_100, R.drawable.ic_info_24);
        PaperOnboardingPage scr2 = new  PaperOnboardingPage("Verify", "Verify that your device is original",
                Color.parseColor("#11AFF6"), R.drawable.ic_baseline_verified_48, R.drawable.ic_verified);
        PaperOnboardingPage scr4 = new  PaperOnboardingPage("More", "And More!",
                Color.parseColor("#11AFF6"), R.drawable.ic_baseline_more_horiz_100, R.drawable.ic_more);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr4);
        return  elements;

    }
}