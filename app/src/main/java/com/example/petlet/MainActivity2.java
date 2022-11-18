package com.example.petlet;

import static android.view.Gravity.*;
import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener, ProfileInterface{
    TextView txt1;
    Button btn;
    private ActionMode mActionMode;
    private ProfileInterface profileInterface;
    BottomNavigationView bottomNavigationView;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //to remove app title from appbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        //to set color to appbar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.P_DarkPurple)));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        //to make action bar transparent
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navdraw = findViewById(R.id.navView);
        navdraw.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                int id = item.getItemId();
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                switch(id){

                    case R.id.nav_contact:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fourthFragment).addToBackStack(null).commit();
                        return true;
                    case R.id.feedback:
                        i = new Intent(MainActivity2.this, Feedback.class);
                        startActivity(i);
                        return true;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(MainActivity2.this, FirstPage.class));
                        return true;
                    case R.id.nav_home:
//                    Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).addToBackStack(null).commit();
                        return true;
                    case R.id.nav_vet:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).addToBackStack(null).commit();
                        return true;

                    case R.id.nav_pet:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).addToBackStack(null).commit();
//                        Intent j = new Intent(MainActivity2.this,PetFragment.class);
//                        startActivity(j);
                        return true;
                    case R.id.nav_about:
                        i = new Intent(MainActivity2.this, aboutus.class);
                        startActivity(i);
                        return true;
                    case R.id.nav_dev:
                        i = new Intent(MainActivity2.this, ContactDevelopers.class);
                        startActivity(i);
                        return true;

                    default:
                        return true;
                }
//                return false;
            }
        });

    }

    private void replacefragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }


    //Bottom navigation

    AnimalsFragment firstFragment = new AnimalsFragment();
    VetFragment secondFragment = new VetFragment();
    PetFragment thirdFragment = new PetFragment();
    SettingFragment fourthFragment = new SettingFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).addToBackStack(null).commit();
                return true;
            case R.id.doctor:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).addToBackStack(null).commit();
                return true;
            case R.id.animal:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).addToBackStack(null).commit();
