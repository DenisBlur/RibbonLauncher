package com.example.ribbonlauncher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;

import static com.example.ribbonlauncher.MainActivity.UNINSTALL_POS_ALL_APPS;
import static com.example.ribbonlauncher.MainActivity.UNINSTALL_POS_FAV_APPS;

public class BottomSheetAppInfo extends BottomSheetDialogFragment {

    private Context mContext;
    private AppObject appObject;
    private int pos;

    BottomSheetAppInfo(Context mContext, AppObject appObject, int pos) {
        this.mContext = mContext;
        this.appObject = appObject;
        this.pos = pos;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_app_info, container, false);

        ImageView mImageApp = view.findViewById(R.id.mImageApp);
        TextView mLabelApp = view.findViewById(R.id.mLabelApp);
        TextView mAppPackage = view.findViewById(R.id.appPackage);
        MaterialCardView btnUnPinApp = view.findViewById(R.id.btnUnPinApp);
        MaterialCardView btnDeleteApp = view.findViewById(R.id.btnDeleteApp);

        mImageApp.setImageDrawable(appObject.getImage());
        mLabelApp.setText(appObject.getName());
        mAppPackage.setText(appObject.getPackageName());
        btnUnPinApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mContext).notifyAdapter(pos, appObject);
                dismiss();
            }
        });
        btnDeleteApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UNINSTALL_POS_ALL_APPS = appObject.getPosition();
                UNINSTALL_POS_FAV_APPS = pos;
                Uri packageUri = Uri.parse("package:" + appObject.getPackageName());
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
                mContext.startActivity(uninstallIntent);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}




