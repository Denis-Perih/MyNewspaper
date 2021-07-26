package com.example.mainactivity.login_signup.signup;

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

public class SignUpFragment extends Fragment implements View.OnClickListener, FirebaseListener {

    private static final String AUTH_EMAIL_PASSWORD = "auth_email_password";
    private static final String AUTH_GOOGLE = "auth_google";
    private static final String AUTH_FACEBOOK = "auth_facebook";

    Button btnSignUp, btnGoogleSignUp, btnFacebookSignUp;

    ConstraintLayout clFrSignUpScreen;

    EditText name;
    EditText email;
    TextInputEditText password;

    FirebaseAuthEmailPasswordSingleton fbaSingleton;

    FirebaseAuthGoogleSingleton fbaGoogleSingleton;

    FirebaseAuthFacebookSingleton fbaFacebookSingleton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbaSingleton = FirebaseAuthEmailPasswordSingleton.getInstance();
        fbaSingleton.init();
        fbaGoogleSingleton = FirebaseAuthGoogleSingleton.getInstance();
        fbaGoogleSingleton.init(getActivity(), this);
        fbaFacebookSingleton = FirebaseAuthFacebookSingleton.getInstance();
        fbaFacebookSingleton.init(getActivity(), this);
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
        btnSignUp.setOnClickListener(this);
        btnGoogleSignUp = v.findViewById(R.id.btnGoogleSignUp);
        btnGoogleSignUp.setOnClickListener(this);
        btnFacebookSignUp = v.findViewById(R.id.btnFacebookSignUp);
        btnFacebookSignUp.setOnClickListener(this);

        clFrSignUpScreen = v.findViewById(R.id.clFrSignUpScreen);

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
                Snackbar.make(clFrSignUpScreen, e.toString(), Snackbar.LENGTH_SHORT).show();
            }
        } else {
            fbaFacebookSingleton.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSignUp) {
            if (TextUtils.isEmpty(name.getText().toString())) {
                Snackbar.make(clFrSignUpScreen, "Please enter your Person Name", Snackbar.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(email.getText().toString())) {
                Snackbar.make(clFrSignUpScreen, "Please enter your email address", Snackbar.LENGTH_SHORT).show();
                return;
            } else if (Objects.requireNonNull(password.getText()).toString().length() < 6) {
                Snackbar.make(clFrSignUpScreen, "Please enter your password (more than 6 characters)", Snackbar.LENGTH_SHORT).show();
                return;
            }

            fbaSingleton.executeSignUp(this, name.getText().toString(),
                    email.getText().toString(), password.getText().toString());

        } else if (id == R.id.btnGoogleSignUp){
            fbaGoogleSingleton.signIn();
        } else if (id == R.id.btnFacebookSignUp){
            LoginManager.getInstance().logInWithReadPermissions(this, Collections.singletonList("public_profile"));
            fbaFacebookSingleton.loginFacebook();
        }
    }

    @Override
    public void onSuccess(String indicatorAuth) {
        if (indicatorAuth.equals(AUTH_EMAIL_PASSWORD)
                || indicatorAuth.equals(AUTH_GOOGLE)
                || indicatorAuth.equals(AUTH_FACEBOOK)) {
            ((MainActivity)requireActivity()).openFragment(new HomeFragment());
        }
    }

    @Override
    public void onFailure(String indicatorAuth) {
        if (indicatorAuth.equals(AUTH_EMAIL_PASSWORD)
                || indicatorAuth.equals(AUTH_GOOGLE)
                || indicatorAuth.equals(AUTH_FACEBOOK)) {
            Snackbar.make(clFrSignUpScreen, "Registration error, please repeat", Snackbar.LENGTH_SHORT).show();
        }
    }
}