package com.example.voice_recorder_app

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.File

class AudioListFragment : Fragment(), Runnable {
    
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false

    lateinit var path: String
    lateinit var directory: File
    lateinit var allFiles: Array<File>
    lateinit var fileToPlay: File

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    // UI element
    lateinit var playerBtn: ImageButton
    lateinit var playerHeader: TextView
    lateinit var playerFilename: TextView
    lateinit var playerSeekBar: SeekBar
    lateinit var seekBarHandler: Handler
    lateinit var updateSeekBar: Runnable

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
        val audioList: RecyclerView = view.findViewById(R.id.audio_list_view)
        val audioListAdapter = AudioListAdapter(allFiles)

        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet)

        playerBtn = view.findViewById(R.id.play_btn)
        playerHeader = view.findViewById(R.id.player_header_title)
        playerFilename = view.findViewById(R.id.player_filename)

        playerSeekBar = view.findViewById(R.id.player_seekBar)

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
                if (isPlaying) {
                    stopAudio()
                    playAudio(fileToPlay)
                } else {
                    fileToPlay = file
                    playAudio(fileToPlay)
                }
            }
        })

        playerBtn.setOnClickListener(object :View.OnClickListener {
            override fun onClick(p0: View?) {
                if (isPlaying) {
                    pauseAudio()
                } else {
                    if (fileToPlay != null) {
                        resumeAudio()
                    }
                }
            }
        })

        playerSeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                // do nothing
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                if (fileToPlay != null) {
                    pauseAudio()
                }
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (fileToPlay != null) {
                    var progress = p0!!.progress
                    mediaPlayer!!.seekTo(progress)
                    resumeAudio()
                }
            }
        })
    }

    private fun pauseAudio() {
        mediaPlayer!!.pause()
        playerBtn.setImageResource(R.drawable.play_arrow)
        isPlaying = false
        seekBarHandler.removeCallbacks(updateSeekBar)
    }

    private fun resumeAudio() {
        mediaPlayer!!.start()
        playerBtn.setImageResource(R.drawable.pause)
        isPlaying = true
        run()
        seekBarHandler.postDelayed(updateSeekBar, 0)    }

    private fun stopAudio() {
        playerBtn.setImageResource(R.drawable.play_arrow)
        playerHeader.text = "Stopped"
        isPlaying = false
        seekBarHandler.removeCallbacks(updateSeekBar)
    }

    private fun playAudio(fileToPlay: File) {
        mediaPlayer = MediaPlayer()

        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        mediaPlayer!!.setDataSource(fileToPlay.absolutePath)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()

        playerBtn.setImageResource(R.drawable.pause)
        playerFilename.text = fileToPlay.name
        playerHeader.text = "Playing"

        isPlaying = true

        mediaPlayer!!.setOnCompletionListener {
            stopAudio()
            playerHeader.text = "Finished"
        }

        playerSeekBar.max = mediaPlayer!!.duration
        seekBarHandler = Handler()
        updateSeekBar = Runnable {this.run()}
        seekBarHandler.postDelayed(updateSeekBar, 0)
    }

    override fun run() {
        playerSeekBar.progress = mediaPlayer!!.currentPosition
        seekBarHandler.postDelayed(this, 500)
    }

    override fun onStop() {
        super.onStop()
        if (isPlaying) {
            stopAudio()
        }
    }

}