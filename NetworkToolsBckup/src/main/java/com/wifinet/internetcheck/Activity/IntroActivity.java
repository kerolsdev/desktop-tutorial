package com.wifinet.internetcheck.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wifinet.internetcheck.Model.ModelAdapterOnBoarding;
import com.wifinet.internetcheck.Adapter.OnBoardingAdapter;
import com.wifinet.internetcheck.R;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        // Toolbar non_show
       // Objects.requireNonNull(getSupportActionBar()).hide();
        //Splash Screen view
        // --

        // OnBoarding Screen Elements view
        // --
        View view_onBoarding_screen = (View) findViewById(R.id.onboarding_screen_layout);
        ViewPager2 viewPager2 = (ViewPager2)findViewById(R.id.viewPager2_onBoarding);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.TabLayout);
        MaterialButton materialButton = (MaterialButton)findViewById(R.id.start_onboarding);
        materialButton.setOnClickListener(this);
        //

        //set value viewpager2 and connect tabLayout with viewpager
        ArrayList<ModelAdapterOnBoarding> arrayList = new ArrayList<>();
        arrayList.add(new ModelAdapterOnBoarding(getString(R.string.check_wifi_connect),R.drawable.ic_family));
        arrayList.add(new ModelAdapterOnBoarding(getString(R.string.check_test_speed),R.drawable.ic_speed_test));
        arrayList.add(new ModelAdapterOnBoarding(getString(R.string.info_wifi_anathilys),R.drawable.ic_wifi_));

        OnBoardingAdapter monBoardingAdapter = new OnBoardingAdapter(arrayList);
        viewPager2.setAdapter(monBoardingAdapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {



            }
        });
        tabLayoutMediator.attach();

        // if OnBoarding is save
         if(restorePrefData()){
              Intent intent = new Intent(this,MainActivity.class);
              startActivity(intent);
              finish();
       /*else if the user open app first */
  }else {

         }
    }
    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;



    }
    // for save view
    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        // start to MainActivity
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        savePrefsData();
        finish();



    }
}