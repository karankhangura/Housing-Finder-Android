package com.example.csci310project2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import com.example.csci310project2.databinding.FragmentViewlistingBinding;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

//sort by relative loc to uni
//https://github.com/android/views-widgets-samples/tree/main/RecyclerView/ Used for reference
public class ViewListingFragment extends Fragment {
    private FragmentViewlistingBinding binding;
    FirebaseDatabase root;
    DatabaseReference reference;
    User currentUser;
    ArrayList<HousingPost> posts;
    String criteria;
    String filterByValue;

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 20;

    boolean adapterSet;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    public interface ViewListingNavigation {
        public void passBundle(Bundle bundle);
    }

    protected LayoutManagerType mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;


    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    private View mRootView;
    private User mCurrentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        adapterSet = false;

        Bundle prevBundle = getArguments();
        if (prevBundle != null) {
            currentUser = (User) prevBundle.getSerializable("user");
            criteria = prevBundle.getString("criteria");
            filterByValue = prevBundle.getString("filterBy");

            //Log.d("Debug", "ViewListingFragment.onCreateView: Found currentUser from bundle");
            //Log.d("Debug", "ViewListingFragment: criteria: " + criteria + " filter by " + filterByValue);
        } else {
            //Log.d("Debug", "Failed to get user information from bundle in ViewListFragment");
            currentUser = new User();
        }

        root = FirebaseDatabase.getInstance();
        reference = root.getReference().child("post");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts = new ArrayList<>();
                ArrayList<HashMap<String, String>> postList = new ArrayList<>();
                for (DataSnapshot userList : snapshot.getChildren()) {

                    if (userList.getKey().equals(Utility.encodeEmail(currentUser.getEmail()))) {
                        continue;
                    }

                    // get user for posts
                    User currentListUser = new User();
                    DatabaseReference userRef = root.getReference().child("user").child(userList.getKey());
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                            currentListUser.copy(new User(snapshotUser));
                            //Log.d("Debug", "Got user: " + currentListUser.getEmail());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    for (DataSnapshot userPost : userList.getChildren()) {

                        HashMap<String, String> items = new HashMap<>();
                        for (DataSnapshot userPostItem : userPost.child("housingInfo").getChildren()) {
                            items.put(userPostItem.getKey(), userPostItem.getValue(String.class));
                            //Log.d("Debug", userList.getKey() + ": " + userPostItem.getKey() + " " + userPostItem.getValue(String.class));
                        }
                        postList.add(items);
                        HousingPost housingPost = new HousingPost();
                        housingPost.setDeadline(new Date());
                        housingPost.setUser(currentListUser);
                        currentListUser.setEmail(userList.getKey());

                        housingPost.setHousingInformation(items);
                        housingPost.setHousingPostName(userPost.child("postName").getValue(String.class));

                        posts.add(housingPost);

                        //Log.d("Debug", "Added another post: " + posts.size());
                    }

                    ArrayList<HousingPost> filteredList = filterHousingPosts(posts, criteria, filterByValue);

                    mDataset = new String[filteredList.size()];
                    for (int i = 0; i < mDataset.length; i++) {
                        mDataset[i] = filteredList.get(i).toString();
                    }

                    ViewListingNavigation navigation = new ViewListingNavigation() {
                        @Override
                        public void passBundle(Bundle bundle) {
                            //Log.d("Debug", "Nav to filtersort");

                            NavHostFragment.findNavController(ViewListingFragment.this)
                                    .navigate(R.id.action_viewListingFragment_to_viewPostFragment, bundle);
                        }
                    };

                    mAdapter = new CustomAdapter(mDataset, filteredList, currentUser, navigation);
                    mRecyclerView.setAdapter(mAdapter);
                    adapterSet = true;
                }
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

        View rootView  = inflater.inflate(R.layout.recycler_view_frag, container, false);
        mRootView = rootView;
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);



