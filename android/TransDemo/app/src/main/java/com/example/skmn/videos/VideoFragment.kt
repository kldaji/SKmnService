package com.example.skmn.videos

import android.app.AlertDialog
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
import com.google.firebase.firestore.FirebaseFirestore

class VideoFragment : Fragment(){
    private val db = FirebaseFirestore.getInstance()
    private val itemList = arrayListOf<ListLayout>()
    private val adapter = ListAdapter(itemList, this.lifecycle)
    private var spinnerArray = mutableListOf("ShowAll","Exercise", "Game", "Music", "Study")
    private lateinit var binding: FragmentVideoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_video, container, false)

        binding.rvList.adapter = adapter

        val spCategory = binding.spCategory
        val spAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, spinnerArray)
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = spAdapter

        spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                if(selectedItem == "ShowAll"){
                    db.collection("ShowAll")
                        .get()
                        .addOnSuccessListener { result ->
                            itemList.clear()
                            for (document in result) {  // 가져온 문서들은 result에 들어감
                                val item =
                                    ListLayout(document["category"] as String, document["title"] as String, document["videoId"] as String)
                                itemList.add(item)
                            }
                            adapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신
                        }
                        .addOnFailureListener { exception ->
                            // 실패할 경우
                            Log.w("VideoFragment", "Error getting documents: $exception")
                        }
                }else{
                    db.collection("ShowAll")
                        .whereEqualTo("category", selectedItem)
                        .get()
                        .addOnSuccessListener { result ->
                            itemList.clear()
                            for (document in result) {  // 가져온 문서들은 result에 들어감
                                val item =
                                    ListLayout(document["category"] as String, document["title"] as String, document["videoId"] as String)
                                itemList.add(item)
                            }
                            adapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신
                        }
                        .addOnFailureListener { exception ->
                            // 실패할 경우
                            Log.w("VideoFragment", "Error getting documents: $exception")
                        }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

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
                val data = hashMapOf(
                    "category" to spinner.selectedItem.toString(),
                    "videoId" to getVideoId(etUrl.text.toString()),
                    "title" to etTitle.text.toString()
                )

                db.collection("ShowAll")
                    .add(data)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "비디오가 추가되었습니다", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Log.w("VideoFragment", "Error getting documents: $exception")
                    }
            }
            builder.setNegativeButton("취소") { dialog, which -> }
            builder.show()
        }
        return binding.root
    }


}