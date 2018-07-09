package com.example.jim.simon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements Observer{

    // private vars
    Model model;

    // buttons
    public Button startButton;
    public Button settingButton;
    public Button helpButton;

    private void init() {
        /* start button */
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toGamePage = new Intent(FullscreenActivity.this, GameActivity.class);

                startActivity(toGamePage);
            }
        });

        /* setting button */
        settingButton = (Button)findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSettingPage = new Intent(FullscreenActivity.this, SettingActivity.class);

                startActivity(toSettingPage);
            }
        });

        /* help button */
        helpButton = (Button)findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHelpPage = new Intent(FullscreenActivity.this, HelpActivity.class);

                startActivity(toHelpPage);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get screen size
        WindowManager vm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = vm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        setContentView(R.layout.activity_fullscreen);

        hide(); // hide menu

        // init model
        model = Model.getInstance();
        model.init(screenWidth, screenHeight);
        model.addObserver(this);
        model.initObservers();

        /* button controller setting */
        init();
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
