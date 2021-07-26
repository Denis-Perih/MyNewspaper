package com.example.mainactivity.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mainactivity.MainActivity;
import com.example.mainactivity.R;
import com.example.mainactivity.home.HomeFragment;
import com.example.mainactivity.login_signup.login.LoginFragment;
import com.example.mainactivity.login_signup.signup.SignUpFragment;

public class SplashScreenFragment extends Fragment  {

    Button btnLogin;
    Button btnSignUp;

    TextView tvSkip;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fr_splash_screen, container, false);
        btnLogin = v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v1 -> ((MainActivity) requireActivity()).openFragment(new LoginFragment()));
        btnSignUp = v.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v12 -> ((MainActivity) requireActivity()).openFragment(new SignUpFragment()));
        tvSkip = v.findViewById(R.id.tvSkip);
        tvSkip.setOnClickListener(v13 -> ((MainActivity) requireActivity()).openFragment(new HomeFragment()));

        return v;
    }
}