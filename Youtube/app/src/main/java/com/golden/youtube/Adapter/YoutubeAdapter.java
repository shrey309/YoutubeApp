package com.golden.youtube.Adapter;
import android.content.Context;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.golden.youtube.R;
import com.golden.youtube.model.VideoItem;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
  Created by Shrey
 */


public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.MyViewHolder> {

    private Context mContext;
    private List<VideoItem> mVideoList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public TextView video_title, video_id, video_description;
        public RelativeLayout video_view;

        public MyViewHolder(View view) {

            super(view);

            //the video_item.xml file is now associated as view object
            //so the view can be called from view's object
            thumbnail = (ImageView) view.findViewById(R.id.video_thumbnail);
            video_title = (TextView) view.findViewById(R.id.video_title);
            video_id = (TextView) view.findViewById(R.id.video_id);
            video_description = (TextView) view.findViewById(R.id.video_description);
            video_view = (RelativeLayout) view.findViewById(R.id.video_view);
        }
    }


    public YoutubeAdapter(Context mContext, List<VideoItem> mVideoList) {
        this.mContext = mContext;
        this.mVideoList = mVideoList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final VideoItem singleVideo = mVideoList.get(position);

        //replace the default text with id, title and description with setText method
        holder.video_id.setText("Video ID : " + singleVideo.getId() + " ");
        holder.video_title.setText(singleVideo.getTitle());
        holder.video_description.setText(singleVideo.getDescription());


        Picasso.get()
                .load(singleVideo.getThumbnailURL())
                .resize(480, 270)
                .centerCrop()

                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
         return mVideoList.size();
    }

}