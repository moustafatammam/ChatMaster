package com.example.android.chatMaster.UI.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.chatMaster.data.Message;
import com.example.android.chatMaster.R;
import com.example.android.chatMaster.UI.adapters.ChatListAdapter;
import com.example.android.chatMaster.databinding.FragmentChatBinding;
import com.example.android.chatMaster.viewmodel.SharedViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;


public class ChatFragment extends Fragment {
    private final String TAG = ChatFragment.class.getSimpleName();
    private FragmentChatBinding mFragmentChatBinding;
    private SharedViewModel sharedViewModel;
    private String userName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mAdapter;



    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentChatBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        return mFragmentChatBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupDisplayName();
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        mDatabaseReference = sharedViewModel.getDatabaseReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserInfo profile = user.getProviderData().get(0);
        String uid = profile.getUid();
        Log.d(TAG, "User id is: " + uid);
        mInputText = mFragmentChatBinding.message;
        mSendButton = mFragmentChatBinding.sendText;
        mChatListView = mFragmentChatBinding.chatList;


        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                sendMessage();
                return true;
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


    }

    private void sendMessage() {

        Log.d(TAG, "I sent something");
        String input = mInputText.getText().toString();
        if (!input.equals("")) {
            Message chat = new Message(input, userName);
            mDatabaseReference.child("messages").push().setValue(chat);
            mInputText.setText("");
        }

    }
    private void setupDisplayName(){

        SharedPreferences prefs = getActivity().getSharedPreferences(RegisterFragment.CHAT_PREFS, Context.MODE_PRIVATE);

        userName = prefs.getString(RegisterFragment.DISPLAY_NAME_KEY, null);

        if (userName == null) userName = "Anonymous";
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new ChatListAdapter(getContext(), mDatabaseReference, userName);
        mChatListView.setAdapter(mAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();

        mAdapter.cleanup();

    }
}
