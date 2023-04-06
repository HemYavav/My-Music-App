package com.example.mymusicapp.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusicapp.MainActivity;
import com.example.mymusicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {

    private TextView dontHaveAnAccount;
    private  TextView resetPassword;
    private FrameLayout frameLayout;

    private EditText  email, password;

    private Button signInButton;

    private FirebaseAuth mAuth;

    private ProgressBar SignInProgress;
    private Drawable errorIcon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        dontHaveAnAccount = view.findViewById(R.id.dont_have_an_account);
        resetPassword = view.findViewById(R.id.reset_password);
        frameLayout = getActivity().findViewById(R.id.register_frame_layout);


        email  = view.findViewById(R.id.email);
        password  = view.findViewById(R.id.password);
        signInButton  = view.findViewById(R.id.signInButton);

        SignInProgress = view.findViewById(R.id.SignInProgress);

        errorIcon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorIcon.setBounds(0,0,errorIcon.getIntrinsicWidth(),errorIcon.getIntrinsicHeight());

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragment(new ResetPasswordFragment());
            }
        });


        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInButton.setEnabled(false);
                signInButton.setTextColor(getResources().getColor(R.color.white));
                signInWithFirebase();
            }
        });
    }

    private void signInWithFirebase() {
        if(email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            SignInProgress.setVisibility(View.INVISIBLE);

            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        SignInProgress.setVisibility(View.INVISIBLE);
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                        else{
                            Toast.makeText(getContext(), task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            signInButton.setEnabled(true);
                            signInButton.setTextColor(getResources().getColor(R.color.white));

                        }
                    }
                });

        }else{
            email.setError("Invalid Email Pattern!",errorIcon);

            signInButton.setEnabled(true);
            signInButton.setTextColor(getResources().getColor(R.color.white));

        }




    }

    private void checkInputs(){


            if(!email.getText().toString().isEmpty()){
                    if(!password.getText().toString().isEmpty()){
                        signInButton.setEnabled(true);
                        signInButton.setTextColor(getResources().getColor(R.color.white));
                    }
                else{
                    signInButton.setEnabled(false);
                        signInButton.setTextColor(getResources().getColor(R.color.white));
                  }

           }
          else{
                signInButton.setEnabled(false);
                signInButton.setTextColor(getResources().getColor(R.color.white));

          }


    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_right,R.anim.out_from_left);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();

    }

}