package newventure.wiresafe.util;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthClass {
    public void callFirebaseRegister(FirebaseAuth mAuth, String email, String pwd, final FirebaseRegisterCallback callback) {
        if (mAuth != null)
            mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                        callback.onSuccess();
                    else
                        callback.onError();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onFailure(e);
                }
            });
    }

    public interface FirebaseRegisterCallback {
        void onSuccess();

        void onError();

        void onFailure(Exception e);
    }
}
