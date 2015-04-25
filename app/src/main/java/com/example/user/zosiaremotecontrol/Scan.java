package com.example.user.zosiaremotecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcel;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Scan extends ActionBarActivity {

    public static final String ZOSIA_MACADRESS = "D4:87:D8:96:CC:21";
    public static final String LOG_TAG = "nazwij_to_jak_chcesz2";
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> bluetoothDevices = new ArrayList<>();  //lista znalezionych urzadzen w postaci obiektow urzadzen

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {  //odpowiada za przechwycenie i obsluzenie informacji o znalezionych urzadzeniach
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);   //rozparcelowac  na obiekty device
                //dodajemy device do listy urzadzen, typ urzadzenie
                bluetoothDevices.add(device);
                // Add the name and address to an array adapter to show in a ListView, typ string
                adapter.add(device.getName());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        //przejscie do drugiego activity po kliknieciu na urzadzenie
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String deviceName = (String) listView.getItemAtPosition(position);  //to urzadzenie ktore zostalo wybrane na liscie

                for (BluetoothDevice tempdevice: bluetoothDevices) //dla kazdego urzadzenia z bluetoothDevices
                {
                    if (tempdevice.getName().equals(deviceName) && tempdevice.getAddress().equals(ZOSIA_MACADRESS))
                    {
                        Log.d("ControlPanel", tempdevice.toString());
                        Toast.makeText(getApplicationContext(), "Ola nie jest super", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ControlPanel.class);    //wiadomosc przekazywana do ControlPanel activity
                        Parcel parcelDevice;
                        tempdevice.writeToParcel(parcelDevice,0);
                        intent.putExtra("ZosiaDevice", parcelDevice);  //doklejam urzadzenie, ktore wybralam
                        Log.d(LOG_TAG,intent.toString());
                        startActivity(intent);      //wystartuje nowe activity
                    }
                }
                Toast.makeText(getApplicationContext(), "Ola jest super", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ControlPanel.class);
                startActivity(intent);      //wystartuje nowe activity
            }
        });
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;
            case R.id.action_enable_discoverability:
                enableDiscoverability();
                return true;

            default:
                return false;
        }

    }

    private void enableDiscoverability() {

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }

    public void scanForAvailableDevices(View view) {

        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
        bluetoothAdapter.startDiscovery();

        adapter.notifyDataSetChanged();   //odswiezanie widoku ListView


    }


}

