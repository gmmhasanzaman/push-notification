package com.example.androidnotification;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.androidnotification.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private Context context;
    private FragmentSignUpBinding binding;
    private FirebaseAuth firebaseAuth;

    private static final String PUSH_NOTIFICATION_APP = "Android Notifications App";
    private DatabaseReference databaseReference;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        initViews();

    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("notifications");
        databaseReference = FirebaseDatabase.getInstance().getReference(PUSH_NOTIFICATION_APP);
    }

    private void initViews() {
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        binding.signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSignInFragment();
            }
        });
    }


    private void signUp() {

        String email = binding.emailET.getText().toString().trim();
        String password = binding.passwordET.getText().toString().trim();
        String conPassword = binding.conPasswordET.getText().toString();

        if (email.isEmpty() || password.isEmpty() || conPassword.isEmpty()) {
            Toast.makeText(context, "Please fill it!", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.signUpPB.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    createToken();
                    startHomeFragment();

                } /*else {
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }*/
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error: -"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                binding.signUpPB.setVisibility(View.GONE);

            }
        });
    }

    private void createToken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    String token = task.getResult().getToken();
                    saveToken(token);

                }
            }
        });
    }

    private void saveToken(String token) {

        String email = firebaseAuth.getCurrentUser().getEmail();
        String userId = firebaseAuth.getCurrentUser().getUid();
        User user = new User(email, token);

        DatabaseReference userRef = databaseReference.child("users");

        //String userId = userRef.push().getKey();
        user.setUserId(userId);
        userRef.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Token Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "error code- " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                binding.signUpPB.setVisibility(View.GONE);
            }
        });

    }

    private void goSignInFragment() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }

    }

    private void startHomeFragment() {
        if (getActivity() != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragment = new HomeFragment();
            ft.replace(R.id.fragmentContainer, fragment)
                    .commit();
        }

    }
}
