package app.pricetag.com.price_tag;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;

import app.pricetag.com.price_tag.fragments.ProductDeatilsFragment;
import it.gmariotti.cardslib.library.utils.BitmapUtils;

/**
 * Created by shekhar on 5/9/14.
 */
public class ProductDetails extends Activity{

  int productId;
  private ShareActionProvider mShareActionProvider;
  private File photofile;
  private AdView adView;
  private ActionBar actionBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_list_detail);

    Intent intent = getIntent();
    if(intent != null){
      productId = intent.getIntExtra("productId",0);
      setTitle(intent.getStringExtra("productName"));
    }
    actionBar = getActionBar();
    actionBar.setIcon(R.drawable.ic_launcher);
    actionBar.setDisplayHomeAsUpEnabled(true);
    setTitleColor(getResources().getColor(R.color.almost_white_bg));
    Toast.makeText(this,"productId : " + productId,Toast.LENGTH_SHORT).show();
    getFragmentManager().beginTransaction().replace(R.id.content_frame_product_list,new ProductDeatilsFragment()).commit();


    if (photofile==null){
      if (mShareActionProvider != null) {
        this.invalidateOptionsMenu();
      }
    }
    adView = (AdView) findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .addTestDevice(getResources().getString(R.string.device_id))
        .build();
    adView.loadAd(adRequest);
  }
  @Override
  public void onResume() {
    super.onResume();
    if (adView != null) {
      adView.resume();
    }
  }

  @Override
  public void onPause() {
    if (adView != null) {
      adView.pause();
    }
    super.onPause();
  }

  /** Called before the activity is destroyed. */
  @Override
  public void onDestroy() {
    // Destroy the AdView.
    if (adView != null) {
      adView.destroy();
    }
    super.onDestroy();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.sharemenu, menu);

    // Locate MenuItem with ShareActionProvider
    MenuItem item = menu.findItem(R.id.carddemo_menu_item_share);

    // Fetch and store ShareActionProvider
    mShareActionProvider = (ShareActionProvider) item.getActionProvider();
    mShareActionProvider.setShareIntent(getShareIntent());

    return true;
  }

  private Intent getShareIntent(){
//    if (cardView!=null){
//      photofile = BitmapUtils.createFileFromBitmap(cardView.createBitmap());
//      if (photofile!=null){
//        return BitmapUtils.createIntentFromImage(photofile);
//      }else{
//        return getDefaultIntent();
//      }
//    }else{
      return getDefaultIntent();
//    }
  }



  /** Defines a default (dummy) share intent to initialze the action provider.
   * However, as soon as the actual content to be used in the intent
   * is known or changes, you must update the share intent by again calling
   * mShareActionProvider.setShareIntent()
   */
  private Intent getDefaultIntent() {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("image/*");
    return intent;
  }
}
