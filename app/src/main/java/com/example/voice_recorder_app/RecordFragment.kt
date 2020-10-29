package com.example.voice_recorder_app

import android.app.Activity
import android.content.pm.PackageManager
import android.media.Image
import android.os.Bundle
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
import java.util.jar.Manifest


class RecordFragment(private var isRecording: Boolean = false) : Fragment(), View.OnClickListener {


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
                    view.findViewById<ImageButton>(R.id.record_btn).setImageResource(R.drawable.stop_recording)
                    isRecording = false
                } else {
                    if (checkPermissions()) {
                        view.findViewById<ImageButton>(R.id.record_btn)
                            .setImageResource(R.drawable.record)
                        isRecording = true
                    }
                }
            }
        }
    }

    private fun checkPermissions(): Boolean {
            if (context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.RECORD_AUDIO) } == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                activity?.let { ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.RECORD_AUDIO), 1234) }
                return false
            }
        }
    }
