package com.example.cy.cody_.Closet;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cy.cody_.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera camera;
    boolean previewing = false;
    LayoutInflater controlInflater = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView)findViewById(R.id.Camera_SurfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.shutter_button, null);
        ViewGroup.LayoutParams layoutParamsControl = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        this.addContentView(viewControl, layoutParamsControl);  // shutter_button.xml을 전체화면에 overlay

        Button buttonShutter = (Button)findViewById(R.id.Shutter_Button);  // 촬영 버튼 클릭 시
        buttonShutter.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                camera.takePicture(myShutterCallback,myPictureCallback_RAW,myPictureCallback_JPG);  // 사진이 찍힘!
            }
        });

        LinearLayout Background = (LinearLayout) findViewById(R.id.Shutter_Background); // 초점을 맞추기 위해 뒤 배경을 클릭 시
        Background.setOnClickListener(new LinearLayout.OnClickListener(){

            @Override
            public void onClick(View view) {
                camera.autoFocus(myAutoFocusCallback); // 자동으로 초점이 맞춰 진다
            }
        });
    }

    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {

        }
    };

    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    Camera.PictureCallback myPictureCallback_RAW = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {

        }
    };

    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {

            int w = camera.getParameters().getPictureSize().width;
            int h = camera.getParameters().getPictureSize().height;
//
//            int w = 1280;
//            int h = 720;


            int orientation = setCameraDisplayOrientation(CameraActivity.this, Camera.CameraInfo.CAMERA_FACING_BACK, camera);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            bitmap = Bitmap.createBitmap(bitmap, 0,0,w,h,matrix, true);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);

            Uri uriTarget = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());

            byte[] currentData = stream.toByteArray();


            OutputStream imageFileOS;
            try{
                imageFileOS = getContentResolver().openOutputStream(uriTarget);
                imageFileOS.write(currentData);
                imageFileOS.flush();
                imageFileOS.close();

                Toast.makeText(CameraActivity.this, "Image saved" + uriTarget.toString(), Toast.LENGTH_SHORT).show();
                Log.v("User's Log in Camera",uriTarget.toString());

            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.putExtra("result",uriTarget.toString());
            setResult(RESULT_OK,intent);

            finish();
        }
    };

    public static int setCameraDisplayOrientation(Activity activity, int cameraID, Camera camera){
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraID, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation){
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        }
        else {  // back_facing  후면? 전면?
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();

        Camera.Parameters parameters= camera.getParameters();
        parameters.setPreviewSize(1280,720);  // 화면에 보여지는 크기
        parameters.setPictureSize(1280,720);  // 저장되는 크기
        camera.setParameters(parameters);

        camera.setDisplayOrientation(90);  // 보여지는 화면을 90도로 회전

//        // 카메라의 회전이 가로/세로일때 화면을 설정한다.  // 실험 안해봄
//        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
//            parameters.set("orientation", "portrait");
//            mCamera.setDisplayOrientation(90);
//            parameters.setRotation(90);
//        } else {
//            parameters.set("orientation", "landscape");
//            mCamera.setDisplayOrientation(0);
//            parameters.setRotation(0);
//        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(previewing){
            camera.stopPreview();
            previewing = false;
        }
        if(camera != null){
            try{
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }
}
