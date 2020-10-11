package com.pirasalbe.pilauncher.ui.launcher.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pirasalbe.pilauncher.R
import com.pirasalbe.pilauncher.ui.gesture.OnSwipeTouchListener

/**
 * App Categories Fragment
 */
class CategoriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout of the app drawer
        val view: View = inflater.inflate(R.layout.fragment_categories, container, false)

        // subscribe swipe
        view.setOnTouchListener(object : OnSwipeTouchListener(activity!!) {
            override fun onSwipeRight() {
                view.findNavController()
                    .navigate(R.id.action_categoriesFragment_to_homeFragment)
            }

            override fun onSwipeLeft() {
                view.findNavController()
                    .navigate(R.id.action_categoriesFragment_to_appDrawerFragment)
            }

            override fun onSwipeTop() {
            }

            override fun onSwipeBottom() {
            }

        });

        return view
    }
}