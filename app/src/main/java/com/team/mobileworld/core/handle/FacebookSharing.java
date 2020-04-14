package com.team.mobileworld.core.handle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

public class FacebookSharing {

    /**
     * Chia se 1 duong link
     * @param queto
     * @param url
     * @param activity
     */
    public static void shareLink(String queto, String url, Activity activity)
    {
        //Thiet lap 1 dialog hien thi thong tin chia se
        ShareDialog share = new ShareDialog(activity);

        if (share.canShow(ShareLinkContent.class)) {
            //Thiet lap 1 object chua doi tuong hien thi
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setQuote(queto)
                    .setContentUrl(Uri.parse(url))
                    .build();
            share.show(linkContent);
        } else
            Toast.makeText(activity, "Sản phẩm chưa được chia sẻ", Toast.LENGTH_SHORT).show();
    }

    /**
     * Chia se 1 hay nhieu buc anh
     */
    public static void shareImage(Bitmap bitmap, String caption, Activity activity){
        //Thiet lap 1 man hinh share
        ShareDialog shareDialog = new ShareDialog(activity);

        //Thiet lap noi dung share
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .setCaption(caption)
                .build();

        SharePhotoContent share = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build();
        shareDialog.show(share);
    }

    /**
     * Chia se 1 video, hay bai hat
     */

    public static void shareVideo(Uri uri, String title, String decription, Activity activity)
    {
        ShareDialog shareDialog = new ShareDialog(activity);

        ShareVideo shareVideo = new ShareVideo.Builder()
                .setLocalUrl(uri)
                .build();

        ShareVideoContent shareContent = new ShareVideoContent.Builder()
                .setVideo(shareVideo)
                .setContentDescription(decription)
                .setContentTitle(title)
                .build();

        shareDialog.show(shareContent);
    }


}
