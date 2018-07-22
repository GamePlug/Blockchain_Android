package com.leichao.util.permission;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.leichao.util.statusbar.StatusBarUtil;

import java.util.ArrayList;

public class PermissionActivity extends Activity {

    public static final String PERMISSIONS = "permissions";
    public static final int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.transparent));
        ArrayList<String> permissions = getIntent().getStringArrayListExtra(PERMISSIONS);
        if (permissions != null && permissions.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), REQUEST_CODE);
        } else {
            result();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            result();
        }
    }

    private void result() {
        PermissionManager.getInstance().result(this);
        finish();
    }

}
