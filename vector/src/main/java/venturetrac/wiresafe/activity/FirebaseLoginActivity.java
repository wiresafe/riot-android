package venturetrac.wiresafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import venturetrac.wiresafe.R;

public class FirebaseLoginActivity extends MXCActionBarActivity {

    private static final int FIREBASE_SIGNIN_CODE = 299;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadFirebaseAuthUI();
    }

    private void loadFirebaseAuthUI() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setLogo(R.drawable.logo_login)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()))
                        .setAllowNewEmailAccounts(true)
                        .setLogo(R.drawable.logo_login)      // Set logo drawable
                        .setTheme(R.style.LoginAppTheme)      // Set theme
                        .build(),
                FIREBASE_SIGNIN_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FIREBASE_SIGNIN_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (null != user && null != response) {
//                    loginOperation(user.getUid(), response.getIdpToken());
                }
                // ...
            } else {
                if (null == response) {
                    loadFirebaseAuthUI();
                }
            }
        }
    }
}
