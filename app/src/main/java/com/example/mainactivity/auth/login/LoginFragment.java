package com.example.mainactivity.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.mainactivity.main.MainRouterContract;
import com.example.mainactivity.R;
import com.example.mainactivity.auth.firebase.FirebaseListener;
import com.example.mainactivity.auth.firebase.FirebaseLoginSingleton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginFragment extends Fragment implements FirebaseListener, LoginContract.View {

    Button btnLogin, btnGoogleLogin, btnFacebookLogin;

    ConstraintLayout clFrLoginScreen;

    EditText email;
    TextInputEditText password;

    FirebaseLoginSingleton fbSingleton;

    private final LoginContract.Presenter presenter = new LoginPresenter(this);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbSingleton = FirebaseLoginSingleton.getInstance();
        fbSingleton.init(getActivity(), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_login_screen, container, false);

        btnLogin = v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v1 -> presenter
                .onLoginClicked(email.getText().toString(),
                        Objects.requireNonNull(password.getText()).toString()));
        btnGoogleLogin = v.findViewById(R.id.btnGoogleLogin);
        btnGoogleLogin.setOnClickListener(v12 -> presenter.onLoginClickedGoogle());
        btnFacebookLogin = v.findViewById(R.id.btnFacebookLogin);
        btnFacebookLogin.setOnClickListener(v13 -> presenter
                .onLoginClickedFacebook(LoginFragment.this));

        clFrLoginScreen = v.findViewById(R.id.clFrLoginScreen);

        email = v.findViewById(R.id.etEmail);
        password = v.findViewById(R.id.etPassword);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(String indicatorAuth) {
        presenter.onSuccessLogin(indicatorAuth);
    }

    @Override
    public void onFailure(String indicatorAuth) {
        presenter.onFailureLogin(indicatorAuth);
    }

    @Override
    public void signInWithEmail(String email, String password) {
        fbSingleton.executeLogin(this, email, password);
    }

    @Override
    public void showSnackBar(String text) {
        Snackbar.make(clFrLoginScreen, text, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void startSignInGoogle() {
        fbSingleton.signIn();
    }

    @Override
    public void startSignInFacebook() {
        fbSingleton.loginFacebook();
    }

    @Override
    public void startFirebaseAuthWithGoogle(String idToken) {
        fbSingleton.firebaseAuthWithGoogle(idToken);
    }

    @Override
    public void startActivityResult(int requestCode, int resultCode, Intent data) {
        fbSingleton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void successLogin() {
        ((MainRouterContract) requireActivity()).openHomeFragment();
    }

    @Override
    public void failureLogin(String text) {
        Snackbar.make(clFrLoginScreen, text, Snackbar.LENGTH_SHORT).show();
    }
}