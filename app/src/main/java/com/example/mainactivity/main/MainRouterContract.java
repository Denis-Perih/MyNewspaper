package com.example.mainactivity.main;

import android.os.Bundle;

public interface MainRouterContract {

    void openHomeFragment();

    void openMoreAboutFragment(Bundle bundle);

    void openSplashFragment();

    void openSplashScreenFragment();

    void openLoginFragment();

    void openSignUpFragment();
}
