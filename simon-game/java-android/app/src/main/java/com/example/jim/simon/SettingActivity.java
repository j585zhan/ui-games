package com.example.jim.simon;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class SettingActivity extends AppCompatActivity implements Observer {

    public Button doneSettingButton;
    public SeekBar buttonNumberSeekBar;
    public TextView buttonNumberLabel;
    public TextView difficultyLabel;
    public SeekBar difficultySeekBar;

    private Model model;

    private void init() {

        /* done button */
        doneSettingButton = (Button)findViewById(R.id.doneSettingButton);
        doneSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /* hint text for button number */
        buttonNumberLabel = (TextView)findViewById(R.id.buttonNumberLabel);
        buttonNumberLabel.setText("number of button: " + model.getButtonNumber());

        /* seek bar for button number */
        buttonNumberSeekBar = (SeekBar)findViewById(R.id.buttonNumberSeekBar);
        buttonNumberSeekBar.setProgress(model.getButtonNumber()-1);
        buttonNumberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                model.setButtonNumber(buttonNumberSeekBar.getProgress()+1);
                buttonNumberLabel.setText("number of button: " + model.getButtonNumber());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /* hint text for difficulty */
        difficultyLabel = (TextView)findViewById(R.id.difficultyLabel);
        difficultyLabel.setText("difficulty: " + model.getDifficulty().name());

        /* seek bar for difficulty */
        difficultySeekBar = (SeekBar)findViewById(R.id.difficultySeekBar);
        difficultySeekBar.setProgress(model.getDifficultyAsNum());
        difficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (i) {
                    case 0:
                        model.setDifficulty(Difficulty.EASY);
                        break;
                    case 2:
                        model.setDifficulty(Difficulty.DIFFICULT);
                        break;
                    default:
                        model.setDifficulty(Difficulty.NORMAL);
                        break;
                }

                difficultyLabel.setText("difficulty: " + model.getDifficulty().name());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* hide title bar */
        hide();

        setContentView(R.layout.activity_setting);
        model = Model.getInstance();
        model.addObserver(this);

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
