package org.unimelb.itime.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Qiushuo Huang on 2017/2/25.
 */

public class CameraActivity extends AppCompatActivity {
    /** 拍照时存储拍照结果的临时文件 */
    private File mTmpFile;
    public final static int REQUEST_CAMERA = 1111;
    public final static String KEY_RESULT = "camera_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showCamera();
    }

    /**
     * 选择相机
     */
    private void showCamera() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager()) != null){
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
//            mTmpFile = OtherUtils.createFile(getApplicationContext());
            if (existSDCard()) {
                mTmpFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
            } else {
                mTmpFile = Environment.getDataDirectory();
            }
            mTmpFile = createFile(mTmpFile, "IMG_", ".jpg");
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }else{
            Toast.makeText(getApplicationContext(),
                    "No camera found", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean existSDCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    /** 根据系统时间、前缀、后缀产生一个文件 */
    private File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory())
            folder.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 相机拍照完成后，返回图片路径
        if(requestCode == REQUEST_CAMERA){
            if(resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(KEY_RESULT, mTmpFile.getAbsolutePath());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }else{
                if(mTmpFile != null && mTmpFile.exists()){
                    mTmpFile.delete();
                }
                finish();
            }
        }
    }
}
