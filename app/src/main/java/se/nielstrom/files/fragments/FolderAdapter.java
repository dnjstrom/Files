package se.nielstrom.files.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import se.nielstrom.files.R;


public class FolderAdapter extends ArrayAdapter<File> {
    public static final int ROW_LAYOUT_ID = R.layout.file_item;

    public FolderAdapter(Context context, File file) {
        super(context, ROW_LAYOUT_ID);
        if (file.isDirectory()) {
            addAll(file.listFiles()); // TODO: handle non-permited paths gracefully
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
            holder.right_arrow = (ImageView) convertView.findViewById(R.id.right_arrow);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        File file = getItem(position);

        holder.titleBox.setText(file.getName());

        holder.right_arrow.setVisibility( file.isDirectory() ? View.VISIBLE : View.GONE );

        return convertView;
    }

    static class ViewHolder {
        TextView titleBox;
        ImageView right_arrow;
    }
}

