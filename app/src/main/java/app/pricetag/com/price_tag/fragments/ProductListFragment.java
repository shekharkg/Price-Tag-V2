package app.pricetag.com.price_tag.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import app.pricetag.com.price_tag.ProductListDetailActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.asynctask.ProductListHttpAsyncTask;

/**
 * Created by shekhar on 28/8/14.
 */
public class ProductListFragment extends Fragment {
  public static Activity c;
  Context context;
  int start = 0;
  public static int totalProductCount;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.context = inflater.getContext();
    return inflater.inflate(R.layout.main_fragment_list_cointainer, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    Toast.makeText(context, ProductListDetailActivity.productListUrl, Toast.LENGTH_SHORT).show();
    new ProductListHttpAsyncTask(context).execute(ProductListDetailActivity.productListUrl + "&limit=25&start=" + start);
    c = getActivity();
  }
}
