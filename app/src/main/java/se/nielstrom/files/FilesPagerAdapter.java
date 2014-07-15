package se.nielstrom.files;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.io.File;

import se.nielstrom.files.fragments.FileListFragment;


public class FilesPagerAdapter extends FragmentStatePagerAdapter {

    private final FileListFragment.OnItemClickListener listener;
    private FragmentManager fragmentManager;
    private String path;
    private String[] directories;
    private File file;

    public FilesPagerAdapter(FragmentManager fm, FileListFragment.OnItemClickListener listener) {
        super(fm);
        fragmentManager = fm;
        this.listener = listener;
    }

    public void setPath(String path) {
        this.path = path;
        this.directories = path.replaceFirst("^/", "").split("/");
        this.file = new File(path);
        notifyDataSetChanged();
    }

    public int getItemPosition(Object object) {
        //TODO: check which fragments can be kept
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        StringBuilder partialPath = new StringBuilder("/");

        for (int i=0; i<directories.length && i<position; i++) {
            partialPath.append(directories[i] + "/");
        }

        FileListFragment fragment = FileListFragment.newInstance(partialPath.toString());
        fragment.setOnItemClickListener(listener);

        return fragment;
    }

    @Override
    public int getCount() {
        return directories.length + 1;
    }
}
