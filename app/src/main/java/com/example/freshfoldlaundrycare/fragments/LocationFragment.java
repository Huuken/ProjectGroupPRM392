package com.example.freshfoldlaundrycare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.freshfoldlaundrycare.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);


        return view;
    }
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Vị trí mặc định (ví dụ: Hà Nội)
        LatLng hanoi = new LatLng(21.0285, 105.8542);
        googleMap.addMarker(new MarkerOptions().position(hanoi).title("Hà Nội"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hanoi, 15));
    }


}
