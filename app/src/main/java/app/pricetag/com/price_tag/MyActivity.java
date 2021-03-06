package app.pricetag.com.price_tag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import app.pricetag.com.price_tag.adapters.DrawerDataAdapter;
import app.pricetag.com.price_tag.dao.ConnectedToInternetOrNot;
import app.pricetag.com.price_tag.fragments.CategoryFragment;
import app.pricetag.com.price_tag.fragments.SubCategoryListFragment;
import de.keyboardsurfer.android.widget.crouton.Crouton;

public class MyActivity extends Activity {

  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private CharSequence mDrawerTitle;
  public static CharSequence mTitle;
  public static String[] myDrawerListItem;
  public static int index;
  public static int fragmentCount;
  ConnectedToInternetOrNot connectedToInternetOrNot;
  int connected;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    myDrawerListItem = getResources().getStringArray(R.array.product_list);
    mTitle = mDrawerTitle = getTitle();
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);

    connectedToInternetOrNot = new ConnectedToInternetOrNot();
    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    // set up the drawer's list view with items and click listener
    DrawerDataAdapter adapter = new DrawerDataAdapter(this);
    mDrawerList.setAdapter(adapter);
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    // enable ActionBar app icon to behave as action to toggle nav drawer
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setHomeButtonEnabled(true);

    // ActionBarDrawerToggle ties together the the proper interactions
    // between the sliding drawer and the action bar app icon
    mDrawerToggle = new ActionBarDrawerToggle(
        this,                  /* host Activity */
        mDrawerLayout,         /* DrawerLayout object */
        R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
        R.string.drawer_open,  /* "open drawer" title for accessibility */
        R.string.drawer_close  /* "close drawer" title for accessibility */
    ) {
      public void onDrawerClosed(View view) {
        getActionBar().setTitle(mTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }

      public void onDrawerOpened(View drawerView) {
        getActionBar().setTitle(mDrawerTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    //Define and set Fragment to container
    getFragmentManager().beginTransaction().add(R.id.content_frame,new CategoryFragment()).commit();
    fragmentCount = 0;
    getActionBar().setIcon(R.drawable.ic_launcher);
    if (savedInstanceState == null) {
      //do action on startup
    }
  }

  @Override
  public void onBackPressed() {
    Crouton.cancelAllCroutons();
    if (fragmentCount == 1) {
      getFragmentManager().beginTransaction().replace(R.id.content_frame,new CategoryFragment()).commit();
      fragmentCount = 0;

      setTitle(getResources().getString(R.string.app_name));
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.my, menu);
    return super.onCreateOptionsMenu(menu);
  }

  /* Called whenever we call invalidateOptionsMenu() */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    switch (item.getItemId()) {
      case R.id.action_search:
        Intent searchIntent = new Intent(this, SearchActivity.class);
        startActivity(searchIntent);
        return(true);
    }
    return super.onOptionsItemSelected(item);
  }

  /* The click listner for ListView in the navigation drawer */
  private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      selectItem(position);
    }
  }

  private void selectItem(int position) {
    connected = connectedToInternetOrNot.ConnectedToInternetOrNot(this);
    if(connected==1){
      // update the main content by replacing fragments
      index = position;
      Fragment fragment = new SubCategoryListFragment();
      FragmentManager fragmentManager = getFragmentManager();
      fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
      fragmentCount =1;
      // update selected item and title, then close the drawer
      mDrawerList.setItemChecked(position, true);
      setTitle(myDrawerListItem[position]);
    }
    mDrawerLayout.closeDrawer(mDrawerList);
  }

  @Override
  public void setTitle(CharSequence title) {
    mTitle = title;
    getActionBar().setTitle(mTitle);
  }

  /**
   * When using the ActionBarDrawerToggle, you must call it during
   * onPostCreate() and onConfigurationChanged()...
   */

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // Pass any configuration change to the drawer toggles
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

}
