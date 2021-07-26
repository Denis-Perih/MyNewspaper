package com.example.mainactivity.login_signup.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.mainactivity.MainActivity;
import com.example.mainactivity.R;
import com.example.mainactivity.home.HomeFragment;
import com.example.mainactivity.login_signup.firebase.FirebaseAuthEmailPasswordSingleton;
import com.example.mainactivity.login_signup.firebase.FirebaseAuthFacebookSingleton;
import com.example.mainactivity.login_signup.firebase.FirebaseAuthGoogleSingleton;
import com.example.mainactivity.login_signup.firebase.FirebaseListener;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Collections;
import java.util.Objects;

import static com.example.mainactivity.login_signup.firebase.FirebaseAuthGoogleSingleton.RC_SIGN_IN;

public class LoginFragment extends Fragment implements View.OnClickListener, FirebaseListener {

    private static final String AUTH_EMAIL_PASSWORD = "auth_email_password";
    private static final String AUTH_GOOGLE = "auth_google";
    private static final String AUTH_FACEBOOK = "auth_facebook";

    Button btnLogin, btnGoogleLogin, btnFacebookLogin;

    ConstraintLayout clFrLoginScreen;

    EditText email;
    TextInputEditText password;
    FirebaseAuthEmailPasswordSingleton fbaSingleton;

    FirebaseAuthGoogleSingleton fbaGoogleSingleton;

    FirebaseAuthFacebookSingleton fbaFacebookSingleton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbaGoogleSingleton = FirebaseAuthGoogleSingleton.getInstance();
        fbaGoogleSingleton.init(getActivity(), this);
        fbaSingleton = FirebaseAuthEmailPasswordSingleton.getInstance();
        fbaSingleton.init();
        fbaFacebookSingleton = FirebaseAuthFacebookSingleton.getInstance();
        fbaFacebookSingleton.init(getActivity(), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_login_screen, container, false);

        btnLogin = v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnGoogleLogin = v.findViewById(R.id.btnGoogleLogin);
        btnGoogleLogin.setOnClickListener(this);
        btnFacebookLogin = v.findViewById(R.id.btnFacebookLogin);
        btnFacebookLogin.setOnClickListener(this);

        clFrLoginScreen = v.findViewById(R.id.clFrLoginScreen);

        email = v.findViewById(R.id.etEmail);
        password = v.findViewById(R.id.etPassword);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    fbaGoogleSingleton.firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                Snackbar.make(clFrLoginScreen, e.toString(), Snackbar.LENGTH_SHORT).show();
            }
        } else {
            fbaFacebookSingleton.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLogin) {
            if (TextUtils.isEmpty(email.getText().toString())) {
                Snackbar.make(clFrLoginScreen, "Please enter your email address", Snackbar.LENGTH_SHORT).show();
                return;
            } else if (Objects.requireNonNull(password.getText()).toString().length() < 6) {
                Snackbar.make(clFrLoginScreen, "Please enter your password (more than 6 characters)", Snackbar.LENGTH_SHORT).show();
                return;
            }

            fbaSingleton.executeLogin(this,
                    email.getText().toString(), password.getText().toString());

        } else if (id == R.id.btnGoogleLogin){
            fbaGoogleSingleton.signIn();
        } else if (id == R.id.btnFacebookLogin){
            LoginManager.getInstance().logInWithReadPermissions(this, Collections.singletonList("public_profile"));
            fbaFacebookSingleton.loginFacebook();
        }
    }

    @Override
    public void onSuccess(String indicatorAuth) {
        if (indicatorAuth.equals(AUTH_EMAIL_PASSWORD)
                || indicatorAuth.equals(AUTH_GOOGLE)
                || indicatorAuth.equals(AUTH_FACEBOOK)) {
            ((MainActivity) requireActivity()).openFragment(new HomeFragment());
        }
    }

    @Override
    public void onFailure(String indicatorAuth) {
        if (indicatorAuth.equals(AUTH_EMAIL_PASSWORD)
                || indicatorAuth.equals(AUTH_GOOGLE)
                || indicatorAuth.equals(AUTH_FACEBOOK)) {
            Snackbar.make(clFrLoginScreen, "Incorrectly entered email or password", Snackbar.LENGTH_SHORT).show();
        }
    }
}
