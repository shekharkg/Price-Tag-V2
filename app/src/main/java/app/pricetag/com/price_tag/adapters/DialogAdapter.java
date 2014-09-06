package app.pricetag.com.price_tag.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.pricetag.com.price_tag.ProductListDetailActivity;
import app.pricetag.com.price_tag.R;

/**
 * Created by shekhar on 6/9/14.
 */
public class DialogAdapter extends ArrayAdapter<String> {
  Context context;
  String[] titleArray;

  public DialogAdapter(Context context, String[] sortName) {
    super(context,R.layout.dialog_single_row, R.id.text1,sortName);
    this.context= context;
    this.titleArray = sortName;
  }


  @Override
  public View getView(int position, View convertView, ViewGroup parent){

    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View row = inflater.inflate(R.layout.dialog_single_row, parent,false);

    TextView textView = (TextView) row.findViewById(R.id.text1);
    textView.setText(titleArray[position]);
    if(position == ProductListDetailActivity.dialogSelectedIndex){
      ImageView imageView = (ImageView) row.findViewById(R.id.imageView);
      imageView.setVisibility(View.VISIBLE);
    }

    return row;

  }
}
