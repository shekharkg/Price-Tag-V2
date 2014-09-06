package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jorgecastilloprz.pagedheadlistview.PagedHeadListView;
import com.jorgecastilloprz.pagedheadlistview.utils.PageTransformerTypes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.pricetag.com.price_tag.ProductDetailsActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.adapters.MockListAdapter;
import app.pricetag.com.price_tag.asynctask.ImageHttpAsyncTask;

/**
 * Created by shekhar on 5/9/14.
 */
public class ProductDetailsFragment extends Fragment {

  private PagedHeadListView mPagedHeadList;
  private View rootView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView =  inflater.inflate(R.layout.product_specifications, container, false);
    return rootView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    new ImageHttpAsyncTask(this).execute(ProductDetailsActivity.imageUrl);
  }

  public void imageDao(String jsonResult) {

    JSONObject jsonObject = null;
    mPagedHeadList = (PagedHeadListView) rootView.findViewById(R.id.pagedHeadListView);
    try{
      jsonObject = new JSONObject(jsonResult);
      JSONArray imageArray = jsonObject.getJSONArray("images");
      for(int i=0; i<imageArray.length(); i++){
        JSONObject imageObject = imageArray.getJSONObject(i);
        String imageStringUrl = imageObject.getString("large_image");
        mPagedHeadList.addFragmentToHeader(new ImageHeaderFragment(imageStringUrl));
      }
      mPagedHeadList.setHeaderOffScreenPageLimit(imageArray.length()-1);
      mPagedHeadList.setHeaderPageTransformer(PageTransformerTypes.ACCORDION);

      ArrayList<String> mockItemList = new ArrayList<String>();
      mockItemList.add(ProductDetailsActivity.productName);
      MockListAdapter mockListAdapter = new MockListAdapter(getActivity(), R.layout.mock_list_item, mockItemList);
      mPagedHeadList.setAdapter(mockListAdapter);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

}

