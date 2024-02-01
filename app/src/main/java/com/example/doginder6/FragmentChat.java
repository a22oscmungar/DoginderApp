package com.example.doginder6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class FragmentChat extends Fragment {
    View rootView;
    RecyclerView recyclerView;
    TextView noChats;
    ChatAdapter chatAdapter;
    List<ChatItem> chatItems;
    List<ChatItem> chatItemsVacio;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = rootView.findViewById(R.id.rvChats);
        noChats = rootView.findViewById(R.id.tvNoChats);

        chatItems = new ArrayList<>();
        chatItems.add(new ChatItem("Koke", "/uploads/1000000033.jpg"));
        chatItems.add(new ChatItem("Olaf", "/uploads/1000043705.jpg"));
        chatItems.add(new ChatItem("Nayla", "/uploads/1000051819.jpg"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter(); // Asegúrate de tener un adaptador llamado ChatAdapter
        recyclerView.setAdapter(chatAdapter);
        ;
        chatAdapter.setChatItems((ArrayList<ChatItem>) chatItems);

        if(chatItems.size() == 0) {
            noChats.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            noChats.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }


        SharedPreferences preferences = rootView.getContext().getSharedPreferences("credenciales", rootView.getContext().MODE_PRIVATE);


        return rootView;
    }
}
