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

import android.os.Bundle;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;


import com.example.csci310project2.databinding.FragmentManagelistingBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

//https://github.com/android/views-widgets-samples/tree/main/RecyclerView/ Used for reference
public class ManagelistingFragment extends Fragment {
    FirebaseDatabase root;
    DatabaseReference reference;
    User currentUser;
    private View mRootView;

    private static final String TAG = "ManagelistingFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 10;

    public interface ManageListingNavigation {
        public void passBundle(Bundle bundle);
    }

    boolean adapterSet;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected ManagelistingFragment.LayoutManagerType mCurrentLayoutManagerType = ManagelistingFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;


    protected RecyclerView mRecyclerView;
    protected CustomAdapter2 mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    protected ArrayList<HousingPost> posts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        //initDataset();

        adapterSet = false;

        Bundle prevBundle = getArguments();
        if (prevBundle != null) {
            currentUser = (User) prevBundle.getSerializable("user");
            Log.d("Debug", "ManagelistingFragment.onCreateView: Found currentUser from bundle " + currentUser.getEmail());
        } else {
            Log.d("Debug", "Failed to get user information from bundle in ManagelistingFragment");
            currentUser = new User();
        }

        root = FirebaseDatabase.getInstance();
        reference = root.getReference().child("post"); //.child(Utility.encodeEmail(currentUser.getEmail()));
        reference = reference.child(Utility.encodeEmail(currentUser.getEmail()));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts = new ArrayList<>();
                for (DataSnapshot userPost : snapshot.getChildren()) {
                    Log.d("Debug", "Snapshot: " + userPost.getKey());
                    HashMap<String, String> items = new HashMap<>();
                    for (DataSnapshot userPostItem : userPost.child("housingInfo").getChildren()) {
                        items.put(userPostItem.getKey(), userPostItem.getValue(String.class));
                        Log.d("Debug", currentUser.getEmail() + ": " + userPostItem.getKey() + " " + userPostItem.getValue(String.class));
                    }
                    HousingPost housingPost = new HousingPost();
                    housingPost.setDeadline(new Date());
                    housingPost.setUser(currentUser);

                    housingPost.setHousingInformation(items);
                    housingPost.setHousingPostName(userPost.child("postName").getValue(String.class));

                    posts.add(housingPost);

                    Log.d("Debug", "Added another post: " + posts.size());
                }

                mDataset = new String[posts.size()];
                for (int i = 0; i < mDataset.length; i++) {
                    mDataset[i] = posts.get(i).toString();
                }

                ManagelistingFragment.ManageListingNavigation navigation = new ManagelistingFragment.ManageListingNavigation() {
                    @Override
                    public void passBundle(Bundle bundle) {
                        Log.d("Debug", "Nav to filtersort");

                        NavHostFragment.findNavController(ManagelistingFragment.this)
                                .navigate(R.id.action_managelistingFragment_to_viewResponsesFragment, bundle);
                    }
                };

                mAdapter = new CustomAdapter2(mDataset, posts, currentUser, navigation);
                mRecyclerView.setAdapter(mAdapter);
                adapterSet = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView != null) {
            return mRootView;
        }

        View rootView = inflater.inflate(R.layout.recycler_view_fragtwo, container, false);

        mRootView = rootView;
        rootView.setTag(TAG);


        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = ManagelistingFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (ManagelistingFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        if (!adapterSet) {
            // set default value
            mAdapter = new CustomAdapter2(new String[0], new ArrayList<>(), currentUser, new ManageListingNavigation() {
                @Override
                public void passBundle(Bundle bundle) {

                }
            });
            // Set CustomAdapter as the adapter for RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
            // END_INCLUDE(initializeRecyclerView)
            adapterSet = true;
        }


        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(ManagelistingFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = ManagelistingFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = ManagelistingFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = ManagelistingFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {

        mDataset =new String[DATASET_COUNT];

        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i]="Housing Post #" + i;
        }
    }


//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        binding.buttonHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(ManagelistingFragment.this)
//                        .navigate(R.id.action_managelistingFragment_to_SecondFragment);
//            }
//        });
//    }
}