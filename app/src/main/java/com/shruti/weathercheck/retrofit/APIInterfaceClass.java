package com.shruti.weathercheck.retrofit;

import com.shruti.weathercheck.constants.Constants;
import com.shruti.weathercheck.dao.WeatherApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface APIInterfaceClass {

    @GET(Constants.weather )
    Call<WeatherApiResponse> getWeather(@QueryMap Map<String,String> options);

}
