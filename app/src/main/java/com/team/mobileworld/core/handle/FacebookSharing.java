package com.team.mobileworld.core.handle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.team.mobileworld.R;
import com.team.mobileworld.activity.LoginActivity;

public class FacebookSharing {

    /**
     * Chia se 1 duong link
     *
     * @param queto
     * @param url
     * @param activity
     */
    public static void shareLink(String queto, String url, Activity activity) {
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

    public static SharePhoto createPhoto(Bitmap bitmap){
       return new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
    }

    /**
     * Chia se anh len facebook
     */
    public static void shareImage(Bitmap bitmap, String caption, Activity activity) {
        //Thiet lap 1 man hinh share
        ShareDialog dialog = new ShareDialog(activity);

        if (dialog.canShow(SharePhotoContent.class)) {
            //Thiet lap noi dung share
            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .setCaption(caption)
                    .build();

            //Thiet lap mot mau share
            SharePhotoContent share = new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build();

            dialog.show(share, ShareDialog.Mode.AUTOMATIC);
            LoginActivity.showToast(activity, "Chia sẻ thành công!");
        } else
            Toast.makeText(activity, "Sản phẩm chưa được chia sẻ", Toast.LENGTH_SHORT).show();
    }


    /**
     * CHia se mot noi dung video len facebook
     */
    public static void shareVideo(Uri uri, String title, String decription, Activity activity) {
        ShareDialog dialog = new ShareDialog(activity);

        if (dialog.canShow(SharePhotoContent.class)) {
            //thiet lap bo load
            ShareVideo shareVideo = new ShareVideo.Builder()
                    .setLocalUrl(uri)
                    .build();

            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(BitmapFactory.decodeResource(null,R.drawable.ic_video))
                    .build();

            //Thiet lap noi dung video
            ShareVideoContent shareContent = new ShareVideoContent.Builder()
                    .setVideo(shareVideo)
                    .setPreviewPhoto(createPhoto(BitmapFactory.decodeResource(null,R.drawable.ic_video)))
                    .setContentDescription(decription)
                    .setContentTitle(title)
                    .build();

            dialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);
            LoginActivity.showToast(activity, "Chia sẻ thành công!");
        } else
            Toast.makeText(activity, "Sản phẩm chưa được chia sẻ", Toast.LENGTH_SHORT).show();
    }


}
