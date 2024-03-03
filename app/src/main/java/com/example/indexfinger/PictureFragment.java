package com.example.indexfinger;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class PictureFragment extends Fragment {
    private static final String ARG_PHOTO_URI = "picture_uri";

    public PictureFragment(){
        //ここは空にしておく
    }
    public static PictureFragment newInstance(String photoUri){
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_URI,photoUri);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_photo,container,false);
        ImageView imageView = view.findViewById(R.id.imageView);
        assert getArguments() != null;
        String photoUri = getArguments().getString(ARG_PHOTO_URI);
        //photoUriを用いてImageViewに画像をセットする処理を行う
        if(photoUri != null){
            Uri uri = Uri.parse(photoUri);
            imageView.setImageURI(uri);
        }
        return view;
    }
}
