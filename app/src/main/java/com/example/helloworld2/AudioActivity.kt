package com.example.helloworld2

import android.Manifest
import android.content.pm.PackageManager
import android.media.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_audio.*
import kotlinx.android.synthetic.main.activity_audio.view.*
import kotlinx.coroutines.*
import java.io.*
import java.lang.Exception
import java.time.LocalTime
import java.util.concurrent.ThreadPoolExecutor

const val l = "MyLogs"

class AudioActivity : AppCompatActivity() {

    private var isRecording = false
    private var isPlaying = false
    private lateinit var recFile: File

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        }

        audioButton.setOnClickListener {
            recordingView.visibility = View.VISIBLE
            stopAudioButton.visibility = View.VISIBLE
            recordingView.recOrPlayText.text = "Recording..."
            val recFileName = "testkjs.raw"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            try {
                recFile = File(storageDir.toString() + "/" + recFileName)
                Log.d(l, storageDir.toString())
                Log.d(l, recFile.toString())
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }
            try {
                val outputStream = FileOutputStream(recFile)
                val bufferedOutputStream = BufferedOutputStream(outputStream)
                val dataOutputStream = DataOutputStream(bufferedOutputStream)

                val minBufferSize = AudioRecord.getMinBufferSize(
                    44100,
                    AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val aFormat = AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(44100)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                    .build()
                val recorder = AudioRecord.Builder()
                    .setAudioSource(MediaRecorder.AudioSource.MIC)
                    .setAudioFormat(aFormat)
                    .setBufferSizeInBytes(minBufferSize)
                    .build()
                val audioData = ByteArray(minBufferSize)

                GlobalScope.launch(Dispatchers.IO) {
                    isRecording = true
                    recorder.startRecording()
                    while (isRecording) {
                        val numofBytes = recorder.read(audioData, 0, minBufferSize)
                        Log.d(l, numofBytes.toString())
                        if (numofBytes > 0) {
                            dataOutputStream.write(audioData)
                        }
                    }
                    recorder.stop()
                    dataOutputStream.close()

                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        stopAudioButton.setOnClickListener {
            recordingView.visibility = View.GONE
            stopAudioButton.visibility = View.GONE
            isRecording = false
            val iStream = FileInputStream(recFile)
            GlobalScope.launch(Dispatchers.Main) {
                recordingView.recOrPlayText.text = "Playing..."
                recordingView.visibility = View.VISIBLE
                isPlaying = true
                withContext(Dispatchers.IO) {
                    playAudio(iStream)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun playAudio(istream: InputStream) {

        val minBufferSize = AudioTrack.getMinBufferSize(
            44100, AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val aBuilder = AudioTrack.Builder()
        val aAttr: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        val aFormat: AudioFormat = AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(44100)
            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
            .build()
        val track = aBuilder.setAudioAttributes(aAttr)
            .setAudioFormat(aFormat)
            .setBufferSizeInBytes(minBufferSize)
            .build()
        track!!.setVolume(0.8f)
        track!!.play()
        var i = 0
        val buffer = ByteArray(minBufferSize)
        try {
            i = istream.read(buffer, 0, minBufferSize)
            while (i != -1) {
                track!!.write(buffer, 0, i)
                i = istream.read(buffer, 0, minBufferSize)
            }
        } catch (e: IOException) {
        }
        try {
            istream.close()
        } catch (e: IOException) {
        }
        track!!.stop()
        track!!.release()
       GlobalScope.launch(Dispatchers.Main) {
           recordingView.visibility = View.GONE
       }

    }
}