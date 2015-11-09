package com.example.ullu.what2watch;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ullu.what2watch.dto.MovieDTO;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MovieDTO detailMovie = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView movieNameView = (TextView) rootView.findViewById(R.id.detail_moviename);
        movieNameView.setText(detailMovie.getOriginalTitle());

        ImageView thumbnailView = (ImageView) rootView.findViewById(R.id.detail_thumbnail);
        Picasso.with(getContext()).load(detailMovie.getImageUrl()).into(thumbnailView);

        TextView plotSynopsisView = (TextView) rootView.findViewById(R.id.detail_plotSynopsis);
        plotSynopsisView.setText(detailMovie.getPlotSynopsis());

        TextView movieRatingView = (TextView) rootView.findViewById(R.id.detail_movierating);
        movieNameView.setText(Double.toString(detailMovie.getUserRating()));

        TextView popularityView = (TextView) rootView.findViewById(R.id.detail_poularity);
        popularityView.setText(Double.toString(detailMovie.getPopularity()));

        return rootView;
    }
}
