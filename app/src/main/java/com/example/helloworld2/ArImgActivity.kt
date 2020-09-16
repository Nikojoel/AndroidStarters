package com.example.helloworld2

import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class ArImgActivity : AppCompatActivity() {

    private lateinit var fragment: ArFragment
    private lateinit var fitToScanImageView: View
    private lateinit var testRenderable: ViewRenderable
    private lateinit var testRenderable2: ViewRenderable

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_img)

        fragment = supportFragmentManager.findFragmentById(R.id.arimage_fragment) as ArFragment
        fitToScanImageView = findViewById<ImageView>(R.id.fit_to_scan_img)
        val renderableFuture = ViewRenderable.builder()
            .setView(this, R.layout.rendtext)
            .build()
        renderableFuture.thenAccept {it -> testRenderable = it }
        val renderableFuture2 = ViewRenderable.builder()
            .setView(this, R.layout.rendtext2)
            .build()
        renderableFuture2.thenAccept {it -> testRenderable2 = it }
        fragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            frameUpdate()
        }
    }

    private fun frameUpdate() {
        val arFrame = fragment.arSceneView.arFrame
        if (arFrame == null || arFrame.camera.trackingState != TrackingState.TRACKING) {
            return
        }
        val updatedAugmentedImages = arFrame.getUpdatedTrackables(AugmentedImage::class.java)
        updatedAugmentedImages.forEach {
            when (it.trackingState) {

                TrackingState.PAUSED -> {
                    val text = "Detected Image: " + it.name + " - need more info"
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
                }

                TrackingState.STOPPED -> {
                    val text = "Tracking stopped: " + it.name
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
                }

                TrackingState.TRACKING -> {
                    val anchors = it.anchors
                    if (anchors.isEmpty()) {
                        fitToScanImageView.visibility = View.GONE
                        val pose = it.centerPose
                        val anchor = it.createAnchor(pose)
                        val anchorNode = AnchorNode(anchor)
                        anchorNode.setParent(fragment.arSceneView.scene)
                        val imgNode = TransformableNode(fragment.transformationSystem)
                        imgNode.setParent(anchorNode)
                        if (it.name == "sofa") {
                            imgNode.renderable = testRenderable
                        }
                        if (it.name == "corals") {
                            imgNode.renderable = testRenderable2
                        }
                    }
                }
            }
        }
    }
}

class AImgFrag: ArFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false
        return view
    }

    override fun getSessionConfiguration(session: Session?): Config {
        val config = super.getSessionConfiguration(session)
        setupAugmentedImageDatabase(config, session)
        return config
    }

    private fun setupAugmentedImageDatabase(config: Config, session: Session?) {
        val augmentedImageDb: AugmentedImageDatabase
        val assetManager = requireContext().assets
        val inputStream1 = assetManager.open("sofa.png")
        val augmentedImageBitmap1 = BitmapFactory.decodeStream(inputStream1)
        val inputStream2 = assetManager.open("coral.png")
        val augmentedImageBitmap2 = BitmapFactory.decodeStream(inputStream2)
        augmentedImageDb = AugmentedImageDatabase(session)
        augmentedImageDb.addImage("sofa", augmentedImageBitmap1)
        augmentedImageDb.addImage("corals", augmentedImageBitmap2)
        config.augmentedImageDatabase = augmentedImageDb
    }
}

