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
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import com.example.csci310project2.databinding.FragmentFiltersortBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FiltersortFragment extends Fragment {

    private FragmentFiltersortBinding binding;
    FirebaseDatabase root;
    DatabaseReference reference;
    User currentUser;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        Log.d("Debug", "FiltersortFragment onCreateView ran");

        if (binding != null) {
            return binding.getRoot();
        }

        Bundle prevBundle = getArguments();
        if (prevBundle != null) {
            currentUser = (User) prevBundle.getSerializable("user");
        } else {
            Log.d("Debug", "Failed to get user information from bundle in SecondFragment");
            currentUser = new User();
        }

        root = FirebaseDatabase.getInstance();
        reference = root.getReference("user");

        binding = FragmentFiltersortBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText filterByLocView = (EditText) view.findViewById(R.id.filterloc);
        EditText filterByRentView = (EditText) view.findViewById(R.id.filterrent);
        EditText filterByBedsView = (EditText) view.findViewById(R.id.filterbed);


        binding.buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);

                NavHostFragment.findNavController(FiltersortFragment.this)
                        .navigate(R.id.action_filtersortFragment_to_SecondFragment, result);
            }
        });

        binding.buttonSortloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);
                result.putString("criteria", "sortLoc");

                NavHostFragment.findNavController(FiltersortFragment.this)
                        .navigate(R.id.action_filtersortFragment_to_viewListingFragment, result);
            }
        });

        binding.buttonSortrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);
                result.putString("criteria", "sortRent");

                NavHostFragment.findNavController(FiltersortFragment.this)
                        .navigate(R.id.action_filtersortFragment_to_viewListingFragment, result);
//                NavHostFragment.findNavController(FiltersortFragment.this)
//                        .navigate(R.id.action_filtersortFragment_to_viewListingSort2Fragment, result);
            }
        });

        binding.buttonSortbed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);
                result.putString("criteria", "sortBeds");

                NavHostFragment.findNavController(FiltersortFragment.this)
                        .navigate(R.id.action_filtersortFragment_to_viewListingFragment, result);
//                NavHostFragment.findNavController(FiltersortFragment.this)
//                        .navigate(R.id.action_filtersortFragment_to_viewListingSort3Fragment, result);
            }
        });

        binding.buttonFilterloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);
                result.putString("criteria", "filterLoc");

                Log.d("Debug", "FiltersortFragment: " + filterByLocView.toString());
                result.putString("filterBy", filterByLocView.getText().toString());

                NavHostFragment.findNavController(FiltersortFragment.this)
                        .navigate(R.id.action_filtersortFragment_to_viewListingFragment, result);
//                NavHostFragment.findNavController(FiltersortFragment.this)
//                        .navigate(R.id.action_filtersortFragment_to_viewListingFilter1Fragment, result);
            }
        });

        binding.buttonFilterrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);
                result.putString("criteria", "filterRent");
                result.putString("filterBy", filterByRentView.getText().toString());

                NavHostFragment.findNavController(FiltersortFragment.this)
                        .navigate(R.id.action_filtersortFragment_to_viewListingFragment, result);
//                NavHostFragment.findNavController(FiltersortFragment.this)
//                        .navigate(R.id.action_filtersortFragment_to_viewListingFilter2Fragment, result);
            }
        });

        binding.buttonFilterbed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle result = new Bundle();
                result.putSerializable("user", currentUser);
                result.putString("criteria", "filterBeds");
                result.putString("filterBy", filterByBedsView.getText().toString());

                NavHostFragment.findNavController(FiltersortFragment.this)
                        .navigate(R.id.action_filtersortFragment_to_viewListingFragment, result);
//                NavHostFragment.findNavController(FiltersortFragment.this)
//                        .navigate(R.id.action_filtersortFragment_to_viewListingFilter3Fragment, result);
            }
        });
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }

}