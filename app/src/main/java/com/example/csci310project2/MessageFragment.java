package com.example.csci310project2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.csci310project2.databinding.FragmentMessageBinding;
import com.example.csci310project2.databinding.FragmentSecondBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

public class MessageFragment extends Fragment {

    private FragmentMessageBinding binding;
    FirebaseDatabase root;
    DatabaseReference reference;
    User currentUser;

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
            Log.d("Debug", "MessageFragment.onCreateView: Found currentUser from bundle");
        } else {
            Log.d("Debug", "Failed to get user information from bundle in SecondFragment");
            currentUser = new User();
        }

        root = FirebaseDatabase.getInstance();

        binding = FragmentMessageBinding.inflate(inflater, container, false);

        binding.buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MessageFragment.this).popBackStack();
            }
        });

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = (TextView) view.findViewById(R.id.messageContentView);

        // find accepted housing post
        DatabaseReference ref = root.getReference().child("user").child(Utility.encodeEmail(currentUser.getEmail())).child("message");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot message : snapshot.getChildren()) {
                    String userEmail = message.getKey();
                    String housingPostName = Utility.decodeEmail(message.getValue(String.class));

                    textView.setText("You have been accepted to post " + housingPostName + " by " + userEmail + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        textView.setText("Welcome, " + currentUser.getFirstName());

        Log.d("Debug", "HomeFragment.onViewCreated: " + currentUser.getFirstName());

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