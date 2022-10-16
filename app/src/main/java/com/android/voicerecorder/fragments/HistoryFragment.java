package com.android.voicerecorder.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.voicerecorder.R;
import com.android.voicerecorder.adapter.ListAdapter;
import com.android.voicerecorder.database.SqliteDatabase;
import com.android.voicerecorder.model.Record;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;
    ListAdapter adapter;
    List<Record> recordList;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        SqliteDatabase database = new SqliteDatabase(requireContext());
        recordList = database.getAllRecords();
        recyclerView = view.findViewById(R.id.history_fragment_recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new ListAdapter(requireContext(), recordList);
        adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Record record) {

            }

            @Override
            public void onLikeClick(Record record, int position) {
                database.updateRecord(record);
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onDeleteClick(Record record, int position) {
                new AlertDialog.Builder(requireContext())
                        .setTitle(getResources().getString(R.string.delete))
                        .setMessage(getResources().getString(R.string.delete_question))
                        .setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> {
                        }).setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> {
                            database.deleteRecord(record.getId());
                            recordList.remove(position);
                            adapter.notifyItemRemoved(position);
                            Snackbar.make(view, getResources().getString(R.string.record_delete), BaseTransientBottomBar.LENGTH_SHORT)
                                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_grey))
                                    .show();
                        }).setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.icon_delete))
                        .show();
            }

            @Override
            public void onPlayClick(Record record, boolean play) {

            }
        });
        recyclerView.setAdapter(adapter);
    }
}