package com.ahmed22.company;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.ahmed22.company.databinding.ActivitySecondBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity {

    ActivitySecondBinding secondBinding;
    FirebaseFirestore db;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        secondBinding= DataBindingUtil.setContentView(this,R.layout.activity_second);
        //
        db=FirebaseFirestore.getInstance();
        //
        storage = FirebaseStorage.getInstance();
        secondBinding.button1.setOnClickListener(v -> {
            uploadImageWithData();
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        });
        //
        secondBinding.openImage.setOnClickListener(v -> {
            // request runtime permission "open an image from gallery"
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                getImageFromGallery();
            }
            else {
                requestReadingPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });
    }
    //permission
    ActivityResultLauncher<String> requestReadingPermission=registerForActivityResult(new ActivityResultContracts
            .RequestPermission(), isGranted -> {
        if(isGranted){
            getImageFromGallery();
        }
    });
    private void getImageFromGallery() {
        //
        Intent galleryIntent=new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }
    ActivityResultLauncher<Intent> galleryLauncher=registerForActivityResult(new ActivityResultContracts
            .StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK){
                if(result.getData()!=null){
                    Uri imageUri=result.getData().getData();
                    secondBinding.image.setImageURI(imageUri);
                }
            }
        }
    });
    //
    private void uploadImageWithData(){
        String firstName,lastName,age,address;
        firstName=secondBinding.firstName.getText().toString();
        lastName=secondBinding.lastName.getText().toString();
        age=secondBinding.age.getText().toString();
        address=secondBinding.address.getText().toString();
        //
        StorageReference rootReference=storage.getReference();
        StorageReference uploadReference=rootReference.child("employeeImage/"+firstName+lastName+".png");
        UploadTask uploadTask=uploadReference.putBytes(getImageBytes());
        //
        uploadTask.continueWithTask(task ->{
                    if (task.isSuccessful())
                        return uploadReference.getDownloadUrl();
                    return null;
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String imageUri=task.getResult().toString();
                        Employee employee= new Employee(firstName,lastName, age,address,imageUri);
                        addToFireStore(employee);
                    } else {
                        Log.println(Log.INFO,"task", Objects.requireNonNull(task.getException()).toString());
                    }
                });
    }
    //

//    private byte[]getBytes(){
//        secondBinding.image.setDrawingCacheEnabled(true);
//        secondBinding.image.buildDrawingCache();
//        Bitmap bitmap=((BitmapDrawable)(secondBinding.image.getDrawable())).getBitmap();
//        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,70,outputStream);
//        byte[] data=outputStream.toByteArray();
//        return data;
//    }

    //resize the image
    private byte[]getImageBytes(){
        secondBinding.image.setDrawingCacheEnabled(true);
        secondBinding.image.buildDrawingCache();
        Bitmap bitmap=((BitmapDrawable)(secondBinding.image.getDrawable())).getBitmap();
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();

        Matrix matrix=new Matrix();
        float scaleWidth=((float) 480)/width;
        float scaleHeight=((float) 480)/height;
        matrix.postScale(scaleWidth,scaleHeight);

        Bitmap resizedBitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);

        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG,70,outputStream);

        return outputStream.toByteArray();
    }

    private void addToFireStore(Employee employee) {
        db.collection("Company")
                .document("cmp")
                .collection("emp")
                .document()
                .set(employee).addOnCompleteListener(task -> {
                    if(task.isComplete()){
                        Toast.makeText(this, "Data uploaded to firebase", Toast.LENGTH_LONG).show();
                    } else if (task.isCanceled()) {
                        Toast.makeText(this, "??????????", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private static class Employee{
        String firstName;
        String lastName;
        String age;
        String address;
        String imageUri;
        public Employee(String firstName, String lastName, String age, String address,String imageUri) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.address = address;
            this.imageUri = imageUri;
        }
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImageUri() {return imageUri;}

        public void setImageUri(String imageUri) {this.imageUri = imageUri;}

        @BindingAdapter({"imageUri"})
        public static void loadImage(ImageView view, String uri){
            Picasso.get().load(uri).into(view);
        }

    }
}