package com.example.mysnsaccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class GoogleLogin extends Activity {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleClient;
    private FirebaseAuth mGoogleLoginModule;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleLoginModule = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        if (mGoogleLoginModule.getCurrentUser() == null) {
            Intent signInIntent = mGoogleClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);

        } else if (mGoogleLoginModule.getCurrentUser() != null) {
            List<String> userInfo = new ArrayList<>();
            GlobalHelper mGlobalHelper = (GlobalHelper) getApplication();
            userInfo.add(String.format("%s-%s", "GOOGLE", mGoogleLoginModule.getCurrentUser().getUid()));
            userInfo.add(mGoogleLoginModule.getCurrentUser().getDisplayName());
            GlobalHelper.setGlobalUserLoginInfo(userInfo);
            Intent intent = new Intent(GoogleLogin.this, SecondActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(getApplicationContext(), "Google Sign In Failed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GoogleLogin.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mGoogleLoginModule.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mGoogleLoginModule.getCurrentUser() != null) {
                                List<String> userInfo = new ArrayList<>();
                                GlobalHelper mGlobalHelper = (GlobalHelper) getApplication();
                                userInfo.add(mGoogleLoginModule.getCurrentUser().getUid());
                                userInfo.add(mGoogleLoginModule.getCurrentUser().getDisplayName());
                                GlobalHelper.setGlobalUserLoginInfo(userInfo);
                                Intent intent = new Intent(GoogleLogin.this, SecondActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Google Authentication Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
