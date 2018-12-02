package com.example.cy.cody_.Closet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cy.cody_.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{
//public class CameraActivity extends AppCompatActivity{

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera camera;
    boolean previewing = false;
    LayoutInflater controlInflater = null;
    Camera.Parameters parameters;
    Bitmap bbitmap;
    private Feature feature;
    String sort= "0";

    HttpTransport httpTransport;
    JsonFactory jsonFactory;
    Vision vision;
    private static final String CLOUD_VISION_API_KEY = "AIzaSyBFh7NKwYFlYXEuXXwjgqziGf2Q0suxlkU";
    private String[] visionAPI = new String[]{"LABEL_DETECTION"};
    private String api = "LABEL_DETECTION";
    static String[] Top = {"sleeve", "t shirt","long sleeved t shirt","sweater","sleeveless shirt","suit"};
    static String[] Bottom = {"jeans", "denim","shorts"};
    static String[] Outer = {"blazer", "jacket","shorts","cardigan"};

    String User_Email;
    String User_Name;

    Timer timer;
    TimerTask tt;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);



        /**********************    메인에서 로그인이 되어있을때 값을 받아옴    *******************/
        Intent GetIntent = getIntent();
        User_Email = GetIntent.getExtras().getString("User_Email");
        User_Name = GetIntent.getExtras().getString("User_Name");
        /*************************************************************************************/

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//
//        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView)findViewById(R.id.Camera_SurfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


//        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
//
//        int width = dm.widthPixels;
//
//        int height = dm.heightPixels;
//
//        Log.v("JIN_w",String.valueOf(width));
//        Log.v("JIN_h",String.valueOf(height));




        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.shutter_button, null);
        ViewGroup.LayoutParams layoutParamsControl = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
//
//        Button btn = new Button(viewControl.getContext());
//        ViewEx viewEx = new ViewEx(viewControl.getContext());
//        addContentView(viewEx, new LinearLayout.LayoutParams(50,50));

        this.addContentView(viewControl, layoutParamsControl);  // shutter_button.xml을 전체화면에 overlay



//        controlInflater = LayoutInflater.from(getBaseContext());
//        View viewControl = controlInflater.inflate(R.layout.shutter_button, null);
//
//        Log.v("JIN_w",String.valueOf(viewControl.getWidth() ));
//        Log.v("JIN_h",String.valueOf(viewControl.getHeight() ));
//        ViewGroup.LayoutParams layoutParamsControl = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
//        this.addContentView(viewControl, layoutParamsControl);  // shutter_button.xml을 전체화면에 overlay







//        ViewEx viewEx = new ViewEx(this);
////        this.setContentView(surfaceView);
//        addContentView(viewEx, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

//        Preview mPreview = new Preview(this);
//        ViewEx viewEx = new ViewEx(this);
//
//        setContentView(mPreview);
//        addContentView(viewEx, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        httpTransport = AndroidHttp.newCompatibleTransport();
        jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer = new VisionRequestInitializer(CLOUD_VISION_API_KEY);

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        vision = builder.build();

        feature = new Feature();
        feature.setMaxResults(10);
        feature.setType(api);

        timer = new Timer();
        tt = timerTaskMake();
        timer.schedule(tt, 1000, 4500);   // 1초 뒤에 시작해서 4.5초마다 계속 반복


        //callCloudVision();

//        Button buttonShutter = (Button)findViewById(R.id.Shutter_Button);  // 촬영 버튼 클릭 시
//        buttonShutter.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//
//                camera.takePicture(null,myPictureCallback_RAW,myPictureCallback_JPG);  // 사진이 찍힘!
//
//            }
//        });

//        LinearLayout Background = (LinearLayout) findViewById(R.id.Shutter_Background); // 초점을 맞추기 위해 뒤 배경을 클릭 시
//        Background.setOnClickListener(new LinearLayout.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                camera.autoFocus(myAutoFocusCallback); // 자동으로 초점이 맞춰 진다
//            }
//        });
    }

