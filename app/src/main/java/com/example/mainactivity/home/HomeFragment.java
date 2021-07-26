package com.example.mainactivity.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mainactivity.MainActivity;
import com.example.mainactivity.R;
import com.example.mainactivity.database.AppDatabase;
import com.example.mainactivity.database.DatabaseApplication;
import com.example.mainactivity.database.PostDatabase;
import com.example.mainactivity.login_signup.firebase.UsersData;
import com.example.mainactivity.more.AnswerMoreDetails;
import com.example.mainactivity.retrofit.AnswerJSON;
import com.example.mainactivity.retrofit.GetObjectJSON;
import com.example.mainactivity.retrofit.Post;
import com.example.mainactivity.splash.SplashScreenFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment implements IOnBackPressed, NavigationView.OnNavigationItemSelectedListener, AnswerJSON, AnswerMoreDetails {

    ImageView headerIconUser;
    ImageView menuIconUser;
    TextView tvPersonName;
    TextView tvEmail;

    TextView tvNoConnect;
    ImageView ivNoConnectIcon;
    ProgressBar pbDownloadNews;
    SwipeRefreshLayout swipe_container;

    DrawerLayout dlHomeDrawer;
    NavigationView nav_view;
    View nav_header_navigation;
    ImageButton btnMenu;

    RecyclerView rvHorizontalBlockNews;
    RecyclerView rvVerticalBlockNews;

    RVHorizontalAdapter rvHorizontalAdapter;
    RVVerticalAdapter rvVerticalAdapter;
    View v;

    private static final int INTERNET_CONNECTED = 1;
    private static final int INTERNET_NOT_CONNECTED = 2;
    private static int internet;

    private List<Post> horizontalBlockNewsData;
    private List<Post> verticalBlockNewsData;

    private final List<Post> blockNewsData = new ArrayList<>();

    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (v == null) {
            v = inflater.inflate(R.layout.fr_home, container, false);

            db = DatabaseApplication.getInstance().getDatabase();

            swipe_container = v.findViewById(R.id.swipe_container);
            swipe_container.setOnRefreshListener(this::connectedToInternet);
            swipe_container.setColorSchemeResources(R.color.coral);

            pbDownloadNews = v.findViewById(R.id.pbDownloadNews);
            pbDownloadNews.setVisibility(View.VISIBLE);

            dlHomeDrawer = v.findViewById(R.id.dlHomeDrawer);
            nav_view = v.findViewById(R.id.nav_view);
            btnMenu = v.findViewById(R.id.btnMenu);
            btnMenu.setOnClickListener(v -> dlHomeDrawer.openDrawer(GravityCompat.START));

            nav_header_navigation = nav_view.getHeaderView(0);

            headerIconUser = v.findViewById(R.id.headerIconUser);
            menuIconUser = nav_header_navigation.findViewById(R.id.menuIconUser);
            tvPersonName = nav_header_navigation.findViewById(R.id.tvPersonName);
            tvEmail = nav_header_navigation.findViewById(R.id.tvEmail);

            tvNoConnect = v.findViewById(R.id.tvNoConnect);
            ivNoConnectIcon = v.findViewById(R.id.ivNoConnectIcon);

            connectedToInternet();

            initializeUsers();

            navigationDrawerMenu();
        }
        return v;
    }

    @Override
    public void onDestroyView() {
        if (v.getParent() != null) {
            ((ViewGroup)v.getParent()).removeView(v);
        }
        super.onDestroyView();
    }

    private void connectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) requireContext()
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

    private void buildNewsPosts() {
        rvHorizontalBlockNews = v.findViewById(R.id.rvHorizontalBlockNews);
        LinearLayoutManager llmHorizontalBlockNews = new LinearLayoutManager(
                rvHorizontalBlockNews.getContext(), RecyclerView.HORIZONTAL, false);
        rvHorizontalBlockNews.setLayoutManager(llmHorizontalBlockNews);
        horizontalBlockNewsData = new ArrayList<>();

        rvVerticalBlockNews = v.findViewById(R.id.rvVerticalBlockNews);
        LinearLayoutManager llmVerticalBlockNews = new LinearLayoutManager(
                rvVerticalBlockNews.getContext(), RecyclerView.VERTICAL, false);
        rvVerticalBlockNews.setLayoutManager(llmVerticalBlockNews);
        verticalBlockNewsData = new ArrayList<>();

        swipe_container.setRefreshing(false);
    }

    private void initializeUsers() {
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

        if (name == null || name.equals("")) {
            if (email != null) {
                tvPersonName.setText(email.substring(0, email.indexOf('@')));
            }
        } else {
            tvPersonName.setText(name);
        }

        if (email == null) {
            tvEmail.setText(R.string.user_anonymous_email);
        } else {
            tvEmail.setText(email);
        }

        if (photoUrl == null) {
            headerIconUser.setImageResource(R.drawable.ic_icon_for_person);
            menuIconUser.setImageResource(R.drawable.ic_icon_for_person_white);
        } else {
            Picasso.get().load(photoUrl).into(headerIconUser);
            Picasso.get().load(photoUrl).into(menuIconUser);
        }
    }

    public void navigationDrawerMenu() {
        nav_view.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), dlHomeDrawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlHomeDrawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);
        nav_view.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            ((MainActivity) requireActivity()).openFragment(new HomeFragment());
        } else if (id == R.id.nav_change_account) {
            FirebaseAuth.getInstance().signOut();
            ((MainActivity) requireActivity()).openFragment(new SplashScreenFragment());
        } else if (id == R.id.nav_exit) {
            requireActivity().finish();
        }
        dlHomeDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onBackPressed() {
        if (dlHomeDrawer.isDrawerOpen(GravityCompat.START)) {
            dlHomeDrawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    private void initializeHorizontalBlockData(List<Post> posts) {
        horizontalBlockNewsData.addAll(posts);
        rvHorizontalAdapter = new RVHorizontalAdapter(horizontalBlockNewsData, this);
        rvHorizontalBlockNews.setAdapter(rvHorizontalAdapter);
    }

    private void initializeVerticalBlockData(List<Post> posts) {
        verticalBlockNewsData.addAll(posts);
        rvVerticalAdapter = new RVVerticalAdapter(verticalBlockNewsData, this);
        rvVerticalBlockNews.setAdapter(rvVerticalAdapter);
    }

    private void separateListPosts(List<Post> posts) {
        int size = posts.size();
        List<Post> horizontalList = posts.subList(0, 5);
        List<Post> verticalList = posts.subList(5, size);
        initializeHorizontalBlockData(horizontalList);
        initializeVerticalBlockData(verticalList);
    }

    private void linkageList(List<Post> posts) {
        blockNewsData.addAll(0, posts);
        Set<Post> set = new LinkedHashSet<>(blockNewsData);
        blockNewsData.clear();
        blockNewsData.addAll(sortedList(set));
        saveDatabase(blockNewsData);
        separateListPosts(blockNewsData);
    }

    private List<Post> sortedList(Set<Post> posts) {
        List<Post> list = new ArrayList<>(posts);
        Comparator<Post> comparator = (o1, o2) ->
                o2.getPubDate().compareTo(o1.getPubDate());
        Collections.sort(list, comparator);
        return list;
    }

    private void saveDatabase(List<Post> posts) {
        List<PostDatabase> postDatabases = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            postDatabases.add(new PostDatabase(posts.get(i)));
        }
        db.postDatabaseDao().insert(postDatabases)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void loadDatabase(List<Post> posts) {
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
                            if (posts.isEmpty()) {
                                if (postsDB.isEmpty()) {
                                    pbDownloadNews.setVisibility(View.INVISIBLE);
                                    tvNoConnect.setText(R.string.text_not_connected_server);
                                    tvNoConnect.setVisibility(View.VISIBLE);
                                    ivNoConnectIcon.setImageResource(R.drawable.ic_nocloudconnected);
                                    ivNoConnectIcon.setVisibility(View.VISIBLE);
                                } else {
                                    linkageList(postsDB);
                                    Snackbar.make(dlHomeDrawer, R.string.text_not_connected_server, Snackbar.LENGTH_SHORT).show();
                                    pbDownloadNews.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                postsDB.addAll(0, posts);
                                linkageList(postsDB);
                                pbDownloadNews.setVisibility(View.INVISIBLE);
                            }
                        } else if (internet == INTERNET_NOT_CONNECTED) {
                            if (postsDB.isEmpty()) {
                                pbDownloadNews.setVisibility(View.INVISIBLE);
                                tvNoConnect.setText(R.string.text_not_connected_internet);
                                tvNoConnect.setVisibility(View.VISIBLE);
                                ivNoConnectIcon.setImageResource(R.drawable.ic_nointernetconnected);
                                ivNoConnectIcon.setVisibility(View.VISIBLE);
                            } else {
                                buildNewsPosts();
                                linkageList(postsDB);
                                pbDownloadNews.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (posts.isEmpty()) {
                            pbDownloadNews.setVisibility(View.INVISIBLE);
                            tvNoConnect.setText(R.string.text_not_connected_server);
                            tvNoConnect.setVisibility(View.VISIBLE);
                            ivNoConnectIcon.setImageResource(R.drawable.ic_nocloudconnected);
                            ivNoConnectIcon.setVisibility(View.VISIBLE);
                        } else  {
                            linkageList(posts);
                            buildNewsPosts();
                            pbDownloadNews.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onSuccessJSON(List<Post> posts) {
        buildNewsPosts();
        loadDatabase(posts);
    }

    @Override
    public void onFailureJSON() {
        buildNewsPosts();
        loadDatabase(null);
    }

    @Override
    public void onSuccessMoreDetails(Bundle bundle) {
        ((MainActivity) requireActivity()).openMoreAboutFragment(bundle);
    }
}