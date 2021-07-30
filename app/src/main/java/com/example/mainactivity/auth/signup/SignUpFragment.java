package com.example.mainactivity.auth.signup;

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

public class SignUpFragment extends Fragment implements FirebaseListener, SignUpContract.View {

    Button btnSignUp, btnGoogleSignUp, btnFacebookSignUp;

    ConstraintLayout clFrSignUpScreen;

    EditText name;
    EditText email;
    TextInputEditText password;

    FirebaseLoginSingleton fbSingleton;

    private final SignUpContract.Presenter presenter = new SignUpPresenter(this);

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

        View v = inflater.inflate(R.layout.fr_sign_up_screen, container, false);

        name = v.findViewById(R.id.etName);
        email = v.findViewById(R.id.etEmail);
        password = v.findViewById(R.id.etPassword);

        btnSignUp = v.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v1 -> presenter.onSignUpClicked(name.getText().toString(),
                email.getText().toString(), Objects.requireNonNull(password.getText()).toString()));
        btnGoogleSignUp = v.findViewById(R.id.btnGoogleSignUp);
        btnGoogleSignUp.setOnClickListener(v12 -> presenter.onSignUpClickedGoogle());
        btnFacebookSignUp = v.findViewById(R.id.btnFacebookSignUp);
        btnFacebookSignUp.setOnClickListener(v13 -> presenter
                .onSignUpClickedFacebook(SignUpFragment.this));

        clFrSignUpScreen = v.findViewById(R.id.clFrSignUpScreen);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(String indicatorAuth) {
        presenter.onSuccessSignUp(indicatorAuth);
    }

    @Override
    public void onFailure(String indicatorAuth) {
        presenter.onFailureSignUp(indicatorAuth);
    }

    @Override
    public void signInWithEmail(String name, String email, String password) {
        fbSingleton.executeSignUp(this, name, email, password);
    }

    @Override
    public void showSnackBar(String text) {
        Snackbar.make(clFrSignUpScreen, text, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void startSignUpGoogle() {
        fbSingleton.signIn();
    }

    @Override
    public void startSignUpFacebook() {
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
    public void successSignUp() {
        ((MainRouterContract) requireActivity()).openHomeFragment();
    }

    @Override
    public void failureSignUp(String text) {
        Snackbar.make(clFrSignUpScreen, text, Snackbar.LENGTH_SHORT).show();
    }
}