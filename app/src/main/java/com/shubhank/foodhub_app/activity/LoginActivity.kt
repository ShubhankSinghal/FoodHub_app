package com.shubhank.foodhub_app.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.model.Food
import com.shubhank.foodhub_app.model.Profile
import com.shubhank.foodhub_app.util.ConnectionManager
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var loginMobileNumber: EditText
    lateinit var loginPassword: EditText
    lateinit var loginButton: Button
    lateinit var loginForgotPassword: TextView
    lateinit var loginRegister: TextView

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        loginMobileNumber = findViewById(R.id.loginMobileNumber)
        loginPassword = findViewById(R.id.loginPassword)
        loginButton = findViewById(R.id.loginButton)
        loginForgotPassword = findViewById(R.id.loginForgotPassword)
        loginRegister = findViewById(R.id.loginRegister)

        loginForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        loginRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {

            val mobileNumber = loginMobileNumber.text.toString()
            val password = loginPassword.text.toString()

            if (mobileNumber.isEmpty() || password.isEmpty()) {

                Toast.makeText(this@LoginActivity, "Enter credentials first!", Toast.LENGTH_SHORT)
                    .show()

            } else if (mobileNumber.length != 10) {

                Toast.makeText(this@LoginActivity, "Enter valid mobile number!", Toast.LENGTH_SHORT)
                    .show()

            } else {

                val queue = Volley.newRequestQueue(this@LoginActivity)
                val url = "http://13.235.250.119/v2/login/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", mobileNumber)
                jsonParams.put("password", password)

                if (ConnectionManager().checkConnectivity(this@LoginActivity)) {

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
                                            val intent =
                                                Intent(this@LoginActivity, MainActivity::class.java)
                                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                                            startActivity(intent)
                                            finishAffinity()
                                        }

                                    } catch (e: JSONException) {

                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Some JSON related error occur!!!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }

                                },
                                Response.ErrorListener {

                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Volley error occurred",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "6f5311403e6661"
                                return headers

                            }
                        }

                    queue.add(jsonObjectRequest)

                }


            }


        }
    }
}
