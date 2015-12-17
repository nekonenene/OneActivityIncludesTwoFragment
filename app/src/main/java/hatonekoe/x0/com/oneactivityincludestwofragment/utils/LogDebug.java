package hatonekoe.x0.com.oneactivityincludestwofragment.utils;

import android.util.Log;

import hatonekoe.x0.com.oneactivityincludestwofragment.BaseApplication;


/**
 * Created by Yokoe on 12/2/15.
 * Error 以外は、デバッグ時のみにしかログ出力しない
 * DBUG_MODE の ON/OFF は、BaseApplication に持たせて、起動時に判定
 */
public class LogDebug
{
    public static void D(final String tag, String message)
    {
        if(BaseApplication.DEBUG_MODE)
        {
            Log.d( tag, message );
        }
    }

    public static void D(String message)
    {
        if(BaseApplication.DEBUG_MODE)
        {
            Log.d( "NO-TAG", message );
        }
    }

    public static void V(final String tag, String message)
    {
        if(BaseApplication.DEBUG_MODE)
        {
            Log.v( tag, message );
        }
    }

    public static void I(final String tag, String message)
    {
        if(BaseApplication.DEBUG_MODE)
        {
            Log.i( tag, message );
        }
    }

    public static void W(final String tag, String message)
    {
        if(BaseApplication.DEBUG_MODE)
        {
            Log.w( tag, message );
        }
    }

    public static void E(final String tag, String message)
    {
        // if(BaseApplication.DEBUG_MODE)
            Log.e( tag, message );
    }


}
