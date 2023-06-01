package com.example.csci310project2;

import static com.example.csci310project2.Utility.specialCharacterRegex;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.csci310project2.databinding.FragmentFirstBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

//reference api: https://firebase.google.com/docs/reference/android/com/google/firebase/database/DatabaseReference
public class RegisterFragment extends Fragment {

    private FragmentFirstBinding binding;

    FirebaseDatabase root;
    DatabaseReference reference;

    FirebaseStorage storage;

    Uri pic;
    Holder temp;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storage =FirebaseStorage.getInstance();

        EditText email = (EditText) view.findViewById(R.id.inputText);
        EditText password = (EditText) view.findViewById(R.id.inputTextPass);
        EditText firstName = (EditText) view.findViewById(R.id.firstName);
        EditText lastName = (EditText) view.findViewById(R.id.lastName);
        TextView errorTextView = (TextView) view.findViewById(R.id.registerErrorTextView);


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



        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root = FirebaseDatabase.getInstance();
                DatabaseReference userInfo = root.getReference("user");

                String firstNameStr = firstName.getText().toString();
                String lastNameStr = lastName.getText().toString();
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();


                if (emailStr.equals("") && passwordStr.equals("") && (firstNameStr.equals("") && lastNameStr.equals(""))) {
                    errorTextView.setText("Failed to register. Enter information.");
                    return;
                }

                if (emailStr.equals("")) {
                    errorTextView.setText("Failed to register. Email is empty.");
                    return;
                }

                if (passwordStr.equals("")) {
                    errorTextView.setText("Failed to register. Password is empty.");
                    return;
                }

                if (Utility.encodeEmail(emailStr).matches(specialCharacterRegex)) {
                    errorTextView.setText("Failed to register. Email contains invalid characters");
                    return;
                }

//                if (temp==null || temp.getUrl()==null) {
//                    errorTextView.setText("Failed to register. No profile image uploaded.");
//                    return;
//                }



                DatabaseReference allUsers = root.getReference("user");
                allUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot user : snapshot.getChildren()) {
                            String emailUserStr = Utility.decodeEmail(user.getKey());
                            if (emailUserStr.equals(emailStr)) {
                                errorTextView.setText("Failed to register. Email is already registered.");
                                return;
                            }
                        }

                        DatabaseReference userInfo = root.getReference().child("user");

                        //setting all inputted data
                        userInfo = userInfo.child(Utility.encodeEmail(emailStr));
                        userInfo.child("password").setValue(passwordStr);
                        userInfo.child("firstName").setValue(firstNameStr);
                        userInfo.child("lastName").setValue(lastNameStr);

                        //uploads pic to user
                        if(temp!=null && temp.getUrl()!=null) {
                            userInfo.child("pic").setValue(temp.getUrl());
                        }
                       // userInfo.child("pic").setValue(temp.getUrl()+".jpeg");
                        User user = new User(firstNameStr, lastNameStr, emailStr, new HashMap<>(), new ArrayList<>());

                        // store data across fragments
                        Bundle result = new Bundle();
                        result.putSerializable("user", user);

                        Log.d("Debug", "Registered user: " + emailStr);

                        NavHostFragment.findNavController(RegisterFragment.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment, result);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.buttonLoginNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.action_FirstFragment_to_loginFragment);
            }
        });
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

}