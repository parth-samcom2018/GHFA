package com.horsnby.gladesvillehorsnby;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.horsnby.gladesvillehorsnby.models.Folder;
import com.horsnby.gladesvillehorsnby.models.Group;
import com.horsnby.gladesvillehorsnby.models.Ladders;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GroupVC extends BaseVC {

    //MODEL
    public static Group group;
    public static Ladders ladder;

    private TextView tv_grp_title,tv_edit;
    private LinearLayout ll_back,ll_edit;
    private GroupVC.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    private NoticeBoardVCN noticeBoardVCN;
    private MediaVC mediaVC;
    private FixturesVC fixturesVC;
    private LaddersVC laddersVC;
    private DocumentsVC documentsVC;
    public Folder rootFolder = null;

    private String[] titles = {"Notification", "Media", "Fixtures", "Ladders", "Documents"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_group_vc); //Call the EVENT layout cause it's the same


        mSectionsPagerAdapter = new GroupVC.SectionsPagerAdapter(this.getSupportFragmentManager());
        try{

            tv_grp_title = findViewById(R.id.group_title);
            tv_grp_title.setText(group.groupName);
        }

        catch (NullPointerException e){
            e.printStackTrace();
        }


        tv_edit = findViewById(R.id.tv_edit);

        ll_back = findViewById(R.id.ll_back);
        ll_edit = findViewById(R.id.ll_edit);

        ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteUserAction();
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.grp_view_pager);

        ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setTitle(titles[position]);

                switch (position)
                {
                    case 0:

                        noticeBoardVCN.loadIfUnloaded();
                        ll_edit.setVisibility(View.VISIBLE);
                        tv_edit.setText("USER +");
                        ll_edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                inviteUserAction();
                            }
                        });
                        break;
                    case 1:

                        mediaVC.loadIfUnloaded();
                        ll_edit.setVisibility(View.VISIBLE);
                        tv_edit.setText("CREATE ALBUM +");

                        break;
                    case 2:
                        fixturesVC.loadIfUnloaded();
                        ll_edit.setVisibility(View.GONE);
                        break;
                    case 3:
                        laddersVC.loadIfUnloaded();
                        ll_edit.setVisibility(View.GONE);
                        break;

                    case 4:
                        documentsVC.loadIfUnloaded();
                        ll_edit.setVisibility(View.VISIBLE);
                        tv_edit.setText("");
                        break;
                }


                if (position==1){
                    ll_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            createAlbumAction();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        mViewPager.addOnPageChangeListener(pageListener);


        mViewPager.setAdapter(mSectionsPagerAdapter);


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        this.noticeBoardVCN = (NoticeBoardVCN) NoticeBoardVCN.instantiate(this, NoticeBoardVCN.class.getName());
        this.noticeBoardVCN.group = group;

        this.mediaVC = (MediaVC) MediaVC.instantiate(this, MediaVC.class.getName());
        this.mediaVC.group = group;

        this.fixturesVC = (FixturesVC) FixturesVC.instantiate(this, FixturesVC.class.getName());
        this.fixturesVC.group = group;

        this.laddersVC = (LaddersVC)LaddersVC.instantiate(this, LaddersVC.class.getName());
        this.laddersVC.ladder = ladder;

        this.documentsVC = (DocumentsVC) DocumentsVC.instantiate(this, DocumentsVC.class.getName());
        this.documentsVC.group = group;

        this.setTitle(group.groupName);

    }


    private void newFolderAction()
    {
        Log.d("hq","new folder click!");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        String message = "Enter your folder name";
        if(rootFolder != null) message += " it will be created as a subfolder of: "+rootFolder.folderName;

        alert.setMessage(message);
        alert.setTitle("Create Folder");

        alert.setView(edittext);

        alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String name = edittext.getText().toString();
                Log.d("hq","create folder with name:"+name);

                Folder f = new Folder();
                f.folderName = name;
                if(rootFolder != null )f.parentFolderId = rootFolder.folderId;
                else f.parentFolderId = null;

                f.groupId = group.groupId;


                DM.getApi().postFolder(DM.getAuthString(), f, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {


                        Toast.makeText(GroupVC.this, "Folder Created!", Toast.LENGTH_LONG).show();
                        DM.hideKeyboard(GroupVC.this);

                        Intent i = new Intent(GroupVC.this, GroupVC.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(GroupVC.this, "Could not create folder:"+error.getMessage(), Toast.LENGTH_LONG).show();
                        DM.hideKeyboard(GroupVC.this);
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }






    private void createAlbumAction() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout lila1 = new LinearLayout(this);
        lila1.setOrientation(LinearLayout.VERTICAL);
        final EditText nameET = new EditText(this);
        nameET.setHint("Album Name");
        final EditText descET = new EditText(this);
        descET.setVisibility(View.GONE);
        descET.setHint("Album Description");
        lila1.addView(nameET);
        //lila1.addView(descET);
        int pad = (int)getResources().getDimension(R.dimen.small_pad);
        lila1.setPadding(pad,pad,pad,pad);
        alert.setView(lila1);

        //alert.setIcon(R.drawable.icon);
        alert.setTitle("Create Album");


        alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {
                String name = nameET.getText().toString();
                String description = descET.getText().toString();

                if(name.length() == 0 || name == null)
                {
                    Toast.makeText(GroupVC.this,"Enter a name",Toast.LENGTH_LONG).show();
                    DM.hideKeyboard(GroupVC.this);
                    return;
                }

                DM.getApi().postMediaAlbum(DM.getAuthString(), name,  group.groupId, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Toast.makeText(GroupVC.this,"Album Created!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        DM.hideKeyboard(GroupVC.this);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(GroupVC.this,"Could not create album: "+error.getMessage(),Toast.LENGTH_LONG).show();


                    }
                });

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DM.hideKeyboard(GroupVC.this);
                dialog.dismiss();
            }
        });

        alert.show();

    }

    private void inviteUserAction()
    {


        final EditText edittext = new EditText(this);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Please enter their email address to invite them to the group");
        alert.setTitle("Invite User");

        alert.setView(edittext);

        alert.setPositiveButton("Invite User", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String email = edittext.getText().toString();
                if(email.isEmpty() || !DM.isEmailValid(email))
                {
                    Toast.makeText(GroupVC.this, "You must provide a valid email", Toast.LENGTH_LONG).show();
                    return;
                }


                makeInviteRequest(email);


            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alert.show();
    }


    private void makeInviteRequest(String email)
    {
        DM.hideKeyboard(GroupVC.this);

        final ProgressDialog pd = DM.getPD(this,"Inviting User...");
        pd.show();

        Log.d("HQ","groupID : "+ group.groupId);

        /*DM.getApi().postInviteUser(DM.getAuthString(), "unknown", email, true, this.group.groupId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

                Toast.makeText(GroupVC.this, "User has been invited!", Toast.LENGTH_LONG).show();
                pd.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(GroupVC.this, "Failed to invite user", Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });*/
        DM.getApi().postInviteUsers(DM.getAuthString(), "unknown", email, true, group.groupId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast.makeText(GroupVC.this, "User has been invited!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(GroupVC.this, "Failed to invite user", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) return noticeBoardVCN;
            else if (position == 1) return mediaVC;
            else if (position == 2) return fixturesVC;
            else if (position ==3) return laddersVC;
            else return documentsVC;

        }


        @Override
        public int getCount() {
            // tab count

            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return titles[position];
        }


    }
}