package com.leichao.biubiu.home;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.leichao.retrofit.example.TestHttp;
import com.leichao.util.AppUtil;
import com.leichao.util.KeyboardUtil;
import com.leichao.util.LogUtil;
import com.leichao.util.NetworkUtil;
import com.leichao.util.PermissionUtil;
import com.leichao.util.StatusBarUtil;
import com.leichao.util.ToastUtil;

import java.util.List;

/**
 * Created by leichao on 2018/1/5.
 */
public class MainActivity extends AppCompatActivity implements AppUtil.OnAppStatusListener, KeyboardUtil.OnKeyboardStatusListener, NetworkUtil.OnNetworkStatusListener {

    TextView textView;
    EditText etInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //StatusBarUtil.setBackgroundColor(this, Color.RED);
        StatusBarUtil.setFullTranslucent(this);
        KeyboardUtil.fixAndroidBug5497(this);

        textView = findViewById(R.id.main_hello);
        etInput = findViewById(R.id.main_edit);

        textView.setText("aaaaaaa");

        etInput.postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardUtil.showSoftInput(etInput);
            }
        }, 200);

        test();

        AppUtil.addStatusListener(this);
        KeyboardUtil.addStatusListener(this, this);
        NetworkUtil.addStatusListener(this);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show(R.string.app_name);
                KeyboardUtil.removeStatusListener(MainActivity.this, MainActivity.this);
                permission();
            }
        });

    }

    @Override
    public void onAppStatus(boolean isForeground) {
        LogUtil.e("AppStatus:" + isForeground);
    }

    @Override
    public void onKeyboardStatus(boolean isShow) {
        LogUtil.e("KeyboardStatus:" + isShow);
    }

    @Override
    public void onNetworkStatus(NetworkUtil.NetworkStatus status) {
        LogUtil.e("NetworkStatus:" + status.isAvailable + "---" + status.networkType + "---" + status.mobileType);
        textView.setText("NetworkStatus:" + status.isAvailable + "---" + status.networkType + "---" + status.mobileType);
    }

    @Override
    protected void onDestroy() {
        KeyboardUtil.fixSoftInputLeaks(this);
        super.onDestroy();
        AppUtil.removeStatusListener(this);
        KeyboardUtil.removeStatusListener(this, this);
        NetworkUtil.removeStatusListener(this);
    }

    private void permission() {
        PermissionUtil.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.OnPermissionResultListener() {
            @Override
            public void onPermissionResult(List<String> grantedList, List<String> deniedList) {
                if (grantedList.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //plugin(MainActivity.this);
                }
            }
        });
    }

    /*public static void plugin(Context context) {
        AssetManager asset = context.getAssets();
        //循环的读取asset下的文件，并且写入到SD卡
        FileOutputStream out = null;
        InputStream in=null;
        try {
            File sdDir = new File(Environment.getExternalStorageDirectory().toString()+"/"+"Plugins");
            if (!sdDir.exists() && !sdDir.mkdirs()) {
                return;// 文件夹不存在且创建失败则返回
            }
            String assetPath = "plugins/mula_travel.apk";
            File sdApk = new File(sdDir.getAbsolutePath(), assetPath.substring(
                    assetPath.lastIndexOf("/"), assetPath.length()));
            if (!sdApk.exists() && !sdApk.createNewFile()) {
                return;// 文件不存在且创建失败则返回
            }
            //将内容写入到文件中
            in=asset.open(assetPath);
            out= new FileOutputStream(sdApk);
            byte[] buffer = new byte[1024];
            int byteCount=0;
            while((byteCount=in.read(buffer))!=-1){
                out.write(buffer, 0, byteCount);
            }
            out.flush();
            // 安装插件
            DroidPluginBridge.INSTANCE.installPackage(sdApk.getAbsolutePath(), 0);
            // 启动插件
            PackageManager pm = context.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage("com.mula.travel");
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                //asset.close();// asset是系统的，不能close
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public void test() {
        TestHttp.test(this);
    }

}
