package com.example.ribbonlauncher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;


public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> {

    Context mContext;
    List<WallpaperObject> wallpaperObjectList;
    LayoutInflater layoutInflater;

    public WallpaperAdapter(Context mContext, List<WallpaperObject> wallpaperObjectList) {
        this.mContext = mContext;
        this.wallpaperObjectList = wallpaperObjectList;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_wallpaper, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final WallpaperObject wallpaperObject = wallpaperObjectList.get(position);
//        Glide.with(mContext).load(wallpaperObject.getURL_image()).into(holder.mImage);
        holder.mImage.setImageDrawable(wallpaperObject.getImage());
        holder.appLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Glide.with(mContext).load(wallpaperObject.getURL_image()).into(mWallpaper);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperObjectList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private MaterialCardView appLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.mImage);
            appLayout = itemView.findViewById(R.id.appLayout);

        }
    }
}
