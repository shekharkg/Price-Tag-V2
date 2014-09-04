package app.pricetag.com.price_tag.asynctask;

import android.os.AsyncTask;

import app.pricetag.com.price_tag.fragments.SearchCategoryFragment;

/**
 * Created by shekhar on 4/9/14.
 */
public class SearchHttpAsyncTask extends AsyncTask<String, Void, String> {

  SearchCategoryFragment fragment;

  public SearchHttpAsyncTask(SearchCategoryFragment fragment) {
    this.fragment = fragment;
  }

  @Override
  protected String doInBackground(String... params) {
    return GetJsonString.GET(params[0]);
  }

  @Override
  protected void onPostExecute(String result) {
    //if result is null then implement action
    fragment.subCategoryDao(result);
  }

}
