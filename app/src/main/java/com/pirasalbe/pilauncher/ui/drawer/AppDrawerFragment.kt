package com.pirasalbe.pilauncher.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pirasalbe.pilauncher.R


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

        return view;
    }


}