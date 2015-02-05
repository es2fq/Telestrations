package song.telephone;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Set;


public class Bluetooth extends Activity {

    private final static int REQUEST_ENABLE_BT = 1;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        String status = "";
        if( bluetooth != null ) {
            if( bluetooth.isEnabled() ) {
                String deviceAddress = bluetooth.getAddress();
                String deviceName = bluetooth.getName();
                status = deviceName + " : " + deviceAddress;

                Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
                if( pairedDevices.size() > 0 ) {
                    for( BluetoothDevice device : pairedDevices ) {
                        arrayAdapter.add( device.getName() + "\n" + device.getAddress() );
                    }
                }
            }
            else {
                Intent enableBluetooth = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
                startActivityForResult( enableBluetooth , REQUEST_ENABLE_BT );
            }
            Toast.makeText( this , status , Toast.LENGTH_LONG ).show();
        }
        else {
            // Does not support bluetooth
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
        return true;
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
}
