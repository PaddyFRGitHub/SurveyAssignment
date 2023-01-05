package com.example.surveyassignment.model

data class User(val userId: Int, var firstName: String, var LastName: String, var age: Int,
                var gender: Int, var address: String, val userName: String, val password: String) {
}