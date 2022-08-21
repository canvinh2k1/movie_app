package com.example.movieapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class PersonFragment(
    private var remMoviesAdapter: MoviesRemAdapter,
    private val showDialog : () -> Unit,
    private val tranferEdit : () -> Unit
) : Fragment() {

    private lateinit var remMoviesLayoutMgr: LinearLayoutManager
    private lateinit var remMoviesList: RecyclerView

    private lateinit var sf : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var tvName : TextView
    private lateinit var tvBday : TextView
    private lateinit var tvEmail : TextView
    private lateinit var tvSex : TextView
    private lateinit var imagePerson : CircleImageView

    private var args : Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sf = this.requireActivity().getSharedPreferences("my_sf", Context.MODE_PRIVATE)
        editor = sf.edit()

        args = this.arguments



        return inflater.inflate(R.layout.fragment_person, container, false)
    }

    override fun onPause() {
        super.onPause()
        editor.apply{
            Log.i("my_tag","3")
            putString("sf_name", tvName.text.toString())
            putString("sf_bday", tvBday.text.toString())
            putString("sf_email", tvEmail.text.toString())
            putString("sf_sex", tvSex.text.toString())
            val image = saveImage()
            putString("sf_images", image)
            commit()
        }
    }

    override fun onResume() {
        super.onResume()
        val name = sf.getString("sf_name",null)
        val bday = sf.getString("sf_bday",null)
        val email = sf.getString("sf_email",null)
        val sex = sf.getString("sf_sex",null)
        val image = sf.getString("sf_images", null)
        if (validateInput(name, bday, email, sex) && args == null) {
            tvName.text = name
            tvBday.text = bday
            tvEmail.text = email
            tvSex.text = sex
            image?.let {
                val imageAsByte = Base64.decode(image.encodeToByteArray(), Base64.DEFAULT)
                imagePerson.setImageBitmap(BitmapFactory.decodeByteArray(imageAsByte,0,imageAsByte.size))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val about = view.findViewById<Button>(R.id.btnAbout)

        remMoviesList = view.findViewById(R.id.list_rem_movie)
        remMoviesLayoutMgr = LinearLayoutManager(context)
        remMoviesList.layoutManager = remMoviesLayoutMgr
        remMoviesList.adapter = remMoviesAdapter


        about.setOnClickListener {
            showDialog()
        }

        val edit = view.findViewById<Button>(R.id.btnEdit)
        edit.setOnClickListener {
            tranferEdit()
        }

        tvName = view.findViewById(R.id.tvName)
        tvBday = view.findViewById(R.id.tvBday)
        tvSex = view.findViewById(R.id.tvSex)
        tvEmail = view.findViewById(R.id.tvEmail)
        imagePerson = view.findViewById(R.id.image_person)

         if (args != null) {
            val name = args?.get("name")
            val bday = args?.get("birthday")
            val sex = args?.get("sex")
            val email = args?.get("email")
            val image = Uri.parse(args?.get("image").toString())

            tvName.text = name.toString()
            tvBday.text = bday.toString()
            tvSex.text = sex.toString()
            tvEmail.text = email.toString()
            imagePerson.setImageURI(image)

        }
    }

    private fun validateInput(name : String?, bday : String?, email : String?, sex : String?) : Boolean {
        return when {
            name.isNullOrEmpty() -> {
                Toast.makeText(context, "Name is empty", Toast.LENGTH_SHORT).show()
                false
            }
            bday.isNullOrEmpty() -> {
                Toast.makeText(context, "BirthDay is empty", Toast.LENGTH_SHORT).show()
                false
            }
            email.isNullOrEmpty() -> {
                Toast.makeText(context, "Email is empty", Toast.LENGTH_SHORT).show()
                false
            }
            sex.isNullOrEmpty() -> {
                Toast.makeText(context, "Sex is empty", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun saveImage(): String {
        imagePerson.buildDrawingCache()
        val bitmapImage = imagePerson.drawingCache
        val stream = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        return Base64.encodeToString(image, 0)
    }
}