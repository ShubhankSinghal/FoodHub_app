package com.shubhank.foodhub_app.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
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

            registerBtn.visibility = View.INVISIBLE

            if (registerName.text.isNullOrEmpty()) {
                Toast.makeText(this@RegistrationActivity, "Name is missing!", Toast.LENGTH_SHORT)
                    .show()
                registerBtn.visibility = View.VISIBLE
            } else if (registerEmail.text.isNullOrEmpty()) {
                Toast.makeText(this@RegistrationActivity, "Email is missing!", Toast.LENGTH_SHORT)
                    .show()
                registerBtn.visibility = View.VISIBLE
            } else if (registerMobileNumber.text.isNullOrEmpty()) {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Mobile Number is missing!",
                    Toast.LENGTH_SHORT
                ).show()
                registerBtn.visibility = View.VISIBLE
            } else if (registerMobileNumber.text.length != 10) {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Invalid Mobile Number",
                    Toast.LENGTH_SHORT
                ).show()
                registerBtn.visibility = View.VISIBLE
            } else if (registerDeliveryAddress.text.isNullOrEmpty()) {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Delivery Address is missing!",
                    Toast.LENGTH_SHORT
                ).show()
                registerBtn.visibility = View.VISIBLE
            } else if (registerPassword.text.isNullOrEmpty() || registerConfirmPassword.text.isNullOrEmpty()) {
                Toast.makeText(
                        this@RegistrationActivity,
                        "Password is Missing!",
                        Toast.LENGTH_SHORT
                    )
                    .show()
                registerBtn.visibility = View.VISIBLE
            } else if (registerPassword.text.length <= 5) {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Password length must be greater than 5",
                    Toast.LENGTH_SHORT
                ).show()
                registerBtn.visibility = View.VISIBLE
            } else if (registerPassword.text.toString() != registerConfirmPassword.text.toString()) {
                Toast.makeText(this@RegistrationActivity, "Password Mismatched", Toast.LENGTH_SHORT)
                    .show()
                registerBtn.visibility = View.VISIBLE
            } else {

                val queue = Volley.newRequestQueue(this@RegistrationActivity)
                val url = "http://13.235.250.119/v2/register/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("name", registerName.text.toString())
                jsonParams.put("mobile_number", registerMobileNumber.text.toString())
                jsonParams.put("password", registerPassword.text.toString())
                jsonParams.put("address", registerDeliveryAddress.text.toString())
                jsonParams.put("email", registerEmail.text.toString())

                if (ConnectionManager().checkConnectivity(this@RegistrationActivity)) {

                    val jsonObjectRequest =
                        object :
                            JsonObjectRequest(
                                Method.POST,
                                url,
                                jsonParams,
                                Response.Listener {

                                    try {

                                        val data = it.getJSONObject("data")
                                        val success = data.getBoolean("success")

                                        if (success) {
                                            Toast.makeText(
                                                this@RegistrationActivity,
                                                "Registered Successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent = Intent(
                                                this@RegistrationActivity,
                                                MainActivity::class.java
                                            )
                                            startActivity(intent)
                                            finishAffinity()
                                        } else {
                                            val msg = data.getString("errorMessage")
                                            Toast.makeText(
                                                this@RegistrationActivity,
                                                msg,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            registerBtn.visibility = View.VISIBLE
                                        }

                                    } catch (e: JSONException) {
                                        Toast.makeText(
                                            this@RegistrationActivity,
                                            "Some unexpected error occurred!!!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        registerBtn.visibility = View.VISIBLE
                                    }
                                },
                                Response.ErrorListener {

                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        "Volley error occurred",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    registerBtn.visibility = View.VISIBLE
                                }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "6f5311403e6661"
                                return headers
                            }
                        }
                    queue.add(jsonObjectRequest)
                } else {
                    registerBtn.visibility = View.VISIBLE
                    val dialog = AlertDialog.Builder(this@RegistrationActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection Not Found")
                    dialog.setPositiveButton("Open Settings") { _, _ ->

                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        finishAffinity()

                    }
                    dialog.setNegativeButton("Exit") { _, _ ->
                        ActivityCompat.finishAffinity((this@RegistrationActivity))
                    }
                    dialog.setCancelable(false)
                    dialog.create()
                    dialog.show()
                }
            }
        }
    }
}
