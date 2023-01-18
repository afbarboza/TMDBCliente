package com.example.tmbdclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tmbdclient.model.Movie;
import com.google.android.material.appbar.MaterialToolbar;

public class MovieActivity extends AppCompatActivity {

    private Movie movie;
    private ImageView movieImage;
    private String image;

    private TextView tvMovieTitle;
    private TextView tvMovieRating;
    private TextView tvReleaseDate;
    private TextView tvPlotsynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        this.movieImage = findViewById(R.id.ivMovieLarge);


        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvMovieRating = findViewById(R.id.tvMovieRating);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvPlotsynopsis = findViewById(R.id.tvPlotsynopsis);

        Intent intent = getIntent();
        if (intent.hasExtra("movie")) {
            movie = (Movie) intent.getParcelableExtra("movie");
            Toast.makeText(this, movie.getOriginalTitle(), Toast.LENGTH_LONG).show();
            image = movie.getPosterPath();
            String path = "https://image.tmdb.org/t/p/w500/" + image;
            Glide.with(this)
                    .load(path)
                    .placeholder(R.drawable.ic_loading_background)
                    .into(movieImage);


            tvMovieTitle.setText(movie.getTitle());
            tvMovieRating.setText(movie.getVoteAverage().toString());
            tvReleaseDate.setText(movie.getReleaseDate());
            tvPlotsynopsis.setText(movie.getOverview());
        }
    }
}