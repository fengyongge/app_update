package com.zzti.fengyongge.appupdatedemo;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private File file;
    private View update_item;
    private TextView confirm_BT;
    private TextView cancel_BT;
    private TextView msg_TV;
    AlertDialog downloadalert;
    private String  message, download_url;
    int code;
    LayoutInflater inflater1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflater1= LayoutInflater.from(MainActivity.this);
        //由后台返回参数
        code=2;
        message="新版本更新";
        download_url="http://www.baidu.com";

        if (getVersionCode() < code) {
            if (downloadalert == null || !downloadalert.isShowing()) {
                showUpdateDialog(message, download_url);
            }
        } else {
            Toast.makeText(MainActivity.this, "暂无新版本", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 获取版本号(内部识别号)
     */
    public int getVersionCode() {
        try {
            PackageInfo pi = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 创建更新提醒对话框
     */
    private void showUpdateDialog(String message, final String download_url) {
        update_item = inflater1.inflate(R.layout.update_item, null);
        msg_TV = (TextView) update_item.findViewById(R.id.msg_TV);
        confirm_BT = (TextView) update_item.findViewById(R.id.confirm_BT);
        cancel_BT = (TextView) update_item.findViewById(R.id.cancel_BT);
        msg_TV.setText(Html.fromHtml(message));
        msg_TV.setMovementMethod(ScrollingMovementMethod.getInstance());// 滚动
        downloadalert = new AlertDialog.Builder(MainActivity.this).setView(update_item).create();
        downloadalert.show();
        confirm_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                confirm_BT.setClickable(false);
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // 下载新的apk 替换安装
                    file = new File(Environment.getExternalStorageDirectory(), "demo.apk");
                } else {
                    file = new File("/data/data/com.zzti.fengyongge.appupdatedemo/temp", "demo.apk");
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                }

                msg_TV.setText("下载进度:0%");
                FinalHttp finalHttp = new FinalHttp();
                finalHttp.download(download_url, file.getAbsolutePath(), new AjaxCallBack<File>() {
                    @Override
                    public void onFailure(Throwable t, String strMsg) {
                        super.onFailure(t, strMsg);
                        Toast.makeText(MainActivity.this.getApplicationContext(),
                                "下载失败", Toast.LENGTH_SHORT).show();
                        // loadMainUI();
                        downloadalert.dismiss();
                        if (t != null)
                            t.printStackTrace();
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        super.onLoading(count, current);
                        int process = (int) (current * 100 / count);
                        msg_TV.setText("下载进度:" + process + "%");
                        msg_TV.setGravity(Gravity.CENTER);
                    }

                    @Override
                    public void onSuccess(File t) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setDataAndType(Uri.parse("file://" + file.toString()),
                                "application/vnd.android.package-archive");
                        startActivity(i);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        confirm_BT.setClickable(true);
                        downloadalert.dismiss();
                        super.onSuccess(t);
                    }
                });

            }
        });
        cancel_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                downloadalert.dismiss();
            }
        });
    }
}
