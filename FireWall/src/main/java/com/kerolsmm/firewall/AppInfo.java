package com.kerolsmm.firewall;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppInfo {
    final String label;
    final String packageName;
    final Drawable icon;
    boolean checked;

    AppInfo(String label, String packageName, Drawable icon) {
        this.label = label;
        this.packageName = packageName;
        this.icon = icon;
    }

    static List<AppInfo> getInstalledApps(Context context) {
        final Set<String> checked = context.getSharedPreferences("kerols",Context.MODE_PRIVATE)
                .getStringSet(OfflineVpnService.APPS_LIST_PREFERENCE, new HashSet<>());

        final PackageManager packageManager = context.getPackageManager();
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> listApps = pm.queryIntentActivities(intent, 0);
        List<AppInfo> apps = new ArrayList<>();
        for (ResolveInfo packageInfo : listApps) {
            if (packageInfo.activityInfo.packageName.equals(context.getPackageName())) continue;
            final AppInfo appInfo = new AppInfo(
                    packageInfo.activityInfo.applicationInfo.loadLabel(packageManager).toString(),
                    packageInfo.activityInfo.packageName,
                    packageInfo.activityInfo.applicationInfo.loadIcon(packageManager)
            );
            appInfo.checked = checked.contains(appInfo.packageName);
            apps.add(appInfo);
        }

        Collections.sort(apps, (a, b) -> a.label.compareTo(b.label));

        return apps;
    }

    interface FilterCallback {
        boolean filtrate(AppInfo app);
    }
}