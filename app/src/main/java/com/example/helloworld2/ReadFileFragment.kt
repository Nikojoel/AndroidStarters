package com.example.helloworld2
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.file_fragment.view.*


class ReadFileFragment: Fragment() {

    companion object {
        fun newInstance() : ReadFileFragment {
            return ReadFileFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.file_fragment, container, false)
        view.fileText.text = context?.openFileInput(FILENAME)?.bufferedReader().use {
            it?.readText() ?: "Failed"
        }
        return view
    }
}