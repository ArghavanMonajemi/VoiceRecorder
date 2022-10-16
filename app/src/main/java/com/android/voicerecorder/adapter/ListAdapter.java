package com.android.voicerecorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.voicerecorder.R;
import com.android.voicerecorder.model.Record;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    Context context;
    List<Record> records;
    OnItemClickListener onItemClickListener;

    public ListAdapter(Context context, List<Record> records) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int i) {
        Record record = records.get(i);
        listViewHolder.name_txt.setText(record.getName());
        listViewHolder.date_txt.setText(record.getDate());
        listViewHolder.start_txt.setText(context.getString(R.string.start_time));
        listViewHolder.extra_layout.setVisibility(View.GONE);
        if (record.isLike()) {
            listViewHolder.like = true;
            listViewHolder.like_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_fill_heart));
        } else {
            listViewHolder.like = false;
            listViewHolder.like_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_heart));
        }
        //Todo:set like and end
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageButton like_btn, play_btn, delete_btn;
        LinearLayout item_layout, extra_layout;
        TextView date_txt, start_txt, end_txt;
        EditText name_txt;
        SeekBar seekBar;
        boolean play = false, like = false;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            like_btn = itemView.findViewById(R.id.history_item_like_btn);
            play_btn = itemView.findViewById(R.id.history_item_play_btn);
            delete_btn = itemView.findViewById(R.id.history_item_delete_btn);
            item_layout = itemView.findViewById(R.id.history_item_layout);
            extra_layout = itemView.findViewById(R.id.history_item_extra_layout);
            name_txt = itemView.findViewById(R.id.history_item_name);
            date_txt = itemView.findViewById(R.id.history_item_date);
            start_txt = itemView.findViewById(R.id.history_item_start);
            end_txt = itemView.findViewById(R.id.history_item_end);
            seekBar = itemView.findViewById(R.id.history_item_seekbar);
            item_layout.setOnClickListener(view -> {
                if (onItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    if (getOldPosition() >= 0)
                        ListAdapter.this.notifyItemChanged(getOldPosition());
                    extra_layout.setVisibility(View.VISIBLE);
                    onItemClickListener.onItemClick(records.get(getAdapterPosition()));
                }
            });
            like_btn.setOnClickListener(view -> {
                if (onItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Record record = records.get(getAdapterPosition());
                    record.setLike(!record.isLike());
                    records.set(getAdapterPosition(),record);
                    if (like)
                        like_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_heart));
                    else
                        like_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_fill_heart));
                    onItemClickListener.onLikeClick(records.get(getAdapterPosition()),getAdapterPosition());
                }
            });
            delete_btn.setOnClickListener(view -> {
                if (onItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClickListener.onDeleteClick(records.get(getAdapterPosition()),getAdapterPosition());
                }
            });
            play_btn.setOnClickListener(view -> {
                if (onItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    if (play)
                        play_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_play_arrow));
                    else
                        play_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_pause));
                    onItemClickListener.onPlayClick(records.get(getAdapterPosition()),play);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Record record);

        void onLikeClick(Record record,int position);

        void onDeleteClick(Record record,int position);

        void onPlayClick(Record record,boolean play);
    }
}
