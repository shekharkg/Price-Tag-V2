package app.pricetag.com.price_tag;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import app.pricetag.com.price_tag.fragments.CategoryFragment;
import app.pricetag.com.price_tag.fragments.SearchCategoryFragment;
import app.pricetag.com.price_tag.fragments.SearchProductListFragment;
import de.keyboardsurfer.android.widget.crouton.Crouton;

/**
 * Created by shekhar on 3/9/14.
 */
public class SearchActivity extends Activity implements SearchView.OnQueryTextListener {

  public static SearchView mSearchView;
  private ActionBar actionBar;
  public static String queryString;
  public static FragmentManager manager;
  public static String searchListUrl;
  public static String searchKey;
  public static int fragmentCount;
  public static InputMethodManager imm;

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
    manager = getFragmentManager();
    searchListUrl = getResources().getString(R.string.search_product_list);
    fragmentCount=0;
  }

  @Override
  public void onBackPressed() {
    Crouton.cancelAllCroutons();
    if (fragmentCount == 1) {
      getFragmentManager().beginTransaction()
          .replace(R.id.content_frame_product_list,new SearchCategoryFragment()).commit();
      fragmentCount = 0;
    } else {
      super.onBackPressed();
      this.finish();
    }
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
      case android.R.id.home:
        Crouton.cancelAllCroutons();
        if (fragmentCount == 1) {
          getFragmentManager().beginTransaction()
              .replace(R.id.content_frame_product_list,new SearchCategoryFragment()).commit();
          fragmentCount = 0;
        } else {
          finish();
        }
        return(true);
    }
    return super.onOptionsItemSelected(item);
  }

  public boolean onQueryTextChange(String newText) {
    return false;
  }

  public boolean onQueryTextSubmit(String query) {
    getFragmentManager().beginTransaction()
        .replace(R.id.content_frame_product_list, new SearchCategoryFragment()).commit();
    queryString = query.replaceAll(" ", "+");
    imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
    return false;
  }
}