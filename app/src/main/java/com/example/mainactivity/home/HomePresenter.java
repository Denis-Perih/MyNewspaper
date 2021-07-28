package com.example.mainactivity.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.mainactivity.R;
import com.example.mainactivity.database.AppDatabase;
import com.example.mainactivity.database.DatabaseApplication;
import com.example.mainactivity.database.PostDatabase;
import com.example.mainactivity.home.rv_adapter.RVHorizontalAdapter;
import com.example.mainactivity.home.rv_adapter.RVVerticalAdapter;
import com.example.mainactivity.login_signup.firebase.UsersData;
import com.example.mainactivity.more.AnswerMoreDetails;
import com.example.mainactivity.retrofit.AnswerJSON;
import com.example.mainactivity.retrofit.GetObjectJSON;
import com.example.mainactivity.retrofit.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter, AnswerJSON, AnswerMoreDetails {

    private static final int INTERNET_CONNECTED = 1;
    private static final int INTERNET_NOT_CONNECTED = 2;
    private static int internet;

    RVHorizontalAdapter rvHorizontalAdapter;
    RVVerticalAdapter rvVerticalAdapter;

    private final List<Post> horizontalBlockNewsData = new ArrayList<>();
    private final List<Post> verticalBlockNewsData = new ArrayList<>();

    private final List<Post> blockNewsData = new ArrayList<>();

    private final AppDatabase db = DatabaseApplication.getInstance().getDatabase();

    private final HomeContract.View view;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void connectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            GetObjectJSON getObjectJSON = new GetObjectJSON();
            getObjectJSON.objectJSON(this);
            internet = INTERNET_CONNECTED;
        } else {
            loadDatabase(null);
            internet = INTERNET_NOT_CONNECTED;
        }
    }

    @Override
    public void initializeUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UsersData usersData = new UsersData();
        if (user != null) {
            usersData.setName(user.getDisplayName());
            usersData.setEmail(user.getEmail());
            usersData.setPhotoUri(user.getPhotoUrl());
        }
        String name = usersData.getName();
        String email = usersData.getEmail();
        Uri photoUrl = usersData.getPhotoUri();

        String tvPersonName = null, tvEmail;

        if (name == null || name.equals("")) {
            if (email != null) {
                tvPersonName = email.substring(0, email.indexOf('@'));
            } else { tvPersonName = "Haven't name"; }
        } else {
            tvPersonName = name;
        }

        if (email == null) {
            tvEmail = "User anonymous email";
        } else {
            tvEmail = email;
        }
        view.initializeUser(tvPersonName, tvEmail);

        if (photoUrl == null) {
            view.initializeUserWithOutPhoto(R.drawable.ic_icon_for_person,
                    R.drawable.ic_icon_for_person_white);
        } else {
            view.initializeUserWithPhoto(photoUrl);
        }
    }

    @Override
    public void onClickedNavigationHome() {
        view.closeDrawer();
    }

    @Override
    public void onClickedNavigationChangeAccount() {
        FirebaseAuth.getInstance().signOut();
        view.openChangeAccount();
    }

    @Override
    public void onClickedNavigationExit() {
        view.activityFinish();
    }

    @Override
    public void backPressed(Context context) {
        FragmentManager manager = ((AppCompatActivity) context)
                .getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            view.activityFinish();
        }
    }

    @Override
    public void loadDatabase(List<Post> posts) {
        db.postDatabaseDao().getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<PostDatabase>>() {
                    @Override
                    public void onSuccess(@NonNull List<PostDatabase> postDatabase) {
                        List<Post> postsDB = new ArrayList<>();
                        for (int i = 0; i < postDatabase.size(); i++) {
                            postsDB.add(new Post(postDatabase.get(i)));
                        }
                        if (internet == INTERNET_CONNECTED) {
                            if (posts == null || posts.isEmpty()) {
                                if (postsDB.isEmpty()) {
                                    view.internetConnectedApiEmptyDatabaseEmpty();
                                } else {
                                    linkageList(postsDB);
                                    view.buildNewsPost();
                                    view.internetConnectedApiEmptyDatabaseNotEmpty();
                                }
                            } else {
                                postsDB.addAll(0, posts);
                                linkageList(postsDB);
                                view.buildNewsPost();
                                view.internetConnectedApiNotEmpty();
                            }
                        } else if (internet == INTERNET_NOT_CONNECTED) {
                            if (postsDB.isEmpty()) {
                                view.internetNotConnectedDatabaseEmpty();
                            } else {
                                view.buildNewsPost();
                                linkageList(postsDB);
                                view.internetNotConnectedDatabaseNotEmpty();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (posts.isEmpty()) {
                            view.databaseErrorApiEmpty();
                        } else  {
                            linkageList(posts);
                            view.buildNewsPost();
                            view.databaseErrorApiNotEmpty();
                        }
                    }
                });
    }

    @Override
    public void saveDatabase(List<Post> posts) {
        List<PostDatabase> postDatabases = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            postDatabases.add(new PostDatabase(posts.get(i)));
        }
        db.postDatabaseDao().insert(postDatabases)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private List<Post> sortedList(Set<Post> posts) {
        List<Post> list = new ArrayList<>(posts);
        Comparator<Post> comparator = (o1, o2) ->
                o2.getPubDate().compareTo(o1.getPubDate());
        Collections.sort(list, comparator);
        return list;
    }

    private void linkageList(List<Post> posts) {
        blockNewsData.addAll(0, posts);
        Set<Post> set = new LinkedHashSet<>(blockNewsData);
        blockNewsData.clear();
        blockNewsData.addAll(sortedList(set));
        saveDatabase(blockNewsData);
        separateListPosts(blockNewsData);
    }

    private void separateListPosts(List<Post> posts) {
        int size = posts.size();
        List<Post> horizontalList = posts.subList(0, 5);
        List<Post> verticalList = posts.subList(5, size);
        initializeHorizontalDataBlock(horizontalList);
        initializeVerticalDataBlock(verticalList);
    }

    public void initializeHorizontalDataBlock(List<Post> posts) {
        horizontalBlockNewsData.addAll(posts);
        rvHorizontalAdapter = new RVHorizontalAdapter(horizontalBlockNewsData, this);
        view.setHorizontalAdapter(rvHorizontalAdapter);
    }

    public void initializeVerticalDataBlock(List<Post> posts) {
        verticalBlockNewsData.addAll(posts);
        rvVerticalAdapter = new RVVerticalAdapter(verticalBlockNewsData, this);
        view.setVerticalAdapter(rvVerticalAdapter);
    }


    @Override
    public void onSuccessJSON(List<Post> posts) {
        view.buildNewsPost();
        loadDatabase(posts);
    }

    @Override
    public void onFailureJSON() {
        view.buildNewsPost();
        loadDatabase(null);
    }

    @Override
    public void onSuccessMoreDetails(Bundle bundle) {
        view.successMoreDetails(bundle);
    }
}
