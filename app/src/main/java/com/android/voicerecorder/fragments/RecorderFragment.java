package com.android.voicerecorder.fragments;

import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.voicerecorder.R;
import com.android.voicerecorder.database.SqliteDatabase;
import com.android.voicerecorder.model.Record;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecorderFragment extends Fragment {

    MaterialButton list_btn;
    ConstraintLayout record_btn;
    Chronometer chronometer;
    ImageView small_circle, medium_circle, large_circle;
    boolean isRecording = false;
    TextView button_txt;
    Animation recordingAnimation;
    MediaRecorder mediaRecorder;
    String path;
    SqliteDatabase database;

    public RecorderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recorder, container, false);
        init(view);
        list_btn.setOnClickListener(view12 -> getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_down, R.anim.fade_out, R.anim.slide_up, R.anim.fade_out)
                .replace(R.id.main_activity_container, new HistoryFragment(), requireContext().getString(R.string.history_fragment))
                .addToBackStack(getTag()).commit());
        record_btn.setOnClickListener(view1 -> {
            if (isRecording) {
                stopRecording();
                Snackbar.make(view, requireContext().getString(R.string.new_record), BaseTransientBottomBar.LENGTH_SHORT)
                        .setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_grey))
                        .show();
            } else {
                startRecording();
            }
        });
        return view;
    }

    private void init(View view) {
        database=new SqliteDatabase(requireContext());
        list_btn = view.findViewById(R.id.recorder_fragment_list_button);
        record_btn = view.findViewById(R.id.recorder_fragment_record_button);
        chronometer = view.findViewById(R.id.recorder_fragment_chronometer);
        small_circle = view.findViewById(R.id.recorder_fragment_record_light_green_button);
        medium_circle = view.findViewById(R.id.recorder_fragment_record_green_button);
        large_circle = view.findViewById(R.id.recorder_fragment_record_dark_green_button);
        button_txt = view.findViewById(R.id.recorder_fragment_record_button_txt);
        recordingAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.active_button_animation);
        path = requireActivity().getExternalFilesDir("/").getAbsolutePath();
    }

    private void startRecording() {
        button_txt.setText(requireContext().getString(R.string.stop));
        small_circle.startAnimation(recordingAnimation);
        medium_circle.startAnimation(recordingAnimation);
        large_circle.startAnimation(recordingAnimation);
        record();
        isRecording = true;
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.stop();
        button_txt.setText(requireContext().getString(R.string.start));
        small_circle.clearAnimation();
        medium_circle.clearAnimation();
        large_circle.clearAnimation();
        isRecording = false;
    }

    private void record() {
        SimpleDateFormat simpleDateFormatRecord = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.US);
        Date date = new Date();
        String name = requireContext().getString(R.string.record_name) + simpleDateFormat.format(date) + ".3gp";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(path + "/" + name);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            database.insertRecord(new Record(name,simpleDateFormatRecord.format(date),path + "/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}