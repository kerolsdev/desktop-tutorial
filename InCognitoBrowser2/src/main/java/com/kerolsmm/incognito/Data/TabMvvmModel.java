package com.kerolsmm.incognito.Data;

import java.util.ArrayList;

public class TabMvvmModel{


    private ArrayList<Tab> tabs;
    private int mPosition;

    public TabMvvmModel (ArrayList<Tab> tab , int mPosition) {
        this.mPosition = mPosition;
        this.tabs = tab;
    }

    public int getmPosition() {
        return mPosition;
    }

    public ArrayList<Tab> getTab() {
        return tabs;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public void setTab(ArrayList<Tab> tab) {
        this.tabs = tab;
    }
}
