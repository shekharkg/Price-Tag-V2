package app.pricetag.com.price_tag.asynctask;

import android.os.AsyncTask;

import app.pricetag.com.price_tag.fragments.ProductDetailsFragment;

/**
 * Created by shekhar on 7/9/14.
 */
public class DetailsHttpASyncTask extends AsyncTask<String, Void, String> {
  ProductDetailsFragment fragment;

  public DetailsHttpASyncTask(ProductDetailsFragment fragment) {
    this.fragment = fragment;
  }

  @Override
  protected String doInBackground(String... params) {
    return GetJsonString.GET(params[0]);
  }

  @Override
  protected void onPostExecute(String result) {
    //if result is null then implement action
    fragment.detailsDao(result);
  }

}