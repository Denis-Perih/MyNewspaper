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

public class RVVerticalAdapter extends RecyclerView.Adapter<RVVerticalAdapter.VerticalViewHolder> {

    List<Post> verticalBlockNewsData;

    public AnswerMoreDetails answerMoreDetails;

    public RVVerticalAdapter(List<Post> verticalBlockNewsData, AnswerMoreDetails answerMoreDetails) {
        this.verticalBlockNewsData = verticalBlockNewsData;
        this.answerMoreDetails = answerMoreDetails;
    }

    public static class VerticalViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvTitle;
        ImageView ivBackground;
        CardView cvVerticalBlockNews;

        VerticalViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvVerticalBlockNewsDate);
            tvTitle = itemView.findViewById(R.id.tvVerticalBlockNewsTitle);
            ivBackground = itemView.findViewById(R.id.ivImageVerticalBlockNews);
            cvVerticalBlockNews = itemView.findViewById(R.id.cvVerticalBlockNews);
        }
    }

    @Override
    public int getItemCount() {
        return verticalBlockNewsData.size();
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_vertical_block_news,
                parent, false);
        return new VerticalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {
        holder.cvVerticalBlockNews.setOnClickListener(v -> {
            answerMoreDetails.onSuccessMoreDetails(verticalBlockNewsData.get(position));
        });
        setDataToCard(holder, position);
    }

    private void setDataToCard(VerticalViewHolder holder, int position) {
        holder.tvDate.setText(verticalBlockNewsData.get(position).getPubDate());
        holder.tvTitle.setLines(3);
        holder.tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        holder.tvTitle.setText(verticalBlockNewsData.get(position).getTitle());
        holder.ivBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (verticalBlockNewsData.get(position).getImageUrl() == null || verticalBlockNewsData.get(position).getImageUrl().equals("")) {
            holder.ivBackground.setImageResource(R.drawable.ic_iconfornoimage);
        } else {
            Picasso.get().load(verticalBlockNewsData.get(position).getImageUrl()).into(holder.ivBackground);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
