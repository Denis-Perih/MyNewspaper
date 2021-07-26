package com.example.mainactivity.more;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mainactivity.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class MoreAboutPostFragment extends Fragment  {

    ConstraintLayout clMoreFragment;

    ImageButton ibBack;

    ImageView ivImageDetailsWindow;
    TextView tvDetailsTitle, tvDetailsDate, tvDetailsTextNews;
    Button btnDetailsAuthor;

    String title, link, description, pubData, image_url;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fr_more_about_post, container, false);

        clMoreFragment = v.findViewById(R.id.clMoreFragment);

        ibBack = v.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(v1 -> {
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            fm.popBackStack();
        });

        ivImageDetailsWindow = v.findViewById(R.id.ivImageDetailsWindow);
        tvDetailsTitle = v.findViewById(R.id.tvDetailsTitle);
        tvDetailsDate = v.findViewById(R.id.tvDetailsDate);
        tvDetailsTextNews = v.findViewById(R.id.tvDetailsTextNews);

        btnDetailsAuthor = v.findViewById(R.id.btnDetailsAuthor);
        btnDetailsAuthor.setOnClickListener(v12 -> openDetailsAuthor());

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.title = bundle.getString("title");
            this.link = bundle.getString("link");
            this.description = bundle.getString("description");
            this.pubData = bundle.getString("pubDate");
            this.image_url = bundle.getString("image_url");
        }

        setDataToPost();

        return v;
    }

    private void setDataToPost() {
        ivImageDetailsWindow.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (image_url == null || image_url.equals("")) {
            ivImageDetailsWindow.setImageResource(R.drawable.ic_iconfornoimage);
        } else {
            Picasso.get()
                    .load(image_url)
                    .into(ivImageDetailsWindow);
        }
        tvDetailsTitle.setText(title);
        tvDetailsDate.setText(pubData);
        tvDetailsTextNews.setText(description);
    }

    public void openDetailsAuthor() {
        if (link == null || link.equals("")) {
            Snackbar.make(clMoreFragment, "No link now", Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            String choiceTitle = "Select a browser";
            Intent chooser = Intent.createChooser(intent, choiceTitle);
            startActivity(chooser);
        }
    }
}
