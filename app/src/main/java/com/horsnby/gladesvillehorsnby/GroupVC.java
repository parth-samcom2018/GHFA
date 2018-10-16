package com.horsnby.gladesvillehorsnby;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
    public Folder rootFolder = null;
    private ImageView iv;
    private TextView tv_grp_title, tv_edit, mTitle, tvend, tv_left, tv_heading;
    private LinearLayout ll_back, ll_edit;
    private GroupVC.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private NoticeBoardVCN noticeBoardVCN;
    private MediaVC mediaVC;
    private VideoVC videoVC;
    private ArticlesVC articlesVC;
    private FixturesVC fixturesVC;
    private LaddersVC laddersVC;
    private DocumentsVC documentsVC;
    private String[] titles = {"Notification", "Fixtures", "Ladders", "Photos", "Videos", "Articles", "Documents"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setContentView(R.layout.activity_group_vc); //Call the EVENT layout cause it's the same

            Toolbar toolbar = findViewById(R.id.toolbar_top);
            mTitle = toolbar.findViewById(R.id.toolbar_title);
            tv_heading = findViewById(R.id.toolbar_heading);
            iv = findViewById(R.id.iv_logo);

            tvend = toolbar.findViewById(R.id.tv_end);
            tv_left = toolbar.findViewById(R.id.tv_left);
            ll_back = findViewById(R.id.ll_back);

            ll_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            tv_heading.setText(group.groupName);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayShowTitleEnabled(false);

            mSectionsPagerAdapter = new SectionsPagerAdapter(this.getSupportFragmentManager());


            // Set up the ViewPager with the sections adapter.
            mViewPager = findViewById(R.id.grp_view_pager);

            ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    setTitle(titles[position]);

                    switch (position) {
                        case 0:

                            noticeBoardVCN.loadIfUnloaded();
                            break;

                        case 1:

                            fixturesVC.loadIfUnloaded();
                            break;

                        case 2:

                            laddersVC.loadIfUnloaded();
                            break;

                        case 3:

                            mediaVC.loadIfUnloaded();
                            break;

                        case 4:

                            videoVC.loadIfUnloaded();
                            break;

                        case 5:

                            articlesVC.loadIfUnloaded();
                            break;

                        case 6:
                            documentsVC.loadIfUnloaded();
                            break;
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

            this.fixturesVC = (FixturesVC) FixturesVC.instantiate(this, FixturesVC.class.getName());
            this.fixturesVC.group = group;

            this.laddersVC = (LaddersVC)LaddersVC.instantiate(this, LaddersVC.class.getName());
            this.laddersVC.group = group;

            this.mediaVC = (MediaVC) MediaVC.instantiate(this, MediaVC.class.getName());
            this.mediaVC.group = group;

            this.videoVC = (VideoVC) VideoVC.instantiate(this, VideoVC.class.getName());
            this.videoVC.group = group;

            this.articlesVC = (ArticlesVC) ArticlesVC.instantiate(this, ArticlesVC.class.getName());
            this.articlesVC.group = group;

            this.documentsVC = (DocumentsVC) DocumentsVC.instantiate(this, DocumentsVC.class.getName());
            this.documentsVC.group = group;

            //this.setTitle(group.groupName);
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }


    private void newFolderAction() {
        Log.d("hq", "new folder click!");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        String message = "Enter your folder name";
        if (rootFolder != null)
            message += " it will be created as a subfolder of: " + rootFolder.folderName;

        alert.setMessage(message);
        alert.setTitle("Create Folder");

        alert.setView(edittext);

        alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String name = edittext.getText().toString();
                Log.d("hq", "create folder with name:" + name);

                Folder f = new Folder();
                f.folderName = name;
                if (rootFolder != null) f.parentFolderId = rootFolder.folderId;
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

                        Toast.makeText(GroupVC.this, "Could not create folder:" + error.getMessage(), Toast.LENGTH_LONG).show();
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


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) return noticeBoardVCN;
            else if (position == 1) return fixturesVC;
            else if (position ==2) return laddersVC;
            else if (position == 3) return mediaVC;
            else if (position == 4) return videoVC;
            else if (position ==5) return articlesVC;
            else return documentsVC;

        }


        @Override
        public int getCount() {
            // tab count

            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return titles[position];
        }


    }
}