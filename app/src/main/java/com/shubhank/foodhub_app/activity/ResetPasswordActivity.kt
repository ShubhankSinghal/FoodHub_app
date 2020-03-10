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

class ResetPasswordActivity : AppCompatActivity() {

    lateinit var otpOtp: EditText
    lateinit var otpNewPassword: EditText
    lateinit var otpConfirmNewPassword: EditText
    lateinit var otpButton: Button
    lateinit var otpBack: ImageView

    var mobile_number: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        otpBack = findViewById(R.id.otpBack)
        otpOtp = findViewById(R.id.otpOTP)
        otpNewPassword = findViewById(R.id.otpNewPassword)
        otpConfirmNewPassword = findViewById(R.id.otpConfirmNewPassword)
        otpButton = findViewById(R.id.otpButton)

        if (intent != null) {
            mobile_number = intent.getStringExtra("mobile_number")
        } else {
            finish()
            Toast.makeText(
                this@ResetPasswordActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        otpBack.setOnClickListener {
            finish()
        }

        otpButton.setOnClickListener {

            otpButton.visibility = View.INVISIBLE

            if (otpNewPassword.text.isNullOrEmpty() || otpConfirmNewPassword.text.isNullOrEmpty() || otpOtp.text.isNullOrEmpty()) {

                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Fields are empty!",
                    Toast.LENGTH_SHORT
                ).show()
                otpButton.visibility = View.VISIBLE

            } else if (otpNewPassword.text.length <= 5) {

                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Password Length must be greater than 5",
                    Toast.LENGTH_SHORT
                ).show()
                otpButton.visibility = View.VISIBLE

            } else if (otpNewPassword.text.toString() != otpConfirmNewPassword.text.toString()) {

                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Password Mismatched",
                    Toast.LENGTH_SHORT
                ).show()
                otpButton.visibility = View.VISIBLE

            } else {

                val queue = Volley.newRequestQueue(this@ResetPasswordActivity)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", mobile_number)
                jsonParams.put("password", otpNewPassword.text.toString())
                jsonParams.put("otp", otpOtp.text.toString())

                if (ConnectionManager().checkConnectivity(this@ResetPasswordActivity)) {

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
                                            val intent = Intent(
                                                this@ResetPasswordActivity,
                                                LoginActivity::class.java
                                            )
                                            startActivity(intent)
                                            finishAffinity()
                                        } else {
                                            val msg = data.getString("errorMessage")
                                            Toast.makeText(
                                                this@ResetPasswordActivity,
                                                msg,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            otpButton.visibility = View.VISIBLE
                                        }

                                    } catch (e: JSONException) {

                                        Toast.makeText(
                                            this@ResetPasswordActivity,
                                            "Some unexpected error occurred!!!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        otpButton.visibility = View.VISIBLE
                                    }
                                },
                                Response.ErrorListener {

                                    Toast.makeText(
                                        this@ResetPasswordActivity,
                                        "Volley error occurred",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    otpButton.visibility = View.VISIBLE
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
                    otpButton.visibility = View.VISIBLE
                    val dialog = AlertDialog.Builder(this@ResetPasswordActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection Not Found")
                    dialog.setPositiveButton("Open Settings") { _, _ ->

                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        finishAffinity()

                    }
                    dialog.setNegativeButton("Exit") { _, _ ->
                        ActivityCompat.finishAffinity((this@ResetPasswordActivity))
                    }
                    dialog.setCancelable(false)
                    dialog.create()
                    dialog.show()
                }
            }
        }
    }
}
