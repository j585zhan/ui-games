package com.example.jim.simon;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import java.util.Observable;
import java.util.Observer;

public class GameActivity extends AppCompatActivity implements Observer {

    int FPS = 60;
    Model model;
    Thread t;

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

        hide(); // hide menu

        // init model
        model = Model.getInstance();
        model.init(screenWidth, screenHeight);
        model.addGameActivity(this);

        // setup views
        final PlayView playView = new PlayView(this, model);

        model.initObservers();

        setContentView(playView);

        /* thread for eventloop */
        final Handler handler=new Handler();
        t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    updateUI();
                    try {
                        sleep(1000 / FPS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            private void updateUI() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        playView.invalidate();
                    }
                });
            }
        };
        t.start();
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void finish() {
        super.finish();
        /* stop the running thread */
        t.interrupt();
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
