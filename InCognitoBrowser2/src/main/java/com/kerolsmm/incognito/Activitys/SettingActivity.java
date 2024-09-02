package com.kerolsmm.incognito.Activitys;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.google.android.ump.ConsentInformation;
import com.google.android.ump.UserMessagingPlatform;
import com.kerolsmm.incognito.BuildConfig;
import com.kerolsmm.incognito.Fragments.AccessibilityDialog;
import com.kerolsmm.incognito.Fragments.DownloadRequestDialog;
import com.kerolsmm.incognito.R;
import com.kerolsmm.incognito.Utilts.SettingValue;
import com.kerolsmm.incognito.Utilts.StaticValue;
import com.kerolsmm.incognito.databinding.ActivitySettingBinding;


import java.io.File;
import java.util.Objects;


public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    String[] listItems;
    boolean[] checkedItems;
    SettingValue settingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingValue = new SettingValue(this);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.ToolbarSetting);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        CookieManager cookieManager = CookieManager.getInstance();
        listItems = new String[]{"Google", "Bing"};
        checkedItems = new boolean[listItems.length];


        binding.PrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent urlIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://sites.google.com/view/incognitoapp/home")
                );
                startActivity(urlIntent);*/
                Intent intent = new Intent(SettingActivity.this,Home.class);
                intent.putExtra("url_site","https://sites.google.com/view/incognitoapp/home");
                /*intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                intent.setAction("addnewtab");
                startActivity(intent);
                finish();
            }
        });

        if (isPrivacyOptionsRequired()) {
            binding.AdsPrivcay.setVisibility(View.VISIBLE);
            binding.AdsPrivcay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserMessagingPlatform.showPrivacyOptionsForm(
                            SettingActivity.this,
                            formError -> {
                                if (formError != null) {
                                    // Handle the error.
                                }
                            }
                    );
                }
            });
        }

        binding.SettingAccessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(new AccessibilityDialog());
            }
        });

      /*  binding.SettingDefaultRealy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.google.android.browser","com.google.android.browser.BrowserActivity"));
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.BROWSABLE");
                Uri uri = Uri.parse(url);
                intent.setData(uri);
                try
                {
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
*/
        binding.SettingDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(new DownloadRequestDialog());
            }
        });


        binding.SearchEngine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAltre();
            }
        });

        binding.SettingGooglePlayStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMarket();

            }
        });

        binding.ShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\n" + getString(R.string.let_recommend) + "\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
    }

    public void showDialog(DialogFragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

      /*  if (getResources().getBoolean(R.bool.large_layout)) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog");
        } else {*/
            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit();
      //  }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    private void setAltre() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingActivity.this);
        mBuilder.setTitle(R.string.Chooseasearchengine);
        mBuilder.setSingleChoiceItems(listItems, settingValue.getSearch(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settingValue.setSearch(which);


            }
        });

        mBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                settingValue.getEditor().apply();
                StaticValue.isSearch = true;

            }
        });

        mBuilder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void sendMessage(String message) {

        // Creating new intent
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        /*Write here Email to */
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"converterfiles1@gmail.com"});
        /*Write Subject */
        email.putExtra(Intent.EXTRA_SUBJECT, "Safe Web - Feedback");
        /*Write Message*/
        email.putExtra(Intent.EXTRA_TEXT, "");
        email.setType("message/rfc822");
        startActivity(email);
    }

    public boolean isPrivacyOptionsRequired() {
        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(this);
        return consentInformation.getPrivacyOptionsRequirementStatus()
                == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED;
    }

}