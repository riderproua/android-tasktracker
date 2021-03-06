package com.serhiipianykh.tasktracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by serhiipianykh on 2017-02-24.
 */

public class LoginFragment extends Fragment {

    private TextInputEditText emailField, passwordField;
    private Button signIn, signUp, forgotPassword;
    private FirebaseAuth auth;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(getContext(),TasksActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        emailField = (TextInputEditText)v.findViewById(R.id.email_field);
        passwordField = (TextInputEditText)v.findViewById(R.id.password_field);

        signIn = (Button)v.findViewById(R.id.login_btn);
        signUp = (Button)v.findViewById(R.id.create_account_btn);
        forgotPassword = (Button)v.findViewById(R.id.forgot_pass);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                final String password = passwordField.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), getString(R.string.empty_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), getString(R.string.empty_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                passwordField.setError(getString(R.string.password_length));
                            } else {
                                Toast.makeText(getContext(), getString(R.string.login_fail),Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent intent = new Intent(getContext(), TasksActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignUpActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return v;
    }
}
