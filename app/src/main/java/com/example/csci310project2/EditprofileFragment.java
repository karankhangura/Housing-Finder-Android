package com.example.csci310project2;

import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.csci310project2.databinding.FragmentEditprofileBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class EditprofileFragment extends Fragment {

    private FragmentEditprofileBinding binding;
    FirebaseDatabase root;
    DatabaseReference reference;
    User currentUser;
    Uri pic;
    Holder temp;
    FirebaseStorage storage;
    String picValue;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        if (binding != null) {
            return binding.getRoot();
        }

        Bundle prevBundle = getArguments();
        if (prevBundle != null) {
            currentUser = (User) prevBundle.getSerializable("user");
        } else {
            Log.d("Debug", "Failed to get user information from bundle in EditprofileFragment");
            currentUser = new User();
        }

        binding = FragmentEditprofileBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView firstNameView = (TextView) view.findViewById(R.id.editProfileInputFirstName);
        TextView lastNameView = (TextView) view.findViewById(R.id.editProfileInputLastName);
        TextView passTextView = (TextView) view.findViewById(R.id.editProfileInputTextPass);
        TextView descriptionView = (TextView) view.findViewById(R.id.editProfileDescription);


        // fill in old data
        firstNameView.setText(currentUser.getFirstName());
        lastNameView.setText(currentUser.getLastName());
        if (currentUser.getProfileInformation().get("description") != null) {
            descriptionView.setText(currentUser.getProfileInformation().get("description"));
        }

        root = FirebaseDatabase.getInstance();
        reference = root.getReference("user");
        storage =FirebaseStorage.getInstance();

        DatabaseReference ref = root.getReference().child("user").child(Utility.encodeEmail(currentUser.getEmail()));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("pic").exists()) {
                    picValue = Utility.decodeEmail(snapshot.child("pic").getValue(String.class));
                    Log.d("Debug", "FILE REFRENCE: " + picValue);
                } else {
                    picValue = "";
                }
                Log.d("Debug", "FILE REFRENCE"+picValue);
                Uri myUri = Uri.parse(picValue);
                temp = new Holder(picValue);
                try {
                    if (myUri != null) {
                        Log.d("Debug", "myUri is not null: " + myUri + " " + picValue);
                        binding.imageChoosePic.setImageURI(myUri);
                        pic = myUri;
                    }
                }
                catch (Exception e){
                    Log.d("Debug", "error"+e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("Debug", "FILE REFRENCE"+picValue);

        Log.d("Debug", "currentUser: " + currentUser.getFirstName() + " " + currentUser.getLastName());
        binding.imageChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        binding.buttonUploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                root = FirebaseDatabase.getInstance();
                DatabaseReference userInfo = root.getReference("user");


                String firstNameStr = firstNameView.getText().toString();
                String lastNameStr = lastNameView.getText().toString();
                String passwordStr = passTextView.getText().toString();
                String descriptionStr = descriptionView.getText().toString();

                currentUser.setFirstName(firstNameStr);
                currentUser.setLastName(lastNameStr);

                //setting all inputted data
                userInfo = userInfo.child(Utility.encodeEmail(currentUser.getEmail()));
                userInfo.child("firstName").setValue(firstNameStr);
                userInfo.child("lastName").setValue(lastNameStr);
                userInfo.child("pic").setValue(temp.getUrl());
                if (!passwordStr.equals("")) {
                    userInfo.child("password").setValue(passwordStr);
                }
                if (!descriptionStr.equals("")) {
                    userInfo.child("description").setValue(descriptionStr);
                }
                currentUser.getProfileInformation().put("description", descriptionStr);

                Log.d("Debug", "Edited user: " + currentUser.getFirstName() + " " + currentUser.getLastName() + " " + passwordStr);

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);

                /*NavHostFragment.findNavController(EditprofileFragment.this).popBackStack();
                NavHostFragment.findNavController(EditprofileFragment.this).popBackStack();*/
                NavHostFragment.findNavController(EditprofileFragment.this)
                        .popBackStack();
            }
        });



        /*binding.buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EditprofileFragment.this)
                        .navigate(R.id.action_editprofileFragment2_to_SecondFragment);
            }
        });*/


    }

    //https://www.youtube.com/watch?v=7p4MBsz__ao used for uploading images to firebase reference
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        binding.imageChoosePic.setImageURI(result);
                        pic = result;
                    }
                }
            });

    //https://www.youtube.com/watch?v=9-oa4OS7lUQ posted by TA on piazza used for reference
    public void uploadImage(){
        if(pic!=null){
            StorageReference ref = storage.getReference().child("images/" + UUID.randomUUID().toString());
            ref.putFile(pic);
            temp = new Holder(pic.toString());

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}