package app.pricetag.com.price_tag.asynctask;
import android.os.AsyncTask;
import app.pricetag.com.price_tag.fragments.SubCategoryListFragment;

/**
 * Created by shekhar on 26/8/14.
 */
public class SubCategoryHttpAsyncTask extends AsyncTask<String, Void, String> {

  SubCategoryListFragment fragment;

  public SubCategoryHttpAsyncTask(SubCategoryListFragment fragment) {
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
