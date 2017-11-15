package com.example.chooseimage.chooseimage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 选择图片或拍照工具
 */
public class ChoosePicUtil {
    private Context context;
    private Uri imageUri;
    private Uri imageCropUri;
    public static final int CODE_CAMERA_REQUEST = 0;
    public static final int CODE_CHOOSE_REQUEST = 1;
    public static final int CODE_CAMERA_CROP_REQUEST = 2;

    public ChoosePicUtil(Context context, Uri imageUri, Uri imageCropUri){
        this.context = context;
        this.imageUri = imageUri;
        this.imageCropUri = imageCropUri;
    }

    /**
     * 拍照
     * @param resultCode
     */
    public void takePic(int resultCode){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ((Activity)context).startActivityForResult(intent, resultCode);
    }

    /**
     * 裁剪图片
     * @param uri
     */
    public void cropImg(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 700);
        intent.putExtra("outputY", 700);
        intent.putExtra("return-data", false);
        intent.putExtra("scale",true);
        intent.putExtra("scaleUpIfNeeded", true);   //防止出现黑边框
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ((Activity)context).startActivityForResult(intent, CODE_CAMERA_CROP_REQUEST);
    }

    public void choosePic(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        ((Activity)context).startActivityForResult(intent, CODE_CHOOSE_REQUEST);
    }

    public String getRealFilePath(final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
