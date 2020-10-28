package com.example.voice_recorder_app

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecordFlagment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)
        view.findViewById<ImageButton>(R.id.record_list_btn).setOnClickListener{
            findNavController().navigate(R.id.action_recordFragment_to_audioListFragment)
        }
        return view
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        view.findViewById<ImageButton>(R.id.record_list_btn).setOnClickListener(this)
//    }
//
//    override fun onClick(view: View?) {
//        when (view?.id) {
//            R.id.record_list_btn -> findNavController().navigate(R.id.action_recordFragment_to_audioListFragment)
//        }
//    }


}