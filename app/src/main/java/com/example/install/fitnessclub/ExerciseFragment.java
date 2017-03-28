package com.example.install.fitnessclub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.install.fitnessclub.MainActivity.fab;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView list;
    TextView exerciseDescriptionTextView;
    LinearLayout galleryLayout;

    private OnFragmentInteractionListener mListener;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseFragment newInstance(String param1, String param2) {
        ExerciseFragment fragment = new ExerciseFragment();
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
    FragmentManager fm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        fm = getActivity().getSupportFragmentManager();
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.content_main, new CreateExerciseFragment());
                ft.commit();
            }
        });

        list = (ListView) view.findViewById(R.id.exerciselist);
        DatabaseHandler db = new DatabaseHandler(getContext());
        //create an ArrayList for the exercise
        //final ArrayList<Exercise> exerciseslist = new ArrayList<Exercise>();
        final ArrayList<Exercise> exerciseslist = db.getAllExercises();
        db.closeDB();
        //adds the exercise to the array
//        exerciseslist.add(new Exercise("Bench Press", "The bench press is an upper body strength training exercise that consists of pressing a weight upwards from a supine position.", "http://www.bodybuilding.com/fun/betteru9.htm"));
//        exerciseslist.add(new Exercise("Squats", "In strength training and fitness, the squat is a compound, full body exercise that trains primarily the muscles of the thighs, hips and buttocks, quadriceps femoris muscle (vastus lateralis, vastus medialis, vastus intermedius and rectus femoris), hamstrings, as well as strengthening the bones, ligaments and insertion of the tendons throughout the lower body.", "http://www.bodybuilding.com/content/how-to-squat-proper-techniques-for-a-perfect-squat.html"));
//        exerciseslist.add(new Exercise("DeadLift", "The deadlift is a weight training exercise in which a loaded barbell or bar is lifted off the ground to the hips, then lowered back to the ground. It is one of the three powerlifting exercises, along with the squat and bench press.", "http://www.bodybuilding.com/fun/how-to-deadlift-beginners-guide.html"));
//        exerciseslist.add(new Exercise("Snatch", "The snatch is the first of two lifts contested in the sport of weightlifting (also known as Olympic weightlifting) followed by the clean and jerk. The objective of the snatch is to lift the barbell from the ground to overhead in one continuous motion.", "http://www.bodybuilding.com/fun/learn-olympic-lifts-snatch-and-clean-and-jerk-progression-lifts.htm"));
//        exerciseslist.add(new Exercise("Clean and Jerk", "The clean and jerk is a composite of two weightlifting movements, most often performed with a barbell: the clean and the jerk. During the clean, the lifter moves the barbell from the floor to a racked position across the Deltoids, without resting fully on the Clavicles.", "http://www.bodybuilding.com/fun/learn-olympic-lifts-snatch-and-clean-and-jerk-progression-lifts.html"));
        final CustomAdapter adapter = new CustomAdapter(getContext(), exerciseslist);
        //add the adpter to thr listview
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //exerciseDescriptionTextView = (TextView) view.findViewById(R.id.description);
                galleryLayout = (LinearLayout) view.findViewById(R.id.galleryLayout);
                TextView details = (TextView) view.findViewById(R.id.details);
                ImageView chevron = (ImageView) view.findViewById(R.id.chevron);
                //if(exerciseDescriptionTextView.getText() != exerciseslist.get(position).getDescription()){
                    //exerciseDescriptionTextView.setText(((Exercise) list.getItemAtPosition(position)).getDescription());
                if(galleryLayout.getVisibility() == View.GONE ||
                        galleryLayout.getVisibility() == View.INVISIBLE){
                    galleryLayout.setVisibility(View.VISIBLE);
                    //update the text of the show more
                    details.setText("Click to show less");
                    //update the chevron image
                    chevron.setImageResource(R.drawable.ic_expand_less_black_24dp);
                }
                else{
                    //exerciseDescriptionTextView.setText("");
                    galleryLayout.setVisibility(View.GONE);
                    //update the text of the show more
                    details.setText("Click to show more");
                    //update the chevron image
                    chevron.setImageResource(R.drawable.ic_expand_more_black_24dp);
                }
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseHandler db = new DatabaseHandler(getContext());
                Exercise location = exerciseslist.get(position);
                db.deleteExercise(location.getId());
                db.closeDB();
                exerciseslist.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return view;
    }

    //create the customadapter
    public class CustomAdapter extends ArrayAdapter<Exercise> {
        public CustomAdapter(Context context, ArrayList<Exercise> items) {
            super(context, 0, items);
        }
        //The construct takes the context and ArrayList and runs the parents construct
        public View getView(int position, View convertView, ViewGroup parent){
            final Exercise item = getItem(position);

            if(convertView == null){
                //create a new layout file for the exercises
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.exercise_view, parent, false);
            }

            //Grab the gallery layout associated with this location
            galleryLayout =
                    (LinearLayout) convertView.findViewById(R.id.galleryLayout);
            //Make the gallery layout invisible
            galleryLayout.setVisibility(View.GONE);
            //only add items to the gallery if the gallery is empty
            if(galleryLayout.getChildCount() == 0){
                //Grab all the photos that match the id of the current location
                DatabaseHandler db = new DatabaseHandler(getContext());
                ArrayList<Picture> pics = db.getAllPictures(item.getId());
                db.closeDB();
                //Add those photos to the gallery
                for(int i =0; i < pics.size(); i++){
                    Bitmap image = BitmapFactory.decodeFile(pics.get(i).getResource());
                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageBitmap(image);
                    imageView.setAdjustViewBounds(true);
                    galleryLayout.addView(imageView);
                }
            }

            exerciseDescriptionTextView =
                    (TextView) convertView.findViewById(R.id.description);
            exerciseDescriptionTextView.setText(
                    ((Exercise) list.getItemAtPosition(position)).getDescription()
            );

            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(item.getName());
            ImageView image = (ImageView) convertView.findViewById(R.id.location);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri webpage = Uri.parse(item.getLocation());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }

                }
            });

            return  convertView;
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
