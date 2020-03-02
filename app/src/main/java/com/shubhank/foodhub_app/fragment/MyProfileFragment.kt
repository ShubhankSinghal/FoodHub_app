package com.shubhank.foodhub_app.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shubhank.foodhub_app.R


/**
 * A simple [Fragment] subclass.
 */
class MyProfileFragment : Fragment() {

    lateinit var profileName : TextView
    lateinit var profileNumber : TextView
    lateinit var profileEmail : TextView
    lateinit var profileAddress : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)

        val sharedPreferences =
            this.activity!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("res_name","John Doe").toString()
        val number = sharedPreferences.getString("res_number","+91-1115555555").toString()
        val email = sharedPreferences.getString("res_email","johnathan@doe.gmail").toString()
        val address = sharedPreferences.getString("res_address","Gurugram").toString()

        profileName = view.findViewById(R.id.textProfileName)
        profileNumber = view.findViewById(R.id.textProfileMobileNumber)
        profileEmail = view.findViewById(R.id.textProfileEmail)
        profileAddress = view.findViewById(R.id.textProfileLocation)

        profileName.text = name
        profileNumber.text = number
        profileEmail.text = email
        profileAddress.text = address

        return view
    }


}
