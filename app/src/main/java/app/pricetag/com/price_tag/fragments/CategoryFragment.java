package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.adapters.CardDataAdapter;

/**
 * Created by shekhar on 24/8/14.
 */
public class CategoryFragment extends Fragment {
  ListView listView;

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup parent, Bundle savedBundle){
    View rootView = inflater.inflate(R.layout.main_fragment_list_cointainer,parent,false);
    listView = (ListView) rootView.findViewById(R.id.listViewCard);
    listView.setAdapter(new CardDataAdapter(rootView.getContext()));
    return rootView;
  }
}
