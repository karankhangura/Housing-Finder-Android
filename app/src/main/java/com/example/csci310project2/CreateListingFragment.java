package com.example.csci310project2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.csci310project2.databinding.FragmentCreatelistingBinding;
import com.example.csci310project2.databinding.FragmentEditprofileBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CreateListingFragment extends Fragment {

    private FragmentCreatelistingBinding binding;
    FirebaseDatabase root;
    DatabaseReference reference;
    User currentUser;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
//        if (binding != null) {
//            Log.d("Debug", "CreateListingFragment.onCreateView: Returned old binding");
//            return binding.getRoot();
//        }

        Bundle prevBundle = getArguments();
        if (prevBundle != null) {
            currentUser = (User) prevBundle.getSerializable("user");
            Log.d("Debug", "CreateListingFragment.onCreateView: Found currentUser from bundle");
        } else {
            Log.d("Debug", "Failed to get user information from bundle in CreateListingFragment");
            currentUser = new User();
        }

        binding = FragmentCreatelistingBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        TextView textView = (TextView) view.findViewById(R.id.textview_second);

        EditText address = (EditText) view.findViewById(R.id.inputAddress);
        EditText rent = (EditText) view.findViewById(R.id.inputRent);
        EditText location = (EditText) view.findViewById(R.id.inputLocation);
        EditText bed = (EditText) view.findViewById(R.id.inputBeds);
        EditText utilities = (EditText) view.findViewById(R.id.inputUtilities);
        EditText schedule = (EditText) view.findViewById(R.id.inputSch);
        EditText academicFocus = (EditText) view.findViewById(R.id.inputAcademic);
        EditText personality = (EditText) view.findViewById(R.id.inputPersonality);
        EditText listingDeadline = (EditText) view.findViewById(R.id.inputDeadline);
        TextView errorTextView = (TextView) view.findViewById(R.id.createErrorTextView);
        //TextView textView = (TextView) view.findViewById(R.id.textview_second);

        EditText postName = (EditText) view.findViewById(R.id.inputPostName);

        root = FirebaseDatabase.getInstance();
        reference = root.getReference("user");

//        // Read from the database
//        reference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//                reference = root.getReference("user");
//                HashMap<String, Object> changedChild = (HashMap<String, Object>) dataSnapshot.getValue();
//                // constructing User object
//
//                // HOUSING POSTS TO PLACE HERE SO YOU CAN ACTUALLY APPEND TO THE ARRAYLIST
//
//                if (changedChild.get("housingPosts") != null) {
//                    currentUser = new User((String) changedChild.get("firstName"),
//                            (String) changedChild.get("lastName"), dataSnapshot.getKey(),
//                            new HashMap<String, String>(), new ArrayList<HousingPost>());//changedChild.get("housingPosts"));
//                }
//
//                else {
//                    currentUser = new User((String) changedChild.get("firstName"),
//                            (String) changedChild.get("lastName"), dataSnapshot.getKey(),
//                            new HashMap<String, String>(), new ArrayList<HousingPost>());
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });
        /*
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                textView.setText(value.toString());
            }
            @Override
            public void onCancelled(DatabaseError error) {
                textView.setText("Error in retrieving your message!");
                Log.w("SecondFragment", "Failed to read value.", error.toException());
            }
        });
         */

        binding.buttonCreateListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address.getText().toString().equals("") || postName.getText().toString().equals("") || rent.getText().toString().equals("")
                        || location.getText().toString().equals("")|| bed.getText().toString().equals("")|| utilities.getText().toString().equals("")
                        || schedule.getText().toString().equals("") || academicFocus.getText().toString().equals("") ||personality.getText().toString().equals("")
                        || listingDeadline.getText().toString().equals("")) {
                    errorTextView.setText("Failed to create. Enter all information.");
                    return;
                }
                // create posts on database
                root = FirebaseDatabase.getInstance();
                reference = root.getReference("post");

                HashMap<String, String> housingInfo = new HashMap<>();
                housingInfo.put("address", address.getText().toString());
                housingInfo.put("rent", rent.getText().toString());

                housingInfo.put("location", location.getText().toString());
                housingInfo.put("bed", bed.getText().toString());

                housingInfo.put("utilities", utilities.getText().toString());
                housingInfo.put("schedule", schedule.getText().toString());
                housingInfo.put("academicFocus", academicFocus.getText().toString());
                housingInfo.put("personality", personality.getText().toString());

                //setting all inputted data
                DatabaseReference itemRef = reference.child(Utility.encodeEmail(currentUser.getEmail()));
                itemRef.child(postName.getText().toString()).child("housingInfo").setValue(housingInfo);
                itemRef.child(postName.getText().toString()).child("postName").setValue(postName.getText().toString());

