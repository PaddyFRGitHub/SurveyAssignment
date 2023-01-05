package com.example.surveyassignment.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

/* Database Config*/
private val DataBaseName = "AppDataBase(1).db"
private val ver : Int = 1

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DataBaseName,null , ver) {

    /* Employee Table */
    private val TableName = "User"

    private val Column_ID = "ID"
    private val Column_FirstName = "FirstName "
    private val Column_LastName =   "LastName"
    private val Column_Age = "Age"
    private val Column_Gender = "Gender"
    private val Column_Address = "Address"
    private val Column_UserName = "UserName"
    private val Column_Password = "Password"
    /*************************/

    // This is called the first time a database is accessed
    // Create a new database
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val sqlCreateStatement: String = "CREATE TABLE " + TableName + " ( " + Column_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + Column_FirstName + " TEXT NOT NULL, " +
                    Column_LastName + " TEXT NOT NULL, " + Column_Age + " INTEGER NOT NULL DEFAULT 0, " +
                    Column_Gender + " INT NOT NULL, " + Column_Address + " TEXT, " +
                    Column_UserName + " TEXT NOT NULL UNIQUE, " + Column_Password + " TEXT NOT NULL )"

            db?.execSQL(sqlCreateStatement)
        }
        catch (e: SQLiteException) {}
    }

    // This is called if the database ver. is changed
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    /**
     * return  1 : the new use has been add to the database successfully
     * return -1 : Error, adding new user
     * return -2 : can not open database
     * return -3 : user name is already exist
     *
     */
    fun addUser(user: User) : Int {

        val isUserNameAlreadyExists = checkUserName(user) // check if the username is already exist in the database
        if(isUserNameAlreadyExists < 0)
            return isUserNameAlreadyExists

        // writableDatabase for insert actions
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(Column_FirstName, user.firstName)
        cv.put(Column_LastName,user.LastName)
        cv.put(Column_Age, user.age)
        cv.put(Column_Gender, user.gender)
        cv.put(Column_Address,user.address)
        cv.put(Column_UserName, user.userName.lowercase())
        cv.put(Column_Password, user.password)

        val success  =  db.insert(TableName, null, cv)

        db.close()
        if (success.toInt() == -1) return success.toInt() //Error, adding new user
        else return 1

    }

    private fun checkUserName(user: User): Int {

        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        }
        catch(e: SQLiteException) {
            return -2
        }

        val userName = user.userName.lowercase()

        val sqlStatement = "SELECT * FROM $TableName WHERE $Column_UserName = ?"
        val param = arrayOf(userName)
        val cursor: Cursor =  db.rawQuery(sqlStatement,param)

        if(cursor.moveToFirst()){
            // The user is found
            val n = cursor.getInt(0)
            cursor.close()
            db.close()
            return -3 // error the user name is already exist
        }

        cursor.close()
        db.close()
        return 0 //User not found

    }

    fun getUser(user: User) : Int {

        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        }
        catch(e: SQLiteException) {
            return -2
        }

        val userName = user.userName.lowercase()
        val userPassword = user.password
        //val sqlStatement = "SELECT * FROM $TableName WHERE $Column_UserName = $userName AND $Column_Password = $userPassword"

        val sqlStatement = "SELECT * FROM $TableName WHERE $Column_UserName = ? AND $Column_Password = ?"
        val param = arrayOf(userName,userPassword)
        val cursor: Cursor =  db.rawQuery(sqlStatement,param)
        if(cursor.moveToFirst()){
            // The user is found
            val n = cursor.getInt(0)
            cursor.close()
            db.close()
            return n
        }

        cursor.close()
        db.close()
        return -1 //User not found

    }
/*
    fun getAllEmployee() : ArrayList<Employee> {

        val employeeList = ArrayList<Employee>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $TableName"

        val cursor: Cursor =  db.rawQuery(sqlStatement,null)

        if(cursor.moveToFirst())
            do {
                val id: Int = cursor.getInt(0)
                val name: String = cursor.getString(1)
                val age: Int = cursor.getInt(2)
                val isActive: Boolean = cursor.getInt(3) == 1

                val emp = Employee(id,name,age,isActive)
                employeeList.add(emp)
            }while(cursor.moveToNext())

        cursor.close()
        db.close()

        return employeeList
    }

 */
}