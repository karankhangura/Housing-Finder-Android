package com.example.csci310project2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ViewResponsesAdapter extends RecyclerView.Adapter<ViewResponsesAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter2";

    public String[] mDataSet;
    public User mUser;
    public ArrayList<User> mRespondedUsers;
    private HousingPost mPost;
    private ViewResponsesFragment.ViewResponsesNavigation mNavigation;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private User mUser;

        public ViewHolder(View v, User user, HousingPost housingPost, ViewResponsesFragment.ViewResponsesNavigation navigation) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v.findViewById(R.id.textView);

                    Bundle result = new Bundle();
                    result.putSerializable("user", user);
                    result.putSerializable("post", housingPost);
                    result.putSerializable("acceptedUser", mUser);

                    navigation.passBundle(result);

                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
        public void setUser(User user) {
            mUser = user;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     *
     */
    public ViewResponsesAdapter(HousingPost post, User user, ArrayList<User> users, ViewResponsesFragment.ViewResponsesNavigation navigation) {
        mDataSet = new String[0];
        mUser = user;
        mNavigation = navigation;
        mRespondedUsers = users;
        mPost = post;
        Log.d("Debug", "ViewResponsesAdapter: " + mRespondedUsers.size());
        for (User u : users) {
            Log.d("Debug", "ViewResponsesAdapter: email = " + u.getEmail());
        }
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewResponsesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewResponsesAdapter.ViewHolder(v, mUser, mPost, mNavigation);
    }

    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewResponsesAdapter.ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set for user " + mRespondedUsers.get(position).getEmail() + " " + mRespondedUsers.get(position).getFirstName());

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        User currUser = mRespondedUsers.get(position);
        String userText = "User: " + currUser.getEmail() + "\nName: "
                + currUser.getFirstName() + " " + currUser.getLastName();
        if (currUser.getProfileInformation().containsKey("description")) {
            userText += "\nDescription: " + currUser.getProfileInformation().get("description");
        }
        viewHolder.getTextView().setText(userText);
//        viewHolder.getTextView().setText("User: " + mRespondedUsers.get(position).getEmail());
        viewHolder.setUser(mRespondedUsers.get(position));
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mRespondedUsers.size();
    }
}