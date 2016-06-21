package com.umeng.soexample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");


        ShareUtils.Builder builder = new ShareUtils.Builder(MainActivity.this);
        builder.shareList(new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE})
                .customPlatform(new String[]{"app_name", "app_name", "ic_launcher", "ic_launcher"});
        final ShareUtils shareUtils = builder.build();
        shareUtils.setCustomPlatClickListener(new ShareUtils.CustomPlatformClickListener() {
            @Override
            public void onClick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                Toast.makeText(MainActivity.this, "costom platform", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.showShareBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareUtils.showShareBoard();
            }
        });


    }
}
