package com.example.dione.noticesapp.modules.dashboard;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.bus.BusProvider;
import com.example.dione.noticesapp.event.RegisterResponseEvent;
import com.example.dione.noticesapp.event.RegisterTokenRequestEvent;
import com.example.dione.noticesapp.firebase.InstanceService;
import com.example.dione.noticesapp.firebase.MessagingService;
import com.example.dione.noticesapp.manager.SharedPreferenceManager;
import com.example.dione.noticesapp.modules.login.LoginActivity;
import com.example.dione.noticesapp.modules.models.AccountsModel;
import com.example.dione.noticesapp.modules.models.ChatModel;
import com.example.dione.noticesapp.modules.models.NoticesModel;
import com.example.dione.noticesapp.modules.models.RefreshModel;
import com.example.dione.noticesapp.utilities.ApplicationConstants;
import com.example.dione.noticesapp.utilities.Helpers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;


import org.joda.time.LocalDate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.internal.zzs.TAG;

public class DashboardActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, ValueEventListener {
    private boolean doubleBackToExitPressedOnce = false;
    private boolean isFirstLoad = true;
    private BroadcastReceiver noticesReceiver;
    private NoticesModel noticesModel;
    private AccountsModel accountsModel;
    private AnnouncementsFragment announcementsFragment;
    private ArrayList<NoticesModel> noticesModelArrayList = new ArrayList<>();
    private ArrayList<AccountsModel> adminsModelArrayList = new ArrayList<>();
    private ArrayList<AccountsModel> normalModelArrayList = new ArrayList<>();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private ProgressDialog progressDialog;
    private SharedPreferenceManager sharedPreferenceManager;
    private DatabaseReference adminNoticeReference;
    private DatabaseReference chatsReference;
    private Helpers helpers;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    TextView navHeaderName;
    TextView navHeaderEmail;
    ImageView navHeaderImage;
    private LocalDate localDate = new LocalDate();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        navHeaderName = (TextView) headerLayout.findViewById(R.id.navheader_name);
        navHeaderEmail = (TextView) headerLayout.findViewById(R.id.navheader_email);
        navHeaderImage = (ImageView) headerLayout.findViewById(R.id.navheader_image);
        announcementsFragment = new AnnouncementsFragment();
        sharedPreferenceManager = new SharedPreferenceManager(this);
        helpers = new Helpers(this);
        bindDrawer();
        selectMenu(0, announcementsFragment);
        startService(new Intent(this, InstanceService.class));
        startService(new Intent(this, MessagingService.class));
        if (!sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_REG_TOKEN, "").isEmpty()) {
            BusProvider.getInstance().post(new RegisterTokenRequestEvent(sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_REG_TOKEN, ""),
                    sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_USERNAME, ""),
                    sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_UID, ""),
                    ApplicationConstants.KEY_USER_TYPE));
            isFirstLoad = false;
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(ApplicationConstants.KEY_BUNDLE_FROM_CLASS).equalsIgnoreCase("login")) {
                initialize();
            }
        }
        if (!sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_DISPLAY_NAME, "").isEmpty()) {
            navHeaderName.setText(sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_DISPLAY_NAME, ""));
            navHeaderEmail.setText(sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_EMAIL, ""));
            Picasso.with(this)
                    .load(sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_PHOTO_URL, ""))
                    .resize(50, 50)
                    .centerCrop()
                    .into(navHeaderImage);
        }
