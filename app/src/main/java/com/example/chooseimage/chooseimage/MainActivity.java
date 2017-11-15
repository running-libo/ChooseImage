package com.example.chooseimage.chooseimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private View rootView;
    private String[] strs = {"拍照","相册选择"};
    private ChoosePicUtil choosePicUtil;
    private String picSavePath = Environment.getExternalStorageDirectory().getPath() + "/ChooseImage";
    private Uri imageUri,imageCropUri;
    private ImageView ivAvator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main,null);
        setContentView(rootView);

        init();
    }

    private void init() {
        File file = new File(picSavePath +  "/avator.jpg");
        imageUri = Uri.fromFile(file);
        File cropFile = new File(picSavePath + "/avator_crop.jpg");
        imageCropUri = Uri.fromFile(cropFile);
        choosePicUtil = new ChoosePicUtil(this,imageUri,imageCropUri);
        ivAvator = (ImageView) findViewById(R.id.iv_avator);

        ivAvator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupwindow();
            }
        });
    }

    private void showPopupwindow(){
        DoubleSelectPopupWindow popupWindow = new DoubleSelectPopupWindow(this);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
        popupWindow.init("头像选择",strs);
        popupWindow.setOnSelectListener(new DoubleSelectPopupWindow.ICallIntListener() {
            @Override
            public void callBack(int pos) {
                if(pos == 0){
                    choosePicUtil.takePic(ChoosePicUtil.CODE_CAMERA_REQUEST);
                }else{
                    choosePicUtil.choosePic();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case ChoosePicUtil.CODE_CAMERA_REQUEST:
                    choosePicUtil.cropImg(imageUri);
                    break;
                case ChoosePicUtil.CODE_CAMERA_CROP_REQUEST:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        //获取bitmap
                        Bitmap bitmap = BitmapFactory.decodeFile(choosePicUtil.getRealFilePath(imageCropUri));
                        ivAvator.setImageBitmap(bitmap);
                    }
                    break;
                case ChoosePicUtil.CODE_CHOOSE_REQUEST:
                    choosePicUtil.cropImg(data.getData());
                    break;
            }
        }
    }

}
