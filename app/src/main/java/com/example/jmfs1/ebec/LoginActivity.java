package com.example.jmfs1.ebec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmfs1.ebec.core.Team;
import com.example.jmfs1.ebec.core.User;
import com.example.jmfs1.ebec.messaging.MessagingUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mTeamView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private List<User> users = new ArrayList();
    private User mUser;
    private Team teamData;
    private List<Team> teams = new ArrayList();

    private boolean cancel = false;
    private View focusView = null;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Verify if an user is already logged in
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("LOGIN_PREFS", 0);
        prefs.edit();

        if (prefs.contains("TEAM")) {
            Intent it = new Intent(LoginActivity.this.getApplicationContext(), MainActivity.class);
            startActivity(it);
            finish();
        }

        // Set up the login form.
        mTeamView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mTeamView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mTeamView.setError(null);
        mPasswordView.setError(null);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = db.child("users");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get users
                users.clear();
                for (DataSnapshot userSnaphot : dataSnapshot.getChildren()) {
                    User user = userSnaphot.getValue(User.class);
                    user.setUsername(userSnaphot.getKey());
                    users.add(user);
                }

                // Store values at the time of the login attempt.
                email = mTeamView.getText().toString();
                password = mPasswordView.getText().toString();

                // Convert username to email if needed
                mUser = null;
                if (!email.contains("@")) {
                    for (User user : users) {
                        if (user.getUsername().equals(email)) {
                            email = user.getEmail();
                            mUser = user;
                            break;
                        }
                    }
                } else {
                    for (User user : users) {
                        if (user.getEmail().equals(email)) {
                            mUser = user;
                            break;
                        }
                    }
                }

                // Check for a valid team.
                if (TextUtils.isEmpty(email)) {
                    mTeamView.setError(getString(R.string.error_field_required));
                    focusView = mTeamView;
                    cancel = true;
                }

                // Check for a valid password, if the user entered one.
                if (TextUtils.isEmpty(password)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mPasswordView;
                    cancel = true;
                }

                // Get teams data
                DatabaseReference db2 = FirebaseDatabase.getInstance().getReference();
                DatabaseReference teamsRef = db2.child("teams");

                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        users.clear();
//                        for (DataSnapshot userSnaphot : dataSnapshot.getChildren()) {
//                            User user = userSnaphot.getValue(User.class);
//                            user.setUsername(userSnaphot.getKey());
//                            users.add(user);
//                        }
                        // Get teams
                        teams.clear();
                        for (DataSnapshot teamSnaphot : dataSnapshot.getChildren()) {

                            Team team = teamSnaphot.getValue(Team.class);

                            teams.add(team);
                        }

                        if (cancel) {
                            // There was an error; don't attempt login and focus the first
                            // form field with an error.
                            focusView.requestFocus();
                        } else {
                            // Show a progress spinner, and kick off a background task to
                            // perform the user login attempt.
                            showProgress(true);
                            mAuthTask = new UserLoginTask(email, password);
                            mAuthTask.execute((Void) null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                teamsRef.addValueEventListener(eventListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        usersRef.addValueEventListener(eventListener);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mTeamView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            if (mUser == null) {
                return false;
            }

            if (!mUser.getPassword().equals(mPassword)) {
                return false;
            }

            // Get team data
            String user_full_name = mUser.getFirst_name() + " " + mUser.getLast_name();
            teamData = null;
            for(Team team : teams) {
                if (team.getParticipants().contains(user_full_name)) {
                    teamData = team;
                    break;
                }
            }

            // Check if team was found
            if (teamData == null) {
                List<String> core_team_users = Arrays.asList("anavasconcelos", "mariadavid", "dinisbruno", "catarinalopes", "anaoliveira");
                List<String> topic_group_users = Arrays.asList("rafaelareis", "catarinagomes", "anamoura", "eduardasilva", "jorgemarques");
                List<String> board_users = Arrays.asList("migueloliveira", "joanacaneco", "franciscabraganca", "anamalta", "ritasardao");
                List<String> mgmt_users = Arrays.asList("diegohernandez", "bogdankulyk", "jeanbrito", "adrianacosta");

                String username = mUser.getUsername();
                if (username.equals("joaovalente")) {
                    teamData = new Team(-1, "mo", 0, null, null, null, null);
                }
                else if (core_team_users.contains(username)) {
                    teamData = new Team(-1, "core-team", 0, null, null, null, null);
                }
                else if (topic_group_users.contains(username)) {
                    teamData = new Team(-1, "topic-group", 0, null, null, null, null);
                }
                else if (board_users.contains(username)) {
                    teamData = new Team(-1, "board", 0, null, null, null, null);
                }
                else if (mgmt_users.contains(username)) {
                    teamData = new Team(-1, "mgmt", 0, null, null, null, null);
                }
                else {
                    teamData = new Team(-1, "organisers", 0, null, null, null, null);
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                // Save data in users phone
                SharedPreferences.Editor prefs = getApplicationContext().getSharedPreferences("LOGIN_PREFS", 0).edit();
                String team = ""+teamData.getName();
                prefs.putString ("TEAM", team);
                prefs.putString("TEAMNAME", teamData.getName());
                prefs.commit();

                //Log.d("Subscribed", "to " + team);
                //Log.d("Subscribed", "to " + teamData.getName());
                // Subscribe topic
                Log.d("Subscribed", "to " + team);
                MessagingUtils.subscribeTopic(team);

                Intent it = new Intent(LoginActivity.this.getApplicationContext(), MainActivity.class);
                startActivity(it);
                finish();
            } else {
                mTeamView.setError(getString(R.string.error_incorrect_password));
                mTeamView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

