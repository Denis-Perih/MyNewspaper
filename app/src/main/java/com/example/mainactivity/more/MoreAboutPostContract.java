package com.example.mainactivity.more;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.mainactivity.retrofit.Post;

public interface MoreAboutPostContract {

    interface View {

        void showSnackBar(String text);

        void getDateToPost(String title, String description, String pubData);

        void openLinkAuthorPost(Intent chooser);

        void getImageToPost(String image_url);

        void getDefaultImageToPost(int image);
    }

    interface Present {

        void openDetailsAuthor();

        void setDataToPost(Post post);
    }
}
