package app.pricetag.com.price_tag;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import app.pricetag.com.price_tag.fragments.ProductListFragment;

/**
 * Created by shekhar on 28/8/14.
 */
public class ProductListDetailActivity extends Activity {
  public static int subCategoryId, dialogSelectedIndex;
  public static String productListUrl;
  ActionBar actionBar;
  public static String sortOrder;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_list_detail);

    Intent intent = getIntent();
    if(intent!=null){
      subCategoryId = intent.getIntExtra("subCategoryId", 1);
      setTitle(intent.getStringExtra("title"));
    }
    sortOrder = "&order_by=popular&limit=25&start=";
    dialogSelectedIndex = 0;
    productListUrl = getResources().getString(R.string.productListUrl) + subCategoryId;
    getFragmentManager().beginTransaction().add(R.id.content_frame, new ProductListFragment()).commit();
    actionBar = getActionBar();
    actionBar.setIcon(R.drawable.ic_launcher);
    actionBar.setDisplayHomeAsUpEnabled(true);
    setTitleColor(getResources().getColor(R.color.almost_white_bg));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.my, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.

    switch (item.getItemId()) {
      case R.id.action_search:
        Intent searchIntent = new Intent(this, SearchActivity.class);
        startActivity(searchIntent);
        return(true);

      case android.R.id.home:
        finish();
        return(true);
    }
        return super.onOptionsItemSelected(item);
  }

}
