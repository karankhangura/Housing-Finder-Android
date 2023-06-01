package com.example.csci310project2;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csci310project2.databinding.FragmentDecideResponseBinding;
import com.example.csci310project2.databinding.FragmentViewPostBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DecideResponseFragment extends Fragment {

    private FragmentDecideResponseBinding binding;
    FirebaseDatabase root;
    DatabaseReference reference;
    User currentUser;
    HousingPost currentPost;
    User acceptedUser;

    protected RecyclerView mRecyclerView;
    protected ViewPostAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private View mRootView;

    private static final String TAG = "ViewPostFragment";
    private static final int SPAN_COUNT = 20;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.


        Bundle prevBundle = getArguments();
        if (prevBundle != null) {
            currentUser = (User) prevBundle.getSerializable("user");
            currentPost = (HousingPost) prevBundle.getSerializable("post");
            acceptedUser = (User) prevBundle.getSerializable("acceptedUser");
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        binding = FragmentDecideResponseBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = (TextView) view.findViewById(R.id.viewUserInfo);
        String userInfo = "User Email: " + acceptedUser.getEmail();
        userInfo += "\n\t\tUser First Name: " + acceptedUser.getFirstName();
        userInfo += "\n\t\tUser Last Name: " + acceptedUser.getLastName();
        userInfo += "\n\t\tUser Description: ";
        if (acceptedUser.getProfileInformation().get("description") != null) {
            userInfo += acceptedUser.getProfileInformation().get("description");
        }
        else {
            userInfo += "User has no description.";
        }
        textView.setText(userInfo);

        binding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Debug", "choosing a user");


                root = FirebaseDatabase.getInstance();
                reference = root.getReference("post");
                reference = reference.child(Utility.encodeEmail(currentPost.getUser().getEmail()));
                reference = reference.child(currentPost.getHousingPostName());
                reference.child("chosenUsers").child(Utility.encodeEmail(acceptedUser.getEmail())).setValue(Utility.encodeEmail(acceptedUser.getEmail()));

                reference = root.getReference("user");
                reference = reference.child(Utility.encodeEmail(acceptedUser.getEmail()));
                reference.child("message").child(Utility.encodeEmail(currentUser.getEmail())).setValue(currentPost.getHousingPostName());

                Log.d("Debug", "User has been chosen" + acceptedUser.getEmail() + " first and last"+acceptedUser.getFirstName() + acceptedUser.getLastName());


                deletePendingResponses();

                NavHostFragment.findNavController(DecideResponseFragment.this).popBackStack();

                //NavHostFragment.findNavController(HomeFragment.this)
                //        .navigate(R.id.action_SecondFragment_to_createListingFragment, result);
            }
        });

        binding.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root = FirebaseDatabase.getInstance();
                reference = root.getReference("post").child(Utility.encodeEmail(currentPost.getUser().getEmail()));
                //reference = reference.child("");

                NavHostFragment.findNavController(DecideResponseFragment.this).popBackStack();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void deletePendingResponses() {
        root = FirebaseDatabase.getInstance();
        DatabaseReference ref = root.getReference().child("post");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    for (DataSnapshot postSnap : userSnap.getChildren()) {
                        if (postSnap.child("accepted").hasChild(Utility.encodeEmail(acceptedUser.getEmail()))) {
                            postSnap.child("accepted").child(Utility.encodeEmail(acceptedUser.getEmail())).getRef().removeValue();
                            Log.d("Debug", "Deleted accepted " + postSnap.getKey() + " with user " + userSnap.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    private enum LayoutManagerType {
//        GRID_LAYOUT_MANAGER,
//        LINEAR_LAYOUT_MANAGER
//    }
//
//    protected ViewPostFragment.LayoutManagerType mCurrentLayoutManagerType = ViewPostFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        Bundle prevBundle = getArguments();
//        if (prevBundle != null) {
//            currentUser = (User) prevBundle.getSerializable("user");
//            currentPost = (HousingPost) prevBundle.getSerializable("post");
//            Log.d("Debug", "ViewListingFragment.onCreateView: Found currentUser from bundle");
//        } else {
//            Log.d("Debug", "Failed to get user information from bundle in ViewListFragment");
//            currentUser = new User();
//            currentPost = new HousingPost();
//        }
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
////        if (binding != null) {
////            return binding.getRoot();
////       }
//
//        binding = FragmentViewPostBinding.inflate(inflater, container, false);
//
//        View rootView  = inflater.inflate(R.layout.fragment_view_post, container, false);
//        mRootView = rootView;
//        rootView.setTag(TAG);
//
//        // BEGIN_INCLUDE(initializeRecyclerView)
//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.viewPostRecyclerView);
//
//        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
//        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
//        // elements are laid out.
//        mLayoutManager = new LinearLayoutManager(getActivity());
//
//        mCurrentLayoutManagerType = ViewPostFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
//
//        if (savedInstanceState != null) {
//            // Restore saved layout manager type.
//            mCurrentLayoutManagerType = (ViewPostFragment.LayoutManagerType) savedInstanceState
//                    .getSerializable(KEY_LAYOUT_MANAGER);
//        }
//        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
//
//
//        mAdapter = new ViewPostAdapter(currentPost, currentUser);
//        // Set CustomAdapter as the adapter for RecyclerView.
//        mRecyclerView.setAdapter(mAdapter);
//
//        // Inflate the layout for this fragment
//        //return binding.getRoot();
//
//
//
//        return rootView;
//    }
//
//
//    /**
//     * Set RecyclerView's LayoutManager to the one given.
//     *
//     * @param layoutManagerType Type of layout manager to switch to.
//     */
//    public void setRecyclerViewLayoutManager(ViewPostFragment.LayoutManagerType layoutManagerType) {
//        int scrollPosition = 0;
//
//        // If a layout manager has already been set, get current scroll position.
//        if (mRecyclerView.getLayoutManager() != null) {
//            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
//                    .findFirstCompletelyVisibleItemPosition();
//        }
//
//        switch (layoutManagerType) {
//            case GRID_LAYOUT_MANAGER:
//                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
//                mCurrentLayoutManagerType = ViewPostFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
//                break;
//            case LINEAR_LAYOUT_MANAGER:
//                mLayoutManager = new LinearLayoutManager(getActivity());
//                mCurrentLayoutManagerType = ViewPostFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
//                break;
//            default:
//                mLayoutManager = new LinearLayoutManager(getActivity());
//                mCurrentLayoutManagerType = ViewPostFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
//        }
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.scrollToPosition(scrollPosition);
//    }
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        binding.acceptButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("Debug", "accepting an invite onclick working");
//
//
//                root = FirebaseDatabase.getInstance();
//                reference = root.getReference("post");
//                reference = reference.child(currentPost.getUser().getEmail());
//                reference = reference.child(currentPost.getHousingPostName());
//                reference = reference.child("housingInfo");
//
//                DatabaseReference itemRef = reference;
//                itemRef.child("accepted").child(currentUser.getEmail());
//                Log.d("Debug", "accepted an invite");
//
//
//
//                //NavHostFragment.findNavController(HomeFragment.this)
//                //        .navigate(R.id.action_SecondFragment_to_createListingFragment, result);
//            }
//        });
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        // Save currently selected layout manager.
//        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
//        super.onSaveInstanceState(savedInstanceState);
//    }
}