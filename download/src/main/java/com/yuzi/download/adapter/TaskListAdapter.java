package com.yuzi.download.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.yuzi.download.R;
import com.yuzi.download.utils.DownloadRecord;
import com.yuzi.download.utils.DownloadUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.yuzi.download.utils.DownloadUtil.STATE_DOWNLOADING;
import static com.yuzi.download.utils.DownloadUtil.STATE_FAILED;
import static com.yuzi.download.utils.DownloadUtil.STATE_FINISHED;
import static com.yuzi.download.utils.DownloadUtil.STATE_INITIAL;
import static com.yuzi.download.utils.DownloadUtil.STATE_PAUSED;
import static com.yuzi.download.utils.DownloadUtil.STATE_REENQUEUE;


/**
 * Created by Y-zi on 2019/8/29
 * Github:https://github.com/Y-zi
 * QQ:992063180
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<DownloadRecord> records;

    public TaskListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        records = new ArrayList<>();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskViewHolder(mLayoutInflater.inflate(R.layout.item_download_task, parent, false));
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        final DownloadRecord record = records.get(position);
        switch (record.getDownloadState()){
            case STATE_DOWNLOADING:
                holder.btnState.setText("暂停");
                holder.tvProgress.setText(record.getProgress() + "%");
                break;
            case STATE_PAUSED:
                holder.btnState.setText("继续");
                holder.tvProgress.setText("已暂停");
                break;
            case STATE_INITIAL:
                holder.btnState.setText("等待开始");
                holder.tvProgress.setText("未开始");
                break;
            case STATE_FINISHED:
                holder.btnState.setText("打开");
                holder.tvProgress.setText("已完成");
                break;
            case STATE_REENQUEUE:
                holder.btnState.setText("等待开始");
                holder.tvProgress.setText("已暂停");
                break;
            case STATE_FAILED:
                holder.btnState.setText("重试");
                holder.tvProgress.setText("下载失败");
                break;
        }

        holder.btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (record.getDownloadState()){
                    case STATE_DOWNLOADING://暂停
                        DownloadUtil.get().pause(record.getId());
                        break;
                    case STATE_PAUSED://继续
                        DownloadUtil.get().reEnqueue(record.getId());
                        break;
                }

            }
        });
        holder.tvTaskName.setText(record.getDownloadName());
        holder.progressBar.setProgress(record.getProgress());
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position, List<Object> payloads) {
        DownloadRecord record = records.get(position);
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads);
        }else{
            holder.progressBar.setProgress(record.getProgress());
            holder.tvProgress.setText(record.getProgress() + "%");
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public DownloadRecord getItem(int position){
        return records.size() == 0 ? null : records.get(position);
    }

    public void setData(Collection data){
        records.clear();
        records.addAll(data);
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        TextView tvProgress;
        Button btnState;
        TextView tvTaskName;

        public TaskViewHolder(View view) {
            super(view);
            progressBar=view.findViewById(R.id.progressBar);
            tvProgress=view.findViewById(R.id.tvProgress);
            btnState=view.findViewById(R.id.btnState);
            tvTaskName=view.findViewById(R.id.tvTaskName);

        }
    }
}
