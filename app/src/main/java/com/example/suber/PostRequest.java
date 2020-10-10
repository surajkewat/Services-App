package com.example.suber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostRequest extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner req_options;
    SharedPreferences sharedPreferences ;
    private String recruiterName;

    String fireUri;
    String fireVidUri;

    String ImageUploadId;
    ///Video Part
    Uri videoUri;
    private static final int REQUEST_CODE=101;
    private StorageReference videoRef;
    VideoView videoView;
    private MediaController mediaController;

    //Image Part
    public static final int Image_Request_Code = 1;
    public static final int REQUEST_PERMISSION = 1;
    private String imageFilePath = "";
    ImageView imgReq;
    StorageReference mStorageRef,Ref;
    public Uri imguri;
    Button btn_img_cap,btn_choose,btnSendReq;

    TextView txtDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_request);
        sharedPreferences = this.getSharedPreferences("categories", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        req_options = findViewById(R.id.spinRequest);
         btnSendReq =findViewById(R.id.btnSendReq);
         txtDetails = findViewById(R.id.et_content);

        btn_img_cap = findViewById(R.id.btnImgCap);
        btn_choose= findViewById(R.id.btnImgChooseReq);
        mStorageRef = FirebaseStorage.getInstance().getReference("Requests");
        imgReq = findViewById(R.id.reqImage);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Videos");
        videoRef = storageReference.child(System.currentTimeMillis()+"3gp");
        videoView= findViewById(R.id.videoView);



        String k=sharedPreferences.getString("category",null);
        if(k.equals("Electrician")){
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.req_electrician,android.R.layout.simple_spinner_item);
            req_options.setAdapter(adapter1);
        }
        else if(k.equals("Plumber")){
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.req_plumber,android.R.layout.simple_spinner_item);
            req_options.setAdapter(adapter1);
        }
        else if(k.equals("Carpenter")){
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.req_carpenter,android.R.layout.simple_spinner_item);
            req_options.setAdapter(adapter1);
        }
        else if(k.equals("Mason")){
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.req_mason,android.R.layout.simple_spinner_item);
            req_options.setAdapter(adapter1);
        }
        else if(k.equals("Labour")){
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.req_labour,android.R.layout.simple_spinner_item);
            req_options.setAdapter(adapter1);
        }
        else {
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.req_painter,android.R.layout.simple_spinner_item);
            req_options.setAdapter(adapter1);
        }

        req_options.setOnItemSelectedListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        btn_img_cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FileChooser();
                openCameraIntent();
            }
        });

        btnSendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("Details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imguri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();
                Date currentTime = Calendar.getInstance().getTime();
                final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                mStorageRef = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));
                mStorageRef.putBytes(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                            {
                                Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                                mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        fireUri=uri.toString();
                                    }
                                });

                            }
                        });
                videoRef.putFile(videoUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri) {
                                String tempName = sharedPreferences.getString("Name",null);
                                String tempSeekerId = sharedPreferences.getString("id",null);
                                fireVidUri = uri.toString();
                                String Temp1= req_options.getSelectedItem().toString();
                                String Temp2 = txtDetails.getText().toString().trim();
                                Log.i("Request ",tempName+tempSeekerId+Temp1+Temp2+"1"+fireUri+fireVidUri);

                                //Requests in Recruiter dir

                                DatabaseReference db4= FirebaseDatabase.getInstance().getReference("RequestsRecruiter");
                                ImageUploadId = db4.push().getKey();
                                RecruiterRequests imageUploadInfo = new RecruiterRequests(tempName,tempSeekerId,Temp1,Temp2,"1",ImageUploadId,fireUri,fireVidUri,"0","0");
                                db4.child(userId).child(ImageUploadId).setValue(imageUploadInfo);


                                DatabaseReference db6 = FirebaseDatabase.getInstance().getReference("person");
                                db6.child(userId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String tempSeekerId = sharedPreferences.getString("id",null);
                                        String Temp1= req_options.getSelectedItem().toString();
                                        String Temp2 = txtDetails.getText().toString().trim();
                                        String Name = dataSnapshot.child("name").getValue().toString();
                                        DatabaseReference db5= FirebaseDatabase.getInstance().getReference("RequestsSeeker");
//                                        String ImageUploadId2 =  db5.push().getKey();
                                        Log.i("Bahan",Name+userId+Temp1+Temp2+"1"+ImageUploadId+fireUri+fireVidUri);
                                        RequestsSeeker imageUploadInfo2 = new RequestsSeeker(Name,userId,Temp1,Temp2,"1",ImageUploadId,fireUri,fireVidUri,"0","0");
                                        db5.child(tempSeekerId).child(ImageUploadId).setValue(imageUploadInfo2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(PostRequest.this,"Request Sent Successfully",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent( PostRequest.this,DrawerActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                //Requests in Seeker dir



                            }
                        });
                        Toast.makeText(PostRequest.this,"Video Upload Successfull : ",Toast.LENGTH_SHORT).show();

                    }
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostRequest.this,"Video Upload Failed : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    }
                    });

