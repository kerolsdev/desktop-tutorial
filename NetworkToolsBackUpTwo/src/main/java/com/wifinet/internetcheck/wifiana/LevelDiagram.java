/*
 *  Copyright (C) 2014 Benjamin W. (bitbatzen@gmail.com)
 *
 *  This file is part of WLANScanner.
 *
 *  WLANScanner is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WLANScanner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WLANScanner.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wifinet.internetcheck.wifiana;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.wifinet.internetcheck.R;

import java.util.ArrayList;


public abstract class LevelDiagram extends View {
  
	protected Paint xLabelsPaint;
	protected Paint yLabelsPaint;
	protected Paint ssidPaint;
	protected Paint borderPaint;
	protected Paint innerRectPaint;
	protected Paint linesPaint;
	protected Paint ovalFillPaint;
	protected Paint ovalBorderPaint;
	protected Paint circlePaint;
	
	protected Rect borderRect;
	protected Rect innerRect;
	
	protected Rect xLabelsBounds;
	protected Rect yLabelsBounds;
	
	protected RectF ovalRect;

	protected ArrayList<WLANDiagramItem> wlans;
	protected ArrayList<WLANDiagramItem> wlanCache = new ArrayList<WLANDiagramItem>();

	protected float rowsMarginLeft = 20;
	protected float rowsMarginRight = 20;

    abstract public void updateDiagram(ArrayList<ScanResult> scanResults);
    abstract public float getXAxisPos(int frequency);
    abstract void drawXAxisLabelsAndLines(Canvas canvas);
    abstract void drawSSIDLabels(Canvas canvas);
	
	public LevelDiagram(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		
		wlans = new ArrayList<WLANDiagramItem>();

		borderRect = new Rect();
		innerRect = new Rect();
		
		borderPaint = new Paint();
		borderPaint.setColor(getResources().getColor(R.color.wlanscanner_diagram_border));
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);
		
		innerRectPaint = new Paint();
		innerRectPaint.setColor(getResources().getColor(R.color.wlanscanner_diagram_bg));
		innerRectPaint.setStyle(Style.FILL);
		
		linesPaint = new Paint();
		linesPaint.setColor(getResources().getColor(R.color.wlanscanner_diagram_lines));
		linesPaint.setStrokeWidth(1);
		
		int scaledTextSize = getResources().getDimensionPixelSize(R.dimen.diagram_axis_labels_fontsize);
		xLabelsPaint = new Paint();
		xLabelsPaint.setColor(getResources().getColor(R.color.wlanscanner_diagram_labels));
		xLabelsPaint.setTextSize(scaledTextSize);
		xLabelsPaint.setTextAlign(Align.CENTER);
		xLabelsBounds = new Rect();
		xLabelsPaint.getTextBounds("1", 0, 1, xLabelsBounds);
		
		yLabelsPaint = new Paint();
		yLabelsPaint.setColor(getResources().getColor(R.color.wlanscanner_diagram_labels));
		yLabelsPaint.setTextSize(scaledTextSize);
		yLabelsPaint.setTextAlign(Align.LEFT);
		yLabelsBounds = new Rect();
		yLabelsPaint.getTextBounds("-90", 0, 3, yLabelsBounds);
		
		ssidPaint = new Paint();
		ssidPaint.setTextSize(scaledTextSize);
		ssidPaint.setTextAlign(Align.CENTER);
		
		ovalRect = new RectF();
		
		ovalFillPaint = new Paint();
		ovalFillPaint.setAntiAlias(true);
		ovalFillPaint.setStyle(Style.FILL);
		
		ovalBorderPaint = new Paint();
		ovalBorderPaint.setAntiAlias(true);
		ovalBorderPaint.setStrokeWidth(1);
		ovalBorderPaint.setStyle(Style.STROKE);
		
		circlePaint = new Paint();
		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Style.FILL);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		updateMeasures();
	}
	
	@Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        updateMeasures();
    }
	
    protected void updateMeasures() {
		float borderStrokeWidth = borderPaint.getStrokeWidth();
		int borderOffsetX = yLabelsBounds.width() + 5; 
		float borderBottom = getHeight() - xLabelsBounds.height() - borderStrokeWidth / 2 - 5;
		borderRect.set(
				(int) (borderOffsetX + borderStrokeWidth / 2),
				(int) (borderStrokeWidth / 2),
				(int) (getWidth() - borderStrokeWidth / 2),
				(int) borderBottom);
		
		innerRect.set(
				(int) (borderRect.left + borderStrokeWidth / 2),
				(int) (borderRect.top + borderStrokeWidth / 2),
				(int) (borderRect.right - borderStrokeWidth / 2),
				(int) (borderRect.bottom - borderStrokeWidth / 2));
    }

    protected float getLevelHeight(int dBm) {
    	float maxLevelHeight = innerRect.bottom - innerRect.top;
    	float levelHeight = maxLevelHeight * (1 - ((float) Math.abs(dBm) - 30) / 70);
    	return levelHeight;
    }

    protected void drawSSIDLabel(Canvas canvas, WLANDiagramItem wdi, String label) {
		float levelHeight = getLevelHeight(wdi.dBm);
		float labelPosY = innerRect.bottom - levelHeight;
		labelPosY = Math.max(labelPosY - 8, 32);
		float labelPosX = getXAxisPos(wdi.frequency);

		ssidPaint.setColor(wdi.color);
		canvas.drawText(label, labelPosX, labelPosY, ssidPaint);
	}
    
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// border
		canvas.drawRect(borderRect, borderPaint);
		// inner color
		canvas.drawRect(innerRect, innerRectPaint);
		
		// x-axis labels and lines
		drawXAxisLabelsAndLines(canvas);
		
		// y-axis labels and lines (-100 to -30)
		float maxLevelHeight = innerRect.bottom - innerRect.top;
		int yLabelsMax = 7;
		float offsetY = maxLevelHeight / (float) yLabelsMax;
		float startY = innerRect.bottom - offsetY;
		for (int i = 0; i < yLabelsMax - 1; i++) {
			float posY = startY - offsetY * i;
			canvas.drawText(Integer.toString(-90 + i * 10), 0, posY + yLabelsBounds.height() / 2f, yLabelsPaint);
			canvas.drawLine(innerRect.left, posY, innerRect.right, posY, linesPaint);
		}

		// clipping
		canvas.clipRect(innerRect);

		// wlan levels
		for (WLANDiagramItem wdi : wlans) {
			// y-axis = -100 to -30
			float levelHeight = getLevelHeight(wdi.dBm);
			float levelY = innerRect.bottom - levelHeight;

			float posXLeft = getXAxisPos(wdi.frequency - wdi.channelWidth / 2);
			float posXRight = getXAxisPos(wdi.frequency + wdi.channelWidth / 2);
			
			ovalRect.set(posXLeft, levelY, posXRight, innerRect.bottom + levelHeight);
			ovalBorderPaint.setColor(wdi.color);
			canvas.drawOval(ovalRect, ovalBorderPaint);
			ovalFillPaint.setColor(wdi.color);
			ovalFillPaint.setAlpha(40);
			canvas.drawOval(ovalRect, ovalFillPaint);
		}
		
		// ssid labels
		drawSSIDLabels(canvas);
	}

	protected WLANDiagramItem checkWLANCache(WLANDiagramItem wdi) {
		for (WLANDiagramItem w : wlanCache) {
			if (w.SSID.equals(wdi.SSID) && w.BSSID.equals(wdi.BSSID)) {
				return w;
			}
		}

		return null;
	}

	protected void handleWLANDiagramItem(ScanResult sr) {
		int[] frequencies = Util.getFrequencies(sr);
		for (int f : frequencies) {
			createWLANDiagramItem(sr.SSID, sr.BSSID, f, Util.getChannelWidth(sr), sr.level);
		}
	}

	private void createWLANDiagramItem(String SSID, String BSSID, int frequency, int channelWidth, int level) {
		WLANDiagramItem wdi = new WLANDiagramItem(SSID, BSSID, frequency, channelWidth, level);
		WLANDiagramItem cachedWLAN = checkWLANCache(wdi);
		if (TextUtils.isEmpty(SSID)){
			 wdi = new WLANDiagramItem("Hidden Network", BSSID, frequency, channelWidth, level);
			 cachedWLAN = checkWLANCache(wdi);
		}

		if (cachedWLAN != null) {
			wdi.color = cachedWLAN.color;
		}
		else {
			wdi.color = Util.getRandomColor(80, 180);
			wlanCache.add(new WLANDiagramItem(wdi));
		}

		wlans.add(wdi);
	}
 }
