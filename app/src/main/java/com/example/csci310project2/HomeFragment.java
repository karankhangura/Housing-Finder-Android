package com.example.csci310project2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.csci310project2.databinding.FragmentSecondBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class HomeFragment extends Fragment {

    private FragmentSecondBinding binding;
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
            Log.d("Debug", "HomeFragment.onCreateView: Returned old binding");
            return binding.getRoot();
        }

        Bundle prevBundle = getArguments();
        if (prevBundle != null) {
            currentUser = (User) prevBundle.getSerializable("user");
            Log.d("Debug", "HomeFragment.onCreateView: Found currentUser from bundle");
        } else {
            Log.d("Debug", "Failed to get user information from bundle in SecondFragment");
            currentUser = new User();
        }

        binding = FragmentSecondBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = (TextView) view.findViewById(R.id.textview_second);

        root = FirebaseDatabase.getInstance();

        textView.setText("Welcome, " + currentUser.getFirstName());

        Log.d("Debug", "HomeFragment.onViewCreated: " + currentUser.getFirstName());

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


        if (reference == null) {
            Log.d("Debug", "onViewCreated reloaded reference");
            reference = root.getReference("user");
        }

        Log.d("Debug", "SecondActivity.onViewCreated loaded");

        binding.buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Debug", "Nav to edit profile");

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);

                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_SecondFragment_to_editprofileFragment2, result);
            }
        });

        binding.buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Debug", "Nav to create listing");

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);

                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_SecondFragment_to_createListingFragment, result);
            }
        });

        binding.buttonRespond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Debug", "Nav to filtersort");

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);

                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_SecondFragment_to_filtersortFragment, result);
            }
        });


        binding.buttonManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);

                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_SecondFragment_to_managelistingFragment, result);
            }
        });

        binding.buttonMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);

                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_SecondFragment_to_messageFragment, result);
            }
        });

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to logout

                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        ref = root.getReference().child("user").child(Utility.encodeEmail(currentUser.getEmail())).child("message");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot message : snapshot.getChildren()) {
                    String userEmail = message.getKey();
                    String housingPostName = Utility.decodeEmail(message.getValue(String.class));

                    Toast.makeText(getActivity(),"You have been accepted to post " + housingPostName + " by " + userEmail + "!",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }

}