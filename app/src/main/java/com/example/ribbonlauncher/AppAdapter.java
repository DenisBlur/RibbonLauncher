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

import com.google.android.material.card.MaterialCardView;

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

        if (viewType == 0) {
            View view = layoutInflater.inflate(R.layout.item_app, parent, false);
            return new ViewHolder(view);
        } else if (viewType == 2) {
            View view = layoutInflater.inflate(R.layout.item_app_recent, parent, false);
            return new ViewHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.item_app_fav, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final AppObject appObject = appList.get(position);

        if (appObject.getIsFavorite() == 0) {

            MaterialCardView appLayout = holder.itemView.findViewById(R.id.appLayout);
            ImageView mImage = holder.itemView.findViewById(R.id.image);
            TextView mLabel = holder.itemView.findViewById(R.id.appLabel);

            mImage.setImageDrawable(appObject.getImage());
            mLabel.setText(appObject.getName());
            appLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).itemPress(appList.get(position));
                }
            });

        } else if (appObject.getIsFavorite() == 2) {
            LinearLayout appLayout = holder.itemView.findViewById(R.id.mLayoutRecent);
            ImageView mImage = holder.itemView.findViewById(R.id.mImageRecent);
            TextView mLabel = holder.itemView.findViewById(R.id.mLabalRecent);

            mImage.setImageDrawable(appObject.getImage());
            mLabel.setText(appObject.getName());
            appLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).itemPress(appList.get(position));
                }
            });
        } else {
            MaterialCardView appLayout = holder.itemView.findViewById(R.id.appLayoutFav);
            ImageView mImage = holder.itemView.findViewById(R.id.appImageFav);

            mImage.setImageDrawable(appObject.getImage());
            appLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        ((MainActivity) context).showStartMenu();
                    } else {
                        ((MainActivity) context).itemPress(appList.get(position));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    @Override
    public int getItemViewType(int position) {
        AppObject appObject = appList.get(position);
        return appObject.getIsFavorite();
    }
}
