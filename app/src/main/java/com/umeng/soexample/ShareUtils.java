package com.umeng.soexample;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

/**
 * 功能描述：友盟分享工具类
 * Created by cfp on 15-12-23.
 */
public class ShareUtils {


    private Context mContext;
    private String title;
    private String contents;
    private String targetUrl;
    private UMImage umImage;
    private SHARE_MEDIA[] displaylist;
    private String[] customPlatfromParams;
    private CustomPlatformClickListener listener;


    public static class Builder {

        private Context mContext;
        private String title;
        private String contents;
        private String targetUrl;
        private UMImage umImage;
        private SHARE_MEDIA[] displaylist;
        private String[] customPlatfromParams;


        public Builder(Activity context) {

            this.mContext = context;
        }

        /**
         * 这只分享标题
         *
         * @param title
         * @return
         */
        public Builder title(String title) {

            this.title = title;
            return this;
        }

        /**
         * 设置分享内容
         *
         * @param contents
         * @return
         */
        public Builder contents(String contents) {

            this.contents = contents;
            return this;
        }

        /**
         * 设置分享链接
         *
         * @param targetUrl
         * @return
         */
        public Builder targetUrl(String targetUrl) {

            this.targetUrl = targetUrl;
            return this;
        }

        /**
         * 分享图标的远程url
         *
         * @param imageUrl
         * @return
         */
        public Builder imageUrl(String imageUrl) {

            if (!TextUtils.isEmpty(imageUrl)) {

                this.umImage = new UMImage(mContext, imageUrl);

            }
            return this;
        }

        /**
         * 使用本地分享图标
         *
         * @param imageId
         * @return
         */
        public Builder imageUrl(int imageId) {
            if (imageId != 0) {

                this.umImage = new UMImage(mContext, imageId);
            }
            return this;
        }

        /**
         * 设置分享平台
         *
         * @param displaylist
         * @return
         */
        public Builder shareList(SHARE_MEDIA[] displaylist) {

            if (displaylist == null) {
                throw new NullPointerException("displaylist is null...");
            }

            this.displaylist = displaylist;

            return this;
        }

        /**
         * 添加自定义平台，现在只支持添加一个
         *
         * @param customPlatfromParams
         * @return
         */
        public Builder customPlatform(String[] customPlatfromParams) {

            this.customPlatfromParams = customPlatfromParams;

            return this;
        }

        public ShareUtils build() {

            return new ShareUtils(this);
        }


    }


    private ShareUtils(Builder builder) {
        this.mContext = builder.mContext;
        this.contents = builder.contents;
        this.targetUrl = builder.targetUrl;
        this.title = builder.title;
        this.umImage = builder.umImage;
        this.displaylist = builder.displaylist;
        this.customPlatfromParams = builder.customPlatfromParams;

    }

    public void showShareBoard() {

        if (displaylist == null) {
            throw new NullPointerException("displaylist is null...");
        }
        ShareAction shareAction = new ShareAction((Activity) mContext).setDisplayList(displaylist)
                .withText(contents)
                .withMedia(umImage)
                .setShareboardclickCallback(shareBoardlistener);
        if (customPlatfromParams != null) {
            shareAction.addButton(customPlatfromParams[0], customPlatfromParams[1], customPlatfromParams[2], customPlatfromParams[3]);
        }
        shareAction.open();


    }

    /**
     * 给自定义platform设置点击事件
     * @param listener
     */
    public void setCustomPlatClickListener(CustomPlatformClickListener listener) {

        this.listener = listener;
    }

    /**
     * 分享回调
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mContext, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

            if (customPlatfromParams != null && snsPlatform.mKeyword.equals(customPlatfromParams[1])) {
                listener.onClick(snsPlatform, share_media);
            } else {

                new ShareAction((Activity) mContext).setPlatform(share_media).setCallback(umShareListener)
                        .withText("多平台分享")
                        .share();
            }


        }
    };

    /**
     * 自定义监听接口
     */
    public interface CustomPlatformClickListener {

        public void onClick(SnsPlatform snsPlatform, SHARE_MEDIA share_media);
    }


}
