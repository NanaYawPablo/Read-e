package com.blo.reade;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import controller.FetchFeedTask;
import model.Constants;
import model.Feed;
import model.MyErrorTracker;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Entertainment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Entertainment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Entertainment extends Fragment {

    private final static String urlAddress = Constants.ENTERTAINMENT_URL;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    GridView gridView;
    private ProgressBar ProgressBarLoading;
    private Feed selectedFeed;
    private Intent intent;
    private Bundle bundle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OnFragmentInteractionListener mListener;
    private final ArrayList<String> urlAddressList= new ArrayList<>();
    public Entertainment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Entertainment.
     */
    // TODO: Rename and change types and number of parameters
    public static Entertainment newInstance(String param1, String param2) {
        Entertainment fragment = new Entertainment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entertainment, container, false);

        gridView = rootView.findViewById(R.id.gridView);
        ProgressBarLoading = rootView.findViewById(R.id.progressbar);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        urlAddressList.add(urlAddress);
        //Fetch feeds
        // new FetchFeedTask().execute((Void) null);
        new FetchFeedTask(getContext(), urlAddressList, gridView, ProgressBarLoading).execute();

        //Handling Swipe Refresh Layout
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask(getContext(), urlAddressList, gridView, ProgressBarLoading).execute();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        //on click of GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFeed = (Feed) parent.getItemAtPosition(position);
                Snackbar snackbar = Snackbar.make(view, "View Article Online", Snackbar.LENGTH_SHORT)
                        .setAction("PROCEED", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //                //moving to next page
                                intent = new Intent(getContext(), WebViewActivity.class);
                                bundle = new Bundle();
                                //check empty link
                                if (selectedFeed.getLink() != null) {
                                    bundle.putSerializable("BundleObject", selectedFeed.getLink());
                                    intent.putExtras(bundle);
                                    Objects.requireNonNull(getContext()).startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "" + MyErrorTracker.EMPTY_URL, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                snackbar.show();


            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
