package app.pricetag.com.price_tag.dao;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by shekhar on 2/9/14.
 */
public class ConnectedToInternetOrNot {

  ConnectivityManager connec;
  Activity activity;

  public int ConnectedToInternetOrNot(Activity activity) {
    this.activity = activity;
    connec = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connec != null &&
        (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) ||
        (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)){
      //You are connected, do something online.
      return 1;
    } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED ) {
      //Not connected.
      crouton();
      return 0;
    }
    return 2;
  }

  private void crouton() {
    final Crouton crouton = Crouton.makeText(activity, "Please connect to Internet", Style.ALERT)
        .setConfiguration(new Configuration.Builder().setDuration(Configuration.DURATION_LONG).build());

    crouton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Crouton.cancelAllCroutons();
      }
    });
      crouton.show();
  }
}
