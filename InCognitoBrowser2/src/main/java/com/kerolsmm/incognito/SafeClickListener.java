package com.kerolsmm.incognito;

import android.os.SystemClock;
import android.view.View;

public class SafeClickListener implements View.OnClickListener {
    private int defaultInterval = 1000;
    private final OnSafeClick onSafeClick;
    private long lastTimeClicked = 0;

    public SafeClickListener(OnSafeClick onSafeClick) {
        this.onSafeClick = onSafeClick;
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return;
        }
        lastTimeClicked = SystemClock.elapsedRealtime();
        onSafeClick.onSafeClick(v);
    }

    public interface OnSafeClick {
        void onSafeClick(View view);
    }
}