/*
                itemRef = itemRef.push();

                itemRef.child("housingInfo").setValue(housingInfo);
                itemRef.child("postName").setValue(postName.getText().toString());

 */

//                String housingPostName = currentUser.getEmail() + " Post " + (currentUser.getHousingPosts().size() + 1);
//                reference = reference.child(housingPostName);
//                reference = reference.child("address");
//                reference.setValue(housingInfo.get("address"));
//                reference = reference.getParent().child("rent");
//                reference.setValue(rent.getText().toString());
//
//                reference = reference.getParent().child("location");
//                reference.setValue(location.getText().toString());
//                reference = reference.getParent().child("bed");
//                reference.setValue(bed.getText().toString());
//
//                reference = reference.getParent().child("utilities");
//                reference.setValue(utilities.getText().toString());
//                reference = reference.getParent().child("schedule");
//                reference.setValue(schedule.getText().toString());
//                reference = reference.getParent().child("academicFocus");
//                reference.setValue(academicFocus.getText().toString());
//                reference = reference.getParent().child("personality");
//                reference.setValue(personality.getText().toString());
//                reference = reference.getParent().child("listingDeadline");
//                reference.setValue(listingDeadline.getText().toString());

                reference = root.getReference("user");

                // consider cases that may be affected by user not currently having the post
                // in it's current list when creating it: can probably avoid entirely by
                // never really using this variable beyond it's name/email
                //TODO: replace new Date() here with the actual deadline when it's worked out
//                HousingPost currentPost = new HousingPost("housingPostName", currentUser, housingInfo, new Date(), new ArrayList<Decision>());
//                currentUser.getHousingPosts().add(currentPost);
//                reference = reference.child(currentUser.getEmail());
//                reference = reference.child("housingPosts");
//
//                for (HousingPost post : currentUser.getHousingPosts()) {
//                    reference = reference.child(post.getHousingPostName());
//                    reference = reference.child("address");
//                    reference.setValue(housingInfo.get("address"));
//                    reference = reference.getParent().child("rent");
//                    reference.setValue(rent.getText().toString());
//
//                    reference = reference.getParent().child("location");
//                    reference.setValue(location.getText().toString());
//                    reference = reference.getParent().child("bed");
//                    reference.setValue(bed.getText().toString());
//
//                    reference = reference.getParent().child("utilities");
//                    reference.setValue(utilities.getText().toString());
//                    reference = reference.getParent().child("schedule");
//                    reference.setValue(schedule.getText().toString());
//                    reference = reference.getParent().child("academicFocus");
//                    reference.setValue(academicFocus.getText().toString());
//                    reference = reference.getParent().child("personality");
//                    reference.setValue(personality.getText().toString());
//                    reference = reference.getParent().child("listingDeadline");
//                    reference.setValue(listingDeadline.getText().toString());
//                    reference = reference.getParent().getParent();
//                }
                //reference.setValue(currentUser.getHousingPosts());

                NavHostFragment.findNavController(CreateListingFragment.this).popBackStack();
            }
        });

        binding.buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CreateListingFragment.this).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}