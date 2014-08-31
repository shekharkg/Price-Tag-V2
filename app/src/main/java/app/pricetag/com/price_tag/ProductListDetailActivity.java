package app.pricetag.com.price_tag;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import app.pricetag.com.price_tag.fragments.ProductListFragment;

/**
 * Created by shekhar on 28/8/14.
 */
public class ProductListDetailActivity extends Activity {
  int subCategoryId;
  public static String productListUrl;
  ActionBar actionBar;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_list_detail);

    Intent intent = getIntent();
    if(intent!=null){
      subCategoryId = intent.getIntExtra("subCategoryId", 1);
      setTitle(intent.getStringExtra("title"));
    }
    productListUrl = getResources().getString(R.string.productListUrl) + subCategoryId;
    getFragmentManager().beginTransaction().add(R.id.content_frame_product_list, new ProductListFragment()).commit();
    actionBar = getActionBar();
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
        Toast.makeText(this, "Search touched", Toast.LENGTH_SHORT).show();
        return(true);

      case android.R.id.home:
        finish();
        return(true);
    }
        return super.onOptionsItemSelected(item);
  }

}
