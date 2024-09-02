/*
package com.wifinet.internetcheck;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class AddViewNativeAdmob {

        AppCompatActivity activity;
    public AddViewNativeAdmob (AppCompatActivity activity) {
        this.activity = activity;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void displayNativeAd(FrameLayout parent, NativeAd ad) {
        MaterialCardView view = (MaterialCardView) activity.getLayoutInflater()
                .inflate(R.layout.layout_ad, null);
        // Inflate a layout and add it to the parent ViewGroup.

        NativeAdView adView = view.findViewById(R.id.card_ad);

        // Locate the view that will hold the headline, set its text, and call the
        // NativeAdView's setHeadlineView method to register it.
        TextView headlineView = view.findViewById(R.id.ad_headline);
        ImageView imageView = view.findViewById(R.id.ad_icon);

        headlineView.setText(ad.getHeadline());
        adView.setHeadlineView(headlineView);
        adView.setIconView(imageView);

        // Repeat the above process for the other assets in the NativeAd
        // using additional view objects (Buttons, ImageViews, etc).

        // If the app is using a MediaView, it should be
        // instantiated and passed to setMediaView. This view is a little different
        // in that the asset is populated automatically, so there's one less step.
        MediaView mediaView = (MediaView) view.findViewById(R.id.ad_media);
        MaterialButton card = view.findViewById(R.id.ad_call_to_action);
        RatingBar ratingBar = view.findViewById(R.id.ad_start);
        card.setText(ad.getCallToAction());
        adView.setMediaView(mediaView);
        adView.setCallToActionView(card);
        adView.setStarRatingView(ratingBar);

        if (ad.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    ad.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (ad.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(ad.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        // Call the NativeAdView's setNativeAd method to register the
        // NativeAdObject.

        adView.setNativeAd(ad);

        // Ensure that the parent view doesn't already contain an ad view.
        parent.removeAllViews();

        // Place the AdView into the parent.
        parent.addView(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void displayNativeAdGift(FrameLayout parent, NativeAd ad) {
        MaterialCardView view = (MaterialCardView) activity.getLayoutInflater()
                .inflate(R.layout.layout_ad, null);
        // Inflate a layout and add it to the parent ViewGroup.

        NativeAdView adView = view.findViewById(R.id.card_ad);

        // Locate the view that will hold the headline, set its text, and call the
        // NativeAdView's setHeadlineView method to register it.
        TextView headlineView = view.findViewById(R.id.ad_headline);
        ImageView imageView = view.findViewById(R.id.ad_icon);

        headlineView.setText(ad.getHeadline());
        adView.setHeadlineView(headlineView);
        adView.setIconView(imageView);

        // Repeat the above process for the other assets in the NativeAd
        // using additional view objects (Buttons, ImageViews, etc).

        // If the app is using a MediaView, it should be
        // instantiated and passed to setMediaView. This view is a little different
        // in that the asset is populated automatically, so there's one less step.
        MediaView mediaView = (MediaView) view.findViewById(R.id.ad_media);
        MaterialButton card = view.findViewById(R.id.ad_call_to_action);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        RatingBar ratingBar = view.findViewById(R.id.ad_start);
        card.setText(ad.getCallToAction());
        adView.setMediaView(mediaView);
        adView.setCallToActionView(card);
        adView.setStarRatingView(ratingBar);

        if (ad.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    ad.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (ad.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(ad.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        // Call the NativeAdView's setNativeAd method to register the
        // NativeAdObject.

        adView.setNativeAd(ad);

        // Ensure that the parent view doesn't already contain an ad view.
        parent.removeAllViews();

        // Place the AdView into the parent.
        parent.addView(view);
    }


}
*/
