package com.example.android.chatMaster.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.chatMaster.R;
import com.example.android.chatMaster.UI.viewmodel.SharedViewModel;
import com.google.firebase.auth.FirebaseAuth;


public class UsersFragment extends Fragment {

    private FirebaseAuth mAuth;
    private SharedViewModel mSharedViewModel;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mSharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        mAuth = mSharedViewModel.getAuth();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                mAuth.signOut();

                NavHostFragment.findNavController(this).navigate(R.id.loginFragment);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
