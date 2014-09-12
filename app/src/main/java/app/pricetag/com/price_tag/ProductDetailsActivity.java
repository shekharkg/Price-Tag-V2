package app.pricetag.com.price_tag;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import java.io.File;

import app.pricetag.com.price_tag.fragments.ProductDetailsFragment;
import it.gmariotti.cardslib.library.utils.BitmapUtils;

/**
 * Created by shekhar on 5/9/14.
 */
public class ProductDetailsActivity extends FragmentActivity {

  int productId;
  private ShareActionProvider mShareActionProvider;
  private ActionBar actionBar;
  public static String idUrl, imageUrl, productName;
  public static String stringShare;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_list_detail);

    Intent intent = getIntent();
    if(intent != null){
      productId = intent.getIntExtra("productId",0);
      productName = intent.getStringExtra("productName");
      setTitle(productName);
      Log.e("ID: ", String.valueOf(productId));
    }
    actionBar = getActionBar();
    actionBar.setIcon(R.drawable.ic_launcher);
    actionBar.setDisplayHomeAsUpEnabled(true);
    setTitleColor(getResources().getColor(R.color.almost_white_bg));
    idUrl = getResources().getString(R.string.product_details);
    imageUrl = getResources().getString(R.string.product_image);
    idUrl = idUrl + productId;
    imageUrl = imageUrl + productId;
    getFragmentManager().beginTransaction().replace(R.id.content_frame,new ProductDetailsFragment()).commit();
    stringShare =  "Compare price of \"" + productName
        + "\" across different sites.\n \n \nGet the app at Play Store \n\n https://play.google.com/store/apps/details?id=app.pricetag.com";
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

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.

    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return(true);
    }
    return super.onOptionsItemSelected(item);
  }

  private Intent getShareIntent(){
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_TEXT, stringShare);
    return shareIntent;
  }
}
