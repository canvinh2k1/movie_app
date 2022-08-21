package com.example.movieapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment


class EditProfileFragment(
    private var remMoviesAdapter: MoviesRemAdapter,
    private val backToHome : () -> Unit,
    private val showDialog : () -> Unit,
    private val tranferEdit : () -> Unit
) : Fragment() {

    private lateinit var personView : ImageView
    private var uri : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val btnDate = view.findViewById<Button>(R.id.btnDate)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)
        val tbSex = view.findViewById<ToggleButton>(R.id.tbSex)
        val ibCam = view.findViewById<ImageButton>(R.id.ibCam)
        personView = view.findViewById<ImageView>(R.id.personView)

        val cancel = view.findViewById<Button>(R.id.btnCancel)
        cancel.setOnClickListener {
            backToHome()
        }


        btnDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ) {
                resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    tvDate.text = date
            }
            }
            datePickerFragment.show(supportFragmentManager,"DatePickerFragment")
        }

        var sex = ""
        tbSex.setOnCheckedChangeListener{ buttonView, isChecked ->
            sex = if (isChecked) "Male" else "Female"
            Toast.makeText(requireActivity(),sex,Toast.LENGTH_SHORT).show()
        }

        ibCam.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }
        }


        val btnSave = view.findViewById<Button>(R.id.btnDone)
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val bday = tvDate.text.toString()
            sex = tbSex.text.toString()

            if (validateInput(name,bday,email,sex,uri)) {
                val  bundle = Bundle()
                bundle.putString("name",name)
                bundle.putString("birthday",bday)
                bundle.putString("sex",sex)
                bundle.putString("email",email)
                bundle.putString("image", uri)
                val fragment = PersonFragment(remMoviesAdapter,showDialog,tranferEdit)
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.mvFragment,fragment)?.addToBackStack(null)?.commit()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent  = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(requireActivity(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data != null) {
                uri = data.data.toString()

            }
            personView.setImageURI(data?.data)
        }
    }

    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }


    private fun validateInput(name : String?, bday : String?, email : String?, sex : String?, uri : String?) : Boolean {
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
            uri.isNullOrEmpty() -> {
                Toast.makeText(context, "Image is empty", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }
}