//        initNoticeReceiver();
//        FirebaseMessaging.getInstance().subscribeToTopic("news");
    }

    private void initialize() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.show();
        adminNoticeReference = database.child(ApplicationConstants.FIREBASE_ENDPOINT_NOTICES_ADMIN);
        adminNoticeReference.addValueEventListener(this);
        chatsReference = database.child(ApplicationConstants.FIREBASE_ENDPOINT_CHATS + localDate.toString());
        chatsReference.addValueEventListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            doubleBackToExitPressedOnce = true;
            helpers.showToast(getString(R.string.info_exit_app));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                BusProvider.getInstance().post(new RefreshModel(true));
                int searchCount = 0;
                for (NoticesModel noticesModel : noticesModelArrayList) {
                    if (noticesModel.getTitle().contains(query)) {
                        BusProvider.getInstance().post(noticesModel);
                        searchCount++;
                    }

                }
                if (searchCount == 0) {
                    helpers.showToast("No " + getString(R.string.info_result_found));
                } else {
                    helpers.showToast(searchCount + " " + getString(R.string.info_result_found));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    BusProvider.getInstance().post(new RefreshModel(true));
                    for (NoticesModel noticesModel : noticesModelArrayList) {
                        BusProvider.getInstance().post(noticesModel);
                    }
                }
                return false;
            }
        });

        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            selectMenu(4, new SettingsFragment());
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_announcements) {
            openFragment(announcementsFragment);
            noticesModelArrayList = new ArrayList<>();
            adminNoticeReference = database.child(ApplicationConstants.FIREBASE_ENDPOINT_NOTICES_ADMIN);
            adminNoticeReference.addValueEventListener(this);
        } else if (id == R.id.nav_profile) {
            openFragment(new ProfileFragment());
        } else if (id == R.id.nav_settings) {
            openFragment(new SettingsFragment());
        } else if (id == R.id.nav_admins) {
            openFragment(new AdminsFragment());
            adminsModelArrayList = new ArrayList<>();
            chatsReference = database.child(ApplicationConstants.FIREBASE_ENDPOINT_CHATS + localDate.toString());
            chatsReference.addValueEventListener(this);
        } else if (id == R.id.nav_logout) {
            helpers.showToast(getString(R.string.info_bye) + " " + sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_DISPLAY_NAME, ""));
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            sharedPreferenceManager.clearPreference();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.dashboard_content_container, fragment)
                .commit();
    }

    private void bindDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void selectMenu(int position, Fragment fragment) {
        navigationView.getMenu().getItem(position).setChecked(true);
        openFragment(fragment);
    }

    private void initNoticeReceiver() {
        noticesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    Menu m = navigationView.getMenu();
                    MenuItem navAnnouncements = m.findItem(R.id.nav_announcements);
//                    count++;
//                    navAnnouncements.setTitle("Announcements" + " - " + count);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter("receive_notices");
        registerReceiver(noticesReceiver, intentFilter);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("SNAPSHOT", dataSnapshot.getKey());
        if (dataSnapshot.getKey().equals("notices")) {
            noticesDbHandler(dataSnapshot);
        } else {
            chatsDbHandler(dataSnapshot);
        }
    }

    private void noticesDbHandler(DataSnapshot dataSnapshot) {
        int count = 0;
        BusProvider.getInstance().post(new RefreshModel(true));

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                noticesModel = new NoticesModel();
                for (DataSnapshot snapsho2 : snapshot.getChildren()){
                    switch (snapsho2.getKey()) {
                        case "date":
                            noticesModel.setDate(snapsho2.getValue().toString());
                            break;
                        case "message":
                            noticesModel.setMessage(snapsho2.getValue().toString());
                            break;
                        case "user":
                            noticesModel.setUser(snapsho2.getValue().toString());
                            break;
                        case "title":
                            noticesModel.setTitle(snapsho2.getValue().toString().toLowerCase());
                            break;
                        case "short_message":
                            noticesModel.setShortMessage(snapsho2.getValue().toString());
                            break;
                    }
                }
                count++;
                if (noticesModel != null) {
                    BusProvider.getInstance().post(noticesModel);
                    noticesModelArrayList.add(noticesModel);
                    Menu m = navigationView.getMenu();
                    MenuItem navAnnouncements = m.findItem(R.id.nav_announcements);
                    navAnnouncements.setTitle(getString(R.string.nav_announcements) + " - " + count);
                }

            }



        }
        if (progressDialog != null) {
            if (progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

    private void chatsDbHandler(DataSnapshot dataSnapshot) {
        BusProvider.getInstance().post(new RefreshModel(true));
        ChatModel chatModel = null;

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            chatModel = new ChatModel();
            for (DataSnapshot ds1 : ds.getChildren()) {
                switch (ds1.getKey()) {
                    case "from":
                        chatModel.setFrom(ds1.getValue().toString());
                        break;
                    case "locTime":
                        chatModel.setLocTime(ds1.getValue().toString());
                        break;
                    case "message":
                        chatModel.setMessage(ds1.getValue().toString());
                        break;
                }
            }
            BusProvider.getInstance().post(chatModel);
        }
    }


    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("ERROR", databaseError.getMessage());
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        adminNoticeReference = database.child(ApplicationConstants.FIREBASE_ENDPOINT_NOTICES_ADMIN);
        adminNoticeReference.addValueEventListener(this);
        chatsReference = database.child(ApplicationConstants.FIREBASE_ENDPOINT_CHATS + localDate.toString());
        chatsReference.addValueEventListener(this);

    }

    @Subscribe
    public void onReceiveRegisterErrorResponse(RegisterResponseEvent registerResponseEvent) {
        helpers.showToast(registerResponseEvent.getStatus());
        helpers.closeProgressDialog();
    }
}
