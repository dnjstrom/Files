package se.nielstrom.files;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;


import java.io.File;

import se.nielstrom.files.fragments.FileListFragment;

public class FilesActivity extends FragmentActivity implements FileListFragment.OnItemClickListener {


    private ViewPager pager;
    private FilesPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        if (savedInstanceState == null) {
            pager = (ViewPager) findViewById(R.id.pager);
            adapter = new FilesPagerAdapter(getSupportFragmentManager(), this);
            adapter.setPath("/sdcard");
            pager.setAdapter(adapter);
            pager.setCurrentItem(adapter.getCount());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.files, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position, View row, File file) {
        Log.d(getClass().getSimpleName(), "Click detected on: " + file.getPath());

        if (file.isDirectory()) {
            adapter.setPath(file.getPath());
            pager.setCurrentItem(adapter.getCount());
        } else {
            Toast.makeText(FilesActivity.this, file.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
