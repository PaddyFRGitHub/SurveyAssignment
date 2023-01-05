package com.example.surveyassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.surveyassignment.model.DataBaseHelper
import com.example.surveyassignment.model.User

class MainActivityNewUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new_user)
    }

    fun saveNewUserButton(view: View) {

        val firstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
        val lastName  = findViewById<EditText>(R.id.editTextLastName).text.toString()
        val userName  = findViewById<EditText>(R.id.editTextNewUserName).text.toString()
        val userPassword  = findViewById<EditText>(R.id.editTextNewUserPassword).text.toString()
        var gender = 0
        val age           = findViewById<EditText>(R.id.editTextNumberAge).text.toString()
        val address = findViewById<EditText>(R.id.editTextAddress).text.toString()

        val message = findViewById<TextView>(R.id.textViewMessage)

        /*check Male or Female*/
        val rg = findViewById<RadioGroup>(R.id.radioGroupMF)
        val rb = findViewById<RadioButton>(rg.checkedRadioButtonId)
        if(rb.text.toString() == "Male")
            gender = 0
        else gender = 1

        // if not empty, covert age to integer,  otherwise zero
        val userAge = if(age.isEmpty()) 0 else age.toInt()

        if(firstName.isEmpty() || lastName.isEmpty() ) // First and last name are required
        // Toast.makeText(this,"First name and last name are required!",Toast.LENGTH_LONG).show()
            message.text = "First name and last name are required!"
        else if(userName.isEmpty() || userPassword.isEmpty() ) // // User name and password are required
        //  Toast.makeText(this,"User name and Password are required!",Toast.LENGTH_LONG).show()
            message.text = "User name and Password are required!"
        else { // Save data
            // Create object
            val newUser = User(-1,firstName, lastName, userAge,gender,address, userName,userPassword)
            val mydatabase = DataBaseHelper(this)
            val result = mydatabase.addUser(newUser)
            when(result) {
                1 ->  {
                    message.text = "Your details has been add to the database successfully"
                    findViewById<Button>(R.id. buttonSave).isEnabled = true
                }
                -1 -> message.text = "Error on creating new account"
                -2 -> message.text = "Error can not open database"
                -3 -> message.text = "User name is already exist"
            }
        }
    }
}