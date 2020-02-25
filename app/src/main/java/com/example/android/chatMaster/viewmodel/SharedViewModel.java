package com.example.android.chatMaster.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SharedViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;


    private final String TAG = SharedViewModel.class.getSimpleName();

    public SharedViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("messages");
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public DatabaseReference getDatabaseReference() {
        return mDatabaseReference;
    }

}
