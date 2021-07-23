package com.jan.localization;

import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MainDisplaySwipeManager extends OnSwipeTouchListener {
    MainDisplay mainDisplay;

    MainDisplaySwipeManager(MainDisplay mainDisplay, ConstraintLayout layout) {
        super(mainDisplay);
        this.mainDisplay = mainDisplay;
        layout.setOnTouchListener(this);
    }

    @Override
    public void onSwipeLeft() {
        super.onSwipeLeft();
        if (mainDisplay.isKmph()){
            mainDisplay.setKmph(false);
            mainDisplay.setMps(true);
        }else if(mainDisplay.isMps()){
            mainDisplay.setMps(false);
            mainDisplay.setKts(true);
        }
    }
    @Override
    public void onSwipeRight() {
        super.onSwipeRight();
        if (mainDisplay.isMps()){
            mainDisplay.setMps(false);
            mainDisplay.setKmph(true);
        }else if (mainDisplay.isKts()){
            mainDisplay.setKts(false);
            mainDisplay.setMps(true);
        }

    }
}
