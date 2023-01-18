package com.example.tmbdclient.model;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tmbdclient.R;
import com.example.tmbdclient.service.MovieDataService;
import com.example.tmbdclient.service.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieRepository {
    private ArrayList<Movie> movies = new ArrayList<>();
    private Application application;
    private Observable<MovieDBResponse> movieDBResponseObservable;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();

    public MovieRepository(Application application) {
        this.application = application;
        getPopularMoviesRx();
    }

    private void getPopularMoviesRx() {
        MovieDataService movieDataService = RetrofitInstance.getInstance();
        movieDBResponseObservable = movieDataService.getPopularMovies(
                application.getApplicationContext().getString(R.string.api_key));

        compositeDisposable.add(movieDBResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<MovieDBResponse, Observable<Movie>>() {
                    @Override
                    public Observable<Movie> apply(MovieDBResponse movieDBResponse) throws Throwable {
                        return Observable.fromArray(movieDBResponse.getResults().toArray(new Movie[0]));
                    }
                })
                .filter(new Predicate<Movie>() {
                    @Override
                    public boolean test(Movie movie) throws Throwable {
                        return movie.getVoteAverage() >= 7.0f;
                    }
                })
                .subscribeWith(new DisposableObserver<Movie>() {
                    @Override
                    public void onNext(@NonNull Movie movie) {
                        movies.add(movie);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        moviesLiveData.postValue(movies);
                    }
                })
        );
    }

    public LiveData<List<Movie>> getMoviesLiveData() {
        return moviesLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
