package com.example.android.chatMaster.UI.Fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.android.chatMaster.R;
import com.example.android.chatMaster.UI.viewmodel.SharedViewModel;
import com.example.android.chatMaster.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;


public class RegisterFragment extends Fragment {

    private final String TAG = RegisterFragment.class.getSimpleName();

    static final String CHAT_PREFS = "ChatPrefs";

    static final String DISPLAY_NAME_KEY = "username";

    private SharedViewModel mSharedViewModel;

    private FragmentRegisterBinding mFragmentRegisterBinding;
    private AutoCompleteTextView mRegisterUserName;
    private AutoCompleteTextView mRegisterEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mSignUpButton;

    private FirebaseAuth mAuth;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentRegisterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        return mFragmentRegisterBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRegisterUserName = mFragmentRegisterBinding.registerUsername;
        mRegisterEmail = mFragmentRegisterBinding.registerEmail;
        mPassword = mFragmentRegisterBinding.registerPassword;
        mConfirmPassword = mFragmentRegisterBinding.registerConfirmPassword;
        mSignUpButton = mFragmentRegisterBinding.registerSignUpButton;

        mSharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        mAuth = mSharedViewModel.getAuth();
        mConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == 200 || id == EditorInfo.IME_NULL) {
                    register();
                    return true;
                }
                return false;
            }
        });


        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });


    }

    private void register() {

        mRegisterEmail.setError(null);
        mPassword.setError(null);
        mRegisterUserName.setError(null);

        String email = mRegisterEmail.getText().toString();
        String password = mPassword.getText().toString();
        String userName = mRegisterUserName.getText().toString();

        Boolean cancel = false;
        View focusView = null;

        //check for valid password
        if(TextUtils.isEmpty(password) || !isPasswordValid(password)){
            Log.e(TAG, "inavalid password");
            mPassword.setError("The password does not match or too short");
            focusView = mPassword;
            cancel = true;
        }

        //check for valid email
        if(TextUtils.isEmpty(email)){
            mRegisterEmail.setError("Email is required");
            focusView = mRegisterEmail;
            cancel = true;
        }else if (!isEmailValid(email)){
            mRegisterEmail.setError("Email address in invalid");
            focusView = mRegisterEmail;
            cancel = true;
        }

        if(TextUtils.isEmpty(userName)){
            mRegisterUserName.setError("User's name is required");
            focusView = mRegisterUserName;
            cancel = true;
        }else if (!isUserValid(userName)){
            mRegisterUserName.setError("User's name is invalid");
            focusView = mRegisterUserName;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else{
            createFireBaseUser();
        }
    }

    private void createFireBaseUser() {
        String email = mRegisterEmail.getText().toString();
        String password = mPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "registration in progress");
                if(!task.isSuccessful()){
                    showErrorDialog("registration failed");
                }else {
                    saveUserName();
                    Navigation.findNavController(getActivity(), R.id.register_sign_up_button).navigate(R.id.loginFragment);

                }
            }
        });
    }



    private boolean isPasswordValid(String password){
        String confirmPassword = mConfirmPassword.getText().toString();
        return confirmPassword.equals(password) && password.length()>=6;
    }

    private boolean isEmailValid(String email){
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);

        if(email == null){
            return false;
        }

        return pattern.matcher(email).matches();
    }

    private boolean isUserValid(String userName){
      String regex = "[A-Za-z0-9]+";
      Pattern pattern = Pattern.compile(regex);
      if (userName == null){
          return false;
      }
      return pattern.matcher(userName).matches();
    }

    private void showErrorDialog(String errorMsg) {
        new AlertDialog.Builder(getContext())
                .setTitle("sorry")
                .setMessage(errorMsg)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void saveUserName(){
        String displayName = mRegisterUserName.getText().toString();
        SharedPreferences prefs = getActivity().getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply();
    }
}
