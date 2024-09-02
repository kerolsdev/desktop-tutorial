package com.kerolsmm.incognito.Data;

public class UpdatePosition{


    private int tabs;
    private int mPosition;

    public UpdatePosition (int tabs, int mPosition) {
        this.tabs = tabs;
        this.mPosition = mPosition;
    }

    public int getmPosition() {
        return mPosition;
    }

    public int getTab() {
        return tabs;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

}
