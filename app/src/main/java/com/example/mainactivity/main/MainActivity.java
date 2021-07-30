package com.example.mainactivity.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mainactivity.R;
import com.example.mainactivity.home.HomeFragment;
import com.example.mainactivity.home.IOnBackPressed;
import com.example.mainactivity.auth.firebase.UsersData;
import com.example.mainactivity.auth.login.LoginFragment;
import com.example.mainactivity.auth.signup.SignUpFragment;
import com.example.mainactivity.more.MoreAboutPostFragment;
import com.example.mainactivity.retrofit.Post;
import com.example.mainactivity.splash.SplashFragment;
import com.example.mainactivity.auth.AuthScreenFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements MainRouterContract {

    private Bundle saverInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.saverInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        openSplashFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void startApp() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UsersData usersData = new UsersData();
        if (user != null) {
            usersData.setName(user.getDisplayName());
            usersData.setEmail(user.getEmail());
            usersData.setPhotoUri(user.getPhotoUrl());
            openHomeFragment();
        } else {
            openSplashScreenFragment();
        }
    }

    public void openFragment(Fragment newFragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top,
                        R.anim.slide_in_top, R.anim.slide_out_bottom)
                .replace(R.id.fragment_container_main, newFragment)
                .addToBackStack(newFragment.getClass().getSimpleName());

        transaction.commit();
    }

    @Override
    public void openMoreAboutFragment(Post post) {
        Fragment newFragment = new MoreAboutPostFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("post", post);

        newFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                        R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragment_container_main, newFragment)
                .addToBackStack(newFragment.getClass().getSimpleName());

        transaction.commit();
    }

    @Override
    public void openHomeFragment() {
        openFragment(new HomeFragment());
    }

    @Override
    public void openSplashFragment() {
        openFragment(new SplashFragment());
    }

    @Override
    public void openSplashScreenFragment() {
        openFragment(new AuthScreenFragment());
    }

    @Override
    public void openLoginFragment() {
        openFragment(new LoginFragment());
    }

    @Override
    public void openSignUpFragment() {
        openFragment(new SignUpFragment());
    }


}