package com.ismt.ismtproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.Dash;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.ismt.ismtproject.databinding.ActivityDashboardJourneyJournalBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardJourneyJournal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDashboardJourneyJournalBinding binding;
    private FloatingActionButton fab;
    private Button logoutBtn;
    private CircleImageView profileImage;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardJourneyJournalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fab = findViewById(R.id.fab);
        logoutBtn = findViewById(R.id.logoutBtn);





        mAuth = FirebaseAuth.getInstance();
        logoutBtn.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(DashboardJourneyJournal.this, "Logging Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashboardJourneyJournal.this, MainActivity.class));
        });



        setSupportActionBar(binding.appBarDashboardJourneyJournal.toolbar);
        binding.appBarDashboardJourneyJournal.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardJourneyJournal.this, JournalEntry.class));
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard_journey_journal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_journey_journal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard_journey_journal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}