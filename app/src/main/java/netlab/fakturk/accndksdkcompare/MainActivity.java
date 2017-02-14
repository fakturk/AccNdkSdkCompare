package netlab.fakturk.accndksdkcompare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity
{
    float[] acc;
    String accMesaage;
    private static DecimalFormat df = new DecimalFormat(".##");

    // Used to load the 'native-lib' library on application startup.
    static
    {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this, SensorService.class));

        Log.d("test","deneme");
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
               acc= intent.getFloatArrayExtra("ACC_DATA");
                if (acc!=null)
                {
                    accMesaage = intent.getLongExtra("TIME",0)+" "+ acc[0]+" "+acc[1]+" "+acc[2]+"";
                    Log.d("accSDK  ", "    "+accMesaage);

                }

            }
        }, new IntentFilter(SensorService.ACTION_SENSOR_BROADCAST));

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        sensorValue();
    }

    @Override
    protected void onDestroy()
    {

        super.onDestroy();
        stopService(new Intent(this,SensorService.class));
    }
    @Override
    protected void onPause() {

        super.onPause();
        stopService(new Intent(this,SensorService.class));


    }

    @Override
    protected void onResume() {

        super.onResume();
        startService(new Intent(this, SensorService.class));
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public  native void sensorValue();
}
