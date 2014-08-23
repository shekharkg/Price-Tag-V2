package app.pricetag.com.price_tag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

/**
 * Created by shekhar on 23/8/14.
 */
public class SetDrawerImageTitle extends ArrayAdapter<String>{

  int[] imageCategory;
  Context context;
  String[] productCategory;

  SetDrawerImageTitle(Context context,String[] productCategory, int[] imageCategory){
    super(context,R.layout.drawer_list_item,R.id.textCategory,productCategory);
    this.context = context;
    this.productCategory = productCategory;
    this.imageCategory = imageCategory;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent){
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View row = inflater.inflate(R.layout.drawer_list_item,parent,false);
    CircularImageView circularImageView = (CircularImageView) row.findViewById(R.id.imageCategory);
    TextView textVIew = (TextView) row.findViewById(R.id.textCategory);
    circularImageView.setImageResource(imageCategory[position]);
    textVIew.setText(productCategory[position]);
    return row;
  }
}
