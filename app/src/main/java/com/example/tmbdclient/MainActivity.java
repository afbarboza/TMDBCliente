package com.example.tmbdclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.Configuration;
import android.os.Bundle;

import com.example.tmbdclient.adapter.MovieAdapter;
import com.example.tmbdclient.model.Movie;
import com.example.tmbdclient.model.MovieDBResponse;
import com.example.tmbdclient.service.MovieDataService;
import com.example.tmbdclient.service.RetrofitInstance;
import com.example.tmbdclient.viewmodel.MoviesViewModel;
import com.example.tmbdclient.viewmodel.MoviesViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Movie> moviesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MoviesViewModel moviesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.srl_root);
        getSupportActionBar().setTitle("TMDB Popular Movies Today");

        moviesViewModel = (new ViewModelProvider(this, new MoviesViewModelFactory(this.getApplication())))
                .get(MoviesViewModel.class);

        getPopularMoviesRx();

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularMoviesRx();
            }
        });
    }

    private void getPopularMoviesRx() {
        moviesViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                moviesList = (ArrayList<Movie>) movies;
                showOnRecyclerView(moviesList);
            }
        });
    }

    private void showOnRecyclerView(ArrayList<Movie> movies) {
        recyclerView = findViewById(R.id.rv_movies);
        movieAdapter = new MovieAdapter(this, movies);
        if (isPortraitOrientation()) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }

    private boolean isPortraitOrientation() {
        return this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        moviesViewModel.clear();
    }
}