package com.example.ribbonlauncher;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Context mContext;
    int DRAWER_PEEK_HEIGHT = 150;
    private LinearLayout start_layout;
    private RecyclerView mDrawerGridView;
    private RecyclerView mRecentApps;
    private RecyclerView mFavAppBottom;
    private RecyclerView mWallpaperBottom;
    private ImageView mWallpaper;
    private BottomSheetBehavior mBottomSheetBehavior;
    CoordinatorLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mContext = this;

        start_layout = findViewById(R.id.start_layout);
        mRecentApps = findViewById(R.id.recentApps);
        mWallpaper = findViewById(R.id.wallpaper);
        mFavAppBottom = findViewById(R.id.favAppBottom);
        mWallpaperBottom = findViewById(R.id.wallpaperBottom);

        mWallpaper.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mFavAppBottom.setVisibility(View.GONE);
                mWallpaperBottom.setVisibility(View.VISIBLE);

                List<WallpaperObject> wallpaperObject = new ArrayList<>();

                wallpaperObject.add(new WallpaperObject(
                        "",
                        "https://images.unsplash.com/photo-1558981420-c532902e58b4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1477&q=80",
                        getResources().getDrawable(R.drawable.whd)));
                wallpaperObject.add(new WallpaperObject(
                        "",
                        "https://images.unsplash.com/photo-1581700575845-b1ecdfb21117?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80",
                        getResources().getDrawable(R.drawable.whd2)));

                mWallpaperBottom.setAdapter(new WallpaperAdapter(mContext, wallpaperObject));

                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                return false;
            }
        });

        MaterialCardView show_all_apps = findViewById(R.id.show_all_apps);
        show_all_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllApps();
            }
        });

        initializeDrawer();
    }

    List<AppObject> installedAppList = new ArrayList<>();
    List<AppObject> favAppList = new ArrayList<>();
    List<AppObject> recentAppList = new ArrayList<>();

    private void initializeDrawer() {
        Toast.makeText(mContext, "gdf", Toast.LENGTH_SHORT).show();
        View mBottomSheet = findViewById(R.id.bottomSheet);
        mDrawerGridView = findViewById(R.id.drawerGrid);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(DRAWER_PEEK_HEIGHT);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (mWallpaperBottom.getVisibility() == View.VISIBLE && newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mWallpaperBottom.setVisibility(View.GONE);
                    mFavAppBottom.setVisibility(View.VISIBLE);
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    params = (CoordinatorLayout.LayoutParams) start_layout.getLayoutParams();
                    params.bottomMargin = ((int) (getDisplayContentHeight() - (bottomSheet.getY() - 250)));
                    start_layout.setLayoutParams(params);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    params = (CoordinatorLayout.LayoutParams) start_layout.getLayoutParams();
                    params.bottomMargin = ((int) (getDisplayContentHeight() - (bottomSheet.getY() - 250)));
                    start_layout.setLayoutParams(params);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        favAppList.add(new AppObject("", "", getResources().getDrawable(R.drawable.start), 1));
        mFavAppBottom.setAdapter(new AppAdapter(mContext, favAppList));

        installedAppList = getInstalledAppList(0);
        mDrawerGridView.setAdapter(new AppAdapter(mContext, installedAppList));

    }

    private List<AppObject> getInstalledAppList(int all) {

        List<AppObject> list = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> untreatedAppList = mContext.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo untreaterApp : untreatedAppList) {
            String appName = untreaterApp.activityInfo.loadLabel(getPackageManager()).toString();
            String appPackageName = untreaterApp.activityInfo.packageName;
            Drawable appImage = untreaterApp.activityInfo.loadIcon(getPackageManager());

            AppObject app = new AppObject(appName, appPackageName, appImage, 0);
            if (all == 0) {
                if (!list.contains(app) && list.size() <= 14) {
                    list.add(app);
                }
            } else {
                if (!list.contains(app)) {
                    list.add(app);
                }
            }
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        if (start_layout.getVisibility() == View.VISIBLE) {
            start_layout.animate().alpha(0).setStartDelay(250).setDuration(250).start();
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.back_animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    start_layout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            start_layout.startAnimation(animation);
        }

    }

    private int getDisplayContentHeight() {

        final WindowManager windowManager = getWindowManager();
        final Point size = new Point();
        int screenHeight;
        windowManager.getDefaultDisplay().getSize(size);
        screenHeight = size.y;
        return screenHeight;
    }

    void itemPress(AppObject app) {

        Intent launchAppIntent = mContext.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
        if (launchAppIntent != null) {
            mContext.startActivity(launchAppIntent);
            recentAppList.add(new AppObject(app.getName(), app.getPackageName(), app.getImage(), 2));
            mRecentApps.setAdapter(new AppAdapter(mContext, recentAppList));
        }

    }

    public void showStartMenu() {
        if (start_layout.getVisibility() == View.GONE) {
            start_layout.setVisibility(View.VISIBLE);
            start_layout.setAlpha(0);
            start_layout.animate().alpha(1).setStartDelay(250).setDuration(250).start();
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.start_animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            start_layout.startAnimation(animation);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            start_layout.setVisibility(View.GONE);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            start_layout.animate().alpha(0).setStartDelay(250).setDuration(250).start();
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.back_animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    start_layout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            start_layout.startAnimation(animation);
        }
    }

    @SuppressLint("SetTextI18n")
    public void showAllApps() {
        TextView show_all_apps_label = findViewById(R.id.show_all_apps_label);
        if (show_all_apps_label.getText().equals("Show All")) {
            show_all_apps_label.setText("Hide");
            installedAppList = getInstalledAppList(1);
            mDrawerGridView.setAdapter(new AppAdapter(mContext, installedAppList));
        } else {
            show_all_apps_label.setText("Show All");
            installedAppList = getInstalledAppList(0);
            mDrawerGridView.setAdapter(new AppAdapter(mContext, installedAppList));
        }

    }
}


//    private void collapseDrawer() {
//        mDrawerGridView.setY(0);
//        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//    }

