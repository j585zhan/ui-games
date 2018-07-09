package com.example.jim.simon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by jim on 25/11/17.
 */

class GameController implements View.OnTouchListener {

    Model model;
    GameController (Model model) {
        this.model = model;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        model.onTouch(motionEvent);
        return false;
    }
}

public class PlayView extends View {


    private Paint paint;
    private Model model;

    private int screenWidth;
    private int screenHeight;
    private String displayMsg = "Touch anywhere to play";

    @Override
    public boolean performClick() {     /* suppressing warning for onTouchListener */
        return super.performClick();
    }

    public PlayView(Context context, final Model model) {
        super(context);

        /* paint for drawing cycle */
        paint = new Paint();
        this.model = model;
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5 * getResources().getDisplayMetrics().density);
        paint.setShader(new LinearGradient(0.40f, 0.0f, 100.60f, 100.0f,
                Color.RED,
                Color.RED,
                Shader.TileMode.CLAMP));

        /* get windows size */
        WindowManager vm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = vm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        /* setting screen info */
        screenWidth = size.x;
        screenHeight = size.y;

        this.setOnTouchListener(new GameController(model));

        // focus
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Simon simon = model.getSimon();
        Simon.State state = simon.getState();
        int nextButton = model.getNextButton();
        int frameCounter = model.getFrameCounter();

        /* computer get next button in sequence */
        if (state != Simon.State.START && frameCounter == 0) {
            model.resetNextButton();
            if (nextButton == -1) {
                model.setUpdateMsg(true);
            }
        }

        /* determine whether animate or not */
        if (state == Simon.State.LOSE || state == Simon.State.WIN) model.startAnimation();

        ArrayList<Cycle> cycles = model.getCycles();

        /* test paint setting */
        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20 * getResources().getDisplayMetrics().density);

        /* check if message need update */
        if (model.isUpdateMsg()) {
            displayMsg = model.getText();
            model.setUpdateMsg(false);
        }

        /* draw game message */
        canvas.drawText("Score: " + simon.getScore(), screenHeight / 10, screenHeight / 20, textPaint);
        canvas.drawText(displayMsg, screenHeight / 10, screenHeight / 10, textPaint);

        /* draw cycles */
        for (int i = 0; i < cycles.size(); i++) {

            /* check should the cycle fill or draw */
            if (nextButton == i && frameCounter > 3) {
                paint.setStyle(Paint.Style.FILL);
            } else {
                paint.setStyle(Paint.Style.STROKE);
            }

            canvas.drawOval(cycles.get(i).getRectF(), paint);
        }

        /* way to back to welcome screen */
        if (state == Simon.State.START || state == Simon.State.WIN || state == Simon.State.LOSE) {
            canvas.drawText("TOUCH TO BACK",
                    screenWidth * 5 / 16, screenHeight - screenHeight / 10, textPaint);
        }

        model.increaseFrameCounter();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
