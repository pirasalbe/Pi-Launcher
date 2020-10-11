package com.pirasalbe.pilauncher.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pirasalbe.pilauncher.R


/**
 * A simple [Fragment] subclass.
 * Use the [AppDrawerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppDrawerFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_drawer, container, false)
    }


}