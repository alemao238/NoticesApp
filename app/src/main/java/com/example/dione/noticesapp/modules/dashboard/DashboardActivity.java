package com.example.dione.noticesapp.modules.dashboard;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.bus.BusProvider;
import com.example.dione.noticesapp.firebase.InstanceService;
import com.example.dione.noticesapp.firebase.MessagingService;
import com.example.dione.noticesapp.manager.SharedPreferenceManager;
import com.example.dione.noticesapp.modules.login.LoginActivity;
import com.example.dione.noticesapp.modules.models.NoticesModel;
import com.example.dione.noticesapp.modules.models.RefreshModel;
import com.example.dione.noticesapp.utilities.ApplicationConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, ValueEventListener {
    private BroadcastReceiver noticesReceiver;
    private NoticesModel noticesModel;
    private AnnouncementsFragment announcementsFragment;
    private ArrayList<NoticesModel> noticesModelArrayList = new ArrayList<>();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private ProgressDialog progressDialog;
    private SharedPreferenceManager sharedPreferenceManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
//    @BindView(R.id.navheader_name)
    TextView navHeaderName;
//    @BindView(R.id.navheader_email)
    TextView navHeaderEmail;
//    @BindView(R.id.navheader_image)
    ImageView navHeaderImage;
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
        bindDrawer();
        selectMenu(0, announcementsFragment);
        startService(new Intent(this, InstanceService.class));
        startService(new Intent(this, MessagingService.class));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("from_class").equalsIgnoreCase("login")) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.loading_data));
                progressDialog.show();
                DatabaseReference adminNoticeReference = database.child("notices/admin");
                adminNoticeReference.addValueEventListener(this);
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                //do search here
                BusProvider.getInstance().post(new RefreshModel(true));
                int searchCount = 0;
                for (NoticesModel noticesModel : noticesModelArrayList) {
                    if (noticesModel.getTitle().equalsIgnoreCase(query)) {
                        BusProvider.getInstance().post(noticesModel);
                        searchCount++;
                    }

                }
                if (searchCount == 0) {
                    Toast.makeText(DashboardActivity.this, "No result found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DashboardActivity.this, searchCount +" results found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //do search here if needed
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
            DatabaseReference adminNoticeReference = database.child("notices/admin");
            adminNoticeReference.addValueEventListener(this);
        } else if (id == R.id.nav_profile) {
            openFragment(new ProfileFragment());
        } else if (id == R.id.nav_settings) {
            openFragment(new SettingsFragment());
        } else if (id == R.id.nav_admins) {

        } else if (id == R.id.nav_co_workers) {

        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "See you later " + sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_DISPLAY_NAME, ""), Toast.LENGTH_LONG).show();
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
        int count = 0;
        BusProvider.getInstance().post(new RefreshModel(true));
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            noticesModel = new NoticesModel();
            for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                switch (snapshot.getKey()) {
                    case "date":
                        noticesModel.setDate(snapshot.getValue().toString());
                        break;
                    case "message":
                        noticesModel.setMessage(snapshot.getValue().toString());
                        break;
                    case "user":
                        noticesModel.setUser(snapshot.getValue().toString());
                        break;
                    case "title":
                        noticesModel.setTitle(snapshot.getValue().toString());
                        break;
                    case "short_message":
                        noticesModel.setShortMessage(snapshot.getValue().toString());
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

            if (progressDialog != null) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("ERROR", databaseError.getMessage());
    }
}