//        ArrayList<HousingPost> repoInfo = new ArrayList<>();
//
//        Collections.sort(repoInfo, new Comparator<HousingPost>() {
//            @Override
//            public int compare(HousingPost first, HousingPost second) {
//                int one = Integer.valueOf(first.getHousingInformation().get("location"));
//                int two = Integer.valueOf(second.getHousingInformation().get("location"));
//                return Integer.compare(one, two);
//            }
//        });



        if (!adapterSet) {
            // set default value
            mAdapter = new CustomAdapter(new String[0], new ArrayList<>(), currentUser, new ViewListingNavigation() {
                @Override
                public void passBundle(Bundle bundle) {

                }
            });
            // Set CustomAdapter as the adapter for RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
            // END_INCLUDE(initializeRecyclerView)
            adapterSet = true;

//            binding.buttonHome.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    NavHostFragment.findNavController(ViewListingFragment.this)
//                            .navigate(R.id.action_viewListingFragment_to_SecondFragment);
//                }
//            });
        }


        return rootView;
    }

    public static ArrayList<HousingPost> filterHousingPosts(ArrayList<HousingPost> housingPosts, String criteriaStr, String filterBy) {
        ArrayList<HousingPost> filteredList = new ArrayList<>();

        //Log.d("Debug", "Filtering based on criteria: " + criteriaStr + " by " + filterBy);

        switch (criteriaStr) {
            case "sortLoc":
                filteredList = (ArrayList<HousingPost>) housingPosts.clone();
                Collections.sort(filteredList, new Comparator<HousingPost>() {
                            @Override
                            public int compare(HousingPost first, HousingPost second) {
//                                int one = Integer.valueOf(first.getHousingInformation().get("location"));
//                                int two = Integer.valueOf(second.getHousingInformation().get("location"));

                                return first.getHousingInformation().get("location").compareTo(second.getHousingInformation().get("location"));
                            }
                        });
                    filteredList = housingPosts;
                break;
            case "filterLoc":
                for (HousingPost housingPost : housingPosts) {
                    if (housingPost.getHousingInformation().get("location").equals(filterBy)) {
                        filteredList.add(housingPost);
                    }
                }
                break;
            case "sortRent":
                filteredList = (ArrayList<HousingPost>) housingPosts.clone();
                Collections.sort(filteredList, new Comparator<HousingPost>() {
                    @Override
                    public int compare(HousingPost first, HousingPost second) {
//                        int one = Integer.valueOf(first.getHousingInformation().get("rent"));
//                        int two = Integer.valueOf(second.getHousingInformation().get("rent"));
//                        return Integer.compare(one, two);

                        return first.getHousingInformation().get("rent").compareTo(second.getHousingInformation().get("rent"));
                    }
                });
                break;
            case "filterRent":
                for (HousingPost housingPost : housingPosts) {
                    if (housingPost.getHousingInformation().get("rent").equals(filterBy)) {
                        filteredList.add(housingPost);
                    }
                }
                break;
            case "sortBeds":
                filteredList = (ArrayList<HousingPost>) housingPosts.clone();
                Collections.sort(filteredList, new Comparator<HousingPost>() {
                    @Override
                    public int compare(HousingPost first, HousingPost second) {
//                        int one = Integer.valueOf(first.getHousingInformation().get("bed"));
//                        int two = Integer.valueOf(second.getHousingInformation().get("bed"));
//                        return Integer.compare(one, two);

                        return first.getHousingInformation().get("bed").compareTo(second.getHousingInformation().get("bed"));
                    }
                });
                break;
            case "filterBeds":
                for (HousingPost housingPost : housingPosts) {
                    if (housingPost.getHousingInformation().get("bed").equals(filterBy)) {
                        filteredList.add(housingPost);
                    }
                }
                break;
        }
        return filteredList;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
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

//
//    }


//    private FragmentViewlistingBinding binding;
//    FirebaseDatabase root;
//    DatabaseReference reference;
//
//    @Override
//    public View onCreateView(
//            LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState
//    ) {
//
//        binding = FragmentViewlistingBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//
//    }
//
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        TextView welcomeMessage = (TextView) view.findViewById(R.id.viewListings);
//        TextView textView = (TextView) view.findViewById(R.id.listingsField);
//
//        root = FirebaseDatabase.getInstance();
//        reference = root.getReference("user");
//
//        // Read from the database
//        reference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//                reference = root.getReference("user");
//                HashMap<String, Object> changedChild = (HashMap<String, Object>) dataSnapshot.getValue();
//                welcomeMessage.setText("Welcome, " + changedChild.get("firstName") + "!");
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
//        /*
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
//                textView.setText(value.toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                textView.setText("Error in retrieving your message!");
//                Log.w("SecondFragment", "Failed to read value.", error.toException());
//            }
//        });
//         */
//
//        reference = root.getReference("post");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
//                if (value != null) {
//                    textView.setText(value.toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                textView.setText("Error in retrieving your message!");
//                Log.w("ViewListingFragment", "Failed to read value.", error.toException());
//            }
//        });
//

//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}