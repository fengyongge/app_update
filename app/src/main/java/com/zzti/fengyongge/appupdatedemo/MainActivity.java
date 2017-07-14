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

/**
 * @author fengyongge
 * @Description
 * @date 2017/5/16
 */
public class MainActivity extends AppCompatActivity {
    AlertDialog downloadalert;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = (Button) findViewById(R.id.bt);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //由后台返回参数
                 String download_url="http://qiye.wxsdc.ediankai.com/static/feihe/apk/meeting.apk";
                int code=2;
                String message="•聊天消息：随时随地收发好友和群消息，一触即达。 \n" +
                        "•语音通话：两人、多人语音通话，高清畅聊。 \n" +
                        "•视频聊天：亲朋好友，想念不如相见。 \n" +
                        "•文件传输：手机、电脑多端互传，方便快捷。 \n" +
                        "•空间动态：更快获知好友动态，分享生活留住感动。 \n" +
                        "•厘 米 秀：换装扮、炫动作、偷胶囊，年轻人最爱的潮爆玩法。 \n" +
                        "•个性装扮：主题、名片、彩铃、气泡、挂件自由选。 \n" +
                        "•游戏中心：天天、全民等最热手游，根本停不下来。 \n" +
                        "•移动支付：话费充值、网购、转账收款，一应俱全。 \n" +
                        "•QQ看点：专为年轻人打造的个性化内容推荐平台。 ";

                if (getVersionCode() < code) {
                    if (downloadalert == null || !downloadalert.isShowing()) {

                        AppUpdateUtils appUpdateUtils = new AppUpdateUtils();
//                        appUpdateUtils.showUpdateDialog(MainActivity.this,message, download_url,false);
                        appUpdateUtils.showMaterialUpdateDialog(MainActivity.this,message, download_url,false);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "暂无新版本", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
