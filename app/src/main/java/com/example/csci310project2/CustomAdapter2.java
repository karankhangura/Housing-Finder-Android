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
public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.ViewHolder> {
    private static final String TAG = "CustomAdapter2";

    private String[] mDataSet;
    public User mUser;
    private ArrayList<HousingPost> mPosts;
    private ManagelistingFragment.ManageListingNavigation mNavigation;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private HousingPost mPost;

        public ViewHolder(View v, User user, ManagelistingFragment.ManageListingNavigation navigation) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v.findViewById(R.id.textView);

                    Bundle result = new Bundle();
                    result.putSerializable("user", user);
                    result.putSerializable("post", mPost);

                    navigation.passBundle(result);

                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
        public void setPost(HousingPost post) {
            mPost = post;
        }

    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CustomAdapter2(String[] dataSet, ArrayList<HousingPost> posts, User user, ManagelistingFragment.ManageListingNavigation navigation) {

        mDataSet = dataSet;
        mUser = user;
        mPosts = posts;
        mNavigation = navigation;
    }

    public CustomAdapter2(String[] dataset, User user) {
        mDataSet = dataset;
        mUser = user;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public CustomAdapter2.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new CustomAdapter2.ViewHolder(v, mUser, mNavigation);
    }

    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CustomAdapter2.ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(mDataSet[position]);
        viewHolder.setPost(mPosts.get(position));
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}