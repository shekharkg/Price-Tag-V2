package app.pricetag.com.price_tag;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by shekhar on 24/8/14.
 */
public class MyFragment extends Fragment {
  ListView listView;

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup parent, Bundle savedBundle){
    View rootView = inflater.inflate(R.layout.card_list_main,parent,false);
    listView = (ListView) rootView.findViewById(R.id.listViewCard);
    listView.setAdapter(new MainCardList(rootView.getContext()));
    return rootView;
  }
}
