package com.example.learnfirestore;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddStudentFragment extends Fragment {

    private TextInputEditText regNoTIET, fnameTIET, lnameTIET, emailTIET, pwdTIET, cpwdTIET;
    private ImageView avatarIV;
    private Button uploadBtn;

    private static final int IMAGE_REQUEST_CODE = 999;

    private Uri imageUri;

    //Firebase member variables
    FirebaseFirestore db;
    StorageReference mStore;

    public AddStudentFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mStore = FirebaseStorage.getInstance().getReference().child("avatars");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        regNoTIET = view.findViewById(R.id.regno_tiet);
        fnameTIET = view.findViewById(R.id.fname_tiet);
        lnameTIET = view.findViewById(R.id.lname_tiet);
        emailTIET = view.findViewById(R.id.email_tiet);
        pwdTIET = view.findViewById(R.id.pwd_tiet);
        avatarIV = view.findViewById(R.id.avatar_iv);

        avatarIV.setOnClickListener(v -> {
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                IMAGE_REQUEST_CODE
                        );
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, IMAGE_REQUEST_CODE);
                    }
                }
        );

        uploadBtn = view.findViewById(R.id.upload_btn);

        uploadBtn.setOnClickListener(v -> uploadStudentInfo());
    }

    public String getFileExtension(Uri uri) {
        ContentResolver resolver = ((MainActivity) getActivity()).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void uploadStudentInfo() {
        String fName = fnameTIET.getText().toString().trim();
        String lName = lnameTIET.getText().toString().trim();
        String email = emailTIET.getText().toString().trim();
        String regNo = regNoTIET.getText().toString().trim();
        String pwd = pwdTIET.getText().toString().trim();

        if (!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(regNo) && !TextUtils.isEmpty(pwd)) {
            if (imageUri != null) {

                StorageReference fileRef = mStore.child(regNo + '.' + getFileExtension(imageUri));
                UploadTask imageUploadTask = fileRef.putFile(imageUri);

                imageUploadTask.addOnSuccessListener(
                        taskSnapshot -> {
                            String imageUrl = taskSnapshot.getMetadata().getName();
                            StudentModel student = new StudentModel(fName, lName, email, regNo, pwd, imageUrl);
                            uploadDetails(student);
                        }
                ).addOnFailureListener(
                        exception -> {
                            Toast.makeText(getActivity(), "An error occurred: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                );

            } else {
                Toast.makeText(getActivity(), "You did not upload an image", Toast.LENGTH_SHORT).show();
                StudentModel student = new StudentModel(fName, lName, email, regNo, pwd, null);
                uploadDetails(student);
            }
        } else {
            Toast.makeText(getActivity(), "Fill in all the fields", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadDetails(StudentModel student) {
        db.collection("students")
                .document(student.getRegNo())
                .set(student)
                .addOnCompleteListener(
                        task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Student Added", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "An error occurred: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IMAGE_REQUEST_CODE) {
            if (resultCode == MainActivity.RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                avatarIV.setImageURI(imageUri);
            }
        }
    }

}
