package com.devkt.adminguninevigation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devkt.adminguninevigation.R
import com.devkt.adminguninevigation.screens.nav.Routs
import com.devkt.adminguninevigation.viewModel.MyViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.loginAdmin.collectAsState()
    val context = LocalContext.current

    val name = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val handledLogin = remember { mutableStateOf(false) }

    when {
        state.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        state.value.error != null -> {
            Text(text = state.value.error!!, modifier = Modifier.padding(50.dp))
            handledLogin.value = true
        }
        state.value.data != null && !handledLogin.value -> {
            val response = state.value.data
            if (response?.status == 200) {
                Toast.makeText(context, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(Routs.AddSubLocationScreen) {
                    popUpTo(Routs.LoginScreen) { inclusive = true }
                }
            } else {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
            handledLogin.value = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(top = 70.dp, start = 10.dp, end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.guni_logo),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text(text = "Email") },
                modifier = Modifier.padding(top = 10.dp)
            )
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = "Password") },
                modifier = Modifier.padding(top = 10.dp)
            )
            Button(
                onClick = {
                    if (name.value == "" || password.value == "") {
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        handledLogin.value = false
                        viewModel.loginAdmin(name.value, password.value)
                    }
                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(text = "Login")
            }
        }
    }
}