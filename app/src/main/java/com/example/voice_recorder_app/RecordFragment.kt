package com.example.voice_recorder_app

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.media.Image
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import java.io.File
import java.io.IOException
import java.util.jar.Manifest
import kotlin.math.log


class RecordFragment() : Fragment(), View.OnClickListener {

    private var isRecording: Boolean = false
    private var mediaRecorder: MediaRecorder? = null
    private var recordPath: File? = null
    private var recordFile: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record, container, false)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.record_list_btn).setOnClickListener(this)
        view.findViewById<ImageButton>(R.id.record_btn).setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.record_list_btn -> findNavController().navigate(R.id.action_recordFragment_to_audioListFragment)
            R.id.record_btn -> {
                if (isRecording) {
                    startRecording()
                    view.findViewById<ImageButton>(R.id.record_btn).setImageResource(R.drawable.stop_recording)
                    isRecording = false
                } else {
                    if (checkPermissions()) {
                        stopRecording()
                        view.findViewById<ImageButton>(R.id.record_btn)
                            .setImageResource(R.drawable.record)
                        isRecording = true
                    }
                }
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            reset()
            release()
        }
        mediaRecorder = null
    }

    private fun startRecording() {
        recordPath = context?.getExternalFilesDir("/")
        recordFile = "name.3gp"


        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(recordPath?.absolutePath + "_" + recordFile)
            Log.e(TAG, "path: " + recordPath?.absoluteFile + "_" + recordFile)
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
    }