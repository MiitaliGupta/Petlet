package com.example.petlet;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AnimalsFragment extends Fragment {
    TextView txt1;
    Button btn;
    private ActionMode mActionMode;
    ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_animals, container, false);
//        //to remove app title from appbar
//        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
//        //to set color to appbar
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.P_DarkPurple)));


        //to make action bar transparent
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//
//        txt1 = (TextView) v.findViewById(R.id.name);
////        Bundle b1 = getActivity().getIntent().getExtras();
////        String s1 = b1.getString("usernm");
////        s1 = "Hi " + s1 + "!";
//        String s1 = "Hi !";
//        txt1.setText(s1);


        img = v.findViewById(R.id.dog1);

        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActionMode != null) {
                    return false;
                }

                mActionMode = v.startActionMode(mActionModeCallback);
//                mActionMode=startSupportActionMode(mActionModeCallback);
                return true;
            }
        });

        // Inflate the layout for this fragment
        return v;

    }

    //Contextual App Bar
    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
       
        private ClipboardManager clipboardManager;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_actionbar, menu);
//        mode.setTitle("Choose your option");

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            this.clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            switch (item.getItemId()) {
//                case R.id.copy:
////                    Intent intent= getActivity().getIntent();
////
////                    ClipData primaryClipData = this.clipboardManager.getPrimaryClip();
////
////                    if(primaryClipData == null)  {
////                        getContext().img.setImageURI(null);
////                        //return true;
////                    }
////
////                    ClipData.Item item1 = primaryClipData.getItemAt(0);
////
////                    Uri uri = item1.getUri();
////
////                    this.img.setImageURI(uri);
////                    Toast.makeText(getContext(),"Copied to Clipboard",Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), "Copy selected", Toast.LENGTH_SHORT).show();
//                    mode.finish();
//                    return true;
                case R.id.share:
                    Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.dog1);
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), b, "Title", null);
                    Uri imageUri =  Uri.parse(path);
                    share.putExtra(Intent.EXTRA_STREAM, imageUri);
                    startActivity(Intent.createChooser(share, "Select"));
//                    Toast.makeText(getContext(), "Share selected", Toast.LENGTH_SHORT).show();
//                    mode.finish();
                    return true;
                case R.id.save:
                    File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
                    if (!storageDir.exists())
                        storageDir.mkdirs();
                    try {
                        File image = File.createTempFile("Petlet",                   /* prefix */
                                ".jpeg",                     /* suffix */
                                storageDir                   /* directory */
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(),"Picture Saved as Petlet7894561237894561235.jpeg",Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), "Share selected", Toast.LENGTH_SHORT).show();
//                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//        super.onPointerCaptureChanged(hasCapture);
//    }

}