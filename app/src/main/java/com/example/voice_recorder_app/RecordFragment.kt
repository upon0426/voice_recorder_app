package com.example.voice_recorder_app

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.media.Image
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import org.w3c.dom.Text
import java.io.File
import java.io.IOException
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.math.log


class RecordFragment() : Fragment(), View.OnClickListener {

    private var isRecording: Boolean = false
    private var mediaRecorder: MediaRecorder? = null
    private var recordPath: File? = null
    private var recordFile: String = ""
    private var timer: Chronometer? = null
    private var fileNameText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record, container, false)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        timer = view.findViewById<Chronometer>(R.id.record_timer)
        fileNameText = view.findViewById<TextView>(R.id.record_filename)

        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.record_list_btn).setOnClickListener(this)
        view.findViewById<ImageButton>(R.id.record_btn).setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.record_list_btn -> {
                if (isRecording) {
                    val alertDialog = AlertDialog.Builder(context)
                            alertDialog.setTitle("Audio still recording")
                            alertDialog.setMessage("Are you sure you want to stop recording?")
                            alertDialog.setPositiveButton("YES") { dialog, which ->
                                findNavController().navigate(R.id.action_recordFragment_to_audioListFragment)
                                isRecording = false
                            }
                            alertDialog.setNegativeButton("NO", null)
                            alertDialog.show()
                } else {
                    findNavController().navigate(R.id.action_recordFragment_to_audioListFragment)
                }
            }
            R.id.record_btn -> {
                if (!isRecording) {
                    startRecording()
                    view.findViewById<ImageButton>(R.id.record_btn).setImageResource(R.drawable.stop_recording)
                    isRecording = true
                } else {
                    if (checkPermissions()) {
                        stopRecording()
                        view.findViewById<ImageButton>(R.id.record_btn)
                            .setImageResource(R.drawable.record)
                        isRecording = false
                    }
                }
            }
        }
    }

    private fun stopRecording() {
        timer?.stop()

        fileNameText?.setText("Recording Stopped, File Saved : " + recordFile)

        mediaRecorder?.apply {
            stop()
            reset()
            release()
        }
        mediaRecorder = null
    }

    private fun startRecording() {
        timer?.setBase(SystemClock.elapsedRealtime())
        timer?.start()

        recordPath = context?.getExternalFilesDir("/")
        val formatter = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.JAPAN)
        val now: java.util.Date = Date()
        recordFile = formatter.format(now) + ".3gp"

        fileNameText?.setText("Recording, File Name : " + recordFile)

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(recordPath?.absolutePath + "_" + recordFile)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed")
            }
            start()
        }
    }

    private fun checkPermissions(): Boolean {
        return if (context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.RECORD_AUDIO) } == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.RECORD_AUDIO), 1234) }
            false
        }
        }

    override fun onStop() {
        super.onStop()
        stopRecording()
    }
    }
