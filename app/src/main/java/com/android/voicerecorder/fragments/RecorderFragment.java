package com.android.voicerecorder.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.voicerecorder.R;
import com.android.voicerecorder.adapter.ListAdapter;

public class RecorderFragment extends Fragment {

    RecyclerView recyclerView;
    ListAdapter adapter;

    public RecorderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recorder, container, false);
        recyclerView = view.findViewById(R.id.history_fragment_recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new ListAdapter(requireContext(), );
        adapter.setOnItemClickListener(record -> {

        });
        recyclerView.setAdapter(adapter);
        return view;
    }
}