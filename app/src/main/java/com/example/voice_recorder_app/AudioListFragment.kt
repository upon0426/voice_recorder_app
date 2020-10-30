package com.example.voice_recorder_app

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.File

class AudioListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playerSheet: ConstraintLayout = view.findViewById(R.id.player_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(playerSheet)
        val audioList: RecyclerView = view.findViewById(R.id.audio_list_view)

        val path: String? = activity?.getExternalFilesDir("/")?.absolutePath
        val directory = File(path).parentFile
        val allFiles = directory.listFiles()
        //Log.e(TAG, directory.listFiles()[3].name)

        val audioListAdapter = AudioListAdapter(allFiles)

        audioList.setHasFixedSize(true)
        audioList.layoutManager = LinearLayoutManager(context)
        audioList.adapter = audioListAdapter


            bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // do nothing
            }
        })
    }

}