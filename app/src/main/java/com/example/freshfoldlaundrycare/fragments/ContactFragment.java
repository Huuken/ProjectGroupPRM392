package com.example.freshfoldlaundrycare.fragments; // Defines the package name for this class, organizing it under the "fragments" subpackage.

import android.os.Bundle; // Imports Bundle class to manage fragment state (e.g., passing arguments or saving/restoring data).

import androidx.fragment.app.Fragment; // Imports Fragment class as the base class for this fragment.

import android.view.LayoutInflater; // Imports LayoutInflater class to inflate the fragment's layout.
import android.view.View; // Imports View class to represent the UI components.
import android.view.ViewGroup; // Imports ViewGroup class to represent the container for the fragment's view.

import com.example.freshfoldlaundrycare.R; // Imports the R class, which contains references to resources (e.g., layouts) in the app.

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment { // Declares the ContactFragment class, extending Fragment for fragment functionality.

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1"; // Defines a constant key for the first argument (currently a placeholder name).
    private static final String ARG_PARAM2 = "param2"; // Defines a constant key for the second argument (currently a placeholder name).

    // TODO: Rename and change types of parameters
    private String mParam1; // Declares a variable to store the first parameter passed to the fragment.
    private String mParam2; // Declares a variable to store the second parameter passed to the fragment.

    public ContactFragment() { // Defines the public constructor for the fragment.
        // Required empty public constructor
        // This is intentionally left empty as per Android's requirement for fragments.
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(String param1, String param2) { // Defines a static factory method to create a new instance of ContactFragment.
        ContactFragment fragment = new ContactFragment(); // Creates a new instance of ContactFragment.
        Bundle args = new Bundle(); // Creates a Bundle to store arguments for the fragment.
        args.putString(ARG_PARAM1, param1); // Adds the first parameter to the Bundle with key ARG_PARAM1.
        args.putString(ARG_PARAM2, param2); // Adds the second parameter to the Bundle with key ARG_PARAM2.
        fragment.setArguments(args); // Sets the Bundle as the fragment's arguments.
        return fragment; // Returns the newly created fragment instance.
    }

    @Override // Indicates that the following method overrides a method from the superclass.
    public void onCreate(Bundle savedInstanceState) { // Defines the onCreate method, called when the fragment is being created.
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method for necessary setup.
        if (getArguments() != null) { // Checks if the fragment has any arguments passed to it.
            mParam1 = getArguments().getString(ARG_PARAM1); // Retrieves the first parameter from the arguments and assigns it to mParam1.
            mParam2 = getArguments().getString(ARG_PARAM2); // Retrieves the second parameter from the arguments and assigns it to mParam2.
        } // Closes the if block.
    } // Closes the onCreate method.

    @Override // Indicates that the following method overrides a method from the superclass.
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // Defines the onCreateView method, called to create the fragment's view.
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false); // Inflates the fragment_contact layout and returns it as the fragment's view.
        // The "false" parameter means the inflated view is not attached to the container yet (handled by the FragmentManager).
    } // Closes the onCreateView method.
} // Closes the ContactFragment class.