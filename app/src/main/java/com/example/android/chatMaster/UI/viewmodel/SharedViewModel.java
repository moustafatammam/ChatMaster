package com.example.android.chatMaster.UI.viewmodel;


import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;


public class SharedViewModel extends ViewModel {

    private FirebaseAuth mAuth;

    private final String TAG = SharedViewModel.class.getSimpleName();

    public SharedViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }


}
