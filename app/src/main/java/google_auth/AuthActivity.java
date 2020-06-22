package google_auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.balance.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import api.Api;
import api.App;
import models.AuthResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    private GoogleSignInClient client;
    private Button button;

    private static final int RC_SIGN_IN = 321;

    private static final String TAG = "AuthActivity";

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
       api = ((App) getApplication()).getApi();//code relate to authorization

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this,gso);

        button = findViewById(R.id.button_auth);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO:Check for an existing sign-in user

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            updateUI(account);
        }
    }

    private void signIn() {
        Log.i(TAG, "signIn: ");

        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account == null){
           showError("Account is null");
           return;
        }
        String id = account.getId();

        api.auth(id).enqueue(new Callback<AuthResult>() {
            @Override
            public void onResponse(Call<AuthResult> call, Response<AuthResult> response) {
             AuthResult result = response.body();//server response
          //      Log.i(TAG, "onResponse: token = " + result.token);
                ((App)getApplication()).saveAuthToken(result.token);
                 finish();
            }

            @Override
            public void onFailure(Call<AuthResult> call, Throwable t) {
              showError("Auth failed " + t.getMessage());
            }
        });
    }

    private void showSuccessfully() {
        Toast.makeText(this, "Account is create successfully", Toast.LENGTH_SHORT).show();
    }


    private void showError(String error){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
