package com.example.mainactivity.more;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.mainactivity.R;

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
            this.title = bundle.getString("title");
            this.link = bundle.getString("link");
            this.description = bundle.getString("description");
            this.pubData = bundle.getString("pubDate");
            this.image_url = bundle.getString("image_url");
        }

        if (image_url == null || image_url.equals("")) {
            view.getDefaultImageToPost(R.drawable.ic_iconfornoimage);
        } else {
            view.getImageToPost(image_url);
        }
        view.getDateToPost(title, description, pubData);
    }
}
