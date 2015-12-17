package hatonekoe.x0.com.oneactivityincludestwofragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        // ツールバーのセット
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);
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
            LogDebug.D(TAG, "\"" + getString(R.string.action_settings) + "\" が押されました");
            return true;
        }
        else if(id == R.id.say_hello)
        {
            Toast.makeText(this, "\"" + getTaskId() + "\" が押されました", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
