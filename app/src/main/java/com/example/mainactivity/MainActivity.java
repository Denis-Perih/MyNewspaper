package com.example.mainactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mainactivity.home.HomeFragment;
import com.example.mainactivity.home.IOnBackPressed;
import com.example.mainactivity.login_signup.firebase.UsersData;
import com.example.mainactivity.more.MoreAboutPostFragment;
import com.example.mainactivity.splash.SplashFragment;
import com.example.mainactivity.splash.SplashScreenFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openFragment(new SplashFragment());
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
            openFragment(new HomeFragment());
        } else {
            openFragment(new SplashScreenFragment());
        }
    }

    public void openFragment(Fragment newFragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top)
                .replace(R.id.fragment_container_main, newFragment)
                .addToBackStack(null);

        transaction.commit();
    }

    public void openMoreAboutFragment(Bundle bundle) {

        Fragment newFragment = new MoreAboutPostFragment();

        newFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container_main, newFragment)
                .addToBackStack(null);

        transaction.commit();
    }
}