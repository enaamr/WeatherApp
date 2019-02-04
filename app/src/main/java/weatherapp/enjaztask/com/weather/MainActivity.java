package weatherapp.enjaztask.com.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import weatherapp.enjaztask.com.weather.DataBase.WeatherDatabase;
import weatherapp.enjaztask.com.weather.Helper.Constant;
import weatherapp.enjaztask.com.weather.Helper.Utils;
import weatherapp.enjaztask.com.weather.Model.WeatherDataModel;
import weatherapp.enjaztask.com.weather.Model.WeatherModel;
import weatherapp.enjaztask.com.weather.RetrofitInterface.WeatherService;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout layout;
    private WeatherDatabase WeatherDatabase;
    private TextView humidity, wind, temperature, country, weather_condition, sunrise, sunset,lastUpdate;
    private SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         // Find a reference to all view  in the layout
           configViews();
        //change background image depend on current time
          getCurrentTime();
        //instance to store data in Sqlite
          WeatherDatabase = new WeatherDatabase(this);
        //check location permission
          loadWeatherCondition();
        //initiate shared shared preference
          sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);



    }

    private void configViews() {
        layout = findViewById(R.id.constraint);
        humidity = findViewById(R.id.humidity);
        wind = findViewById(R.id.wind);
        temperature = findViewById(R.id.temperature);
        country = findViewById(R.id.country);
        weather_condition = findViewById(R.id.weather_condition);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        lastUpdate=findViewById(R.id.last_update);
    }

    private void getCurrentTime() {

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            layout.setBackgroundResource(R.drawable.morning);
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            layout.setBackgroundResource(R.drawable.afternoon);
        } else if (timeOfDay >= 16 && timeOfDay <= 24) {
            layout.setBackgroundResource(R.drawable.night);

        }
    }

    private void loadWeatherCondition() {
        //check if the android version <6.0 (Marshmallow)
        if (shouldAskPermissions()) {
            //require run time permission
            askForLocationPermissions();
        } else {
            // >6.0 run time permission not necessary
            WeatherFeed();
        }

    }

    private void askForLocationPermissions() {
        //run time permission for location (using Dexter library)
        Dexter.withActivity(this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    WeatherFeed();

                }
            }

            //permission denied
            @Override
            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                Toast.makeText(MainActivity.this, "Access Location permission denied, PLEASE ACCEPT IT.", Toast.LENGTH_SHORT).show();

            }


        }).check();


    }

    private void WeatherFeed() {
        //check user device network connection
        if (Utils.isNetworkAvailable(this)) {
            //fetch data from api
            UserLiveData();
        } else {
            //display local data
            UseLocalData();
        }
    }

    private void UserLiveData() {
        //build retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.CLIENTS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //instance of GPSTracker class to get current user location
        GPSTracker gpsTracker = new GPSTracker(this);
        //implement WeatherService with retrofit
        WeatherService service = retrofit.create(WeatherService.class);

        Call<WeatherModel> listCall = service.getWeather(gpsTracker.getLatitude(), gpsTracker.getLongitude(),
                "metric", getResources().getString(R.string.API));
        listCall.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (response.isSuccessful()) {
                    //store Number of Api Call in Shared Preference
                    editNumOfApiCall();
                    //get date from response
                    WeatherModel weatherList = response.body();

                    for (int z = 0; z < weatherList.getWeather().size(); z++) {
                        //display data fetched from ip in the view
                        displayData(weatherList, z);
                        //store data in sqllite to fetch it if the user is offline
                        storeDataLocally(weatherList, z);

                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {


            }
        });


    }

    private void displayData(WeatherModel weatherList, int z) {
        weather_condition.setText(String.valueOf(weatherList.getWeather().get(z).getMain()));
        temperature.setText(String.valueOf(weatherList.getMain().getTemp().intValue()));
        humidity.setText(String.valueOf(weatherList.getMain().getHumidity()));
        wind.setText(String.valueOf(weatherList.getWind().getSpeed()) + " m/h");
        country.setText(String.valueOf(weatherList.getSys().getCountry())+"," +String.valueOf(weatherList.getName()));
        sunrise.setText(UnixTime(Long.parseLong(String.valueOf(weatherList.getSys().getSunrise()))));
        sunset.setText(UnixTime(Long.parseLong(String.valueOf(weatherList.getSys().getSunset()))));


    }

    private void storeDataLocally(WeatherModel weatherList, int z) {
        if (WeatherDatabase.TableNotEmpty()) {

            WeatherDatabase.updateValues("1", weatherList.getWeather().get(z).getMain(),

                    String.valueOf(weatherList.getMain().getTemp().intValue()),
                    weatherList.getMain().getHumidity().toString(),
                    weatherList.getWind().getSpeed().toString(),
                    weatherList.getSys().getCountry(),
                    weatherList.getSys().getSunrise().toString(),
                    weatherList.getSys().getSunset().toString(),
                    weatherList.getName(), currentDate());


        } else {

            WeatherDatabase.addDataInDB(weatherList.getWeather().get(z).getMain(),
                    String.valueOf(weatherList.getMain().getTemp().intValue()),
                    weatherList.getMain().getHumidity().toString()
                    , weatherList.getWind().getSpeed().toString(),
                    weatherList.getSys().getCountry()
                    , weatherList.getSys().getSunrise().toString(),
                    weatherList.getSys().getSunset().toString()
                    , weatherList.getName(), currentDate());

        }

    }

    private String UnixTime(long timex) {

        Date date = new Date(timex * 1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    private void UseLocalData() {

        List<WeatherDataModel> dataList = WeatherDatabase.getAllData();
        displaySnackbar("you are offline now");

        for (WeatherDataModel data : dataList) {

            weather_condition.setText(String.valueOf(data.getMain()));
            temperature.setText(String.valueOf(data.getTemp()));
            humidity.setText(String.valueOf(data.getHumidity()));
            wind.setText(String.valueOf(data.getSpeed()) + " m/h");
            country.setText(String.valueOf(data.getCountry())+"," +String.valueOf(data.getName()));
            sunrise.setText(UnixTime(Long.parseLong(String.valueOf(data.getSunrise()))));
            sunset.setText(UnixTime(Long.parseLong(String.valueOf(data.getSunset()))));
            lastUpdate.setText("last Update: "+ data.getDate());

        }

    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void displaySnackbar(String text) {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG);
                   snackbar.show();
    }

    private String currentDate() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        return df.format(Calendar.getInstance().getTime());
    }

    private int readNumOfApiCall(){
      return sharedpreferences.getInt("NumOfApiCall",0);

    }

    private void editNumOfApiCall(){
        int n= sharedpreferences.getInt("NumOfApiCall",0);
        sharedpreferences.edit().putInt("NumOfApiCall", (n+1)).apply();


    }
}