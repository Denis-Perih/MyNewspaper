package com.example.mainactivity.home;

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

import com.example.mainactivity.main.MainRouterContract;
import com.example.mainactivity.R;
import com.example.mainactivity.home.rv_adapter.RVHorizontalAdapter;
import com.example.mainactivity.home.rv_adapter.RVVerticalAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class HomeFragment extends Fragment implements IOnBackPressed, NavigationView.OnNavigationItemSelectedListener, HomeContract.View {

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

    View v;

    private final HomeContract.Presenter presenter = new HomePresenter(this);
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (v == null) {
            v = inflater.inflate(R.layout.fr_home, container, false);

            swipe_container = v.findViewById(R.id.swipe_container);
            swipe_container.setOnRefreshListener(() -> presenter.connectedToInternet(getContext()));
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

            presenter.connectedToInternet(getContext());

            presenter.initializeUser();

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

    private void navigationDrawerMenu() {
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
            presenter.onClickedNavigationHome();
        } else if (id == R.id.nav_change_account) {
            presenter.onClickedNavigationChangeAccount();
        } else if (id == R.id.nav_exit) {
            presenter.onClickedNavigationExit();
        }
        dlHomeDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onBackPressed() {
        presenter.backPressed(requireContext());
        if (dlHomeDrawer.isDrawerOpen(GravityCompat.START)) {
            dlHomeDrawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void buildNewsPost() {
        rvHorizontalBlockNews = v.findViewById(R.id.rvHorizontalBlockNews);
        LinearLayoutManager llmHorizontalBlockNews = new LinearLayoutManager(
                rvHorizontalBlockNews.getContext(), RecyclerView.HORIZONTAL, false);
        rvVerticalBlockNews = v.findViewById(R.id.rvVerticalBlockNews);
        LinearLayoutManager llmVerticalBlockNews = new LinearLayoutManager(
                rvVerticalBlockNews.getContext(), RecyclerView.VERTICAL, false);
        rvHorizontalBlockNews.setLayoutManager(llmHorizontalBlockNews);
        rvVerticalBlockNews.setLayoutManager(llmVerticalBlockNews);
        swipe_container.setRefreshing(false);
    }

    @Override
    public void initializeUser(String tvPersonName, String tvEmail) {
        this.tvPersonName.setText(tvPersonName);
        this.tvEmail.setText(tvEmail);
    }

    @Override
    public void initializeUserWithOutPhoto(int icon, int iconWhite) {
        headerIconUser.setImageResource(icon);
        menuIconUser.setImageResource(iconWhite);
    }

    @Override
    public void initializeUserWithPhoto(Uri photoUrl) {
        Picasso.get().load(photoUrl).into(headerIconUser);
        Picasso.get().load(photoUrl).into(menuIconUser);
    }

    @Override
    public void closeDrawer() {
        dlHomeDrawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void openChangeAccount() {
        ((MainRouterContract) requireActivity()).openSplashScreenFragment();
    }

    @Override
    public void activityFinish() {
        requireActivity().finish();
    }

    @Override
    public void successMoreDetails(Bundle bundle) {
        ((MainRouterContract) requireActivity()).openMoreAboutFragment(bundle);
    }

    @Override
    public void setHorizontalAdapter(RVHorizontalAdapter rvHorizontalAdapter) {
        rvHorizontalBlockNews.setAdapter(rvHorizontalAdapter);
    }

    @Override
    public void setVerticalAdapter(RVVerticalAdapter rvVerticalAdapter) {
        rvVerticalBlockNews.setAdapter(rvVerticalAdapter);
    }

    @Override
    public void internetConnectedApiEmptyDatabaseEmpty() {
        pbDownloadNews.setVisibility(View.INVISIBLE);
        tvNoConnect.setText(R.string.text_not_connected_server);
        tvNoConnect.setVisibility(View.VISIBLE);
        ivNoConnectIcon.setImageResource(R.drawable.ic_nocloudconnected);
        ivNoConnectIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void internetConnectedApiEmptyDatabaseNotEmpty() {
        Snackbar.make(dlHomeDrawer, R.string.text_not_connected_server, Snackbar.LENGTH_SHORT).show();
        pbDownloadNews.setVisibility(View.INVISIBLE);
    }

    @Override
    public void internetConnectedApiNotEmpty() {
        pbDownloadNews.setVisibility(View.INVISIBLE);
    }

    @Override
    public void internetNotConnectedDatabaseEmpty() {
        pbDownloadNews.setVisibility(View.INVISIBLE);
        tvNoConnect.setText(R.string.text_not_connected_internet);
        tvNoConnect.setVisibility(View.VISIBLE);
        ivNoConnectIcon.setImageResource(R.drawable.ic_nointernetconnected);
        ivNoConnectIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void internetNotConnectedDatabaseNotEmpty() {
        pbDownloadNews.setVisibility(View.INVISIBLE);
    }

    @Override
    public void databaseErrorApiEmpty() {
        pbDownloadNews.setVisibility(View.INVISIBLE);
        tvNoConnect.setText(R.string.text_not_connected_server);
        tvNoConnect.setVisibility(View.VISIBLE);
        ivNoConnectIcon.setImageResource(R.drawable.ic_nocloudconnected);
        ivNoConnectIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void databaseErrorApiNotEmpty() {
        pbDownloadNews.setVisibility(View.INVISIBLE);
    }
}