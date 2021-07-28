package com.example.mainactivity.splash;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mainactivity.main.MainActivity;
import com.example.mainactivity.R;

public class SplashFragment extends Fragment {

    private static final int SPLASH_TIME_OUT = 3000;

    Animation topAnimation, bottomAnimation;

    ImageView ivLogoSplash;
    TextView tvTextSplash;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fr_splash, container, false);

        topAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_animation);

        ivLogoSplash = v.findViewById(R.id.ivLogoSplash);
        tvTextSplash = v.findViewById(R.id.tvTextSplash);

        ivLogoSplash.setAnimation(topAnimation);
        tvTextSplash.setAnimation(bottomAnimation);

        new Handler().postDelayed(() -> ((MainActivity)requireActivity()).startApp(), SPLASH_TIME_OUT);

        return v;
    }
}