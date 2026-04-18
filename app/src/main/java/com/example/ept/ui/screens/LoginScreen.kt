package com.example.ept.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ept.ui.theme.*

// Simple hardcoded admin/hr/emp credentials – replace with real auth when ready
private const val ADMIN_USERNAME = "admin"
private const val ADMIN_PASSWORD = "admin123"
private const val HR_USERNAME = "hr"
private const val HR_PASSWORD = "hr123"
private const val EMP_USERNAME = "emp"
private const val EMP_PASSWORD = "emp123"

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isLoginMode by remember { mutableStateOf(true) }

    val roles = listOf("Admin", "HR", "Employee")
    var selectedRole by remember { mutableStateOf(roles[0]) }

    // Pulsing logo animation
    val logoScale = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        while (true) {
            logoScale.animateTo(1.08f, animationSpec = tween(900, easing = EaseInOut))
            logoScale.animateTo(1f, animationSpec = tween(900, easing = EaseInOut))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Indigo700, Indigo600, Indigo500)
                )
            )
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = (-60).dp, y = (-60).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.06f))
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 50.dp, y = 50.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.06f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Logo ──────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .scale(logoScale.value)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = "App Logo",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "EPTracker",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = "Smart Workforce Management",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))

            // ── Card ─────────────────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isLoginMode) "Welcome Back" else "Create Account",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground
                    )
                    Text(
                        text = if (isLoginMode) "Login to continue" else "Sign up to track performance",
                        fontSize = 13.sp,
                        color = Subtitle
                    )

                    // Role Selection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        roles.forEach { role ->
                            val isSelected = selectedRole == role
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Indigo600 else Indigo50)
                                    .clickable { selectedRole = role; errorMessage = "" }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = role,
                                    color = if (isSelected) Color.White else Indigo600,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Username
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it; errorMessage = "" },
                        label = { Text("Username") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Indigo600,
                            focusedLabelColor = Indigo600,
                            focusedLeadingIconColor = Indigo600
                        )
                    )

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = "" },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible)
                                        "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Indigo600,
                            focusedLabelColor = Indigo600,
                            focusedLeadingIconColor = Indigo600
                        )
                    )

                    // Confirm Password
                    if (!isLoginMode) {
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it; errorMessage = "" },
                            label = { Text("Confirm Password") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null)
                            },
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo600,
                                focusedLabelColor = Indigo600,
                                focusedLeadingIconColor = Indigo600
                            )
                        )
                    }

                    // Error
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Red600,
                            fontSize = 13.sp
                        )
                    }

                    // Login / Register Button
                    Button(
                        onClick = {
                            if (username.isBlank() || password.isBlank() || (!isLoginMode && confirmPassword.isBlank())) {
                                errorMessage = "Please fill in all fields."
                            } else if (!isLoginMode && password != confirmPassword) {
                                errorMessage = "Passwords do not match."
                            } else {
                                if (isLoginMode) {
                                    val isValid = when (selectedRole) {
                                        "Admin" -> username == ADMIN_USERNAME && password == ADMIN_PASSWORD
                                        "HR" -> username == HR_USERNAME && password == HR_PASSWORD
                                        "Employee" -> username == EMP_USERNAME && password == EMP_PASSWORD
                                        else -> false
                                    }

                                    if (isValid) {
                                        onLoginSuccess()
                                    } else {
                                        errorMessage = "Invalid credentials for $selectedRole"
                                    }
                                } else {
                                    // Handle registration
                                    isLoginMode = true
                                    errorMessage = "Account created! Please login."
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                    ) {
                        Text(
                            text = if (isLoginMode) "Login" else "Register",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    // Toggle Login/Register
                    TextButton(
                        onClick = {
                            isLoginMode = !isLoginMode
                            errorMessage = ""
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = if (isLoginMode) "Don't have an account? Sign Up" else "Already have an account? Login",
                            color = Indigo600,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
