package com.example.yaphet.m_camera;

import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    @BindView(R.id.open_camera)
    Button openCamera;
    @BindView(R.id.View_show)
    SurfaceView ViewShow;
    SurfaceHolder Mholder;
    private Camera Mcamera;
    private File file;
    //文件输出流
    private OutputStream os;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Mholder = ViewShow.getHolder();
        Mholder.addCallback(this);
        ViewShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Mcamera!=null) {
                    Mcamera.autoFocus(null);
                }
            }
        });
        Mholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //创建File对象，用于保存拍照后的图片
        String path = Environment.getExternalStorageDirectory() + "/m_camera" + System.currentTimeMillis() + ".jpg";
        file = new File(path);
    }
    @OnClick(R.id.open_camera)//拍照功能
    public void onViewClicked() {
        Camera.Parameters params = Mcamera.getParameters();
        params.setPictureFormat(ImageFormat.JPEG);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        params.setSceneMode(Camera.Parameters.SCENE_MODE_ACTION);
        Mcamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                Mcamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            os = new BufferedOutputStream(new FileOutputStream(file));
                            os.write(data);
                            os.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            try {
                                os.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        openPreView();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCamera();
    }

    private void openCamera() {
        if (Mcamera == null) {
            Mcamera = Camera.open(0);
        }
    }
    /**
     * 设置预览功能
     */
    private void openPreView() {
        Log.e("openPreView", "openPreView");
        try {
            if (Mcamera != null) {
                Mcamera.setPreviewDisplay(Mholder);
                Mcamera.startPreview();
                int degree = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 0 : 90;
                Mcamera.setDisplayOrientation(degree);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        realseCamera();
    }
    /**
     * 释放相机资源
     */
    private void realseCamera() {
        if (Mcamera != null) {
            Mcamera.setPreviewCallback(null);
            Mcamera.stopPreview();
            Mcamera.release();
            Mcamera = null;
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openPreView();
        Log.e("surfaceCreated", "surfaceCreated");
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) {
            return;
        }
        Log.e("surfaceChanged", "surfaceChanged");
        Mcamera.stopPreview();
        openPreView();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        realseCamera();
    }
}
