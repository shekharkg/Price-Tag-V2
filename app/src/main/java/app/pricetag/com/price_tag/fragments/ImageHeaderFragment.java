package app.pricetag.com.price_tag.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import app.pricetag.com.price_tag.R;


/**
 * Created by jorge on 31/07/14.
 */
public class ImageHeaderFragment extends Fragment {

  private View rootView;
  private String imageStringUrl;

  public ImageHeaderFragment(String imageStringUrl) {
    this.imageStringUrl = imageStringUrl;
  }

  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_header_first, container, false);
        return rootView;
    }
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ImageView imageView = (ImageView) rootView.findViewById(R.id.image_head);
    Ion.with(imageView).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).load(imageStringUrl);

  }
}
