package com.pirasalbe.pilauncher.ui.launcher.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pirasalbe.pilauncher.R
import com.pirasalbe.pilauncher.ui.gesture.OnSwipeTouchListener


/**
 * App Drawer
 */
class AppDrawerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout of the app drawer
        val view: View = inflater.inflate(R.layout.fragment_app_drawer, container, false)

        // get the recycler view and set the adapter
        val recyclerView: RecyclerView = view.findViewById(R.id.AppDrawer)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val appDrawerAdapter = AppDrawerAdapter(activity!!)
        recyclerView.adapter = appDrawerAdapter

        // subscribe swipe
        view.setOnTouchListener(object : OnSwipeTouchListener(activity!!) {
            override fun onSwipeRight() {
                view.findNavController()
                    .navigate(R.id.action_appDrawerFragment_to_categoriesFragment)
            }

            override fun onSwipeLeft() {
            }

            override fun onSwipeTop() {
            }

            override fun onSwipeBottom() {
            }

        });

        return view;
    }


}