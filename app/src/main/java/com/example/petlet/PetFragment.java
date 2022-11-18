package com.example.petlet;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
//
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class PetFragment extends Fragment {

    public List<Profile> profileList = new ArrayList<>();
    public RecyclerView profilesRecyclerView;
    public ProfileAdapter profileAdapter;
    public boolean pAIsMultiSelect = false;
    public List<Integer> pASelectedPositions = new ArrayList<>();
    public ActionMode actionMode;
    public FloatingActionButton nPFloatingActionButton;
    public int PICK_IMAGE_REQUEST = 1;
    public ProfilesDatabaseAdapter profilesDatabaseAdapter;
    public LinearLayout profilesActivityESLL;
    public boolean isEditingPPU = false;
    public int selectedPosition = 0;
    View rootLayout;

    protected Button updateAppBtn;
    protected LinearLayout updateAppLinearLayout;
    protected Double latestAppVersion = 1.0;
    protected Double currentAppVersion;

//    private AdView pAAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pet, container, false);
//        pAAdView = (AdView) v.findViewById(R.id.pa_ad_view);
//        AdRequest pAAdRequest = new AdRequest.Builder().build();
//        pAAdView.loadAd(pAAdRequest);

        updateAppLinearLayout = v.findViewById(R.id.update_app_linear_layout);

//        updateAppBtn = v.findViewById(R.id.update_app_btn);
//        updateAppBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final String packageName = getActivity().getPackageName();
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
//                }
//
//            }
//        });

        Float elevation = 0.0f;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(elevation);

        rootLayout = v.findViewById(R.id.profiles_activity_rl);
        profilesActivityESLL = v.findViewById(R.id.pa_empty_state_linear_layout);

        profilesDatabaseAdapter = new ProfilesDatabaseAdapter(getContext());
        profilesDatabaseAdapter.open();

        nPFloatingActionButton = v.findViewById(R.id.new_profile_fab);
        nPFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startImagePickingProcedure();
            }
        });

        initializeProfileList();

        profilesRecyclerView = v.findViewById(R.id.profiles_recycler_view);

        profileAdapter = new ProfileAdapter(getContext(), profileList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        profilesRecyclerView.setLayoutManager(mLayoutManager);
        profilesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        profilesRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), profilesRecyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (pAIsMultiSelect) {
                    multiSelect(position);
                } else {

                    Profile clickedProfile = profileList.get(position);

                    String name = clickedProfile.getName();
                    String profilePictureUri = clickedProfile.getProfilePictureUri();
                    int weight = clickedProfile.getWeight();
                    int gender = clickedProfile.getGender();
                    String breed = clickedProfile.getBreed();
                    String dob = clickedProfile.getDateOfBirth();
                    int age = clickedProfile.getAge();


                    openDetailActivity(name, profilePictureUri, weight, gender, breed, dob, age);

                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!pAIsMultiSelect) {
                    pASelectedPositions = new ArrayList<>();
                    pAIsMultiSelect = true;

                    if (actionMode == null) {
                        nPFloatingActionButton.hide();
                        actionMode = v.startActionMode(mActionModeCallback);
                    }
                }

                multiSelect(position);
            }
        }));

        profilesRecyclerView.setAdapter(profileAdapter);
        setProfilesActivityEmptyState();

        String currentAppVersionString = "1.0";
        try {
            currentAppVersionString = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);

        currentAppVersion = Double.valueOf(currentAppVersionString);

        PetFragment.GetLatestVersion getLatestVersion = new GetLatestVersion();
        getLatestVersion.execute();
        return v;
    }
    public void showEditCSnackBar(){
        Snackbar.make(rootLayout, "Profile Updated", Snackbar.LENGTH_SHORT).show();
    }

    public void updateProfile(final int profileId, final String nProfileName, final int nProfileWeight, final int nProfileGender, final String nProfileBreed, final String nProfileDob, final int profilePosition) {

        Runnable updateProfileRunnable = new Runnable() {
            @Override
            public void run() {
                profilesDatabaseAdapter.updateProfile(profileId, nProfileName, nProfileWeight, nProfileGender, nProfileBreed, nProfileDob);
            }
        };
        Thread updateProfileThread = new Thread(updateProfileRunnable);
        updateProfileThread.start();
        try {
            updateProfileThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        updateProfileListItem(nProfileName, nProfileWeight, nProfileGender, nProfileBreed, nProfileDob, profilePosition);
    }

    public void updateProfileListItem(String nProfileName, int nProfileWeight, int nProfileGender, String nProfileBreed, String nProfileDob, int profilePosition) {

        int age = getAgeFromDateOfBirth(nProfileDob);

        profileList.get(profilePosition).setName(nProfileName);
        profileList.get(profilePosition).setWeight(nProfileWeight);
        profileList.get(profilePosition).setGender(nProfileGender);
        profileList.get(profilePosition).setBreed(nProfileBreed);
        profileList.get(profilePosition).setDateOfBirth(nProfileDob);
        profileList.get(profilePosition).setAge(age);

        PetFragment.ProfileComparator profileComparator = new ProfileComparator();
        Collections.sort(profileList, profileComparator);

        profileAdapter.notifyDataSetChanged();
    }

    public void setProfilesActivityEmptyState() {

        if (profileList.size() == 0) {

            if (profilesRecyclerView.getVisibility() == View.VISIBLE) {
                profilesRecyclerView.setVisibility(GONE);
            }

            if (profilesActivityESLL.getVisibility() == GONE) {
                profilesActivityESLL.setVisibility(View.VISIBLE);
            }


        } else {

            if (profilesActivityESLL.getVisibility() == View.VISIBLE) {
                profilesActivityESLL.setVisibility(GONE);
            }

            if (profilesRecyclerView.getVisibility() == GONE) {
                profilesRecyclerView.setVisibility(View.VISIBLE);
            }

        }

    }

    public void openNewProfileFSD(String profilePictureUri) {
        FragmentManager oNPFSD = getActivity().getSupportFragmentManager();
        ProfileFSD newProfileFSD = ProfileFSD.newInstance(profilePictureUri, 0, "", 0, 0, "", "", 0, false);
        FragmentTransaction oNPFSDT = oNPFSD.beginTransaction();
        oNPFSDT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        oNPFSDT.add(android.R.id.content, newProfileFSD).addToBackStack(null).commit();
    }

    public void openEditProfileFSD(int id, String name, int weight, int gender, String breed, String dateOfBirth, int profilePosition) {

        FragmentManager oEPFSD = getActivity().getSupportFragmentManager();
        ProfileFSD editProfileFSD = ProfileFSD.newInstance("", id, name, weight, gender, breed, dateOfBirth, profilePosition, true);
        FragmentTransaction oEPFSDT = oEPFSD.beginTransaction();
        oEPFSDT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        oEPFSDT.add(android.R.id.content, editProfileFSD).addToBackStack(null).commit();

    }

    public void startImagePickingProcedure() {
        Toast.makeText(getActivity(), "Select a profile picture", Toast.LENGTH_SHORT).show();
        Intent imagePickingIntent = new Intent();
        imagePickingIntent.setType("image/*");
        if (Build.VERSION.SDK_INT < 19) {
            imagePickingIntent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            imagePickingIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        startActivityForResult(Intent.createChooser(imagePickingIntent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    public void updateProfilePicture(int selectedPosition1, final String imagePickerUriS) {
        final int sProfileId = profileList.get(selectedPosition1).getId();

        Runnable updateProfilePRunnable = new Runnable() {
            @Override
            public void run() {
                profilesDatabaseAdapter.updateProfilePicture(sProfileId, imagePickerUriS);
            }
        };
        Thread updateProfilePThread = new Thread(updateProfilePRunnable);
        updateProfilePThread.start();
        try {
            updateProfilePThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        profileList.get(selectedPosition1).setProfilePictureUri(imagePickerUriS);
        profileAdapter.notifyItemChanged(selectedPosition1);
        isEditingPPU = false;
        selectedPosition = 0;

        Snackbar.make(rootLayout, "Profile Picture Updated", Snackbar.LENGTH_SHORT).show();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Toast.makeText(getActivity(), "Profile picture selected", Toast.LENGTH_SHORT).show();

            Uri imagePickerUri = data.getData();

            if (Build.VERSION.SDK_INT >= 19) {
                final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                try {
                    getActivity().getApplicationContext().getContentResolver().takePersistableUriPermission(imagePickerUri, takeFlags);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }


            if (isEditingPPU) {
                updateProfilePicture(selectedPosition, String.valueOf(imagePickerUri));
            } else {
                openNewProfileFSD(String.valueOf(imagePickerUri));
            }

        }

    }

    public void initializeProfileList() {

        Runnable initializeProfileListRunnable = new Runnable() {
            @Override
            public void run() {
                profileList = profilesDatabaseAdapter.fetchProfiles();
            }
        };
        Thread initializeProfileListThread = new Thread(initializeProfileListRunnable);
        initializeProfileListThread.start();
        try {
            initializeProfileListThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public long getTIMFromDS(String pDTS) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(pDTS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long reminderTIM = 0;

        if (date != null) {
            reminderTIM = date.getTime();
        }

        return reminderTIM;
    }

    public long getNowTIM() {
        Date currentDateTime = new Date();
        long currentDateTimeInMills = currentDateTime.getTime();
        return currentDateTimeInMills;
    }

    public int getAgeFromDateOfBirth(String dob) {

        long dobMills = getTIMFromDS(dob);

        long nowTIM = getNowTIM();

        long ageInMills = nowTIM - dobMills;

        int ageInDays = (int) (ageInMills / (1000 * 60 * 60 * 24));

        int ageInYears = (int) (ageInDays / (365));

        return ageInYears;
    }

//    public void addProfile(final String name, final String profilePictureUri, final int weight, final int gender, final String breed, final String dateOfBirth) {
//        final int[] newProfileId = new int[1];
//
//        Runnable addProfileRunnable = new Runnable() {
//            @Override
//            public void run() {
//                newProfileId[0] = profilesDatabaseAdapter.createProfile(name, profilePictureUri, weight, gender, breed, dateOfBirth);
//
//            }
//        };
//        Thread addProfileThread = new Thread(addProfileRunnable);
//        addProfileThread.start();
//        try {
//            addProfileThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        int age = getAgeFromDateOfBirth(dateOfBirth);
//
//        Profile newProfile = new Profile(newProfileId[0], name, profilePictureUri, weight, gender, breed, dateOfBirth, age);
//        addProfileToList(newProfile);
//        setProfilesActivityEmptyState();
//    }

    public void addProfileToList(Profile newProfile) {
        profileList.add(newProfile);

        PetFragment.ProfileComparator profileComparator = new ProfileComparator();
        Collections.sort(profileList, profileComparator);

        profileAdapter.notifyDataSetChanged();

        openDetailActivity(newProfile.getName(), newProfile.getProfilePictureUri(), newProfile.getWeight(), newProfile.getGender(), newProfile.getBreed(), newProfile.getDateOfBirth(), newProfile.getAge());
    }

    public void multiSelect(int position) {
        Profile selectedProfile = profileAdapter.getItem(position);
        if (selectedProfile != null) {
            if (actionMode != null) {
                int previousPosition = -1;
                if (pASelectedPositions.size() > 0) {
                    previousPosition = pASelectedPositions.get(0);
                }
                pASelectedPositions.clear();
                pASelectedPositions.add(position);

                profileAdapter.setSelectedPositions(previousPosition, pASelectedPositions);
            }
        }
    }

    public void openDetailActivity(String name, String profilePictureUri, int weight, int gender, String breed, String dob, int age) {
        Intent mainToFlexibleActivityIntent = new Intent(getActivity(), DetailActivity.class);
        mainToFlexibleActivityIntent.putExtra("name", name);
        mainToFlexibleActivityIntent.putExtra("profilePictureUri", profilePictureUri);
        mainToFlexibleActivityIntent.putExtra("weight", weight);
        mainToFlexibleActivityIntent.putExtra("gender", gender);
        mainToFlexibleActivityIntent.putExtra("breed", breed);
        mainToFlexibleActivityIntent.putExtra("dob", dob);
        mainToFlexibleActivityIntent.putExtra("age", age);

        startActivity(mainToFlexibleActivityIntent);
    }

    public void hideActionBar(){
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    public void showActionBar(){
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    public void deleteProfileListItem(int profilePosition) {
        profileList.remove(profilePosition);
        profileAdapter.notifyItemRemoved(profilePosition);
    }

//    public MenuInflater getMenuInflater() {
//        return new MenuInflater(getActivity());
//    }

    //APP BAR MENU
//    @Override
//    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
//        getMenuInflater().inflate(R.menu.profiles_activity_options_menu, menu);
//        super.onCreateOptionsMenu(menu,inflater);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem items){
//        switch (items.getItemId()) {
//            case R.id.profiles_activity_help:
//                openHelp();
//                return true;
//
//            case R.id.profiles_activity_sa:
//                shareApp();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(items);
//        }
//
//
//    }

//    protected void shareApp() {
//        Intent sAIntent = new Intent();
//        sAIntent.setAction(Intent.ACTION_SEND);
//        sAIntent.putExtra(Intent.EXTRA_TEXT, "Petlet is a fast and simple app that enables me to store basic information about my pets for free.");
//        sAIntent.setType("text/plain");
//        Intent.createChooser(sAIntent, "Share via");
//        startActivity(sAIntent);
//    }
//
//    protected void openHelp() {
//        Intent homeToHelpActivityIntent = new Intent(getActivity(), HelpActivity.class);
//        startActivity(homeToHelpActivityIntent);
//    }


    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.profiles_avw, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {

                case R.id.delete_profile:
                    AlertDialog.Builder deleteProfileDialogBuilder = new AlertDialog.Builder(getActivity());
                    deleteProfileDialogBuilder.setTitle(getResources().getString(R.string.delete_profile_dialog_title));
                    deleteProfileDialogBuilder.setMessage(getResources().getString(R.string.delete_profile_dialog_message));
                    deleteProfileDialogBuilder.setNegativeButton(getResources().getString(R.string.delete_profile_dialog_negative_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            actionMode.finish();

                        }
                    });

                    deleteProfileDialogBuilder.setPositiveButton(getResources().getString(R.string.delete_profile_dialog_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (pASelectedPositions.size() > 0) {
                                int selectedPosition = pASelectedPositions.get(0);
                                final int profileId = profileList.get(selectedPosition).getId();

                                Runnable deleteProfileRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        profilesDatabaseAdapter.deleteProfile(profileId);
                                    }
                                };
                                Thread deleteProfileThread = new Thread(deleteProfileRunnable);
                                deleteProfileThread.start();
                                try {
                                    deleteProfileThread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                deleteProfileListItem(selectedPosition);
                                setProfilesActivityEmptyState();
                            }

                            dialogInterface.dismiss();
                            actionMode.finish();
                        }
                    });
                    deleteProfileDialogBuilder.create().show();

                    return true;

                case R.id.edit_pdetails:

                    if (pASelectedPositions.size() > 0) {
                        int selectedPosition = pASelectedPositions.get(0);
                        Profile selectedProfile = profileList.get(selectedPosition);
                        openEditProfileFSD(selectedProfile.getId(), selectedProfile.getName(), selectedProfile.getWeight(), selectedProfile.getGender(), selectedProfile.getBreed(), selectedProfile.getDateOfBirth(), selectedPosition);
                    }
                    actionMode.finish();
                    return true;

                case R.id.edit_ppicture:

                    if (pASelectedPositions.size() > 0) {
                        int selectedPosition1 = pASelectedPositions.get(0);
                        isEditingPPU = true;
                        selectedPosition = selectedPosition1;
                        startImagePickingProcedure();
                    }

                    actionMode.finish();
                    return true;
                default:

            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            nPFloatingActionButton.show();
            actionMode = null;
            pAIsMultiSelect = false;

            int previousPosition = -1;
            if (pASelectedPositions.size() > 0) {
                previousPosition = pASelectedPositions.get(0);
            }
            pASelectedPositions = new ArrayList<>();

            profileAdapter.setSelectedPositions(previousPosition, new ArrayList<Integer>());

        }
    };



    protected static class ProfileComparator implements Comparator<Profile> {

        public int compare(Profile profileOne, Profile profileTwo) {
            String profileOName = profileOne.getName();
            String profileTName = profileTwo.getName();

            int comparisonResult = profileOName.compareToIgnoreCase(profileTName);

            return comparisonResult;
        }


    }

    protected class GetLatestVersion extends AsyncTask<Void, Void, String> {


        GetLatestVersion() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject getLatestVersionObject = new JSONObject(s);
                String latestAppVersionInString = getLatestVersionObject.getString("version");
                latestAppVersion = Double.valueOf(latestAppVersionInString);

                Handler handler=new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(currentAppVersion<latestAppVersion){

                            if(updateAppLinearLayout.getVisibility()==GONE){
                                updateAppLinearLayout.setVisibility(View.VISIBLE);
                            }

                        }

                        else {

                            if(updateAppLinearLayout.getVisibility()==View.VISIBLE){
                                updateAppLinearLayout.setVisibility(GONE);
                            }

                        }

                    }
                });



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Void... voids) {

            String getLatestVersionUrlInString="https://chanresti.com///PetManager/PetManagerVersion/v1/index.php?";

            StringBuilder getLatestVersionStringBuilder = new StringBuilder();
            try {
                URL getLatestVersionUrl = new URL(getLatestVersionUrlInString);
                HttpURLConnection getLatestVersionConnection = (HttpURLConnection) getLatestVersionUrl.openConnection();
                BufferedReader getLatestVersionBufferedReader = new BufferedReader(new InputStreamReader(getLatestVersionConnection.getInputStream()));

                String s;
                while ((s = getLatestVersionBufferedReader.readLine()) != null) {
                    getLatestVersionStringBuilder.append(s + "\n");
                }
            } catch (Exception e) {
            }

            return getLatestVersionStringBuilder.toString();

        }
    }


}
