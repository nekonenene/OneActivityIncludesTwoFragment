package hatonekoe.x0.com.oneactivityincludestwofragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import hatonekoe.x0.com.oneactivityincludestwofragment.fragment.ButtonsFragment;
import hatonekoe.x0.com.oneactivityincludestwofragment.utils.LogDebug;

public class IndexActivity extends AppCompatActivity implements ButtonsFragment.OnFragmentInteractionListener
{
    private static final String TAG = "IndexActivity";

    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        // ツールバーのセット
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings)
        {
            Toast.makeText(this, "\"" + getTaskId() + "\" が押されました", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.action_info)
        {
            displayNotification( R.integer.info_icon_notification , "右上のアクションバーから押されました");
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        LogDebug.D(TAG, "Get Fragment Interaction : " + uri);
    }

    /** 通知の表示をおこなう */
    public static void displayNotification(Integer id, String message)
    {
        Notification notification = new Notification.Builder(mContext)
                .setContentTitle( mContext.getString( R.string.app_name ) )
                .setContentText( message )
                .setSmallIcon(R.drawable.ic_alarm_on_indigo_a400_48dp)
                .setPriority(Notification.PRIORITY_LOW)
                .setTicker("これは ticker です")
                .setLights(Color.BLUE, 500, 500)
                .build();

        NotificationManager manager = (NotificationManager)mContext.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(id, notification );

        LogDebug.D(TAG, "いちおうここまでは来た id : " + id );
    }
}
