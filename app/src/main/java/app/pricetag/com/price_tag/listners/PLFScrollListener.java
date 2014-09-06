package app.pricetag.com.price_tag.listners;

import android.widget.AbsListView;

import app.pricetag.com.price_tag.ProductListDetailActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.asynctask.ProductListHttpAsyncTask;
import app.pricetag.com.price_tag.fragments.ProductListFragment;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.listener.SwipeOnScrollListener;

/**
 * Created by shekhar on 2/9/14.
 */
public class PLFScrollListener extends SwipeOnScrollListener {
  ProductListFragment fragment;

  public PLFScrollListener(ProductListFragment fragment) {
    this.fragment = fragment;
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {
    super.onScrollStateChanged(view,scrollState);
  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    //what is the bottom item that is visible
    int lastInScreen = firstVisibleItem + visibleItemCount;
    if(lastInScreen == fragment.start){
      new ProductListHttpAsyncTask(fragment).execute(ProductListDetailActivity.productListUrl +
          ProductListDetailActivity.sortOrder + fragment.start);
      if(fragment.start==0){
        Card card = new Card(fragment.getActivity());
        card.setInnerLayout(R.layout.loading_view_starting);
        card.setShadow(false);
        fragment.mCardArrayAdapter.add(card);
      }
      else{
        Card card = new Card(fragment.getActivity());
        card.setInnerLayout(R.layout.loading_view_card);
        fragment.mCardArrayAdapter.add(card);
        fragment.mCardArrayAdapter.setNotifyOnChange(true);
        fragment.mCardArrayAdapter.notifyDataSetChanged();
      }
      fragment.start += 25;
    }
  }
}
