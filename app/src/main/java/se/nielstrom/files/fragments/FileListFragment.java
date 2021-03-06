package se.nielstrom.files.fragments;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import se.nielstrom.files.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class FileListFragment extends Fragment {

    public static final String DEFAULT_PATH = "/";

    public static final String BUNDLE_KEY_PATH = "path";

    private OnItemClickListener listener;

    private boolean selectionInProgress = false;

    private File currentFile;
    private ListView list;
    private View rootView;

    public FileListFragment() {}

    public static FileListFragment newInstance(String path) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_PATH, path);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_file_list, container, false);
        setHasOptionsMenu(true);

        Bundle args = getArguments();

        if (args != null) {
            currentFile = new File(args.getString("path"), DEFAULT_PATH);
        } else {
            currentFile = new File(DEFAULT_PATH);
        }

        list = (ListView) rootView.findViewById(R.id.file_list);
        list.setAdapter(new FolderAdapter(getActivity(), currentFile));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectionInProgress = false;
                if (listener != null) {
                    listener.onClick(i, list, view, (File) adapterView.getAdapter().getItem(i));
                }
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!selectionInProgress) {
                    selectionInProgress = true;
                    list.clearChoices();
                }

                list.setItemChecked(i, true);

                if (listener != null) {
                    return listener.onLongClick(i, list, view, (File) adapterView.getAdapter().getItem(i));
                }

                return false;
            }
        });

        if (list.getAdapter().getCount() == 0) {
            rootView.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.file_list, menu);
    }

    public interface OnItemClickListener {
        public void onClick(int position, ListView list, View row, File file);

        public boolean onLongClick(int position, ListView list, View row, File file);
    }
}
