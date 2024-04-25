package com.example.doginder6.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.doginder6.R;

public class RecoveryFragment extends Fragment {

    private EditText emailEditText;
    private Button recoveryButton;

    public RecoveryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery, container, false);

        // Find views
        emailEditText = view.findViewById(R.id.emailEditText);
        recoveryButton = view.findViewById(R.id.recoveryButton);

        // Set click listener to the recovery button
        recoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle recovery button click
                String email = emailEditText.getText().toString().trim();
                // Check if the email is valid
                if (isValidEmail(email)) {
                    // Call method to send recovery email
                    sendRecoveryEmail(email);
                } else {
                    // Show error message indicating invalid email
                    Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // Method to check if the email is valid
    private boolean isValidEmail(String email) {
        // Implement your email validation logic here
        // For simplicity, this example only checks if the email is not empty
        return !TextUtils.isEmpty(email);
    }

    // Method to send recovery email
    private void sendRecoveryEmail(String email) {
        // Implement your logic to send recovery email to the server
        // For simplicity, this example just logs the email
        Log.d("RecoveryFragment", "Sending recovery email to: " + email);
    }
}

