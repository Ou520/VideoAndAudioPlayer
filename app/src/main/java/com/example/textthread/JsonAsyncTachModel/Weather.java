package com.example.textthread.JsonAsyncTachModel;

public class Weather {
    private WeatherInfo weatherinfo;

    public WeatherInfo getWeatherinfo() {
        return weatherinfo;
    }

    public void setWeatherinfo(WeatherInfo weatherinfo) {
        this.weatherinfo = weatherinfo;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "weatherinfo=" + weatherinfo +
                '}';
    }
}
