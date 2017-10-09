package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class HomeFragment extends Fragment {

    public static final String ARG_USERNAME = "username";
    private String username = "";
    private View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView t = (TextView) view.findViewById(R.id.username);
        t.setText(username);

        ImageView i = (ImageView) view.findViewById(R.id.imageView2);
        switch(getActivity().getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                i.setImageResource(R.drawable.info_horizontal);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                i.setImageResource(R.drawable.info_vertical);
                break;
        }
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration _newConfig) {

        ImageView i = (ImageView) view.findViewById(R.id.imageView2);
        switch(getActivity().getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                i.setImageResource(R.drawable.info_horizontal);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                i.setImageResource(R.drawable.info_vertical);
                break;
        }

        super.onConfigurationChanged(_newConfig);
    }

}
