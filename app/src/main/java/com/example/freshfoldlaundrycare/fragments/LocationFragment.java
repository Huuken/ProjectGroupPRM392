package com.example.freshfoldlaundrycare.fragments; // Defines the package name for this class, organizing it under the "fragments" subpackage.

import android.os.Bundle; // Imports Bundle class to manage fragment state (e.g., saving/restoring data).
import android.view.LayoutInflater; // Imports LayoutInflater class to inflate the fragment's layout.
import android.view.View; // Imports View class to represent UI components.
import android.view.ViewGroup; // Imports ViewGroup class to represent the container for the fragment's view.

import androidx.annotation.NonNull; // Imports NonNull annotation to indicate parameters or return values cannot be null.
import androidx.fragment.app.Fragment; // Imports Fragment class as the base class for this fragment.

import com.example.freshfoldlaundrycare.R; // Imports the R class, which contains references to resources (e.g., layouts) in the app.
import com.google.android.gms.maps.CameraUpdateFactory; // Imports CameraUpdateFactory to control the map's camera (e.g., zoom, move).
import com.google.android.gms.maps.GoogleMap; // Imports GoogleMap class to interact with the Google Maps API.
import com.google.android.gms.maps.OnMapReadyCallback; // Imports OnMapReadyCallback interface to handle map initialization.
import com.google.android.gms.maps.SupportMapFragment; // Imports SupportMapFragment to embed a Google Map in the fragment.
import com.google.android.gms.maps.model.LatLng; // Imports LatLng class to represent geographic coordinates (latitude, longitude).
import com.google.android.gms.maps.model.MarkerOptions; // Imports MarkerOptions class to add markers to the map.

public class LocationFragment extends Fragment implements OnMapReadyCallback { // Declares the LocationFragment class, extending Fragment and implementing OnMapReadyCallback.
    private GoogleMap googleMap; // Declares a GoogleMap variable to store the map instance.

    @Override // Indicates that the following method overrides a method from the superclass.
    public void onCreate(Bundle savedInstanceState) { // Defines the onCreate method, called when the fragment is being created.
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method for necessary setup.
        // No additional initialization is performed here (empty for now).
    } // Closes the onCreate method.

    @Override // Indicates that the following method overrides a method from the superclass.
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // Defines the onCreateView method to create the fragment's view.
        View view = inflater.inflate(R.layout.fragment_location, container, false); // Inflates the fragment_location layout and assigns it to a View variable.
        // The "false" parameter means the inflated view is not attached to the container yet (handled by the FragmentManager).

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager() // Retrieves the SupportMapFragment from the layout using its ID (R.id.map).
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this); // Requests the GoogleMap object asynchronously, triggering onMapReady when ready.
        // "this" refers to the fragment, which implements OnMapReadyCallback.

        return view; // Returns the inflated view to be displayed by the fragment.
    } // Closes the onCreateView method.

    @Override // Indicates that the following method overrides a method from OnMapReadyCallback.
    public void onMapReady(@NonNull GoogleMap map) { // Defines the onMapReady method, called when the Google Map is ready to be used.
        googleMap = map; // Assigns the provided GoogleMap instance to the class variable.

        // Vị trí mặc định (ví dụ: Hà Nội) // Comment in Vietnamese: "Default location (e.g., Hanoi)".
        LatLng hanoi = new LatLng(21.0285, 105.8542); // Creates a LatLng object with coordinates for Hanoi (latitude: 21.0285, longitude: 105.8542).
        googleMap.addMarker(new MarkerOptions().position(hanoi).title("Hà Nội")); // Adds a marker at the Hanoi coordinates with the title "Hà Nội".
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hanoi, 15)); // Moves and zooms the camera to the Hanoi location with a zoom level of 15 (street-level detail).
    } // Closes the onMapReady method.
} // Closes the LocationFragment class.