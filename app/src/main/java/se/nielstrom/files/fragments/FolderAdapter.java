package se.nielstrom.files.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

import se.nielstrom.files.R;


public class FolderAdapter extends ArrayAdapter<File> {
    public static final int ROW_LAYOUT_ID = R.layout.file_item;

    public FolderAdapter(Context context, File file) {
        super(context, ROW_LAYOUT_ID);
        if (file.exists() && file.canRead() && file.isDirectory()) {

            File[] folders = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });

            Arrays.sort(folders, new FileNameComparator());
            addAll(folders);

            File[] files = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isFile();
                }
            });

            Arrays.sort(files, new FileNameComparator());
            addAll(files);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(ROW_LAYOUT_ID, parent, false);

            holder = new ViewHolder();

            holder.titleBox = (TextView) convertView.findViewById(R.id.title);
            holder.rightArrow = (ImageView) convertView.findViewById(R.id.right_arrow);
            holder.fileIcon = (ImageView) convertView.findViewById(R.id.file_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        File file = getItem(position);

        holder.titleBox.setText(file.getName());
        holder.rightArrow.setVisibility(file.isDirectory() ? View.VISIBLE : View.GONE);
        holder.fileIcon.setVisibility(file.isDirectory() ? View.VISIBLE : View.GONE);

        if (!file.canRead()) {
            holder.titleBox.setTextColor(getContext().getResources().getColor(R.color.non_writeable));
        } else {
            holder.titleBox.setTextColor(getContext().getResources().getColor(R.color.text));
        }

        return convertView;
    }

    static class ViewHolder {
        TextView titleBox;
        ImageView rightArrow;
        ImageView fileIcon;
    }

    private class FileNameComparator implements Comparator<File> {
        @Override
        public int compare(File file, File file2) {
            return file.getName().compareTo(file2.getName());
        }
    }
}

