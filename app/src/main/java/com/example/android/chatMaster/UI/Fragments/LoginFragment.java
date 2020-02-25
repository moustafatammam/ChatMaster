package com.example.android.chatMaster.UI.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.chatMaster.R;
import com.example.android.chatMaster.viewmodel.SharedViewModel;
import com.example.android.chatMaster.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private final String TAG = LoginFragment.class.getSimpleName();

    private FragmentLoginBinding mFragmentLoginBinding;
    private SharedViewModel mSharedViewModel;
    private AutoCompleteTextView mEmailLogin;
    private EditText mPasswordLogin;
    private Button logInButton;
    private Button registrationButton;
    private FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return mFragmentLoginBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEmailLogin = mFragmentLoginBinding.loginEmail;
        mPasswordLogin = mFragmentLoginBinding.loginPassword;
        logInButton = mFragmentLoginBinding.loginSignInButton;
        registrationButton = mFragmentLoginBinding.loginRegisterButton;
        mSharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        mAuth = mSharedViewModel.getAuth();

        mPasswordLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == 100 || id == EditorInfo.IME_NULL) {
                    logIn();
                    return true;
                }
                return false;
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.login_register_button).navigate(R.id.registerFragment);
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

    }

    private void logIn() {

        String email = mEmailLogin.getText().toString();
        String password = mPasswordLogin.getText().toString();

        if (email.isEmpty()){
            if (email.equals("") || password.equals("")){
                return;
            }
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "registration in progress");
                if(!task.isSuccessful()){
                    showErrorDialog("Login failed");
                }else {
                    Navigation.findNavController(getActivity(), R.id.login_sign_in_button).navigate(R.id.chatFragment);
                }
            }
        });

    }

    private void showErrorDialog(String errorMsg) {
        new AlertDialog.Builder(getContext())
                .setTitle("sorry")
                .setMessage(errorMsg)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
