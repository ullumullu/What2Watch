package com.example.ullu.what2watch.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.ullu.what2watch.dto.MovieDTO;
import com.example.ullu.what2watch.logic.MovieLogic;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.util.List;

/**
 * Created by Ullu on 25.10.2015.
 */
public class MovieAdapter extends ArrayAdapter<MovieDTO> {

    private static final String CLASSNAME = MovieAdapter.class.getSimpleName();

    private int mResource;

    private int mImageId;

    public MovieAdapter(Context context,int resource, int imageViewResourceId, List<MovieDTO> moviesList) {
        super(context, 0, moviesList);
        this.mResource = resource;
        this.mImageId = imageViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieDTO movie = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.setIconView((ImageView) convertView.findViewById(mImageId));
            viewHolder.setMovieUrl(movie.getImageUrl());

            convertView.setTag(viewHolder);
        } else {
            viewHolder = ((ViewHolder) convertView.getTag());
        }

        new SetImageTask().execute(viewHolder);

        return convertView;
    }

    private class SetImageTask extends AsyncTask<ViewHolder, Void, Bitmap> {
        private ViewHolder v;
        @Override
        protected Bitmap doInBackground(ViewHolder... viewHolder) {
            v = viewHolder[0];
            Bitmap map = null;
            try {
                map = Picasso.with(getContext()).load(v.getMovieUrl()).get();
            } catch (IOException e) {

            }
            return map;
        }

        @Override
        protected void onPostExecute(Bitmap request) {
            super.onPostExecute(request);
            v.getIconView().setImageBitmap(request);
        }
    }

    static class ViewHolder {
        ImageView iconView;
        String movieUrl;

        public String getMovieUrl() {
            return movieUrl;
        }

        public void setMovieUrl(String movieUrl) {
            this.movieUrl = movieUrl;
        }

        public ImageView getIconView() {
            return iconView;
        }

        public void setIconView(ImageView iconView) {
            this.iconView = iconView;
        }
    }
}