//    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {
//        @Override
//        public void onAutoFocus(boolean b, Camera camera) {
//            Log.v("JIN,Auto", "123");
//        }
//    };

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

            int orientation = setCameraDisplayOrientation(CameraActivity.this, Camera.CameraInfo.CAMERA_FACING_BACK, camera);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bbitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            bbitmap = Bitmap.createBitmap(bbitmap, 0, 0, w, h, matrix, true);  // 현재 프리뷰 상태의 화면을 비트맵화 시켜서 비전 api에 넘긴다

            callCloudVision(bbitmap, feature);

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

        parameters= camera.getParameters();
        parameters.setPreviewSize(640,480);  // 화면에 보여지는 크기
        parameters.setPictureSize(640,480);  // 저장되는 크기
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

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


    @SuppressLint("StaticFieldLeak")
    private void callCloudVision(final Bitmap bitmap, final Feature feature) {
        //imageUploadProgress.setVisibility(View.VISIBLE);
        final List<Feature> featureList = new ArrayList<>();

        featureList.add(feature);

        final List<AnnotateImageRequest> annotateImageRequests = new ArrayList<>();

        AnnotateImageRequest annotateImageReq = new AnnotateImageRequest();
        annotateImageReq.setFeatures(featureList);
        annotateImageReq.setImage(getImageEncodeImage(bitmap));
        annotateImageRequests.add(annotateImageReq);


        new AsyncTask<Object, Void, String>() {

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Object... params) {
                try {

                    tt.cancel();

                    Log.v("JIN", "api 실행중");
                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(annotateImageRequests);

                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse response = annotateRequest.execute();

                    return convertResponseToString(response);
                } catch (GoogleJsonResponseException e) {

                    Log.v("JIN", "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.v("JIN", "failed to make API request because of other IOException " + e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                //visionAPIData.setText(result);
                //imageUploadProgress.setVisibility(View.INVISIBLE);
                Log.v("JIN_result",result);

                if(sort !=  "0"){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bbitmap.compress(Bitmap.CompressFormat. JPEG, 80, stream);

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

                    //Toast.makeText(CameraActivity.this, uriTarget.toString(), Toast.LENGTH_LONG).show();

                    Intent Return_Intent = new Intent();
                    Return_Intent.putExtra("sort", sort);
                    Return_Intent.putExtra("uriTarget", uriTarget.toString());
                    setResult(RESULT_OK, Return_Intent);
                    finish();
                }
                else{
                    tt = timerTaskMake();
                    timer.schedule(tt, 0);
                }
            }
        }.execute();
    }

    @NonNull
    private Image getImageEncodeImage(Bitmap bitmap) {
        Image base64EncodedImage = new Image();
        // Convierte el bitmap a JPEG
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Base64 encode the JPEG
        base64EncodedImage.encodeContent(imageBytes);
        return base64EncodedImage;
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {

        AnnotateImageResponse imageResponses = response.getResponses().get(0);

        List<EntityAnnotation> entityAnnotations;

        String message = "";
        switch (api) {
            case "LABEL_DETECTION":
                entityAnnotations = imageResponses.getLabelAnnotations();
                //Log.v("JIN_imageResponse",entityAnnotations.toString());
                message = formatAnnotation(entityAnnotations);
                break;
        }
        return message;
    }

    private final String formatAnnotation(List<EntityAnnotation> entityAnnotation) {
        String message = "\n";
        String rename = "null.jpg";

        if (entityAnnotation != null) {
            for (EntityAnnotation entity : entityAnnotation) {  // 점수가 높은 것 부터 뽑아옴
                message = message + "  " + entity.getDescription() + " " + entity.getScore();
                message += "\n";

                boolean check = false;
                if(Float.parseFloat(String.valueOf(entity.getScore())) > 0.70){
                    if(check == false){
                        for(int i=0; i<Top.length; i++){
                            if(Top[i].equals(entity.getDescription())){
                                check = true;
                                 sort = "Top"; // php를 이용하여 상의란 것을 전달
                                Log.v("JIN", "kbkjbk "+Top[i]);
                            }
                        }
                    }
                    if(check == false){
                        for(int i=0; i<Bottom.length; i++){
                            if(Bottom[i].equals(entity.getDescription())){
                                check = true;
                                sort = "Bottom";
                                Log.v("JIN", "kbkjbk "+Bottom[i]);
                            }
                        }
                    }
                    if(check == false){
                        for(int i=0; i<Outer.length; i++) {
                            if (Outer[i].equals(entity.getDescription())) {
                                check = true;
                                sort = "Outer";
                                Log.v("JIN", "kbkjbk " + Outer[i]);
                            }
                        }
                    }

                    if(check != false){
                        break;
                    }
                }


            }
        } else {
            message = "No Encontrado";
        }
        return message;

    }

    public TimerTask timerTaskMake(){
        TimerTask tempTask = new TimerTask() {
            @Override
            public void run() {
                camera.takePicture(null,myPictureCallback_RAW,myPictureCallback_JPG);  // 사진이 찍힘!
            }
        };
        return tempTask;
    }
//
//    protected class ViewEx extends View{
//
//        public ViewEx(Context context) {
//            super(context);
//        }
//        public  void onDraw(Canvas canvas){
//            canvas.drawColor(Color.WHITE);
//            Paint myPaint = new Paint();
//            canvas.drawRect(10,10,10,10, myPaint);
//        }
//    }

//    class Preview extends SurfaceView implements SurfaceHolder.Callback{
//
//        public Preview(Context context) {
//            super(context);
//            surfaceHolder = getHolder();
//            surfaceHolder.addCallback(this);
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
//
//        @Override
//        public void surfaceCreated(SurfaceHolder surfaceHolder) {
//            camera = Camera.open();
//
//            parameters= camera.getParameters();
//            parameters.setPreviewSize(640,480);  // 화면에 보여지는 크기
//            parameters.setPictureSize(640,480);  // 저장되는 크기
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//
//            camera.setParameters(parameters);
//
//            camera.setDisplayOrientation(90);  // 보여지는 화면을 90도로 회전
//
////        // 카메라의 회전이 가로/세로일때 화면을 설정한다.  // 실험 안해봄
////        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
////            parameters.set("orientation", "portrait");
////            mCamera.setDisplayOrientation(90);
////            parameters.setRotation(90);
////        } else {
////            parameters.set("orientation", "landscape");
////            mCamera.setDisplayOrientation(0);
////            parameters.setRotation(0);
////        }
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//            if(camera != null){
//            try{
//                camera.setPreviewDisplay(surfaceHolder);
//                camera.startPreview();
//                previewing = true;
//            }
//            catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//        }
//    }

}