//                Intent intent = new Intent(PostRequest.this,Test.class);
//                startActivity(intent);

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    ////////////////////////Image Part
    private void openCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            File photofile=null;
            try{
                photofile = createImageFile();
            }
            catch (IOException e){
                e.printStackTrace();
                return;
            }
            imguri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() +".provider",photofile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imguri);
            startActivityForResult(intent, Image_Request_Code);
        }
    }

    private String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }


    ////////////////////////////Video part

    public void uploadVideo(View view) {
        if (videoUri !=null){
            UploadTask uploadTask = videoRef.putFile(videoUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PostRequest.this,"Video Upload Successfull : ",Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostRequest.this,"Video Upload Failed : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    updateProgress(taskSnapshot);
                }
            });

        }

    }

    public void record(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,REQUEST_CODE);
        }
    }

    public void download(View view) {
        try {
            final File localFile = File.createTempFile("userReq","3gp");
            videoRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PostRequest.this,"Download complete ",Toast.LENGTH_LONG).show();
                    final VideoView videoView = (VideoView) findViewById(R.id.videoView);
                    videoView.setVideoURI(Uri.fromFile(localFile));
                    videoView.start();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostRequest.this,"Download Failed ",Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (IOException e) {
            Toast.makeText(PostRequest.this,"Failed to create temp file "+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode == REQUEST_CODE){
                videoUri = data.getData();
                if(resultCode == RESULT_OK){

                    videoView.setVideoURI(videoUri);
                    mediaController = new MediaController(this);
                    mediaController.setAnchorView(videoView);
                    videoView.setMediaController(mediaController);
                    videoView.start();
                    Toast.makeText(PostRequest.this,"Video Saved to :\n"+videoUri,Toast.LENGTH_SHORT).show();
                }
                else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(PostRequest.this,"Video recording cancelled ",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PostRequest.this,"Failed to record the video ",Toast.LENGTH_SHORT).show();
                }
            }
            if (requestCode == Image_Request_Code) {
                if (resultCode == RESULT_OK) {
                    imgReq.setImageURI(Uri.parse(imageFilePath));
                }
                else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e){
            Toast.makeText(PostRequest.this,"data == null "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }




    private void updateProgress(UploadTask.TaskSnapshot taskSnapshot) {
        long fileSize = taskSnapshot.getTotalByteCount();

        long uploadBytes = taskSnapshot.getBytesTransferred();
        long progress = (100* uploadBytes)/fileSize;

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pbar);

        progressBar.setProgress((int)progress);

    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    ////////////////////////////////////////////////////
    private void FileUploader() {
        StorageReference  Ref = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));
        Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(PostRequest.this,"upload succss",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }
    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
}
