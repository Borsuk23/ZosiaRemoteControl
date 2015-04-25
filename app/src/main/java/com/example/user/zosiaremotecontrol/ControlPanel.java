package com.example.user.zosiaremotecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;


public class ControlPanel extends ActionBarActivity {

    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;
    public static final String LOG_TAG = "nazwij_to_jak_chcesz";
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    BluetoothDevice device;
    BluetoothChatService bluetoothChatService;
    BluetoothAdapter bluetoothAdapter;

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext() , "Connected", Toast.LENGTH_SHORT).show() ;
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            Toast.makeText(getApplicationContext() , "Connecting", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            Toast.makeText(getApplicationContext() , "Not connected", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    Toast.makeText(getApplicationContext(), "Ola nie jest super", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_READ:
                    Toast.makeText(getApplicationContext(), "Ola nie jest super", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_DEVICE_NAME:
                   Toast.makeText(getApplicationContext(), "Ola nie jest super", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), "Ola nie jest super", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_control_panel);

        Bundle bundle = intent.getExtras();

        Log.d(LOG_TAG,bundle.toString());
        device =  intent.getParcelableExtra("ZosiaDevice");     //deklarowany device jako Zosia

       dumpIntent(intent);
        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();                    //wyciaganie adaptera z managera

        bluetoothChatService = new BluetoothChatService(getApplicationContext(),mHandler);
        if (device != null) {
            bluetoothChatService.connect(device);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control_panel, menu);
        return true;
    }

    public static void dumpIntent(Intent i){

        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            Log.e(LOG_TAG,"Dumping Intent start");
            while (it.hasNext()) {
                String key = it.next();
                Log.e(LOG_TAG,"[" + key + "=" + bundle.get(key)+"]");
            }
            Log.e(LOG_TAG,"Dumping Intent end");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goDown (View view)
    {
        Toast.makeText(getApplicationContext(), "Idz do dolu", Toast.LENGTH_SHORT).show();
    }
    public void goUp (View view)
    {
        Toast.makeText(getApplicationContext(), "Idz do gory", Toast.LENGTH_SHORT).show();
    }
    public void goRight (View view)
    {
        Toast.makeText(getApplicationContext(), "Idz w prawo", Toast.LENGTH_SHORT).show();
    }
    public void goLeft (View view)
    {
        Toast.makeText(getApplicationContext(), "Idz w lewo", Toast.LENGTH_SHORT).show();
    }

    private void pairDevice(BluetoothDevice device) {
        try { Log.d("pairDevice()", "Start Pairing...");
            Method m = device.getClass().getMethod("createBond", (Class[]) null);
            m.invoke(device, (Object[]) null); Log.d("pairDevice()", "Pairing finished.");
        } catch (Exception e) {
            Log.e("pairDevice()", e.getMessage());
        }
    }


}
