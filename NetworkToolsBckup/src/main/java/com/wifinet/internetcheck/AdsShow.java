/*
package com.wifinet.internetcheck;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener;
import com.unity3d.services.banners.api.BannerListener;


public class AdsShow {


    private Activity context;
    FrameLayout frameLayout;
    private IronSourceBannerLayout mIronSourceBannerLayout;

    public AdsShow (FrameLayout frameLayout , Activity context){
       this.frameLayout = frameLayout;
       this.context = context;
    }

    public AdsShow (){

    }

    public void createAndloadBanner() {

        // choose banner size
        ISBannerSize size = ISBannerSize.SMART;

        Log.e("ds", "onAdLoadeewqeqeqedsdsss: " );
        // instantiate IronSourceBanner object, using the IronSource.createBanner API
        mIronSourceBannerLayout = IronSource.createBanner(context, size);

        // add IronSourceBanner to your container
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayout.addView(mIronSourceBannerLayout, 0, layoutParams);

        if (mIronSourceBannerLayout != null) {
            // set the banner listener
            mIronSourceBannerLayout.setLevelPlayBannerListener(new LevelPlayBannerListener() {
                // Invoked each time a banner was loaded. Either on refresh, or manual load.
                //  AdInfo parameter includes information about the loaded ad
                @Override
                public void onAdLoaded(AdInfo adInfo) {
                    frameLayout.setVisibility(View.VISIBLE);
                    Log.e("TAG", "onAdLoadeewqeqeqed: " );
                }
                // Invoked when the banner loading process has failed.
                //  This callback will be sent both for manual load and refreshed banner failures.
                @Override
                public void onAdLoadFailed(IronSourceError error) {
                    Log.e("TAG", "onAdLoadFailedeee  " + error.getErrorMessage());
                }
                // Invoked when end user clicks on the banner ad
                @Override
                public void onAdClicked(AdInfo adInfo) {}
                // Notifies the presentation of a full screen content following user click
                @Override
                public void onAdScreenPresented(AdInfo adInfo) {}
                // Notifies the presented screen has been dismissed
                @Override
                public void onAdScreenDismissed(AdInfo adInfo) {}
                //Invoked when the user left the app
                @Override
                public void onAdLeftApplication(AdInfo adInfo) {}

            });

            // load ad into the created banner
            IronSource.loadBanner(mIronSourceBannerLayout);
        } else {
            Log.e("dsadasdadsa", "sadsadsadsadsadsa: ");
        }
    }
    public void createAndloadBanner2() {

        // choose banner size
        ISBannerSize size = ISBannerSize.SMART;

        // instantiate IronSourceBanner object, using the IronSource.createBanner API
        mIronSourceBannerLayout = IronSource.createBanner(context, size);

        // add IronSourceBanner to your container
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayout.addView(mIronSourceBannerLayout, 0, layoutParams);

        if (mIronSourceBannerLayout != null) {
            // set the banner listener
            mIronSourceBannerLayout.setLevelPlayBannerListener(new LevelPlayBannerListener() {
                // Invoked each time a banner was loaded. Either on refresh, or manual load.
                //  AdInfo parameter includes information about the loaded ad
                @Override
                public void onAdLoaded(AdInfo adInfo) {}
                // Invoked when the banner loading process has failed.
                //  This callback will be sent both for manual load and refreshed banner failures.
                @Override
                public void onAdLoadFailed(IronSourceError error) {
                    Log.e("TAG", "onAdLoadFailed: eee  " + error.getErrorMessage());
                }
                // Invoked when end user clicks on the banner ad
                @Override
                public void onAdClicked(AdInfo adInfo) {}
                // Notifies the presentation of a full screen content following user click
                @Override
                public void onAdScreenPresented(AdInfo adInfo) {}
                // Notifies the presented screen has been dismissed
                @Override
                public void onAdScreenDismissed(AdInfo adInfo) {}
                //Invoked when the user left the app
                @Override
                public void onAdLeftApplication(AdInfo adInfo) {}

            });

            // load ad into the created banner
            IronSource.loadBanner(mIronSourceBannerLayout);
        } else {

        }
    }
    public void destroyAndDetachBanner() {
        IronSource.destroyBanner(mIronSourceBannerLayout);
        if (frameLayout != null) {
            frameLayout.removeView(mIronSourceBannerLayout);
        }
    }



  */
/*  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showCustomDialog(AppCompatActivity activity) {


        final Dialog dialog = new Dialog(activity);


        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.


        View view = LayoutInflater.from(activity).inflate(R.layout.rate_app, null);


        dialog.setContentView(R.layout.view_gift);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(false);

        dialog.create();

        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);

        LottieAnimationView animationView = dialog.findViewById(R.id.animationView);
        ImageView imageView = dialog.findViewById(R.id.Cancelgift);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        *//*
*/
/* Admob ads Banner*//*
*/
/*
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdLoader adLoader = new AdLoader.Builder(activity, activity.getString(R.string.native_ad))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        animationView.setVisibility(View.GONE);
                        ColorDrawable colorDrawable = new ColorDrawable(activity.getResources().getColor(R.color.card_color));
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                        FrameLayout template = (FrameLayout) dialog.findViewById(R.id.my_template_ad_gift);
                        template.setVisibility(View.VISIBLE);
                        AddViewNativeAdmob addViewNativeAdmob = new AddViewNativeAdmob(activity);
                        addViewNativeAdmob.displayNativeAd(template,nativeAd);

                    }
                })
                .build();
        adLoader.loadAd(new AdManagerAdRequest.Builder().build());

        dialog.show();


    }*//*



}
*/
