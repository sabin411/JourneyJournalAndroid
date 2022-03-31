package com.ismt.ismtproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismt.ismtproject.database.User;
import com.ismt.ismtproject.databinding.ActivityDashboardJourneyJournalBinding;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardJourneyJournal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDashboardJourneyJournalBinding binding;
    private FloatingActionButton fab;
    private Button logoutBtn;
    private CircleImageView profilePic;
    private TextView greeting_text, profileName, profileEmail;
    public String name = null;
    public String email = null;
    public String profileLink = null;



     private RecyclerView entriesListings;
     DatabaseReference databaseReference;
     DatabaseReference profileReference;
     MyAdapter myAdapter;
     ArrayList<Journal> list;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardJourneyJournalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fab = findViewById(R.id.fab);
        logoutBtn = findViewById(R.id.logoutBtn);
        greeting_text = findViewById(R.id.greeting_text);
        profilePic = (CircleImageView) findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);

//        here to Code for mapping data from firebase to recycler view.....
        entriesListings = (RecyclerView)findViewById(R.id.entriesListings);
        entriesListings.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser rUser = FirebaseAuth.getInstance().getCurrentUser();
        assert rUser != null;
        String userId = rUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Entries").child(userId);

        FirebaseRecyclerOptions<Journal> options = new FirebaseRecyclerOptions.Builder<Journal>().setQuery(databaseReference, Journal.class).build();

        myAdapter=new MyAdapter(options);
        entriesListings.setAdapter(myAdapter);
//here the code ends....

//      Code to display user information in dashboard... where needed.

        Intent intent = getIntent();
        String user_full_name = intent.getStringExtra("name");
        String user_email = intent.getStringExtra("email");
        String user_phone = intent.getStringExtra("phone");
        String user_address = intent.getStringExtra("address");
        String user_password = intent.getStringExtra("password");
        String user_profileUrl = intent.getStringExtra("profileUrl");

        greeting_text.setText("Hi " + user_full_name + " \uD83D\uDC4B\uD83C\uDFFB");

        mAuth = FirebaseAuth.getInstance();

        logoutBtn.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(DashboardJourneyJournal.this, "Logging Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashboardJourneyJournal.this, MainActivity.class));
        });

//        The code to display user information ends here....



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
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    protected  void onStop(){
        super.onStop();
        myAdapter.stopListening();
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