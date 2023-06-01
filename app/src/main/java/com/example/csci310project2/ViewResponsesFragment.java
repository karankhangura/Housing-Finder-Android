package com.example.csci310project2;

import android.os.Bundle;
import android.telephony.BarringInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.csci310project2.databinding.FragmentViewresponsesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewResponsesFragment extends Fragment {

    private FragmentViewresponsesBinding binding;
    FirebaseDatabase root;
    DatabaseReference reference;
    User currentUser;
    HousingPost currentPost;
    private boolean adapterSet;

    protected RecyclerView mRecyclerView;
    protected ViewResponsesAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private View mRootView;
    private ArrayList<User> mUsers;

    private static final String TAG = "ViewPostFragment";
    private static final int SPAN_COUNT = 20;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected ViewResponsesFragment.LayoutManagerType mCurrentLayoutManagerType = ViewResponsesFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

    public interface ViewResponsesNavigation {
        public void passBundle(Bundle bundle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle prevBundle = getArguments();
        if (prevBundle != null) {
            currentUser = (User) prevBundle.getSerializable("user");
            currentPost = (HousingPost) prevBundle.getSerializable("post");
            Log.d("Debug", "ViewListingFragment.onCreateView: Found currentUser from bundle");
        } else {
            Log.d("Debug", "Failed to get user information from bundle in ViewListFragment");
            currentUser = new User();
            currentPost = new HousingPost();
        }

        root = FirebaseDatabase.getInstance();
        reference = root.getReference().child("post");
        reference = reference.child(Utility.encodeEmail(currentUser.getEmail()));
        reference = reference.child(currentPost.getHousingPostName());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AtomicInteger foundUsers = new AtomicInteger(0);
                HashMap<String, String> items = new HashMap<>();
                for (DataSnapshot userPostItem : snapshot.child("housingInfo").getChildren()) {
                    items.put(userPostItem.getKey(), userPostItem.getValue(String.class));
                }

                ViewResponsesFragment.ViewResponsesNavigation navigation = new ViewResponsesNavigation() {
                    @Override
                    public void passBundle(Bundle bundle) {
                        Log.d("Debug", "Nav to response");

                        NavHostFragment.findNavController(ViewResponsesFragment.this)
                                .navigate(R.id.action_viewResponsesFragment_to_decideResponseFragment, bundle);
                    }
                };

                HousingPost housingPost = new HousingPost();
                housingPost.setDeadline(new Date());
                housingPost.setUser(currentUser);

                housingPost.setHousingInformation(items);
                housingPost.setHousingPostName(snapshot.getKey());

                mUsers = new ArrayList<>();
                for (DataSnapshot userResponded : snapshot.child("accepted").getChildren()) {
                    String userEmail = userResponded.getValue(String.class);

                    User currentListUser = new User();
                    mUsers.add(currentListUser);
                    DatabaseReference userRef = root.getReference().child("user").child(userEmail);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                            currentListUser.copy(new User(snapshotUser));
                            Log.d("Debug", "Got user: " + currentListUser.getEmail());

                            mAdapter = new ViewResponsesAdapter(currentPost, currentUser, mUsers, navigation);
                            // Set CustomAdapter as the adapter for RecyclerView.
                            mRecyclerView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                mAdapter = new ViewResponsesAdapter(currentPost, currentUser, mUsers, navigation);
                // Set CustomAdapter as the adapter for RecyclerView.
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

//        if (binding != null) {
//            return binding.getRoot();
//       }

        if (mRootView != null) {
            return mRootView;
        }

        //binding = FragmentViewresponsesBinding.inflate(inflater, container, false);

        View rootView  = inflater.inflate(R.layout.fragment_viewresponses, container, false);
        mRootView = rootView;
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.viewResponsesRecyclerView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = ViewResponsesFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (ViewResponsesFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        ViewResponsesFragment.ViewResponsesNavigation navigation = new ViewResponsesNavigation() {
            @Override
            public void passBundle(Bundle bundle) {
            }
        };

        if (!adapterSet && false) {
            mAdapter = new ViewResponsesAdapter(currentPost, currentUser, new ArrayList<>(), navigation);
            // Set CustomAdapter as the adapter for RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
            adapterSet = true;
        }

        // Inflate the layout for this fragment
        //return binding.getRoot();
        return rootView;
    }


    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(ViewResponsesFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = ViewResponsesFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = ViewResponsesFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = ViewResponsesFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
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
}