//                Intent i = new Intent(MainActivity2.this,ProfilesActivity.class);
//                startActivity(i);
                return true;
            case R.id.contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fourthFragment).addToBackStack(null).commit();
                return true;

        }
        return false;
    }

    //APP BAR MENU
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.appbar, menu);
            return true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem items){
        Intent i;
        //NAV DRAWER
            if (actionBarDrawerToggle.onOptionsItemSelected(items)) {
                return true;
            }
        //APP BAR NAV
            switch (items.getItemId()) {
                case R.id.feedback:
                    i = new Intent(MainActivity2.this, Feedback.class);
                    startActivity(i);
                    return true;
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).addToBackStack(null).commit();
                    return true;
                case R.id.profiles_activity_help:
                    openHelp();
                    return true;

                case R.id.profiles_activity_sa:
                    shareApp();
                    return true;
                default:
                    return super.onOptionsItemSelected(items);

            }


        }
    protected void shareApp() {
        Intent sAIntent = new Intent();
        sAIntent.setAction(Intent.ACTION_SEND);
        sAIntent.putExtra(Intent.EXTRA_TEXT, "Petlet is a fast and simple app that enables me to store basic information about my pets for free.");
        sAIntent.setType("text/plain");
        Intent.createChooser(sAIntent, "Share via");
        startActivity(sAIntent);
    }

    protected void openHelp() {
        Intent homeToHelpActivityIntent = new Intent(MainActivity2.this, HelpActivity.class);
        startActivity(homeToHelpActivityIntent);
    }

    //POP UP MENU
    public void showPopUp(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.popup_menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.goBack:
            case R.id.goLogin:
                Intent i2 = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(i2);
                return true;
            case R.id.goFront:
                Toast.makeText(MainActivity2.this, "FRONT!!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.goBlank:
                Toast.makeText(MainActivity2.this, "GOOO BLANKKKK!!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }
    });
    popup.show();
    }

    public void showEditCSnackBar(){
        Snackbar.make(thirdFragment.rootLayout, "Profile Updated", Snackbar.LENGTH_SHORT).show();
    }

    public void updateProfile(final int profileId, final String nProfileName, final int nProfileWeight, final int nProfileGender, final String nProfileBreed, final String nProfileDob, final int profilePosition) {

        Runnable updateProfileRunnable = new Runnable() {
            @Override
            public void run() {
                thirdFragment.profilesDatabaseAdapter.updateProfile(profileId, nProfileName, nProfileWeight, nProfileGender, nProfileBreed, nProfileDob);
            }
        };
        Thread updateProfileThread = new Thread(updateProfileRunnable);
        updateProfileThread.start();
        try {
            updateProfileThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thirdFragment.updateProfileListItem(nProfileName, nProfileWeight, nProfileGender, nProfileBreed, nProfileDob, profilePosition);
    }

//    protected void updateProfileListItem(String nProfileName, int nProfileWeight, int nProfileGender, String nProfileBreed, String nProfileDob, int profilePosition) {
//
//        int age = getAgeFromDateOfBirth(nProfileDob);
//
//        thirdFragment.profileList.get(profilePosition).setName(nProfileName);
//        thirdFragment.profileList.get(profilePosition).setWeight(nProfileWeight);
//        thirdFragment.profileList.get(profilePosition).setGender(nProfileGender);
//        thirdFragment.profileList.get(profilePosition).setBreed(nProfileBreed);
//        thirdFragment.profileList.get(profilePosition).setDateOfBirth(nProfileDob);
//        thirdFragment.profileList.get(profilePosition).setAge(age);
//
//        ProfilesActivity.ProfileComparator profileComparator = new ProfilesActivity.ProfileComparator();
//        Collections.sort(thirdFragment.profileList, profileComparator);
//
//        thirdFragment.profileAdapter.notifyDataSetChanged();
//    }

//    protected void setProfilesActivityEmptyState() {
//
//        if (thirdFragment.profileList.size() == 0) {
//
//            if (thirdFragment.profilesRecyclerView.getVisibility() == View.VISIBLE) {
//                thirdFragment.profilesRecyclerView.setVisibility(GONE);
//            }
//
//            if (thirdFragment.profilesActivityESLL.getVisibility() == GONE) {
//                thirdFragment.profilesActivityESLL.setVisibility(View.VISIBLE);
//            }
//
//
//        } else {
//
//            if (thirdFragment.profilesActivityESLL.getVisibility() == View.VISIBLE) {
//                thirdFragment.profilesActivityESLL.setVisibility(GONE);
//            }
//
//            if (thirdFragment.profilesRecyclerView.getVisibility() == GONE) {
//                thirdFragment.profilesRecyclerView.setVisibility(View.VISIBLE);
//            }
//
//        }
//
//    }

//    public void openNewProfileFSD(String profilePictureUri){
//        FragmentManager oNPFSD = getSupportFragmentManager();
//        ProfileFSD newProfileFSD = ProfileFSD.newInstance(profilePictureUri,0,"",0, 0, "", "", 0, false);
//        FragmentTransaction oNPFSDT = oNPFSD.beginTransaction();
//        oNPFSDT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        oNPFSDT.add(android.R.id.content, newProfileFSD).addToBackStack(null).commit();
//    }

    public void openEditProfileFSD(int id, String name, int weight, int gender, String breed, String dateOfBirth, int profilePosition){

        FragmentManager oEPFSD = getSupportFragmentManager();
        ProfileFSD editProfileFSD = ProfileFSD.newInstance("",id,name,weight, gender, breed, dateOfBirth, profilePosition, true);
        FragmentTransaction oEPFSDT = oEPFSD.beginTransaction();
        oEPFSDT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        oEPFSDT.add(android.R.id.content, editProfileFSD).addToBackStack(null).commit();

    }

//    public void startImagePickingProcedure(){
//        Toast.makeText(getApplicationContext(),"Select a profile picture",Toast.LENGTH_SHORT).show();
//        Intent imagePickingIntent = new Intent();
//        imagePickingIntent.setType("image/*");
//        if (Build.VERSION.SDK_INT <19){
//            imagePickingIntent.setAction(Intent.ACTION_GET_CONTENT);
//        } else {
//            imagePickingIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        }
//        startActivityForResult(Intent.createChooser(imagePickingIntent, "Select Profile Picture"), thirdFragment.PICK_IMAGE_REQUEST);
//    }

//    private void updateProfilePicture(int selectedPosition1, final String imagePickerUriS){
//        final int sProfileId = thirdFragment.profileList.get(selectedPosition1).getId();
//
//        Runnable updateProfilePRunnable = new Runnable() {
//            @Override
//            public void run() {
//                profilesDatabaseAdapter.updateProfilePicture(sProfileId, imagePickerUriS);
//            }
//        };
//        Thread updateProfilePThread = new Thread(updateProfilePRunnable);
//        updateProfilePThread.start();
//        try {
//            updateProfilePThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        thirdFragment.profileList.get(selectedPosition1).setProfilePictureUri(imagePickerUriS);
//        thirdFragment.profileAdapter.notifyItemChanged(selectedPosition1);
//        thirdFragment.isEditingPPU = false;
//        thirdFragment.selectedPosition=0;
//
//        Snackbar.make(thirdFragment.rootLayout, "Profile Picture Updated", Snackbar.LENGTH_SHORT).show();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == thirdFragment.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Toast.makeText(getApplicationContext(),"Profile picture selected",Toast.LENGTH_SHORT).show();
//
//            Uri imagePickerUri = data.getData();
//
//            if (Build.VERSION.SDK_INT >= 19) {
//                final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//
//                try {
//                    getContentResolver().takePersistableUriPermission(imagePickerUri, takeFlags);
//                }
//                catch (SecurityException e){
//                    e.printStackTrace();
//                }
//            }
//
//
//            if(thirdFragment.isEditingPPU){
//                thirdFragment.updateProfilePicture(thirdFragment.selectedPosition, String.valueOf(imagePickerUri));
//            }
//
//            else {
//                thirdFragment.openNewProfileFSD(String.valueOf(imagePickerUri));
//            }
//
//        }
//
//    }

//    protected void initializeProfileList() {
//
//        Runnable initializeProfileListRunnable = new Runnable() {
//            @Override
//            public void run() {
//                thirdFragment.profileList = profilesDatabaseAdapter.fetchProfiles();
//            }
//        };
//        Thread initializeProfileListThread = new Thread(initializeProfileListRunnable);
//        initializeProfileListThread.start();
//        try {
//            initializeProfileListThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//    }

//    public long getTIMFromDS(String pDTS){
//        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
//        Date date = null;
//        try {
//            date=dateFormat.parse(pDTS);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        long reminderTIM = 0;
//
//        if (date != null) {
//            reminderTIM = date.getTime();
//        }
//
//        return reminderTIM;
//    }

//    protected long getNowTIM(){
//        Date currentDateTime =new Date();
//        long currentDateTimeInMills = currentDateTime.getTime();
//        return currentDateTimeInMills;
//    }

//    protected int getAgeFromDateOfBirth(String dob){
//
//        long dobMills = getTIMFromDS(dob);
//
//        long nowTIM = getNowTIM();
//
//        long ageInMills = nowTIM - dobMills;
//
//        int ageInDays = (int) (ageInMills/ (1000*60*60*24));
//
//        int ageInYears = (int) (ageInDays/ (365));
//
//        return ageInYears;
//    }

    public void addProfile(final String name, final String profilePictureUri, final int weight, final int gender, final String breed, final String dateOfBirth) {
        final int[] newProfileId = new int[1];

        Runnable addProfileRunnable = new Runnable() {
            @Override
            public void run() {
                newProfileId[0] = thirdFragment.profilesDatabaseAdapter.createProfile(name, profilePictureUri, weight, gender, breed, dateOfBirth);

            }
        };

        Thread addProfileThread = new Thread(addProfileRunnable);
        addProfileThread.start();
        try {
            addProfileThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int age = thirdFragment.getAgeFromDateOfBirth(dateOfBirth);

        Profile newProfile = new Profile(newProfileId[0], name, profilePictureUri, weight, gender, breed, dateOfBirth, age);
        thirdFragment.addProfileToList(newProfile);
        thirdFragment.setProfilesActivityEmptyState();
    }


    public void hideActionBar(){
        getSupportActionBar().hide();
    }

    public void showActionBar(){
        getSupportActionBar().show();
    }



}
