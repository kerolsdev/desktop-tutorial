package com.wifinet.internetcheck;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;


import java.util.Date;

public class ShowDialog {


    String Bookname;
    MaterialCardView Cancel;

    public ShowDialog (){

    }


   public void showCustomDialog(AppCompatActivity activity) {


        final Dialog dialog = new Dialog(activity);


        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.


        View view = LayoutInflater.from(activity).inflate(R.layout.rate_app, null);


        dialog.setContentView(R.layout.rate_app);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(false);

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           dialog.create();
       }

       dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);


        TextView textView = dialog.findViewById(R.id.startRate);
        RatingBar ratingBar = dialog.findViewById(R.id.rate_us);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final String appPackageName = BuildConfig.APPLICATION_ID;
              /*  try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    dialog.dismiss();
                } catch (android.content.ActivityNotFoundException anfe) {*/
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    dialog.dismiss();
               // }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = BuildConfig.APPLICATION_ID;
     /*           try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    dialog.dismiss();
                } catch (android.content.ActivityNotFoundException anfe) {*/
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    dialog.dismiss();
             //   }
            }
        });



/*        final EditText nameEt = dialog.findViewById(R.id.name_et);

        final TextView Cancel = dialog.findViewById(R.id.Cancel);

        final  TextView Ok =  dialog.findViewById(R.id.Ok);*/

/*
        final TextView textView = dialog.findViewById(R.id.textView);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
*/


       dialog.show();


   }

}
