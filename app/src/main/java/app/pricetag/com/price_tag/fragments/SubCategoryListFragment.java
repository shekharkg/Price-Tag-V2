package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import app.pricetag.com.price_tag.MyActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.asynctask.HttpAsyncTask;

/**
 * Created by shekhar on 26/8/14.
 */
public class SubCategoryListFragment extends Fragment {
  String categoryUrl;
  String[] categoryIndex;
  Context context;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    categoryUrl = container.getResources().getString(R.string.subCategoryUrl);
    categoryIndex = container.getResources().getStringArray(R.array.category_list);
    this.context = inflater.getContext();
    return inflater.inflate(R.layout.main_fragment_list_cointainer, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    categoryUrl = categoryUrl + categoryIndex[MyActivity.index];
    Toast.makeText(context,categoryUrl,Toast.LENGTH_SHORT).show();
    new HttpAsyncTask(context).execute(categoryUrl);
  }
}
