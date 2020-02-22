package com.shubhank.foodhub_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class RegistrationActivity : AppCompatActivity() {

    lateinit var registerBack: ImageView
    lateinit var registerName: EditText
    lateinit var registerEmail: EditText
    lateinit var registerMobileNumber: EditText
    lateinit var registerDeliveryAddress: EditText
    lateinit var registerPassword: EditText
    lateinit var registerConfirmPassword: EditText
    lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        registerBack = findViewById(R.id.registerBack)
        registerName = findViewById(R.id.registerName)
        registerEmail = findViewById(R.id.registerEmail)
        registerMobileNumber = findViewById(R.id.registerMobileNumber)
        registerDeliveryAddress = findViewById(R.id.registerDeliveryAddress)
        registerPassword = findViewById(R.id.registerPassword)
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword)
        registerBtn = findViewById(R.id.registerBtn)

        registerBack.setOnClickListener {
            finish()
        }

        registerBtn.setOnClickListener {
            if (registerPassword != registerConfirmPassword) {
                Toast.makeText(this@RegistrationActivity, "Password Mismatched", Toast.LENGTH_SHORT)
                    .show()
            } else {

                val queue = Volley.newRequestQueue(this@RegistrationActivity)
                val url = "http://13.235.250.119/v2/login/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("name", registerName)
                jsonParams.put("mobile_number", registerMobileNumber)
                jsonParams.put("password", registerPassword)
                jsonParams.put("address", registerDeliveryAddress)
                jsonParams.put("email", registerEmail)

                if (ConnectionManager().checkConnectivity(this@RegistrationActivity)) {

                    val jsonObjectRequest =
                        object :
                            JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                jsonParams,
                                Response.Listener {

                                    try {

                                        val success = it.getBoolean("success")

                                        if (success) {
                                            val intent = Intent(
                                                this@RegistrationActivity,
                                                MainActivity::class.java
                                            )
                                            startActivity(intent)
                                        }

                                    } catch (e: JSONException) {
                                        Toast.makeText(
                                            this@RegistrationActivity,
                                            "Some unexpected error occur!!!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }


                                },
                                Response.ErrorListener {

                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        "Volley error occurred",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                return headers

                            }
                        }

                    queue.add(jsonObjectRequest)

                }


            }


        }
    }
}