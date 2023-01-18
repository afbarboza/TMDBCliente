package com.example.tmbdclient.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tmbdclient.model.Movie;
import com.example.tmbdclient.model.MovieRepository;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
    }

    public LiveData<List<Movie>> getAllMovies() {
        return movieRepository.getMoviesLiveData();
    }

    public void clear() {
        movieRepository.clear();
    }
}
