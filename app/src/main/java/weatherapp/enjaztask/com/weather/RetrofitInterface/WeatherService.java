package weatherapp.enjaztask.com.weather.RetrofitInterface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import weatherapp.enjaztask.com.weather.Model.WeatherModel;

public interface WeatherService {
    @GET("2.5/weather")
    Call<WeatherModel> getWeather(@Query("lat") double lat,
                                  @Query("lon") double lon,
                                  @Query("units") String units,
                                  @Query("appid") String appid);

}
