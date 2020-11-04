package com.example.voice_recorder_app

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

class AudioListFragment : Fragment()  {
    lateinit var path: String
    lateinit var directory: File
    lateinit var allFiles: Array<File>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        path = activity?.getExternalFilesDir("/")?.absolutePath!!
        directory = File(path).parentFile
        allFiles = directory.listFiles()
        return inflater.inflate(R.layout.fragment_audio_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playerSheet: ConstraintLayout = view.findViewById(R.id.player_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(playerSheet)
        val audioList: RecyclerView = view.findViewById(R.id.audio_list_view)
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

        audioListAdapter.setOnItemClickListener(object :AudioListAdapter.OnItemListClick{
            override fun onClickListener(view: View, file: File, position: Int) {
                Log.d("PLAY LOG", "File Playing" + file.name)
            }
        })
    }




}