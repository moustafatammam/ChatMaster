package com.example.android.chatMaster.UI.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.android.chatMaster.R;
import com.example.android.chatMaster.UI.viewmodel.SharedViewModel;
import com.example.android.chatMaster.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;
    private SharedViewModel mSharedViewModel;
    private NavController mController;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mSharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        mController = Navigation.findNavController(this, R.id.nav_host);
        NavigationUI.setupActionBarWithNavController(this, mController);
        mAuth = mSharedViewModel.getAuth();
        checkUserLogIn(mAuth);

    }

    private void checkUserLogIn(FirebaseAuth auth){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null){
            mController.navigate(R.id.loginFragment);
        }
    }

    @Override
    public void onBackPressed() {
        if (mController.getCurrentDestination().getId() == R.id.loginFragment){
            moveTaskToBack(true);
            finish();
        }
    }

}
