package com.example.suber;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    EditText txtEmail,txtName,txtPassword,txtConfirmPass,txtAdhaar,txtSteet,txtContact;
    Button btn_sign_up,btnSaveEdit,btnCancelEdit;
    Spinner spinnerGender,spinnnerProf,spinnerState,spinnerCity;
    DatabaseReference refUsers,refSeeker,refRecruiter,refPerson,dR;
    String state = null;
    String city = null;
    RadioGroup rgProf;
    RadioButton r1,r2;

    public static final int Image_Request_Code = 1;
    public static final int REQUEST_PERMISSION = 1;
    private String imageFilePath = "";
    Button btn_img_ch,btn_upload;
    ImageView userImage;
    StorageReference mStorageRef,Ref;
    public Uri imguri;
    SharedPreferences sharedPreferences;
    ArrayAdapter<CharSequence> adapter1,adapter2,adapter3,adapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        txtName = (EditText)findViewById(R.id.txtNameEdit);
        txtEmail =(EditText)findViewById(R.id.txtEmailEdit);
        txtContact = findViewById(R.id.txtContactEdit);
//        txtPassword = (EditText)findViewById((R.id.txtPasswordEdit));
//        txtConfirmPass =(EditText)findViewById(R.id.txtConfirmPassEdit);
        rgProf = findViewById(R.id.rgGenderEdit);
        r1 = findViewById(R.id.rbRecuiterEdit);
        r2  = findViewById(R.id.rbSeekerEdit);


        btn_img_ch = findViewById(R.id.btnImgChooseEdit);
        btn_upload = findViewById(R.id.btnUploadEdit);
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        userImage = findViewById(R.id.userImageEdit);

        btnSaveEdit = findViewById(R.id.btn_SaveEdit);
        btnCancelEdit = findViewById(R.id.btn_CancelEdit);


        spinnnerProf = findViewById(R.id.spinProfessionEdit);
        adapter1 = ArrayAdapter.createFromResource(this,R.array.profession,android.R.layout.simple_spinner_item);
        spinnnerProf.setAdapter(adapter1);
        spinnnerProf.setOnItemSelectedListener(this);

        spinnerState = findViewById(R.id.spinStatesEdit);
        adapter2 = ArrayAdapter.createFromResource(this,R.array.States,android.R.layout.simple_spinner_item);
        spinnerState.setAdapter(adapter2);
        spinnerState.setOnItemSelectedListener(this);

        spinnerCity = findViewById(R.id.spinCitiesEdit);
        adapter3 = ArrayAdapter.createFromResource(this,R.array.Cities,android.R.layout.simple_spinner_item);
        spinnerCity.setAdapter(adapter3);
        spinnerCity.setOnItemSelectedListener(this);

        spinnerGender = findViewById(R.id.spinGenderEdit);
        adapter4 = ArrayAdapter.createFromResource(this,R.array.Gender,android.R.layout.simple_spinner_item);
        spinnerGender.setAdapter(adapter4);
        spinnerGender.setOnItemSelectedListener(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        dR = FirebaseDatabase.getInstance().getReference("person");
        dR.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtName.setText(dataSnapshot.child("name").getValue().toString());
                txtContact.setText(dataSnapshot.child("contact").getValue().toString());
                String t1  = dataSnapshot.child("gender").getValue().toString();
                String t2  = dataSnapshot.child("personCode").getValue().toString();
                String t3  = dataSnapshot.child("state").getValue().toString();
                String t4  = dataSnapshot.child("city").getValue().toString();
                String t5 = dataSnapshot.child("profession").getValue().toString();

                String t6 = dataSnapshot.child("email").getValue().toString();
                txtEmail.setText(t6);
                txtEmail.setEnabled(false);

                if (t1 != null) {
                    int spinnerPosition = adapter1.getPosition(t1);
                    spinnerGender.setSelection(spinnerPosition);


                }

                if(t2.equals("2") ){
                    if (t5!= null){
                        int spinnerPosition = adapter2.getPosition(t5);
                        spinnnerProf.setSelection(spinnerPosition);
                        r2.setChecked(true);

                    }

                }
                else{
                    spinnnerProf.setEnabled(false);
                    r1.setChecked(true);
                }

                if (t3 != null) {
                    int spinnerPosition = adapter3.getPosition(t3);
                    spinnerState.setSelection(spinnerPosition);
                }
                if (t4 != null) {
                    int spinnerPosition = adapter4.getPosition(t4);
                    spinnerCity.setSelection(spinnerPosition);
                }
                Picasso.get().load(dataSnapshot.child("imgUrl").getValue().toString()).into(userImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_img_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FileChooser();
                openCameraIntent();
            }
        });

        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(EditProfile.this,DrawerActivity.class);
                startActivity(intent);
            }
        });
        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();

                Ref = mStorageRef.child(userId+"."+getExtension(imguri));

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
                                                Intent intent = new Intent(EditProfile.this,DrawerActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }
                        });
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
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
                        Toast.makeText(EditProfile.this,"upload succss",Toast.LENGTH_SHORT).show();
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
