package weatherapp.enjaztask.com.weather.Model;


public class WeatherDataModel {

    private String date;
    private String main;
    private String description;
    private String icon;
    private String temp;
    private String humidity;
    private String speed;
    private String country;
    private String sunrise;
    private String sunset;
    private String name;



    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getTemp() {
        return temp;
    }

    public String getHumidity() {
        return humidity;
    }



    public String getSpeed() {
        return speed;
    }

    public String getCountry() {
        return country;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getName() {
        return name;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
