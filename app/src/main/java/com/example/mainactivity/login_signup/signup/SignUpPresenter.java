package com.example.mainactivity.login_signup.signup;

import android.content.Intent;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Collections;

import static com.example.mainactivity.login_signup.firebase.FirebaseLoginSingleton.RC_SIGN_IN;

public class SignUpPresenter implements SignUpContract.Presenter {

    private static final String AUTH_EMAIL_PASSWORD = "auth_email_password";
    private static final String AUTH_GOOGLE = "auth_google";
    private static final String AUTH_FACEBOOK = "auth_facebook";

    private final SignUpContract.View view;

    public SignUpPresenter(SignUpContract.View view) {
        this.view = view;
    }

    @Override
    public void onSignUpClicked(String name, String email, String password) {
        if (TextUtils.isEmpty(name)) {
            view.showSnackBar("Please enter your Person Name");
            return;
        } else if (TextUtils.isEmpty(email)) {
            view.showSnackBar("Please enter your email address");
            return;
        } else if (password.length() < 6) {
            view.showSnackBar("Please enter your password (more than 6 characters)");
            return;
        }
        view.signInWithEmail(name, email, password);
    }

    @Override
    public void onSignUpClickedGoogle() {
        view.startSignUpGoogle();
    }

    @Override
    public void onSignUpClickedFacebook(Fragment fragment) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, Collections.singletonList("public_profile"));
        view.startSignUpFacebook();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    view.startFirebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                view.showSnackBar(e.toString());
            }
        } else {
            view.startActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSuccessSignUp(String indicatorAuth) {
        if (indicatorAuth.equals(AUTH_EMAIL_PASSWORD)
                || indicatorAuth.equals(AUTH_GOOGLE)
                || indicatorAuth.equals(AUTH_FACEBOOK)) {
            view.successSignUp();
        }
    }

    @Override
    public void onFailureSignUp(String indicatorAuth) {
        if (indicatorAuth.equals(AUTH_EMAIL_PASSWORD)
                || indicatorAuth.equals(AUTH_GOOGLE)
                || indicatorAuth.equals(AUTH_FACEBOOK)) {
            view.failureSignUp("Registration error, please repeat");
        }
    }
}
