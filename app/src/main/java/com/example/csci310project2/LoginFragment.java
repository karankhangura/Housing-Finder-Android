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

import com.example.csci310project2.databinding.FragmentLoginBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    FirebaseDatabase root;
    DatabaseReference reference;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView emailView = (TextView) view.findViewById(R.id.loginEmailInput);
        TextView passView = (TextView) view.findViewById(R.id.loginPasswordInput);
        TextView errorView = (TextView) view.findViewById(R.id.loginErrorTextView);

        root = FirebaseDatabase.getInstance();
        reference = root.getReference("user");


        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = emailView.getText().toString();
                String passwordStr = passView.getText().toString();

                // authenticate and login
                DatabaseReference item = reference.child(Utility.encodeEmail(emailStr));

                item.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = new User(snapshot);
                        String realPassword = snapshot.child("password").getValue(String.class);

                        if (snapshot.hasChild("password") && user.getEmail().equals(emailStr) && realPassword.equals(passwordStr)) {
                            Bundle result = new Bundle();
                            result.putSerializable("user", user);
                            //getParentFragmentManager().setFragmentResult("SecondFragment", result);

                            Log.d("Debug", "Registered user: " + user.getEmail() + "  firstName: " + user.getFirstName());

                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_loginFragment_to_SecondFragment, result);
                        }
                        else {
                            errorView.setText("Failed to login. Email or password is incorrect");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Log.d("Debug", "Returned to LoginFragment after query");
            }
        });

        binding.buttonRegisterNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_FirstFragment);
            }
        });

    }

}