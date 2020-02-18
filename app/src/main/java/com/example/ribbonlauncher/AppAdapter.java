package com.example.ribbonlauncher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<AppObject> appList;
    private List<AppObject> appListFilter;
    private LayoutInflater layoutInflater;


    public AppAdapter(Context context, List<AppObject> appList) {
        this.context = context;
        this.appList = appList;
        this.layoutInflater = LayoutInflater.from(context);
        this.appListFilter = appList;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final AppObject appObject = appList.get(position);

        if (appObject.getType() == 0) {

            MaterialCardView appLayout = holder.itemView.findViewById(R.id.appLayout);
            ImageView mImage = holder.itemView.findViewById(R.id.image);
            TextView mLabel = holder.itemView.findViewById(R.id.appLabel);
            appList.get(position).setPosition(position);

            mImage.setImageDrawable(appObject.getImage());
            mLabel.setText(appObject.getName());
            appLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final int action = MotionEventCompat.getActionMasked(event);

                    switch (action) {
                        case MotionEvent.ACTION_DOWN: {

                            v.animate().scaleX((float) 0.8).scaleY((float) 0.8).setDuration(50).start();

                            break;
                        }

                        case MotionEvent.ACTION_UP: {
                            v.animate().scaleX((float) 1.0).scaleY((float) 1.0).setDuration(50).start();

                        }

                        case MotionEvent.ACTION_CANCEL: {
                            v.animate().scaleX((float) 1.0).scaleY((float) 1.0).setDuration(50).start();
                        }
                    }

                    return false;
                }
            });
            appLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).itemPress(appList.get(position));
                }
            });
            appLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (appObject.getIsFavorite() == 0) {
                        ((MainActivity) context).doFavorite(appList.get(position));
                        appList.get(position).setIsFavorite(1);
                    } else {
                        Toast.makeText(context, "Ты еблан?", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

        } else if (appObject.getType() == 2) {
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
            appLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final int action = MotionEventCompat.getActionMasked(event);

                    switch (action) {
                        case MotionEvent.ACTION_DOWN: {

                            v.animate().scaleX((float) 0.8).scaleY((float) 0.8).setDuration(50).start();

                            break;
                        }

                        case MotionEvent.ACTION_UP: {
                            v.animate().scaleX((float) 1.0).scaleY((float) 1.0).setDuration(50).start();
                        }

                        case MotionEvent.ACTION_CANCEL: {
                            v.animate().scaleX((float) 1.0).scaleY((float) 1.0).setDuration(50).start();
                        }
                    }

                    return false;
                }
            });
            appLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).itemPress(appList.get(position));
                }
            });
            appLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    ((MainActivity) context).notifyAdapter(position, appObject);
                    BottomSheetAppInfo bottomSheetAppInfo = new BottomSheetAppInfo(context, appList.get(position), position);
                    bottomSheetAppInfo.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetAppInfo.getTag());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return appListFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    appListFilter = appList;
                } else {
                    List<AppObject> filteredList = new ArrayList<>();
                    for (AppObject row : appList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    appListFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = appListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                appListFilter = (ArrayList<AppObject>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    @Override
    public int getItemViewType(int position) {
        AppObject appObject = appList.get(position);
        return appObject.getType();
    }

}
