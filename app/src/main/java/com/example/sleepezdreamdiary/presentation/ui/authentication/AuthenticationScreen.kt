package com.example.sleepezdreamdiary.presentation.ui.authentication

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AuthenticationScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val userExists = sharedPreferences.contains("username")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (userExists) {
                    val savedUsername = sharedPreferences.getString("username", "")
                    val savedPassword = sharedPreferences.getString("password", "")
                    if (username == savedUsername && password == savedPassword) {
                        navController.navigate("home") {
                            popUpTo("authentication") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    with(sharedPreferences.edit()) {
                        putString("username", username)
                        putString("password", password)
                        apply()
                    }
                    navController.navigate("home") {
                        popUpTo("authentication") { inclusive = true }
                    }
                }
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (userExists) "Login" else "Create Account")
        }
    }
}