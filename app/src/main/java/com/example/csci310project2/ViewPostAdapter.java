package com.example.csci310project2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csci310project2.databinding.FragmentViewPostBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPostAdapter extends RecyclerView.Adapter<ViewPostAdapter.ViewHolder> {
    private static final String TAG = "ViewPostAdapter";
    private HousingPost mPost;
    private HashMap<String, String> mHousingInfo;
    private ArrayList<String> mHousingInfoOrdering;
    private User mUser;

    public ViewPostAdapter(HousingPost post, User user) {
        Log.d("Debug", "Initialized ViewPostAdapter");
        mPost = post;
        mHousingInfo = post.getHousingInformation();
        mHousingInfoOrdering = new ArrayList<>();
        mHousingInfoOrdering.add("address");
        mHousingInfoOrdering.add("rent");
        mHousingInfoOrdering.add("location");
        mHousingInfoOrdering.add("bed");
        mHousingInfoOrdering.add("utilities");
        mHousingInfoOrdering.add("schedule");
        mHousingInfoOrdering.add("academicFocus");
        mHousingInfoOrdering.add("personality");

        for (String key : mHousingInfo.keySet()) {
            if (!mHousingInfoOrdering.contains(key)) {
                mHousingInfoOrdering.add(key);
            }
        }

        mUser = user;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v, HousingPost post, User user) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v.findViewById(R.id.textView);
                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    @Override
    public ViewPostAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewPostAdapter.ViewHolder(v, mPost, mUser);
    }

    @Override
    public void onBindViewHolder(ViewPostAdapter.ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        Log.d("Debug", "ViewPostAdapter: position = " + position);
//        String[] x = (String[]) mHousingInfoOrdering.toArray();
//        viewHolder.getTextView().setText(x[position]);
        viewHolder.getTextView().setText(Utility.toTitleCase(mHousingInfoOrdering.get(position)) + ": " + mHousingInfo.get(mHousingInfoOrdering.get(position)));
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mHousingInfo.size();
    }
}