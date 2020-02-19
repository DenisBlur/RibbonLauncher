package com.example.ribbonlauncher;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    Context mContext;
    int DRAWER_PEEK_HEIGHT = 180;
    private LinearLayout start_layout, favBottomS;
    public static RecyclerView mDrawerGridView;
    public static RecyclerView mRecentApps;
    public static RecyclerView mFavAppBottom;
    private RecyclerView mWallpaperBottom;
    private LinearLayout setting;
    private ImageView mWallpaper;
    private BottomSheetBehavior mBottomSheetBehavior;
    CoordinatorLayout.LayoutParams params;
    DisplayMetrics metrics;
    UninstallIntentReceiver receiver;

    public static List<AppObject> installedAppList = new ArrayList<>();
    public static List<AppObject> favAppList = new ArrayList<>();
    public static List<AppObject> recentAppList = new ArrayList<>();
    public static AppAdapter appAdapter;
    public static int UNINSTALL_POS_ALL_APPS = -1;
    public static int UNINSTALL_POS_FAV_APPS = -1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mContext = this;
        metrics = mContext.getResources().getDisplayMetrics();

        start_layout = findViewById(R.id.start_layout);
        mRecentApps = findViewById(R.id.recentApps);
        mWallpaper = findViewById(R.id.wallpaper);
        mFavAppBottom = findViewById(R.id.favAppBottom);
        mWallpaperBottom = findViewById(R.id.wallpaperBottom);
        setting = findViewById(R.id.setting);
        favBottomS = findViewById(R.id.favBottomS);

        EditText editText = findViewById(R.id.editText);
        CoordinatorLayout layout = findViewById(R.id.backAction);
        MaterialCardView startButton = findViewById(R.id.startButton);
        MaterialCardView openImage = findViewById(R.id.openImage);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                appAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((ViewGroup) findViewById(R.id.root)).getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);

        startButton.setOnTouchListener(new View.OnTouchListener() {
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
                        showStartMenu();
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        v.animate().scaleX((float) 1.0).scaleY((float) 1.0).setDuration(50).start();
                    }
                }

                return true;
            }
        });

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openSetting();
                return false;
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartMenu();
            }
        });
        openImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
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

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new UninstallIntentReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Objects.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
                    getInstalledAppList(0);
                    if (favAppList.size() > 0) {
                        favAppList.remove(UNINSTALL_POS_FAV_APPS);
                        Objects.requireNonNull(mFavAppBottom.getAdapter()).notifyItemRemoved(UNINSTALL_POS_FAV_APPS);
                    } else {
                        favAppList = new ArrayList<>();
                        mFavAppBottom.setAdapter(new AppAdapter(mContext, favAppList));
                    }
                    UNINSTALL_POS_FAV_APPS = -1;
                    Collections.sort(installedAppList, new Comparator<AppObject>() {
                        @Override
                        public int compare(AppObject o1, AppObject o2) {
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });
                } else if(Objects.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
                    Toast.makeText(context, "oh yeah! It`s:" + intent.getDataString(), Toast.LENGTH_SHORT).show();
                    getInstalledAppList(0);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);

        filter.addDataScheme("package");
        registerReceiver(receiver, filter);
    }

    private void openSetting() {
        favBottomS.setVisibility(View.GONE);
        setting.setVisibility(View.VISIBLE);

        List<WallpaperObject> wallpaperObject = new ArrayList<>();

        mWallpaperBottom.setAdapter(new WallpaperAdapter(mContext, wallpaperObject));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri chosenImageUri = data.getData();
                mWallpaper.setImageURI(chosenImageUri);
                setWall();
            }
        }
    }

    private void setWall() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mWallpaper.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        WallpaperManager wallpaperManager;
        wallpaperManager = WallpaperManager.getInstance(mContext);
        try {
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeDrawer() {
        View mBottomSheet = findViewById(R.id.bottomSheet);
        mDrawerGridView = findViewById(R.id.drawerGrid);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(DRAWER_PEEK_HEIGHT);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (setting.getVisibility() == View.VISIBLE && newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    setting.setVisibility(View.GONE);
                    favBottomS.setVisibility(View.VISIBLE);
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                    params = (CoordinatorLayout.LayoutParams) start_layout.getLayoutParams();
//                    params.bottomMargin = ((int) (getDisplayContentHeight() - (bottomSheet.getY() - 250)));
//                    start_layout.setLayoutParams(params);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    params = (CoordinatorLayout.LayoutParams) start_layout.getLayoutParams();
//                    params.bottomMargin = ((int) (getDisplayContentHeight() - (bottomSheet.getY() - 250)));
//                    start_layout.setLayoutParams(params);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                params = (CoordinatorLayout.LayoutParams) start_layout.getLayoutParams();
                params.bottomMargin = ((int) (getDisplayContentHeight() - (bottomSheet.getY() - 86 * metrics.density)));
                start_layout.setLayoutParams(params);
            }
        });
        mFavAppBottom.setAdapter(new AppAdapter(mContext, favAppList));
        getInstalledAppList(0);
    }

    private void getInstalledAppList(int st) {
        all = st;
        new getInstalledAppListThread().execute();
    }


    int all = 0;

    @SuppressLint("StaticFieldLeak")
    public class getInstalledAppListThread extends AsyncTask<Void, String, String> {

        List<AppObject> list = new ArrayList<>();

        @Override
        protected String doInBackground(Void... voids) {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> untreatedAppList = mContext.getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo untreaterApp : untreatedAppList) {
                String appName = untreaterApp.activityInfo.loadLabel(getPackageManager()).toString();
                String appPackageName = untreaterApp.activityInfo.packageName;
                Drawable appImage = untreaterApp.activityInfo.loadIcon(getPackageManager());

                AppObject app = new AppObject(appName, appPackageName, appImage, 0, 0);

                if (!list.contains(app)) {
                    list.add(app);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            installedAppList = list;
            updateStuff();
        }
    }

    private void updateStuff() {
        appAdapter = new AppAdapter(mContext, installedAppList);
        mDrawerGridView.setAdapter(appAdapter);
        Collections.sort(installedAppList, new Comparator<AppObject>() {
            @Override
            public int compare(AppObject o1, AppObject o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (start_layout.getVisibility() == View.VISIBLE) {
            int resId = R.anim.item_animation_fall_up;
            Animation animation = AnimationUtils.loadAnimation(mContext, resId);
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
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
        if (recentAppList.size() != 0) {
            if (forRecentApps(app) == 1) {
                Intent launchAppIntent = mContext.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
                if (launchAppIntent != null) {
                    mContext.startActivity(launchAppIntent);
                }
            } else {
                Intent launchAppIntent = mContext.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
                if (launchAppIntent != null) {
                    mContext.startActivity(launchAppIntent);
                    recentAppList.add(new AppObject(app.getName(), app.getPackageName(), app.getImage(), 2, 0));
                    mRecentApps.setAdapter(new AppAdapter(mContext, recentAppList));
                }
            }
        } else {
            Intent launchAppIntent = mContext.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
            if (launchAppIntent != null) {
                mContext.startActivity(launchAppIntent);
                recentAppList.add(new AppObject(app.getName(), app.getPackageName(), app.getImage(), 2, 0));
                mRecentApps.setAdapter(new AppAdapter(mContext, recentAppList));
            }
        }

    }

    private int forRecentApps(AppObject app) {

        int result = 0;

        for (int i = 0; i < recentAppList.size(); i++) {
            if (app.getPackageName().equals(recentAppList.get(i).getPackageName())) {
                recentAppList.remove(i);
                Objects.requireNonNull(mRecentApps.getAdapter()).notifyItemRemoved(i);
                recentAppList.add(new AppObject(app.getName(), app.getPackageName(), app.getImage(), 2, app.getPosition()));
                result = 1;
            } else result = 0;
        }

        return result;
    }

    public void showStartMenu() {
        if (start_layout.getVisibility() == View.GONE) {
            start_layout.setVisibility(View.VISIBLE);
            int resId = R.anim.item_animation_fall_down;
            Animation animation = AnimationUtils.loadAnimation(mContext, resId);
            start_layout.startAnimation(animation);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            int resId = R.anim.item_animation_fall_up;
            Animation animation = AnimationUtils.loadAnimation(mContext, resId);
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
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @SuppressLint("SetTextI18n")
    public void showAllApps() {
        TextView show_all_apps_label = findViewById(R.id.show_all_apps_label);
        if (show_all_apps_label.getText().equals("Show All")) {
            show_all_apps_label.setText("Hide");
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) mDrawerGridView.getLayoutParams();
            ll.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            ll.width = LinearLayout.LayoutParams.MATCH_PARENT;
            mDrawerGridView.setLayoutParams(ll);
        } else {
            show_all_apps_label.setText("Show All");
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) mDrawerGridView.getLayoutParams();
            ll.height = (int) (230 * metrics.density);
            ll.width = LinearLayout.LayoutParams.MATCH_PARENT;
            mDrawerGridView.setLayoutParams(ll);
        }

    }

    public void doFavorite(AppObject app) {
        start_layout.setVisibility(View.GONE);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        favAppList.add(new AppObject(app.getName(), app.getPackageName(), app.getImage(), 1, app.getPosition()));
        mFavAppBottom.setAdapter(new AppAdapter(mContext, favAppList));
    }

    public void notifyAdapter(int pos, AppObject app) {
        if (favAppList.size() > 1) {
            installedAppList.get(app.getPosition()).setIsFavorite(0);
            favAppList.remove(pos);
            Objects.requireNonNull(mFavAppBottom.getAdapter()).notifyItemRemoved(pos);
        } else {
            installedAppList.get(app.getPosition()).setIsFavorite(0);
            favAppList = new ArrayList<>();
            mFavAppBottom.setAdapter(new AppAdapter(mContext, favAppList));
        }
    }

}


//    private void collapseDrawer() {
//        mDrawerGridView.setY(0);
//        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//    }

