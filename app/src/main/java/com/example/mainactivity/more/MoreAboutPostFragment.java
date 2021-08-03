package com.example.mainactivity.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mainactivity.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class MoreAboutPostFragment extends Fragment implements MoreAboutPostContract.View {

    ConstraintLayout clMoreFragment;

    ImageButton ibBack;

    ImageView ivImageDetailsWindow;
    TextView tvDetailsTitle, tvDetailsDate, tvDetailsTextNews;
    Button btnDetailsAuthor;

    private final MoreAboutPostContract.Present presenter = new MoreAboutPostPresenter(this);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fr_more_about_post, container, false);

        clMoreFragment = v.findViewById(R.id.clMoreFragment);

        ibBack = v.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(v1 -> {
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            fm.popBackStack(MoreAboutPostFragment.class.getSimpleName(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });

        ivImageDetailsWindow = v.findViewById(R.id.ivImageDetailsWindow);
        ivImageDetailsWindow.setScaleType(ImageView.ScaleType.CENTER_CROP);
        tvDetailsTitle = v.findViewById(R.id.tvDetailsTitle);
        tvDetailsDate = v.findViewById(R.id.tvDetailsDate);
        tvDetailsTextNews = v.findViewById(R.id.tvDetailsTextNews);

        btnDetailsAuthor = v.findViewById(R.id.btnDetailsAuthor);
        btnDetailsAuthor.setOnClickListener(v12 -> presenter.openDetailsAuthor());

        getBundleData();

        return v;
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        presenter.setDataToPost(bundle);
    }

    @Override
    public void showSnackBar(String text) {
        Snackbar.make(clMoreFragment, text, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void getDateToPost(String title, String description, String pubData) {
        tvDetailsTitle.setText(title);
        tvDetailsDate.setText(pubData);
        tvDetailsTextNews.setText(description);
    }

    @Override
    public void openLinkAuthorPost(Intent chooser) {
        startActivity(chooser);
    }

    @Override
    public void getImageToPost(String image_url) {
        Picasso.get().load(image_url).into(ivImageDetailsWindow);
    }

    @Override
    public void getDefaultImageToPost(int image) {
        ivImageDetailsWindow.setImageResource(image);
    }
}
