package com.shruti.weathercheck.activities.viewModel;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;

import com.shruti.weathercheck.BR;
import com.shruti.weathercheck.R;
import com.shruti.weathercheck.constants.Constants;
import com.shruti.weathercheck.dao.WeatherApiResponse;
import com.shruti.weathercheck.retrofit.RetrofitClass;
import com.shruti.weathercheck.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends BaseObservable {

    private WeatherApiResponse response = new WeatherApiResponse();

    String cityName;

    public ObservableBoolean isDataAvailable = new ObservableBoolean(false);

    @Bindable
    private String toastMessage = null;

    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    public MainViewModel() {
        response = new WeatherApiResponse();
    }

    @Bindable
    public String getCityName() {
        return cityName;
    }

    @Bindable
    public void setCityName(String cityName) {
        this.cityName = cityName;
        notifyPropertyChanged(BR.cityName);
    }

    @Bindable
    public String getTemp() {
        String temp = "";
        if (response.getMain()!=null){
            temp = String.valueOf(response.getMain().getTemp());
        }
        return temp;
    }

    @Bindable
    public void setTemp(Double temp) {
        response.getMain().setTemp(temp);
        notifyPropertyChanged(BR.temp);
    }

    @Bindable
    public String getTempMin() {
        //&#176;
        String tempMin = "";
        if (response.getMain() !=null) {
            tempMin = "Min:" + response.getMain().getTemp_min();
        }
        return tempMin;
    }

    @Bindable
    public void setTempMin(Double tempMin) {
        response.getMain().setTemp_min(tempMin);
        notifyPropertyChanged(BR.tempMin);
    }

    @Bindable
    public String getTempMax() {
        //&#176;
        String tempMax = "";
        if (response.getMain() !=null) {
            tempMax = "Max:" + response.getMain().getTemp_max();
        }
        return tempMax;
    }

    @Bindable
    public void setTempMax(Double tempMax) {
        response.getMain().setTemp_max(tempMax);
        notifyPropertyChanged(BR.tempMax);
    }

    @Bindable
    public String getWind() {
        String windSpeed = "";
        if (response.getWind()!= null) {
            windSpeed = "Wind: " + response.getWind().getSpeed() + "km/h";
        }
        return windSpeed;
    }

    @Bindable
    public void setWind(Double wind) {
        response.getWind().setSpeed(wind);
        notifyPropertyChanged(BR.wind);
    }

    public void onSearchClicked(View view) {
        if (isInputValid()) {
            if (Utils.isNetworkAvailable(view.getContext())) {
                Map<String, String> data = new HashMap<>();
                data.put("units", Constants.units);
                data.put("appid", Constants.Weather_API_KEY);
                data.put("q", getCityName());

                RetrofitClass.getRetrofitClient()
                        .getWeather(data)
                        .enqueue(new Callback<WeatherApiResponse>() {
                            @Override
                            public void onResponse(Call<WeatherApiResponse> call, Response<WeatherApiResponse> response1) {
                                if (response1.body() != null) {
                                    response = response1.body();
                                    MainViewModel.this.setTemp(response.getMain().getTemp());
                                    MainViewModel.this.setTempMin(response.getMain().getTemp_min());
                                    MainViewModel.this.setTempMax(response.getMain().getTemp_max());
                                    MainViewModel.this.setWind(response.getWind().getSpeed());
                                    isDataAvailable.set(true);
                                } else {
                                    setToastMessage("City not found!!!");
                                    isDataAvailable.set(false);
                                }
                            }

                            @Override
                            public void onFailure(Call<WeatherApiResponse> call, Throwable t) {
                                setToastMessage(t.getMessage());
                                isDataAvailable.set(false);
                            }
                        });
            } else {
                setToastMessage("No Internet Connection Available.");
                isDataAvailable.set(false);
            }
        } else {
            setToastMessage("Please enter a valid city name");
            isDataAvailable.set(false);
        }
    }

    public boolean isInputValid() {
        return !getCityName().isEmpty() && !getCityName().trim().isEmpty();
    }
}
