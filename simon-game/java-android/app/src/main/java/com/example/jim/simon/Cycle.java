package com.example.jim.simon;

import android.graphics.RectF;

/**
 * Created by jim on 25/11/17.
 */

public class Cycle {

    private int id;
    private int total;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private int diameter;

    private boolean animation = true;

    int offset() {
        /* for animation */
        return (int) (Math.sin((System.currentTimeMillis() / 2 + id * 360 / total) * Math.PI / 180) * diameter / 8);
    }

    Cycle(int id, int total, int width, int height) {
        this.id = id;
        this.total = total;
        this.diameter = width/8;

        /* setting cycle boundary the cycle */
        recenter(width, height);
    }

    void recenter(int width, int height) {
        /* gap between cycles */
        int gap = (width - total * diameter) / (total + 1);

        /* cycle's boundary */
        this.top = height / 3;
        this.bottom = this.top + diameter;
        this.left = (diameter + gap) * id + gap;
        this.right = this.left + diameter;
    }

    boolean isInCycle(int x, int y) {
        int center_x = this.left + diameter / 2;
        int center_y = this.top + diameter / 2;

        return (x - center_x) * (x - center_x) + (y - center_y) * (y - center_y) <= diameter / 2 * diameter / 2;
    }

    /* return the boundary of cycle as a rectF */
    RectF getRectF() {
        if (animation) {
            /* when animating, adding offset to y-axis */
            return new RectF(left, top+offset(), right, bottom+offset());
        } else {
            return new RectF(left, top, right, bottom);
        }
    }

    public void cancelAnimation() {
        animation = false;
    }

    public void startAnimation() {
        animation = true;
    }
}