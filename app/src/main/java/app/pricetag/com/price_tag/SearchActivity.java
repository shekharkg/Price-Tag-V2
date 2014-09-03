package app.pricetag.com.price_tag;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shekhar on 3/9/14.
 */
public class SearchActivity extends Activity implements SearchView.OnQueryTextListener {

  private SearchView mSearchView;
  private ActionBar actionBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

    setContentView(R.layout.search_main);
    actionBar = getActionBar();
    actionBar.setIcon(R.drawable.ic_launcher);
    actionBar.setDisplayHomeAsUpEnabled(true);
    setTitleColor(getResources().getColor(R.color.almost_white_bg));
    actionBar.setTitle("Search any Product");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.search, menu);
    MenuItem searchItem = menu.findItem(R.id.action_search);
    mSearchView = (SearchView) searchItem.getActionView();
    mSearchView.setIconified(false);
    mSearchView.setOnQueryTextListener(this);
    mSearchView.setQueryHint("Search any Product");

    int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
    EditText searchEditText = (EditText) mSearchView.findViewById(searchSrcTextId);
    searchEditText.setTextColor(getResources().getColor(R.color.almost_white_bg));
    searchEditText.setHintTextColor(getResources().getColor(R.color.almost_white_bg));
    int closeButtonId = getResources().getIdentifier("android:id/search_close_btn", null, null);
    ImageView closeButtonImage = (ImageView) mSearchView.findViewById(closeButtonId);
    closeButtonImage.setImageResource(R.drawable.ic_action_content_remove);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
//      case R.id.action_search:
//        Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
//        return(true);

      case android.R.id.home:
        finish();
        return(true);
    }
    return super.onOptionsItemSelected(item);
  }

  public boolean onQueryTextChange(String newText) {
    return false;
  }

  public boolean onQueryTextSubmit(String query) {
    Toast.makeText(this,query,Toast.LENGTH_SHORT).show();
    return false;
  }
}