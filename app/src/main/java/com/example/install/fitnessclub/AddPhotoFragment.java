package com.example.install.fitnessclub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddPhotoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPhotoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner spin;
    ImageView cameraButton;
    LinearLayout galleryLayout;
    private static final int CAMERA_INTENT = 1;
    private String imageLocation;

    private OnFragmentInteractionListener mListener;

    public AddPhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPhotoFragment newInstance(String param1, String param2) {
        AddPhotoFragment fragment = new AddPhotoFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);

        galleryLayout =
                (LinearLayout) view.findViewById(R.id.galleryLayout);
        spin = (Spinner) view.findViewById(R.id.locationSpinner);

        DatabaseHandler db = new DatabaseHandler(getContext());

        ArrayList<Exercise> list = db.getAllExercises();

        db.closeDB();

        ArrayAdapter adapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_dropdown_item, list);
        spin.setAdapter(adapter);

        cameraButton = (ImageView) view.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File picture = null;
                try{
                    picture = createImage();
                }catch(IOException e){
                    e.printStackTrace();
                }
                Intent i = new Intent();
                i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                if(i.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(i, CAMERA_INTENT);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_INTENT && resultCode == RESULT_OK){
            Bitmap image = BitmapFactory.decodeFile(imageLocation);
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(image);
            galleryLayout.addView(imageView);
            DatabaseHandler db = new DatabaseHandler(getContext());
            int picID = db.addPicture(new Picture(imageLocation));
            if(picID != -1){
                Exercise exercise = (Exercise) spin.getSelectedItem();
                db.addImageLocation(picID, exercise.getId());
                Toast.makeText(getActivity(), "Photo Added",
                        Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getActivity(), "Photo Not Added",
                        Toast.LENGTH_LONG).show();
            }
            db.closeDB();
        }
    }

    File createImage() throws IOException{
        //Create a timestamp to help create a collision free name
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHss").format(new Date());
        //Create the name of the image
        String fileName = "hiking_log_" + timeStamp;
        //Grab the directory we want to save the image
        File directory =
                Environment.
                        getExternalStoragePublicDirectory
                                (Environment.DIRECTORY_PICTURES);
        //Create the image in that directory
        File picture = File.createTempFile(fileName, ".jpg", directory);
        //Save the location of the image
        imageLocation = picture.getAbsolutePath();
        return picture;
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
