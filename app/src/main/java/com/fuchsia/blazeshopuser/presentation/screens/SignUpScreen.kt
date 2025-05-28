package com.fuchsia.blazeshopuser.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fuchsia.blazeshopuser.R
import com.fuchsia.blazeshopuser.domain.models.UserDataModel
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MyViewModel = hiltViewModel()

) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }

    var isSuccessfulReg = remember { mutableStateOf(false) }

    val createUserState = viewModel.createUserState.collectAsState()
    when {
        createUserState.value.isLoading -> {
            Box(
                modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                AlertDialog(
                    onDismissRequest = { /*TODO*/ },
                    text = { CircularProgressIndicator() },
                    confirmButton = { /*TODO*/ },
                    dismissButton = { /*TODO*/ },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(80.dp)
                )
            }
        }

        createUserState.value.error != null -> {
            Box(
                modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = createUserState.value.error!!)

            }
        }

        createUserState.value.isSuccess != null -> {
            Box(
                modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = createUserState.value.isSuccess!!)
                LaunchedEffect(Unit) {
                    isSuccessfulReg.value = true
                }

            }
        }

    }

    if (isSuccessfulReg.value == true) {
        navController.navigate(
            Routes.SuccessfulRegScreen(
                regSuccess = "1"
            )
        )

    }

    Box(modifier = modifier.fillMaxSize()) {

        Image(
            modifier = Modifier
                .size(220.dp)
                .align(
                    alignment = Alignment.TopEnd
                ), painter = painterResource(id = R.drawable.ellipsetop), contentDescription = null
        )

        Image(
            modifier = Modifier
                .size(150.dp)

                .align(
                    alignment = Alignment.BottomStart
                ),
            painter = painterResource(id = R.drawable.ellipsebottom),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .systemBarsPadding()
                .padding(top = 25.dp, start = 30.dp, end = 30.dp)
        ) {

            Text(
                text = "Sign Up",
                modifier.padding(top = 80.dp),
                style = MaterialTheme.typography.headlineLarge,
            )

            Row(
                modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)

            ) {
                OutlinedTextField(modifier = modifier.weight(1f),
                    value = firstName.value,
                    onValueChange = {
                        firstName.value = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(20.dp),


                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color(0xFFEBF5FC),
                        unfocusedContainerColor = Color(0xFFEBF5FC),
                        focusedIndicatorColor = Color(0xFFF68B8B),
                        unfocusedIndicatorColor = Color(0xFFF68B8B),
                    ),
                    singleLine = true,
                    label = {
                        Text(
                            text = "First Name", color = Color.Gray
                        )
                    })

                Spacer(modifier = Modifier.width(10.dp))

                OutlinedTextField(modifier = modifier.weight(1f),
                    value = lastName.value,
                    onValueChange = {
                        lastName.value = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(20.dp),


                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color(0xFFEBF5FC),
                        unfocusedContainerColor = Color(0xFFEBF5FC),
                        focusedIndicatorColor = Color(0xFFF68B8B),
                        unfocusedIndicatorColor = Color(0xFFF68B8B),
                    ),
                    singleLine = true,
                    label = {
                        Text(
                            text = "Last Name", color = Color.Gray
                        )
                    })


            }

            OutlinedTextField(modifier = modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
                value = email.value,
                onValueChange = {
                    email.value = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(20.dp),


                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(0xFFEBF5FC),
                    unfocusedContainerColor = Color(0xFFEBF5FC),
                    focusedIndicatorColor = Color(0xFFF68B8B),
                    unfocusedIndicatorColor = Color(0xFFF68B8B),
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                singleLine = true,
                label = {
                    Text(
                        text = "Email", color = Color.Gray
                    )
                })

            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.Gray

                    )
                },
                shape = RoundedCornerShape(20.dp),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(0xFFEBF5FC),
                    unfocusedContainerColor = Color(0xFFEBF5FC),
                    focusedIndicatorColor = Color(0xFFF68B8B),
                    unfocusedIndicatorColor = Color(0xFFF68B8B),
                ),
                singleLine = true,
                label = {
                    Text(
                        text = "Create Password", color = Color.Gray

                    )
                },
            )

            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                value = confirmPassword.value,
                onValueChange = {
                    confirmPassword.value = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.Gray

                    )
                },
                shape = RoundedCornerShape(20.dp),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(0xFFEBF5FC),
                    unfocusedContainerColor = Color(0xFFEBF5FC),
                    focusedIndicatorColor = Color(0xFFF68B8B),
                    unfocusedIndicatorColor = Color(0xFFF68B8B),
                ),
                singleLine = true,
                label = {
                    Text(
                        text = "Confirm Password", color = Color.Gray

                    )
                },
            )


            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (firstName.value.isNotEmpty() && lastName.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty() && confirmPassword.value.isNotEmpty()

                    ) {
                        if (password.value == confirmPassword.value) {
                            val userData = UserDataModel(
                                firstName = firstName.value,
                                lastName = lastName.value,
                                email = email.value,
                                password = password.value
                            )
                            viewModel.createUser(userData)

                        }

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF68B8B),

                    )

            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account?", color = Color.Gray
                )
                Text(
                    modifier = modifier.clickable {
                        navController.popBackStack()

                    }, text = " Log In", fontWeight = FontWeight.Bold, color = Color(0xFFF68B8B)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .width(100.dp)
                        .background(Color.Black),
                )

                Text(
                    text = "    OR    ", fontWeight = FontWeight.Bold, color = Color(0xFFF68B8B)
                )

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .width(100.dp)
                        .background(Color.Black),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(
                        width = 1.5.dp, color = Color(0xFFF68B8B), shape = RoundedCornerShape(20.dp)
                    ), verticalAlignment = CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(start = 25.dp),
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null
                )
                Text(
                    text = "Log in with Google",
                    color = Color.Gray,

                    modifier = Modifier.padding(start = 50.dp),
                )
            }
        }


    }

}


