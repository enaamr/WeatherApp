package weatherapp.enjaztask.com.weather.Helper;

public class Constant {

    public static final String CLIENTS_URL = "http://api.openweathermap.org/data/";

    public static final String DB_NAME = "weather";
    public static final String TABLE_NAME = "weathertable";
    public static final int DB_VERSION = +1;
    public static final String DROP_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String GET_PRODUCTS_QUERY = "SELECT * FROM " + TABLE_NAME;
    public static final String KEY_ID = "keyid";
    public static final String MAIN = "main";
    public static final String TEMP = "temp";
    public static final String HUMIDITY = "humidity";
    public static final String SPEED = "speed";
    public static final String COUNTRY = "country";
    public static final String SUNRISE = "sunrise";
    public static final String SUNSET = "sunset";
    public static final String NAME = "name";
    public static final String DATE= "date";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + MAIN + " TEXT,"
            + TEMP + " TEXT,"
            + HUMIDITY + " TEXT,"
            + SPEED + " TEXT,"
            + COUNTRY + " TEXT,"
            + SUNRISE + " TEXT,"
            + SUNSET + " TEXT,"
            + NAME + " TEXT," +DATE+ " TEXT"+ ")";

}

