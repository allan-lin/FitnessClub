package com.example.install.fitnessclub;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClubHoursFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClubHoursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClubHoursFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //variable store viewpaher and adapter
    private ViewPager viewPager;
    private SectionPagerAdapter sectionPagerAdapter;

    private OnFragmentInteractionListener mListener;

    public ClubHoursFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClubHoursFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClubHoursFragment newInstance(String param1, String param2) {
        ClubHoursFragment fragment = new ClubHoursFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_club_hours, container, false);
        //create a adapter and link it to the fragmentManager
        sectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.hourcontent);
        //set the adapter to the viewpager
        viewPager.setAdapter(sectionPagerAdapter);

        //set the transformer
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        ImageButton leftButton = (ImageButton) view.findViewById(R.id.left_nav);
        ImageButton rightButton = (ImageButton) view.findViewById(R.id.right_nav);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int location = viewPager.getCurrentItem();
                if(location > 0) {
                    location--;
                    viewPager.setCurrentItem(location);
                } else if (location == 0){
                    viewPager.setCurrentItem(viewPager.getChildCount());
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int location = viewPager.getCurrentItem();
                location++;
                if(location >= viewPager.getChildCount()+2) {
                    location++;
                    viewPager.setCurrentItem(0);
                } else {
                    viewPager.setCurrentItem(location);
                }
            }
        });
        return view;
    }


    public class SectionPagerAdapter extends FragmentPagerAdapter {
        //takes the fm as a parameter
        public SectionPagerAdapter(FragmentManager fm){
            super(fm);
        }
        public Fragment getItem(int position){
            //adds the items as param to the new Fragment
            switch(position) {
                case 0:
                    return HoursFragment.newInstance("Chest",R.drawable.chest,"");
                case 1:
                    return HoursFragment.newInstance("Back",R.drawable.back, "");
                case 2:
                    return HoursFragment.newInstance("Shoulders",R.drawable.shoulders, "");
                case 3:
                    return HoursFragment.newInstance("Arms",R.drawable.arms, "");
                case 4:
                    return HoursFragment.newInstance("Legs",R.drawable.legs, "");
                default:
                    return HoursFragment.newInstance("Chest",R.drawable.chest, "");
            }
        }
        //getCount method determines how many fragment will be returned
        public int getCount(){
            return 5;
        }

    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
