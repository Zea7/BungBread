package com.example.bungeoppang

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class MapFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_map, container, false) as ViewGroup
        val button: Button = rootView.findViewById(R.id.server)
        button.setOnClickListener{
            val intent = Intent(requireContext(), Temp::class.java)
            startActivity(intent)
        }
        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MapFragment()
    }
}