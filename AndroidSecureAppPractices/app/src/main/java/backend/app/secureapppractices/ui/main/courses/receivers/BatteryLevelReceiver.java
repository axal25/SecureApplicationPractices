package backend.app.secureapppractices.ui.main.courses.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

public class BatteryLevelReceiver extends BroadcastReceiver {

    int scale = -1;
    int level = -1;
    int voltage = -1;
    int temp = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
//        Toast.makeText(context, "Battery low, please do something with this...", Toast.LENGTH_LONG).show();
        Toast.makeText(context, "Current battery level: " + level, Toast.LENGTH_LONG).show();
//        Toast.makeText(context, "Current scale: " + scale, Toast.LENGTH_LONG).show();
//        Toast.makeText(context, "Current temp: " + temp, Toast.LENGTH_LONG).show();
//        Toast.makeText(context, "Current voltage: " + voltage, Toast.LENGTH_LONG).show();
    }

}