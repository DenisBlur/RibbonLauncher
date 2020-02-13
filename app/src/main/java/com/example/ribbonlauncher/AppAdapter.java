package com.example.ribbonlauncher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    Context context;
    List<AppObject> appList;
    LayoutInflater layoutInflater;


    public AppAdapter(Context context, List<AppObject> appList) {
        this.context = context;
        this.appList = appList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AppObject appObject = appList.get(position);
        holder.mImage.setImageDrawable(appObject.getImage());
        holder.mLabel.setText(appObject.getName());
        holder.appLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchAppIntent = context.getPackageManager().getLaunchIntentForPackage(appList.get(position).getPackageName());
                if (launchAppIntent != null) {
                    context.startActivity(launchAppIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout appLayout;
        ImageView mImage;
        TextView mLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appLayout = itemView.findViewById(R.id.appLayout);
            mImage = itemView.findViewById(R.id.image);
            mLabel = itemView.findViewById(R.id.appLabel);

        }
    }
}
