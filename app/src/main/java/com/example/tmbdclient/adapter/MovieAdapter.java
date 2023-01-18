package com.example.tmbdclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmbdclient.MovieActivity;
import com.example.tmbdclient.R;
import com.example.tmbdclient.model.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context context;
    private ArrayList<Movie> movieArrayList;


    public MovieAdapter(Context context, ArrayList<Movie> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie currentMovie = movieArrayList.get(position);
        holder.movieTitle.setText(currentMovie.getOriginalTitle());
        holder.rate.setText(Double.toString(currentMovie.getVoteAverage()));

        String imgPath = "https://image.tmdb.org/t/p/w500/"
                + currentMovie.getPosterPath();

        Glide.with(context)
                .load(imgPath)
                .placeholder(R.drawable.ic_loading_background)
                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public TextView rate;
        public TextView movieTitle;
        public ImageView movieImage;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            rate = itemView.findViewById(R.id.tvRating);
            movieImage = itemView.findViewById(R.id.ivMovie);
            movieTitle = itemView.findViewById(R.id.tvTitle);

            movieImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Movie selectedMovie = movieArrayList.get(position);
                        Intent i = new Intent(context, MovieActivity.class);
                        i.putExtra("movie", selectedMovie);
                        context.startActivity(i);
                    }
                }
            });
        }
    }
}
