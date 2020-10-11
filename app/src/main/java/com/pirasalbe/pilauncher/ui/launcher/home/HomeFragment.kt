package com.pirasalbe.pilauncher.ui.launcher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pirasalbe.pilauncher.R
import com.pirasalbe.pilauncher.ui.gesture.OnSwipeTouchListener


/**
 * Home pages fragment
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout of the app drawer
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        // subscribe swipe
        view.setOnTouchListener(object : OnSwipeTouchListener(activity!!) {
            override fun onSwipeRight() {
            }

            override fun onSwipeLeft() {
                view.findNavController().navigate(R.id.action_homeFragment_to_categoriesFragment)
            }

            override fun onSwipeTop() {
            }

            override fun onSwipeBottom() {
            }

        });

        return view
    }


}