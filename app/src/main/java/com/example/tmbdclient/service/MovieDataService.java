package com.example.tmbdclient.service;

import com.example.tmbdclient.model.MovieDBResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDataService {
    //@GET("movie/popular")
    //Call<MovieDBResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Observable<MovieDBResponse> getPopularMovies(@Query("api_key") String apiKey);
}
