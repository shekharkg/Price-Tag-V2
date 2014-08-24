package app.pricetag.com.price_tag;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

/**
 * Created by shekhar on 24/8/14.
 */
public class MainCardList extends BaseAdapter {

  ArrayList<ListRow> list;
  Context context;

  public MainCardList(Context context) {
    this.context = context;
    list = new ArrayList<ListRow>();
    Resources res = context.getResources();
    String[] nameArr = res.getStringArray(R.array.product_list);
    int[] imageArr = {R.drawable.cameras,R.drawable.computers,R.drawable.electronics,R.drawable.bikes
        ,R.drawable.cars,R.drawable.books,R.drawable.lifestyle,R.drawable.baby_products,R.drawable.appliances
        ,R.drawable.entertainment,R.drawable.flower_gifts,R.drawable.sports,R.drawable.health_beauty
        ,R.drawable.home_decor,R.drawable.handicrafts,R.drawable.furniture};

    for(int i=0; i<16; i++){
      list.add(new ListRow(nameArr[i], imageArr[i]));
    }
  }

  @Override
  public int getCount() {
    return list.size();
  }

  @Override
  public Object getItem(int position) {
    return list.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent){
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View row = inflater.inflate(R.layout.main_list_item_card,parent,false);
    CircularImageView circularImageView = (CircularImageView) row.findViewById(R.id.imageCategory);
    TextView textVIew = (TextView) row.findViewById(R.id.textCategory);

    ListRow temp = list.get(position);
    circularImageView.setImageResource(temp.productImage);
    textVIew.setText(temp.productName);
    return row;
  }
}