package com.zzti.fengyongge.appupdatedemo.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zzti.fengyongge.appupdatedemo.DownLoadService;
import com.zzti.fengyongge.appupdatedemo.R;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * @author fengyongge
 * @Description
 * @date 2017/5/16 0016 下午 6:13
 */
public class AppUpdateUtils {

     File file;
     View update_item;
     TextView tvConfirm;
     TextView tvCancle;
     TextView tvMessage;
     AlertDialog downloadalert;
     Context context;
    MaterialDialog mMaterialDialog;


    private  File fileName= Environment.getExternalStorageDirectory();//目标文件存储的文件夹路径
    private String destFileName = "test.apk";//目标文件存储的文件名

    /**
     * 创建更新提醒对话框
     * @param context
     * @param message
     * @param download_url
     * @param isForceUpdate
     */
    public void showUpdateDialog(final Context context, String message, final String download_url, boolean isForceUpdate) {

        this.context=context;
        update_item = LayoutInflater.from(context).inflate(R.layout.update_item, null);
        tvMessage = (TextView) update_item.findViewById(R.id.tvMessage);
        tvConfirm = (TextView) update_item.findViewById(R.id.tvConfirm);
        tvCancle = (TextView) update_item.findViewById(R.id.tvCancle);
        tvMessage.setText(Html.fromHtml(message));
        tvMessage.setMovementMethod(ScrollingMovementMethod.getInstance());// 滚动

        downloadalert = new AlertDialog.Builder(context).setView(update_item).create();
        downloadalert.show();

        if(isForceUpdate){
            tvCancle.setVisibility(View.GONE);//不显示取消
            downloadalert.setCancelable(false);//强制更新
        }else{
            tvCancle.setVisibility(View.VISIBLE);

        }

        tvConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                tvConfirm.setClickable(false);
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    file = new File(fileName,destFileName);

                } else {
                    file = new File("/data/data/com.diankai.ecosphere/temp", "test.apk");

                     if (!file.exists()) {
                        file.mkdirs();
                     }

                }


                tvMessage.setText("下载进度:0%");

                FinalHttp finalHttp = new FinalHttp();

                finalHttp.download(download_url, file.getAbsolutePath(), new AjaxCallBack<File>() {
                    @Override
                    public void onFailure(Throwable t, String strMsg) {
                        super.onFailure(t, strMsg);

                        Toast.makeText(context, "下载失败"+strMsg, Toast.LENGTH_SHORT).show();

                        downloadalert.dismiss();
                        if (t != null)
                            t.printStackTrace();
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        super.onLoading(count, current);
                        int process = (int) (current * 100 / count);
                        tvMessage.setText("下载进度:" + process + "%");
                        tvMessage.setGravity(Gravity.CENTER);
                    }

                    @Override
                    public void onSuccess(File t) {

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
                        context.startActivity(i);
                        Process.killProcess(Process.myPid());
                        tvConfirm.setClickable(true);
                        downloadalert.dismiss();
                        super.onSuccess(t);
                    }
                });

            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                downloadalert.dismiss();
            }
        });
    }



    /**
     * 创建更新提醒对话框
     * @param context
     * @param message
     * @param download_url
     * @param isForceUpdate
     */
    public void showMaterialUpdateDialog(final Context context, String message, final String download_url, boolean isForceUpdate) {

        this.context = context;

        mMaterialDialog = new MaterialDialog(context)
                .setTitle("应用更新")
                .setMessage(Html.fromHtml(message))
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, DownLoadService.class);
                        context.startService(intent);
                        mMaterialDialog.dismiss();

                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();

                    }
                });

        mMaterialDialog.show();
    }

}
