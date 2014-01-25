package com.mirabellehegnet.campus.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirabellehegnet.campus.R;
import com.mirabellehegnet.campus.University;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by thomas on 1/25/14.
 */
public class UniversityGridViewAdapter extends ArrayAdapter<University> {
    Context context;
    int layoutResourceId;
    ArrayList<University> universities = null;

    public UniversityGridViewAdapter(Context context, int layoutResourceId, ArrayList<University> universities) {
        super(context, layoutResourceId, universities);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.universities = universities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.name = (TextView) row.findViewById(R.id.name);
            holder.abbrName = (TextView) row.findViewById(R.id.abbr_name);
            holder.url = (TextView) row.findViewById(R.id.url);
            holder.image = (ImageView) row.findViewById(R.id.imageView);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        University university = universities.get(position);
        holder.name.setText(university.getName());
        holder.abbrName.setText(university.getShortName());
        holder.url.setText(university.getUrl());
        new DownloadImageTask(holder.image, university.getIcon()).execute();
        //holder.image.setImageBitmap(course.getImage());
        return row;

    }

    static class RecordHolder {
        TextView name;
        ImageView image;
        TextView url;
        TextView abbrName;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        private String url;
        private Hashtable<String, Bitmap> bitmaps = new Hashtable<String, Bitmap>();

        public DownloadImageTask(ImageView view, String url) {
            this.imageView = view;
            this.url = url;
        }

        protected Bitmap doInBackground(final String... args) {
            if (bitmaps.contains(url))
                return bitmaps.get(url);

            try {
                InputStream in = new java.net.URL(url).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                bitmaps.put(url, bitmap);
                return bitmap;
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
