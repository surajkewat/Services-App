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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class signUp_form extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText txtEmail,txtName,txtPassword,txtConfirmPass,txtAdhaar,txtSteet,txtContact;
    Button btn_sign_up;
    Spinner spinnerGender,spinnnerProf,spinnerState,spinnerCity;
    DatabaseReference refUsers,refSeeker,refRecruiter,refPerson,dR;
    String state = null;
    String city = null;
    RadioGroup rgProf;

    public static final int Image_Request_Code = 1;
    public static final int REQUEST_PERMISSION = 1;
    private String imageFilePath = "";
    Button btn_img_ch,btn_upload;
    ImageView userImage;
    StorageReference mStorageRef,Ref;
    public Uri imguri;
    SharedPreferences sharedPreferences;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_form);
        txtName = (EditText)findViewById(R.id.txtName);
        txtEmail =(EditText)findViewById(R.id.txtEmail);
        txtContact = findViewById(R.id.txtContact);
        txtPassword = (EditText)findViewById((R.id.txtPassword));
        txtConfirmPass =(EditText)findViewById(R.id.txtConfirmPass);
        btn_sign_up = (Button)findViewById(R.id.btn_Signup);
        rgProf = findViewById(R.id.rgGender);

        btn_img_ch = findViewById(R.id.btnImgChoose);
        btn_upload = findViewById(R.id.btnUpload);
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        userImage = findViewById(R.id.userImage);

        spinnnerProf = findViewById(R.id.spinProfession);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.profession,android.R.layout.simple_spinner_item);
        spinnnerProf.setAdapter(adapter1);
        spinnnerProf.setOnItemSelectedListener(this);

        spinnerState = findViewById(R.id.spinStates);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.States,android.R.layout.simple_spinner_item);
        spinnerState.setAdapter(adapter2);
        spinnerState.setOnItemSelectedListener(this);

        spinnerCity = findViewById(R.id.spinCities);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.Cities,android.R.layout.simple_spinner_item);
        spinnerCity.setAdapter(adapter3);
        spinnerCity.setOnItemSelectedListener(this);

        spinnerGender = findViewById(R.id.spinGender);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,R.array.Gender,android.R.layout.simple_spinner_item);
        spinnerGender.setAdapter(adapter4);
        spinnerGender.setOnItemSelectedListener(this);



        firebaseAuth = FirebaseAuth.getInstance();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }


        dR = FirebaseDatabase.getInstance().getReference();

        btn_img_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FileChooser();
                openCameraIntent();
            }
        });

