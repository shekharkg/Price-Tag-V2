package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.SearchActivity;
import app.pricetag.com.price_tag.asynctask.SearchHttpAsyncTask;
import app.pricetag.com.price_tag.dao.ConnectedToInternetOrNot;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by shekhar on 4/9/14.
 */
public class SearchCategoryFragment extends Fragment {
  String searchCategoryUrl;
  Context context;
  CardListView listView;
  CardArrayAdapter mCardArrayAdapter;
  ArrayList<Card> cards;
  ConnectedToInternetOrNot connectedToInternetOrNot;
  int connected;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    searchCategoryUrl = container.getResources().getString(R.string.search_product);
    this.context = inflater.getContext();
    return inflater.inflate(R.layout.main_fragment_list_cointainer, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    searchCategoryUrl = searchCategoryUrl + SearchActivity.queryString;

    listView = (CardListView) getActivity().findViewById(R.id.card_list);
    cards  = new ArrayList<Card>();
    mCardArrayAdapter = new CardArrayAdapter(context, cards);
    Card card = new Card(getActivity());
    card.setInnerLayout(R.layout.loading_view_starting);
    card.setShadow(false);
    mCardArrayAdapter.add(card);
    listView.setAdapter(mCardArrayAdapter);
    connectedToInternetOrNot = new ConnectedToInternetOrNot();
    new SearchHttpAsyncTask(this).execute(searchCategoryUrl);
  }

  public void subCategoryDao(String jsonString) {
    cards.remove(0);
    if(jsonString == ""){
      Toast.makeText(context,"No item found!!!" + "\n" + "Try something else.",Toast.LENGTH_SHORT).show();
      mCardArrayAdapter.clear();
    } else{
        try {
          JSONObject jsonObject = new JSONObject(jsonString);
          JSONArray jsonArray = jsonObject.getJSONArray("results");

          for(int i=0; i<jsonArray.length(); i++) {
            JSONObject resultObject = jsonArray.getJSONObject(i);
            String resultCategoryListName = resultObject.getString("category");
            int resultCategoryProductCount = Integer.parseInt(resultObject.getString("count"));
            int resultCategoryProductId = Integer.parseInt(resultObject.getString("id_category"));
            ResultCategoryList card = new ResultCategoryList(resultCategoryListName,resultCategoryProductCount,resultCategoryProductId);
            cards.add(card);
          }
          AnimationAdapter animCardArrayAdapter;
          if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
            animCardArrayAdapter = new ScaleInAnimationAdapter(mCardArrayAdapter);
            animCardArrayAdapter.setAbsListView(listView);
            listView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);
          }

        } catch (JSONException e) {
          Card card = new Card((SearchActivity) getActivity());
          card.setInnerLayout(R.layout.retry_loading);
          card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
              connected = connectedToInternetOrNot.ConnectedToInternetOrNot((SearchActivity) getActivity());
              if(connected == 1) {
                SearchCategoryFragment searchCategoryFragment = new SearchCategoryFragment();
                FragmentTransaction transaction = SearchActivity.manager.beginTransaction();
                transaction.replace(R.id.content_frame_product_list, searchCategoryFragment,"searchCategoryFragment");
                transaction.commit();
                Crouton.cancelAllCroutons();
              }
            }
          });
          cards.add(card);
          if (listView!=null) {
            listView.setAdapter(mCardArrayAdapter);
          }
          e.printStackTrace();
        }
    }
  }

  public class ResultCategoryList extends Card{

    String resultCategoryListName;
    int resultCategoryProductCount;
    int resultCategoryProductId;

    public ResultCategoryList(String resultCategoryListName,int resultCategoryProductCount,int resultCategoryProductId) {
      super(context, R.layout.sub_category_single_row);
      this.resultCategoryListName = resultCategoryListName;
      this.resultCategoryProductCount = resultCategoryProductCount;
      this.resultCategoryProductId = resultCategoryProductId;
    }


    @Override
    public void setupInnerViewElements(final ViewGroup parent, View view) {

      //Retrieve elements
      TextView title = (TextView) parent.findViewById(R.id.titleSubCategory);

      if (title != null)
        title.setText(resultCategoryListName + " (" + resultCategoryProductCount + ")");

      setOnClickListener(new OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
          connected = connectedToInternetOrNot.ConnectedToInternetOrNot((SearchActivity) getActivity());
          if(connected == 1){
            SearchActivity.fragmentCount = 1;
            resultCategoryListName = resultCategoryListName.replaceAll(" " , "+");
            SearchActivity.searchKey = SearchActivity.queryString + "&category=" + resultCategoryListName;
            getFragmentManager().beginTransaction()
                .replace(R.id.content_frame_product_list, new SearchProductListFragment()).commit();
            SearchActivity.imm.hideSoftInputFromWindow(SearchActivity.mSearchView.getWindowToken(), 0);
            Crouton.cancelAllCroutons();
          }
        }
      });
    }
  }
}



