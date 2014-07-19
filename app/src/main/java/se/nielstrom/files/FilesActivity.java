package se.nielstrom.files;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.nielstrom.files.fragments.FileListFragment;

public class FilesActivity extends FragmentActivity  {

    public static final String DEFAULT_PATH = "/sdcard";
    private static final int MAX_SUBTITLE_LENGTH = 40;

    private ViewPager pager;
    private FilesPagerAdapter adapter;
    private ListView drawerList;
    private List<String> drawerListItems;
    private ArrayAdapter<String> drawerListAdapter;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        actionbar = getActionBar();

        if (savedInstanceState == null) {
            pager = (ViewPager) findViewById(R.id.pager);
            pager.setOnPageChangeListener(new PagerListener());
            adapter = new FilesPagerAdapter(getSupportFragmentManager(), new FilesItemClickListener());
            adapter.setPath(DEFAULT_PATH);
            pager.setAdapter(adapter);
            pager.setCurrentItem(adapter.getCount());

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            drawerToggle = new ActionBarDrawerToggle(
                    this,                  /* host Activity */
                    drawer,         /* DrawerLayout object */
                    R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                    R.string.drawer_open,  /* "open drawer" description */
                    R.string.drawer_close  /* "close drawer" description */
            ) {

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    //getActionBar().setTitle(mTitle);
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    //getActionBar().setTitle(mDrawerTitle);
                }
            };

            // Set the drawer toggle as the DrawerListener
            drawer.setDrawerListener(drawerToggle);

            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);

            drawerList = (ListView) findViewById(R.id.path_list);
            drawerListItems = new ArrayList<String>();
            drawerListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerListItems);
            drawerList.setAdapter(drawerListAdapter);
            drawerList.setOnItemClickListener(new DrawerItemClickListener());
            updateDrawer();
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }


    private void updateDrawer() {
        drawerListItems.clear();
        drawerListItems.addAll(Arrays.asList(adapter.getDirectories()));
        drawerListAdapter.notifyDataSetChanged();
    }

    private void updateActionBar(String path) {
        updateActionBar(new File(path));
    }

    private void updateActionBar(File file) {
        actionbar.setTitle(file.getName());
        String possiblyPartialPath = file.getPath();

        if (possiblyPartialPath.length() > 35) {
            possiblyPartialPath = ".." + possiblyPartialPath.substring(possiblyPartialPath.length() - 33);
        }

        actionbar.setSubtitle(possiblyPartialPath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.files, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openFile(File file) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null) {
            type = "*/*";
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), type);
        startActivity( Intent.createChooser(intent, "Open file with.."));
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
            updateDrawer();
        }
    }

    private class FilesItemClickListener implements FileListFragment.OnItemClickListener {
        @Override
        public void onClick(int position, ListView list, View row, File file) {
            Log.d(getClass().getSimpleName(), "Click detected on: " + file.getPath());

            list.clearChoices();
            list.setItemChecked(position, true);

            if ( !file.exists() ) {
                Toast.makeText(FilesActivity.this, "The file \"" + file.getName() + "\" does not exist", Toast.LENGTH_SHORT).show();
            } else if ( !file.canRead() ) {
                Toast.makeText(FilesActivity.this, "Can't read file/directory.", Toast.LENGTH_SHORT).show();
            } else if (file.isDirectory()) {
                adapter.setPath(file.getPath());
                pager.setCurrentItem(adapter.getCount());
                updateDrawer();

                //FileListFragment fragment = (FileListFragment) adapter.getItem(pager.getCurrentItem());
                //fragment.getList().clearChoices();
            } else {
                openFile(file);
            }
        }

        @Override
        public boolean onLongClick(int position, ListView list, View row, File file) {
            return true;
        }
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            pager.setCurrentItem(i);
            drawer.closeDrawers();
        }
    }

    private class PagerListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            StringBuilder partialPath;

            if (position == 0) {
                partialPath = new StringBuilder();
            } else {
                partialPath = new StringBuilder("/");
            }

            String[] folders = adapter.getDirectories();

            for (int i=1; i<position && i<folders.length; i++) {
                partialPath.append(folders[i]);
                partialPath.append("/");
            }

            if (partialPath.length() >= MAX_SUBTITLE_LENGTH) {
                partialPath.delete(0, partialPath.length() - (MAX_SUBTITLE_LENGTH - 2));
                partialPath.insert(0, "..");
            }

            actionbar.setSubtitle(partialPath);

            actionbar.setTitle(folders[position]);
        }
    }
}
