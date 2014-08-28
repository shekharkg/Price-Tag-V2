package app.pricetag.com.price_tag.asynctask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import app.pricetag.com.price_tag.dao.ProductListDao;
import app.pricetag.com.price_tag.dao.SubCategoryDao;

/**
 * Created by shekhar on 28/8/14.
 */
public class ProductListHttpAsyncTask extends AsyncTask<String, Void, String> {

  Dialog dialog;
  Context context;

  public ProductListHttpAsyncTask(Context context) {
    this.context = context;
  }

  @Override
  protected void onPreExecute() {
    // TODO Auto-generated method stub
    dialog = new ProgressDialog(context);
    dialog.setTitle("Loading...");
    dialog.show();
    super.onPreExecute();
  }

  @Override
  protected String doInBackground(String... params) {
    return GetJsonString.GET(params[0]);
  }

  @Override
  protected void onPostExecute(String result) {
    //if result is null then implement action
    new ProductListDao(result, context);
    dialog.dismiss();
  }

}
