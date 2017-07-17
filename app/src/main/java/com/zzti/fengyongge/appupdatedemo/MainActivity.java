package com.zzti.fengyongge.appupdatedemo;

import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.zzti.fengyongge.appupdatedemo.utils.AppUpdateUtils;


public class MainActivity extends AppCompatActivity {
    AlertDialog downloadalert;
    Button bt_1,bt_2;
    String download_url,message;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_1 = (Button) findViewById(R.id.bt_1);
        bt_2 = (Button) findViewById(R.id.bt_2);
        loadMore();

        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getVersionCode() < code) {
                    if (downloadalert == null || !downloadalert.isShowing()) {
                        AppUpdateUtils appUpdateUtils = new AppUpdateUtils();
                        appUpdateUtils.showUpdateDialog(MainActivity.this,message, download_url,false);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "暂无新版本", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getVersionCode() < code) {
                    if (downloadalert == null || !downloadalert.isShowing()) {
                        AppUpdateUtils appUpdateUtils = new AppUpdateUtils();
                        appUpdateUtils.showMaterialUpdateDialog(MainActivity.this,message, download_url,false);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "暂无新版本", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void loadMore(){
         download_url="http://qiye.wxsdc.ediankai.com/static/feihe/apk/meeting.apk";
         code=2;
         message="•聊天消息：随时随地收发好友和群消息，一触即达。 <br>" +
                "•语音通话：两人、多人语音通话，高清畅聊。 <br>" +
                "•视频聊天：亲朋好友，想念不如相见。 <br>" +
                "•文件传输：手机、电脑多端互传，方便快捷。 <br>" +
                "•空间动态：更快获知好友动态，分享生活留住感动。 <br>" +
                "•厘 米 秀：换装扮、炫动作、偷胶囊，年轻人最爱的潮爆玩法。 <br>" +
                "•个性装扮：主题、名片、彩铃、气泡、挂件自由选。 <br>" +
                "•游戏中心：天天、全民等最热手游，根本停不下来。 <br>" +
                "•移动支付：话费充值、网购、转账收款，一应俱全。 <br>" +
                "•QQ看点：专为年轻人打造的个性化内容推荐平台。 ";
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




}
