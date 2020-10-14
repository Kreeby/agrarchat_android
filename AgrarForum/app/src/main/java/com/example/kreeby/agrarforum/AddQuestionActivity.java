package com.example.kreeby.agrarforum;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddQuestionActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private static final int REQUEST_CODE = 3;
    String myToken = "";
    String imageName = "";
    private static final String TAG = "ss";
    String realPath = "";
    String hisId = "";
    EditText nameQuestion;
    EditText questionItself;
    String valueQuestionName;
    String valueQuestionItself;
    final int KeyGallery = 100, ReadExternalRequestCode = 200;
    Button post;
    Bitmap bm;
    Bitmap photo;
    File f;
    Spinner spinner;
    Button uploadImage;
    ArrayAdapter<CharSequence> adapter;
    ArrayList<String> categories = new ArrayList<String>();
    String picturePath = "";
    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    protected void onCreate(Bundle savedInstanceState) {

            askPermissions();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_question_activity);

            spinner = findViewById(R.id.categories_questions);
            adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);



            String id = getIntent().getStringExtra("ID");
            hisId += id;

            String token = getIntent().getStringExtra("TOKEN");

            myToken+= token;


            questionItself = (EditText) findViewById(R.id.question_itself);


            post = findViewById(R.id.post_question);
            post.setOnClickListener(this);
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
//                valueQuestionName = nameQuestion.getText().toString();
                    valueQuestionItself = questionItself.getText().toString();




                    runInBackround(valueQuestionItself, myToken, hisId, spinner.getSelectedItem().toString());



                }
            });





    }




    private void runInBackround(final String str2, final String str3, final String str4, final String str5){

        new Thread(new Runnable() {
            @Override
            public void run() {
                //method containing process logic.
                try {
//                    makeNetworkRequest("http://192.168.99.64:8000/addQuestion/", str1, str2, str3, str4, str5, new File(picturePath));
                    uploadImage(new File(picturePath), str2, str3, str4, str5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }





    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            } else {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (!filePathColumn.equals(null)) {
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        Log.d("RASHAD ", picturePath);
//                        if (!picturePath.equals("")) {
//                            try {
//                                uploadImage(new File(picturePath));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
                    }
                }
            }
        }
    }

    public void uploadImage(File file, String str2, String str3, String str4, String str5)
            throws IOException {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder requestBodyBuilder;
        RequestBody requestBody;
        String responseString = "";
        Log.d("FILE", "FILE NAME" + file.getName());
        requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM)

                .addFormDataPart("text", str2)
                .addFormDataPart("api_token", str3)
                .addFormDataPart("added_to", str4)
                .addFormDataPart("category", str5);

        if (file.length() != 0) {
            requestBodyBuilder
                    .addFormDataPart("image", file.getName(),
                            RequestBody.create(MediaType.parse("image/*"), file));
        }

        requestBody = requestBodyBuilder.build();
            Request request = new Request.Builder()
                    .url("http://10.10.10.54/addQuestion/")
                    .post(requestBody)
                    .build();

            Call call = client.newCall(request);

            Response response = call.execute();


            Log.d("RESPONSE", response.toString());
            runOnUiThread(new Runnable() {
                public void run() {
                    Intent changeToProfile = new Intent(AddQuestionActivity.this, QuestionsOfProfessionalFromSimple.class);
//                                changeToProfile.putExtra("ID", myID);
//                                changeToProfile.putExtra("GRANTED", myGranted);
                    changeToProfile.putExtra("ID", hisId);
                    changeToProfile.putExtra("TOKEN", myToken);
                    startActivity(changeToProfile);
                }
            });
        }






    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), KeyGallery);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ReadExternalRequestCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                } else {
                    Toast.makeText(getBaseContext(), "permission problem!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ReadExternalRequestCode);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ReadExternalRequestCode);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        boolean result = checkPermission(AddQuestionActivity.this);
        if(result)
            galleryIntent();
    }
}
