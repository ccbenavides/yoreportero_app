package com.grupolts.ccbenavides.fireui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;
    private static final int RC_SIGN_IN = 1 ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ListView listUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                    // Toast.makeText(MainActivity.this, "Usuario se ha logeado correctamente", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    initApp();
                } else {
                    // User is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .setTheme(R.style.MiTheme)
                                    .build(),
                            RC_SIGN_IN);

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        listUsers = (ListView) findViewById(R.id.listUsuarios);

    }

    private void initApp() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Usuario usuario = new Usuario("Marko", "Hola soy, marko", "U001");
        myRef.push().setValue(usuario);

        myRef.limitToLast(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String info = "";

                for (DataSnapshot msgSnapshot: snapshot.getChildren()) {
                    Usuario u = msgSnapshot.getValue(Usuario.class);
                    info += u.toString() + "\n";
                }

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("Chat", "The read failed: " + firebaseError.getMessage());
            }
        });

        FirebaseListAdapter adapter = new FirebaseListAdapter<Usuario>(this, Usuario.class, R.layout.three_line_list_item,
                myRef) {
            @Override
            protected void populateView(View v, Usuario model, int position) {
                ((TextView) v.findViewById(R.id.txtName)).setText(model.getName());
                ((TextView) v.findViewById(R.id.txtText)).setText(model.getText());
                ((TextView) v.findViewById(R.id.txtUid)).setText(model.getUid());
            }
        };
        listUsers.setAdapter(adapter);
    }


    /*
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }*/

    /*
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if(mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthListener);
    }


    public void signOut(View v) {
        AuthUI.getInstance().signOut(this);
      /*  if (v.getId() == R.id.btnSalir) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                           // startActivity(new Intent(MainActivity.this, SignInActivity.class));
                           // finish();
                        }


                    });
        }*/
    }
}
