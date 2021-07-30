package com.example.mainactivity.more;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.mainactivity.R;
import com.example.mainactivity.retrofit.Post;

public class MoreAboutPostPresenter implements MoreAboutPostContract.Present {

    private String title, link, description, pubData, image_url;

    private final MoreAboutPostContract.View view;

    public MoreAboutPostPresenter(MoreAboutPostContract.View view) {
        this.view = view;
    }

    @Override
    public void openDetailsAuthor() {
        if (link == null || link.equals("")) {
            view.showSnackBar("No link now");
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            String choiceTitle = "Select a browser";
            Intent chooser = Intent.createChooser(intent, choiceTitle);
            view.openLinkAuthorPost(chooser);
        }
    }

    @Override
    public void setDataToPost(Fragment fragment) {
        Bundle bundle = fragment.getArguments();
        if (bundle != null) {
            Post post = bundle.getParcelable("post");
            this.title = post.getTitle();
            this.link = post.getLink();
            this.description = post.getDescription();
            this.pubData = post.getPubDate();
            this.image_url = post.getImageUrl();
        }

        if (image_url == null || image_url.equals("")) {
            view.getDefaultImageToPost(R.drawable.ic_iconfornoimage);
        } else {
            view.getImageToPost(image_url);
        }
        view.getDateToPost(title, description, pubData);
    }
}
