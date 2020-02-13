package com.example.ribbonlauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MainActivity extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        float radius = 25f;
        View decorView = getWindow().getDecorView();
        mContext = MainActivity.this;

        BlurView blurView = findViewById(R.id.blurView);

        Drawable windowBackground = decorView.getBackground();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(false);

        initializeDrawer();

    }

    List<AppObject> installedAppList = new ArrayList<>();
    private void initializeDrawer() {
        View mBottomSheet = findViewById(R.id.bottomSheet);
        final RecyclerView mDrawerGridView = findViewById(R.id.drawerGrid);
        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(190);

        installedAppList = getInstalledAppList();
        mDrawerGridView.setAdapter(new AppAdapter(mContext, installedAppList));

    }

    private List<AppObject> getInstalledAppList() {

        List<AppObject> list = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> untreatedAppList = mContext.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo untreaterApp: untreatedAppList) {
            String appName = untreaterApp.activityInfo.loadLabel(getPackageManager()).toString();
            String appPackageName = untreaterApp.activityInfo.packageName;
            Drawable appImage = untreaterApp.activityInfo.loadIcon(getPackageManager());

            AppObject app = new AppObject(appName, appPackageName, appImage);
            if (!list.contains(app)){
                list.add(app);
            }
        }
        return list;
    }
}
