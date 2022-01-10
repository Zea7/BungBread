package com.example.bungeoppang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide

class InfoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView:ViewGroup = inflater.inflate(R.layout.fragment_info, container, false) as ViewGroup
        Glide.with(requireContext()).load(R.drawable.bung).circleCrop().into(rootView.findViewById(R.id.profile_img))
        val text: TextView = rootView.findViewById(R.id.nickname) as TextView
        text.text = Variables.USER_NAME

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InfoFragment()
    }
}