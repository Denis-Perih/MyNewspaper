package com.example.mainactivity.home.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainactivity.R;
import com.example.mainactivity.more.AnswerMoreDetails;
import com.example.mainactivity.retrofit.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVHorizontalAdapter extends RecyclerView.Adapter<RVHorizontalAdapter.HorizontalViewHolder> {

    List<Post> horizontalBlockNewsData;

    public AnswerMoreDetails answerMoreDetails;

    public RVHorizontalAdapter(List<Post> horizontalBlockNewsData, AnswerMoreDetails answerMoreDetails) {
        this.horizontalBlockNewsData = horizontalBlockNewsData;
        this.answerMoreDetails = answerMoreDetails;
    }

    public static class HorizontalViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvTitle;
        ImageView ivBackground;
        CardView cvHorizontalBlockNews;

        HorizontalViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvHorizontalBlockNewsDate);
            tvTitle = itemView.findViewById(R.id.tvHorizontalBlockNewsTitle);
            ivBackground = itemView.findViewById(R.id.ivImageHorizontalBlockNews);
            cvHorizontalBlockNews = itemView.findViewById(R.id.cvHorizontalBlockNews);
        }
    }

    @Override
    public int getItemCount() {
        return horizontalBlockNewsData.size();
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_horizontal_block_news,
                parent, false);
        return new HorizontalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        holder.cvHorizontalBlockNews.setOnClickListener(v -> {
            answerMoreDetails.onSuccessMoreDetails(horizontalBlockNewsData.get(position));
        });
        setDataToCard(holder, position);
    }

    private void setDataToCard(HorizontalViewHolder holder, int position) {
        holder.cvHorizontalBlockNews.setRadius(65f);
        holder.tvDate.setText(horizontalBlockNewsData.get(position).getPubDate());
        holder.tvTitle.setLines(3);
        holder.tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        holder.tvTitle.setText(horizontalBlockNewsData.get(position).getTitle());
        holder.ivBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (horizontalBlockNewsData.get(position).getImageUrl() == null || horizontalBlockNewsData.get(position).getImageUrl().equals("")) {
            holder.ivBackground.setImageResource(R.drawable.ic_iconfornoimage);
        } else {
            Picasso.get().load(horizontalBlockNewsData.get(position).getImageUrl()).into(holder.ivBackground);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
