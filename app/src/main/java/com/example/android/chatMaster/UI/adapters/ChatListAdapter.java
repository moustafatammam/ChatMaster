package com.example.android.chatMaster.UI.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.android.chatMaster.data.Message;
import com.example.android.chatMaster.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;



public class ChatListAdapter extends BaseAdapter {
    private Context mContext;
    private List<DataSnapshot> mSnapShot;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;



    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mSnapShot.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Context mContext, DatabaseReference ref, String mDisplayName) {
        this.mContext = mContext;
        this.mDisplayName = mDisplayName;
        mSnapShot = new ArrayList<>();
        mDatabaseReference = ref.child("messages");
        mDatabaseReference.addChildEventListener(mListener);

    }

    private static class ViewHolder{
        TextView userName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapShot.size();
    }

    @Override
    public Message getItem(int position) {
        DataSnapshot dataSnapshot = mSnapShot.get(position);
        return dataSnapshot.getValue(Message.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_item, parent, false);

            final ViewHolder holder = new ViewHolder();
            holder.userName = view.findViewById(R.id.user_name);
            holder.body = view.findViewById(R.id.chat_msg);
            holder.params = (LinearLayout.LayoutParams) holder.userName.getLayoutParams();
            view.setTag(holder);

        }
        final Message message = getItem(position);
        final ViewHolder holder = (ViewHolder) view.getTag();


        String author = message.getUserName();
        holder.userName.setText(author);

        String msg = message.getMsg();
        holder.body.setText(msg);


        return view;

    }

    public void cleanup() {

        mDatabaseReference.removeEventListener(mListener);
    }


}
