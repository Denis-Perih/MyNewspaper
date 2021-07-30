package com.example.mainactivity.home;

import android.content.Context;
import android.net.Uri;

import com.example.mainactivity.home.adapter.RVHorizontalAdapter;
import com.example.mainactivity.home.adapter.RVVerticalAdapter;
import com.example.mainactivity.retrofit.Post;

import java.util.List;

public interface HomeContract {

    interface View {

        void buildNewsPost();

        void initializeUser(String tvPersonName, String tvEmail);

        void initializeUserWithOutPhoto(int icon, int iconWhite);

        void initializeUserWithPhoto(Uri photoUrl);

        void closeDrawer();

        void openChangeAccount();

        void activityFinish();

        void openMoreDetails(Post post);

        void setHorizontalAdapter(RVHorizontalAdapter rvHorizontalAdapter);

        void setVerticalAdapter(RVVerticalAdapter rvVerticalAdapter);

        void showScreenIfApiEmptyDatabaseEmpty();

        void showScreenIfApiEmptyDatabaseNoEmpty();

        void showScreenIfApiNoEmpty();

        void showScreenIfNoInternetDatabaseEmpty();

        void showScreenIfNoInternetDatabaseNotEmpty();

        void showScreenIfDatabaseErrorApiEmpty();

        void showScreenIfDatabaseErrorApiNoEmpty();
    }

    interface Presenter {

        void connectedToInternet(Context context);

        void initializeUser();

        void onClickedNavigationHome();

        void onClickedNavigationChangeAccount();

        void onClickedNavigationExit();

        void backPressed(Context context);

        void loadDatabase(List<Post> posts);

        void saveDatabase(List<Post> posts);
    }
}
