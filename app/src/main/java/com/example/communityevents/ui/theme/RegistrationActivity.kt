
package com.example.communityevents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val Forest = Color(0xFF19463F)
private val Terracotta = Color(0xFFE05B36)
private val Cream = Color(0xFFFFFCF7)
private val Muted = Color(0xFF758780)
private val Hint = Color(0xFF98A69F)
private val BorderGreen = Color(0xFFCBD8D1)
private val TabBackground = Color(0xFFE9EFEB)

@Composable
fun RegisterScreen(
    onSignInClick: () -> Unit = {}
) {
    var selectedMethod by remember { mutableStateOf("Email") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFE7ECE7)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 560.dp)
                    .fillMaxHeight()
                    .background(
                        Cream,
                        RoundedCornerShape(
                            topStart = 30.dp,
                            topEnd = 30.dp,
                            bottomStart = 30.dp,
                            bottomEnd = 30.dp
                        )
                    )
                    .border(
                        1.dp,
                        BorderGreen.copy(alpha = 0.65f),
                        RoundedCornerShape(30.dp)
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 28.dp, vertical = 20.dp)
            ) {
                // Brand header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(
                                Forest,
                                RoundedCornerShape(17.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Groups,
                            contentDescription = "Community",
                            tint = Color.White,
                            modifier = Modifier.size(27.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "neighbourly",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Forest,
                            letterSpacing = (-0.7).sp
                        )
                        Text(
                            text = "COMMUNITY EVENTS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Muted,
                            letterSpacing = 1.5.sp
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                Text(
                    text = "JOIN THE COMMUNITY",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Terracotta,
                    letterSpacing = 1.5.sp
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = "Create your account",
                    fontSize = 37.sp,
                    lineHeight = 43.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1.4).sp,
                    color = Forest
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Find local events and connect with your neighbours.",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Muted
                )

                Spacer(Modifier.height(28.dp))

                // Email / Phone selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            TabBackground,
                            RoundedCornerShape(17.dp)
                        )
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Email", "Phone number").forEach { method ->
                        val selected = selectedMethod == method

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .background(
                                    if (selected) Color.White
                                    else Color.Transparent,
                                    RoundedCornerShape(13.dp)
                                )
                                .clickable {
                                    selectedMethod = method
                                    errorMessage = ""
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = method,
                                color = if (selected) Forest else Muted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Name field
                RegisterField(
                    label = "Full name",
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = "Your first and last name",
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null
                        )
                    }
                )

                Spacer(Modifier.height(20.dp))

                // Email or phone field
                RegisterField(
                    label = if (selectedMethod == "Email")
                        "Email address" else "Phone number",
                    value = if (selectedMethod == "Email") email else phone,
                    onValueChange = {
                        if (selectedMethod == "Email") email = it
                        else phone = it
                    },
                    placeholder = if (selectedMethod == "Email")
                        "you@example.com" else "Enter your phone number",
                    keyboardType = if (selectedMethod == "Email")
                        KeyboardType.Email else KeyboardType.Phone,
                    leadingIcon = {
                        Icon(
                            if (selectedMethod == "Email")
                                Icons.Outlined.Email
                            else Icons.Outlined.Person,
                            contentDescription = null
                        )
                    }
                )

                Spacer(Modifier.height(20.dp))

                // Password field
                RegisterField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "At least 8 characters",
                    keyboardType = KeyboardType.Password,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Lock,
                            contentDescription = null
                        )
                    },
                    passwordField = true,
                    passwordVisible = passwordVisible,
                    onVisibilityClick = {
                        passwordVisible = !passwordVisible
                    }
                )

                Spacer(Modifier.height(20.dp))

                // Confirm password field
                RegisterField(
                    label = "Confirm password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Enter your password again",
                    keyboardType = KeyboardType.Password,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Lock,
                            contentDescription = null
                        )
                    },
                    passwordField = true,
                    passwordVisible = confirmVisible,
                    onVisibilityClick = {
                        confirmVisible = !confirmVisible
                    }
                )

                Spacer(Modifier.height(18.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Terracotta,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                // Front-end form validation only
                Button(
                    onClick = {
                        errorMessage = when {
                            fullName.isBlank() ->
                                "Please enter your full name."
                            selectedMethod == "Email" &&
                                    email.isBlank() ->
                                "Please enter your email address."
                            selectedMethod == "Email" &&
                                    !android.util.Patterns.EMAIL_ADDRESS
                                        .matcher(email.trim()).matches() ->
                                "Please enter a valid email address."
                            selectedMethod == "Phone number" &&
                                    phone.isBlank() ->
                                "Please enter your phone number."
                            password.length < 8 ->
                                "Password must be at least 8 characters."
                            password != confirmPassword ->
                                "Your passwords do not match."
                            else ->
                                "Your details look good! Registration is not connected yet."
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    shape = RoundedCornerShape(17.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Terracotta,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Create account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Sign-in navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Already have an account? ",
                        fontSize = 14.sp,
                        color = Muted
                    )
                    Text(
                        text = "Sign in",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Terracotta,
                        modifier = Modifier.clickable {
                            onSignInClick()
                        }
                    )
                }

                Spacer(Modifier.height(22.dp))

                Text(
                    text = "By continuing, you agree to our Terms and Privacy Policy.",
                    fontSize = 11.sp,
                    color = Muted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    passwordField: Boolean = false,
    passwordVisible: Boolean = false,
    onVisibilityClick: () -> Unit = {}
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF315750)
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 62.dp),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Hint,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            leadingIcon = {
                Box(
                    modifier = Modifier.padding(start = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides Muted
                    ) {
                        leadingIcon()
                    }
                }
            },
            trailingIcon = if (passwordField) {
                {
                    IconButton(onClick = onVisibilityClick) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Outlined.Visibility
                            else Icons.Outlined.VisibilityOff,
                            contentDescription = if (passwordVisible)
                                "Hide password" else "Show password",
                            tint = Muted
                        )
                    }
                }
            } else null,
            visualTransformation = if (passwordField && !passwordVisible)
                PasswordVisualTransformation()
            else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BorderGreen,
                unfocusedBorderColor = BorderGreen,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Forest,
                unfocusedTextColor = Forest,
                cursorColor = Forest
            )
        )
    }
}
