package com.kerolsmm.firewall;

import static com.kerolsmm.firewall.OfflineVpnService.isRunning;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder>{

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final List<AppInfo> mSource;
    private List<AppInfo> mList;

    AppAdapter(Context context, List<AppInfo> source) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mSource = source;

        filtrate(null);
    }

    public void filtrate(AppInfo.FilterCallback callback) {
        mList = new ArrayList<>();
        if(callback == null) {
            mList.addAll(mSource);
        } else {
            for (AppInfo app : mSource) {
                if (callback.filtrate(app)) {
                    mList.add(app);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.app_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppInfo app = mList.get(position);
        holder.labelView.setText(app.label);
        holder.packageView.setText(app.packageName);
        holder.iconView.setImageDrawable(app.icon);
        holder.checkBoxView.setChecked(app.checked);
        if ((mList.size() - 1) == position) {
            holder.layoutParams.setMargins(0, 0, 0, 190);
        }else  {
            holder.layoutParams.setMargins(0, 0, 0, 0);

        }
        holder.root.setOnClickListener(v -> {
            boolean checked = !holder.checkBoxView.isChecked();
            holder.checkBoxView.setChecked(checked);
            app.checked = checked;

            SharedPreferences prefs = mContext.getSharedPreferences("kerols",Context.MODE_PRIVATE);
            Set<String> apps = new HashSet<>(prefs.getStringSet(
                    OfflineVpnService.APPS_LIST_PREFERENCE, new HashSet<>()));
            if (apps.contains(app.packageName)) {
                apps.remove(app.packageName);
            } else {
                apps.add(app.packageName);
            }
            prefs.edit().putStringSet(OfflineVpnService.APPS_LIST_PREFERENCE, apps).apply();
            if (isRunning) {
                OfflineVpnService.connect(mContext);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View root;
        final ImageView iconView;
        final TextView labelView;
        final TextView packageView;
        final CheckBox checkBoxView;
        final RecyclerView.LayoutParams layoutParams;

        ViewHolder(View view) {
            super(view);

            root = view;
            iconView = (ImageView) view.findViewById(R.id.icon);
            labelView = (TextView) view.findViewById(R.id.app_label);
            packageView = (TextView) view.findViewById(R.id.app_package);
            checkBoxView = (CheckBox) view.findViewById(R.id.checkBox);
            layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        }
    }
}