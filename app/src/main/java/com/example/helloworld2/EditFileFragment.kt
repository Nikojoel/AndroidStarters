package com.example.helloworld2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.edit_fragment.view.*

class EditFileFragment: Fragment() {
    companion object {
        fun newInstance() : EditFileFragment {
            return EditFileFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.edit_fragment, container, false)
        view.saveButton.setOnClickListener {
            context?.openFileOutput(FILENAME, Context.MODE_APPEND).use {
                it?.write("${view.editText.text}\n".toByteArray())
            }
            activity?.onBackPressed()
        }
        return view
    }


}