//        btn_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FileUploader();
//            }
//        });


        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password  = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPass.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(signUp_form.this,"Please Enter Email-id",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(signUp_form.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(signUp_form.this,"Please Enter Confirmed Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<8){
                    Toast.makeText(signUp_form.this,"Password is too short",Toast.LENGTH_SHORT).show();
                }
                if(rgProf.getCheckedRadioButtonId()==-1){
                    Toast.makeText(signUp_form.this,"Seletct profession",Toast.LENGTH_SHORT).show();
                }
                if(password.equals(confirmPassword)){
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(signUp_form.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
//                                        HashMap<String,Object> map = new HashMap<>();
//                                        map.put("name",txtName.getText().toString());
//                                        map.put("email",txtEmail.getText().toString());
//                                        if(getSelectedRabdioButton()==1){
//                                            map.put("category","recruiter");
//                                        }
//                                        else {
//                                            map.put("category","seeker");
//                                        }
//                                        map.put("contact",txtContact.getText().toString());
//                                        map.put("state",spinnerState.getSelectedItem().toString());
//                                        map.put("city",spinnerCity.getSelectedItem().toString());
//                                        map.put("gender",spinnerGender.getSelectedItem().toString());
//                                        DatabaseReference db=FirebaseDatabase.getInstance().getReference("users");
//                                        db.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).setValue(map);

//                                        FirebaseDatabase.getInstance().getReference().child("Users")
//                                                .setValue(map)
//                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        Log.i("Check","OnComplete");
//                                                    }
//                                                }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.i("Check","OnFailure: "+e.toString());
//                                            }
//                                        });

//                                        HashMap<String,Object> map2 = new HashMap<>();
//                                        map2.put("name",txtName.getText().toString());
//                                        map2.put("email",txtEmail.getText().toString());
//                                        map2.put("contact",txtContact.getText().toString());
//                                        map2.put("profession",spinnnerProf.getSelectedItem().toString());
//                                        map2.put("state",spinnerState.getSelectedItem().toString());
//                                        map2.put("city",spinnerCity.getSelectedItem().toString());
//                                        map2.put("gender",spinnerGender.getSelectedItem().toString());
//                                        if(getSelectedRabdioButton()==1){
//                                            map2.remove("profession");
//                                            DatabaseReference db2=FirebaseDatabase.getInstance().getReference("recruiter");
//                                            db2.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).setValue(map2);
//                                        }
//                                        else{
//                                            DatabaseReference db3=FirebaseDatabase.getInstance().getReference("seeker");
//                                            db3.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).setValue(map2);
//                                        }

                                        final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        DatabaseReference db5=FirebaseDatabase.getInstance().getReference("contacts");
                                        db5.child(txtContact.getText().toString()).setValue(userId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        });

                                        Ref = mStorageRef.child(userId+"."+getExtension(imguri));
//                                        Ref.putFile(imguri)
//                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                                    @Override
//                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                        // Get a URL to the uploaded content
//                                                        Toast.makeText(signUp_form.this,"upload succss",Toast.LENGTH_SHORT).show();
//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception exception) {
//                                                        // Handle unsuccessful uploads
//                                                        // ...
//                                                    }
//                                                });

                                        Bitmap bmp = null;
                                        try {
                                            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imguri);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                                        byte[] data = baos.toByteArray();

                                        Ref.putBytes(data)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                                    {
                                                        Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                                                        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                String PersonId ;
                                                                String url=uri.toString();
                                                                String userId= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                                                                String Temp1 = txtName.getText().toString().trim();
                                                                String Temp2 = txtContact.getText().toString().trim();
                                                                String Temp3;
                                                                sharedPreferences = getSharedPreferences("userDetail", Context.MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                                                if(getSelectedRabdioButton()==1){
                                                                    PersonId = "1";
                                                                    Temp3 = "Recruiter";
                                                                    editor.putString("user","recruiter");
                                                                    editor.commit();
                                                                }
                                                                else{
                                                                    PersonId = "2";
                                                                    Temp3 = spinnnerProf.getSelectedItem().toString();
                                                                    editor.putString("user","seeker");
                                                                    editor.commit();
                                                                }

                                                                String Temp4 = spinnerState.getSelectedItem().toString();
                                                                String Temp5 = spinnerCity.getSelectedItem().toString();
                                                                String Temp6 = txtEmail.getText().toString();
                                                                String Temp7 = spinnerGender.getSelectedItem().toString();
                                                                Log.i("Values ",PersonId+Temp1+Temp2+Temp3+Temp4+Temp5+userId+url);
                                                                Person imageUploadInfo = new Person(PersonId,Temp1,Temp2,Temp3,Temp4,Temp5,userId,url,Temp6,Temp7);
                                                                DatabaseReference db4=FirebaseDatabase.getInstance().getReference("person");
                                                                db4.child(userId).setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Intent intent = new Intent(signUp_form.this,DrawerActivity.class);
                                                                        startActivity(intent);
                                                                    }
                                                                });
//                                                                onBackPressed();
                                                            }
                                                        });


                                                    }
                                                });


                                    }
                                    else {


                                        Toast.makeText(signUp_form.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void FileUploader() {
        StorageReference  Ref = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));
        Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(signUp_form.this,"upload succss",Toast.LENGTH_SHORT).show();
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1 && resultCode==RESULT_OK && data != null && data.getData() !=null){
//            imguri = data.getData();
//            userImage.setImageURI(imguri);
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code) {
            if (resultCode == RESULT_OK) {
                userImage.setImageURI(Uri.parse(imageFilePath));
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int getSelectedRabdioButton(){
        int i=-1;
        switch (rgProf.getCheckedRadioButtonId()){
            case R.id.rbRecuiter:
                i=1;
                break;
            case R.id.rbSeeker:
                i=2;
                break;
        }
        return i;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbRecuiter:
                if (checked)
                    spinnnerProf.setEnabled(false);
                    break;
            case R.id.rbSeeker:
                if (checked)
                    spinnnerProf.setEnabled(true);
                    break;
        }
    }
}
