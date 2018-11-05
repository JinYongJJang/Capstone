package com.example.cy.cody_;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.api.services.vision.v1.model.ColorInfo;
import com.google.api.services.vision.v1.model.DominantColorsAnnotation;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageProperties;
import com.google.api.services.vision.v1.model.SafeSearchAnnotation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClosetActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DrawerLayout mDrawerLayout; // 서랍 레이아웃
    private RecyclerView mRecyclerView;
    private ListViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button picture;
    private static ArrayList<item> itemArrayList;
    File file;

    Uri uri;
    private  static boolean signal = false;
    private static final String TAG = "JIN_ERR";
    private static final int DENIED_PERMISSION_CAMERA = 1200;
    private static final int PERMISSIONS_CAMERA = 1201;
    private static final String CLOUD_VISION_API_KEY = "AIzaSyBFh7NKwYFlYXEuXXwjgqziGf2Q0suxlkU";
    static String[] Top = {"sleeve", "t shirt","long sleeved t shirt","sweater","sleeveless shirt","suit"};
    static String[] Bottom = {"jeans", "denim","shorts"};
    static String[] Outer = {"blazer", "jacket","shorts","cardigan"};
    String dir;
    String FileName;
    ImageView visionAPIData;
    private Feature feature;
    private Bitmap bitmap;
    private String[] visionAPI = new String[]{"LABEL_DETECTION"};

    private String api = "LABEL_DETECTION";
    private String shirts = "shirt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        visionAPIData = (ImageView) findViewById(R.id.visionAPIData);
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                int hascameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(hascameraPermission == PackageManager.PERMISSION_GRANTED && hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED){

                }
                else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

                }
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);



        feature = new Feature();
        feature.setMaxResults(10);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, visionAPI);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        picture = findViewById(R.id.take_picture);
        picture.setOnClickListener(new View.OnClickListener() {   // 사진 찍기 버튼
            @Override
            public void onClick(View view) {
                takePictureFromCamera();
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);// 뒤로가기 버튼
        actionBar.setDisplayHomeAsUpEnabled(true); // 네비게이션 메뉴를 사용하기 위한 코드

        mDrawerLayout = findViewById(R.id.drawer_layout); // 네비게이션 메뉴 사용 객체

        NavigationView navigationView = findViewById(R.id.navigation_view);
        // 네비게이션 메뉴바 구동
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_attachment:
                        Intent MyInfo = new Intent(ClosetActivity.this, UserinfoActivity.class);
                        startActivity(MyInfo);
                        break;
                    case R.id.nav_sub_menu_item01:
                        Toast.makeText(ClosetActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_sub_menu_item02:
                        Toast.makeText(ClosetActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_sub_menu_item03:
                        Toast.makeText(ClosetActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_sub_menu_item04:
                        Toast.makeText(ClosetActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });

           // 리스트뷰
        itemArrayList = new ArrayList<>();
        //ArrayList에 값 추가하기
        itemArrayList.add(new item("상의",  R.drawable.top));
        itemArrayList.add(new item("하의",  R.drawable.bottom));
        itemArrayList.add(new item("아우터",  R.drawable.outer));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);//옵션
        //Linear layout manager 사용
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //어답터 세팅
        mAdapter = new ListViewAdapter(itemArrayList); //스트링 배열 데이터 인자로
        mRecyclerView.setAdapter(mAdapter);

    }
    public class item {
        String name;
        int photo;

        public item(String name, int photo) {
            this.name = name;
            this.photo = photo;
        }

        public String getName() {
            return name;
        }
        public int getPhoto() {
            return photo;
        }
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (checkPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//           // picture.setVisibility(View.VISIBLE);
//        } else {
//            //picture.setVisibility(View.INVISIBLE);
//            makeRequest(Manifest.permission.CAMERA);
//        }
//    }

//    private int checkPermission(String permission) {
//        return ContextCompat.checkSelfPermission(this, permission);
//    }
//
//    private void makeRequest(String permission) {
//        ActivityCompat.requestPermissions(this, new String[]{permission}, RECORD_REQUEST_CODE);
//    }

    public void takePictureFromCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAMERA_REQUEST_CODE);

//        Intent intent = new Intent(this, CameraActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent,3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {

        if(resultCode == RESULT_OK){
            //visionAPIData.setText(data.getStringExtra("result"));
            String a = data.getStringExtra("result");
            uri = Uri.parse(a);

            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader cursorLoder = new CursorLoader(this, uri,proj,null,null,null);
            Cursor cursor = cursorLoder.loadInBackground();

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            String Real_path = cursor.getString(column_index);  //    /storage/emulated/0/Pictures/1538545064763.jpg
            dir = Real_path.substring(0,29);
            FileName = Real_path.substring(29,Real_path.length());
            file = new File(dir,FileName);

            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);


                //imageView.setImageURI(uri);
                feature.setType(api);

                callCloudVision(bitmap,feature);
            }
            catch (Exception e){
                //Log.e("JIN_ERR",e.toString());
                e.printStackTrace();
            }


        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == RECORD_REQUEST_CODE) {
//            if (grantResults.length == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                finish();
//            } else {
//                //picture.setVisibility(View.VISIBLE);
//            }
//        }
//    }

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
            protected String doInBackground(Object... params) {
                try {
                    Log.v("JIN", "api 실행중");
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer = new VisionRequestInitializer(CLOUD_VISION_API_KEY);

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(annotateImageRequests);

                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse response = annotateRequest.execute();

                    return convertResponseToString(response);
                } catch (GoogleJsonResponseException e) {

                    Log.v(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.v(TAG, "failed to make API request because of other IOException " + e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                //visionAPIData.setText(result);
                //imageUploadProgress.setVisibility(View.INVISIBLE);
                Log.v("JIN",result);
            }
        }.execute();
    }

    @NonNull
    private Image getImageEncodeImage(Bitmap bitmap) {
        Image base64EncodedImage = new Image();
        // Convierte el bitmap a JPEG
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
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
//            case "LANDMARK_DETECTION":
//                entityAnnotations = imageResponses.getLandmarkAnnotations();
//                message = formatAnnotation(entityAnnotations);
//                break;
//            case "LOGO_DETECTION":
//                entityAnnotations = imageResponses.getLogoAnnotations();
//                message = formatAnnotation(entityAnnotations);
//                break;
//            case "SAFE_SEARCH_DETECTION":
//                SafeSearchAnnotation annotation = imageResponses.getSafeSearchAnnotation();
//                message = getImageAnnotation(annotation);
//                break;
//            case "IMAGE_PROPERTIES":
//                ImageProperties imageProperties = imageResponses.getImagePropertiesAnnotation();
//                message = getImageProperty(imageProperties);
//                break;
            case "LABEL_DETECTION":
                entityAnnotations = imageResponses.getLabelAnnotations();
                //Log.v("JIN_imageResponse",entityAnnotations.toString());
                message = formatAnnotation(entityAnnotations);
                break;
        }
        return message;
    }

    private String getImageAnnotation(SafeSearchAnnotation annotation) {
        return String.format("adult: %s\nmedical: %s\nspoofed: %s\nviolence: %s\n",
                annotation.getAdult(),
                annotation.getMedical(),
                annotation.getSpoof(),
                annotation.getViolence());
    }

    private String getImageProperty(ImageProperties imageProperties) {
        String message = "";
        DominantColorsAnnotation colors = imageProperties.getDominantColors();
        for (ColorInfo color : colors.getColors()) {
            message = message + "" + color.getPixelFraction() + " - " + color.getColor().getRed() + " - " + color.getColor().getGreen() + " - " + color.getColor().getBlue();
            message = message + "\n";
        }
        return message;
    }

    private String formatAnnotation(List<EntityAnnotation> entityAnnotation) {
        String message = "\n";
        int[] count = {0, 0, 0};

        String rename = "null.jpg";
        if (entityAnnotation != null) {
            for (EntityAnnotation entity : entityAnnotation) {
                message = message + "    " + entity.getDescription() + " " + entity.getScore();
                float per=0;
                if(Float.parseFloat(String.valueOf(entity.getScore())) > 0.70){
                    for(int i=0; i<Top.length; i++){
                        if(Top[i].equals(entity.getDescription())){
                            if(entity.getScore() > per){
                                rename = "_top.jpg";
                                per = entity.getScore();
                            }
                            Log.v("JIN", "kbkjbk "+Top[i]);

                        }
                    }
                    for(int i=0; i<Bottom.length; i++){
                        if(Bottom[i].equals(entity.getDescription())){
                            if(entity.getScore() > per){
                                rename = "_Bottom.jpg";
                                per = entity.getScore();
                            }
                            Log.v("JIN", "kbkjbk "+Bottom[i]);
                        }
                    }
                    for(int i=0; i<Outer.length; i++) {
                        if (Outer[i].equals(entity.getDescription())) {
                            if(entity.getScore() > per){
                                rename = "_Outer.jpg";
                                per = entity.getScore();
                            }
                            Log.v("JIN", "kbkjbk " + Outer[i]);
                        }
                    }
                }
                message += "\n";
                Arrays.sort(count);

            }
            String chang_name = file.getName();
            String result = chang_name.substring(0,chang_name.length()-4);
            File fileNew = new File(file.getParent(),result+rename);
            try {
                fileNew.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if( file.exists() ) {
                if (!file.renameTo(new File("/storage/emulated/0/Pictures/"+result+rename))) {
                    Log.v("JIN_ERRRRRRRRRRRR","이름 변경 에러 : " + file);
                }
            }
        } else {
            message = "No Encontrado";
        }
        return message;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        api = (String) adapterView.getItemAtPosition(i);
        feature.setType(api);
        if (bitmap != null)
            callCloudVision(bitmap, feature);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
