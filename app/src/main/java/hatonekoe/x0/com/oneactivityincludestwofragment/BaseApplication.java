package hatonekoe.x0.com.oneactivityincludestwofragment;

import android.app.Application;

/**
 * Created by Yokoe on 12/7/15.
 * アプリ起動時に呼ばれる
 */
public class BaseApplication extends Application
{
    public static final Boolean DEBUG_MODE = true ;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

}
