package com.example.skmn.videos

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.skmn.R
import com.example.skmn.databinding.FragmentVideoBinding
import com.example.skmn.videos.YoutubeUtil.getVideoId
import com.google.android.material.textfield.TextInputEditText

@SuppressLint("StaticFieldLeak")
class VideoFragment : Fragment(), OnDeleteListener{
    lateinit var db: VideoDatabase
    var itemList = listOf<VideoEntity>()
    private var spinnerArray = mutableListOf("ShowAll","Exercise", "Game", "Music", "Study")
    private lateinit var binding: FragmentVideoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_video, container, false)

        // get video database
        db = VideoDatabase.getInstance(requireContext())!!

        // main category
        val spCategory = binding.spCategory
        val spAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, spinnerArray)
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = spAdapter

        // click plus button
        binding.btnWrite.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
            val etUrl: TextInputEditText = dialogView.findViewById(R.id.et_url)
            val etTitle: TextInputEditText = dialogView.findViewById(R.id.et_title)

            val spinner = dialogView.findViewById<Spinner>(R.id.spinner)
            val sa = ArrayAdapter<String>(view!!.context, android.R.layout.simple_spinner_item, spinnerArray)
            spinner.adapter = sa

            builder.setView(dialogView)
            builder.setTitle("Add Video")
            builder.setPositiveButton("추가") { dialog, which ->
                // Add video
                // videoId, title, category
                val video = VideoEntity(null, getVideoId(etUrl.text.toString()), etTitle.text.toString(), spinner.selectedItem.toString())
                insertVideo(video)
            }
            builder.setNegativeButton("취소") { dialog, which -> }
            builder.show()
        }

        spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                // get filter category
                getCategoryVideo(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        return binding.root
    }

    private fun insertVideo(video: VideoEntity) {
        // not coroutine
        val insertTask = object: AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                db.videoDAO().insert(video)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllVideos()
            }
        }

        insertTask.execute()
    }

    private fun getAllVideos(){
        val getTask = object: AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                itemList = db.videoDAO().getAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(itemList)
            }
        }

        getTask.execute()
    }

    private fun deleteVideo(video: VideoEntity){
        val deleteTask = object: AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                db.videoDAO().delete(video)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllVideos()
            }
        }

        deleteTask.execute()
    }

    private fun setRecyclerView(itemList: List<VideoEntity>){
        binding.rvList.adapter = ListAdapter(itemList, this.lifecycle, this)
    }

    private fun getCategoryVideo(key: String){
        val getTask = object: AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                itemList = db.videoDAO().getCategory(key)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(itemList)
            }
        }

        getTask.execute()
    }

    override fun onDeleteListener(video: VideoEntity) {
        deleteVideo(video)
    }


}