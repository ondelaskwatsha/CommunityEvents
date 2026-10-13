
package com.example.communityevents

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.communityevents.ui.theme.CommunityEventsTheme

private val PageBackground = Color(0xFFF1F3ED)
private val CardBackground = Color(0xFFFFFCF7)
private val DarkGreen = Color(0xFF19443F)
private val MutedGreen = Color(0xFF758780)
private val BorderGreen = Color(0xFFCAD8D1)
private val Orange = Color(0xFFDE5B38)
private val LabelGreen = Color(0xFF31544E)
private val HintGreen = Color(0xFF98A6A0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            CommunityEventsTheme {
                var currentScreen by rememberSaveable {
                    mutableStateOf("login")
                }

                when (currentScreen) {
                    "login" -> {
                        SignInScreen(
                            onSignIn = { email, password ->
                                val identifier = email.trim()

                                val isEmail = identifier.contains("@") &&
                                        identifier.substringAfter("@").contains(".")

                                val isPhone =
                                    identifier.count { it.isDigit() } >= 8 &&
                                            identifier.all {
                                                it.isDigit() || it == '+' ||
                                                        it == ' ' || it == '-' ||
                                                        it == '(' || it == ')'
                                            }

                                when {
                                    identifier.isBlank() || password.isBlank() -> {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Please enter your email or phone number and password.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    !isEmail && !isPhone -> {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Please enter a valid email address or phone number.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    else -> {
                                        // Front-end navigation for now
                                        currentScreen = "explore"
                                    }
                                }
                            },
                            onGoogleSignIn = {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Google sign-in will be connected later.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onForgotPassword = {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Password recovery will be added later.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onCreateAccount = {
                                currentScreen = "register"
                            }
                        )
                    }

                    "register" -> {
                        RegisterScreen(
                            onSignInClick = {
                                currentScreen = "login"
                            }
                        )
                    }

                    "explore" -> {
                        ExploreScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun SignInScreen(
    onSignIn: (String, String) -> Unit,
    onGoogleSignIn: () -> Unit,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFFDCE2DA),
                    shape = RoundedCornerShape(26.dp)
                )
                .background(
                    color = CardBackground,
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(
                    start = 27.dp,
                    end = 27.dp,
                    top = 15.dp,
                    bottom = 24.dp
                )
        ) {
            // Brand logo and app name
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(
                            DarkGreen,
                            RoundedCornerShape(17.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PeopleAlt,
                        contentDescription = "Community",
                        tint = Color.White,
                        modifier = Modifier.size(27.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "neighbourly",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = DarkGreen,
                        lineHeight = 28.sp
                    )

                    Text(
                        text = "COMMUNITY EVENTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = MutedGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(33.dp))

            // Welcome heading
            Text(
                text = "WELCOME BACK",
                color = Orange,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.6.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Sign in to continue",
                color = DarkGreen,
                fontSize = 39.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                lineHeight = 45.sp,
                letterSpacing = (-1.2).sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Discover what’s happening around you.",
                color = MutedGreen,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(29.dp))

            // Google sign-in button (visual frontend only)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .clickable { onGoogleSignIn() },
                shape = RoundedCornerShape(17.dp),
                color = Color.White,
                border = BorderStroke(1.dp, BorderGreen)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GoogleLetter()

                    Spacer(modifier = Modifier.width(13.dp))

                    Text(
                        text = "Continue with Google",
                        color = DarkGreen,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider with text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFDCE2DA)
                )

                Text(
                    text = "  or sign in with your details  ",
                    color = MutedGreen,
                    fontSize = 12.sp
                )

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFDCE2DA)
                )
            }

            Spacer(modifier = Modifier.height(29.dp))

            // Email label
            Text(
                text = "Email or phone number",
                color = LabelGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(9.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                placeholder = {
                    Text(
                        text = "you@example.com or +44...",
                        color = HintGreen,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email",
                        tint = MutedGreen
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(16.dp),
                colors = inputFieldColors()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password label and forgot password
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Password",
                    color = LabelGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Forgot password?",
                    color = Orange,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onForgotPassword()
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                placeholder = {
                    Text(
                        text = "Enter your password",
                        color = HintGreen,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Password",
                        tint = MutedGreen
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Outlined.Visibility
                        } else {
                            Icons.Outlined.VisibilityOff
                        },
                        contentDescription = if (passwordVisible) {
                            "Hide password"
                        } else {
                            "Show password"
                        },
                        tint = MutedGreen,
                        modifier = Modifier.clickable {
                            passwordVisible = !passwordVisible
                        }
                    )
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                shape = RoundedCornerShape(16.dp),
                colors = inputFieldColors()
            )

            Spacer(modifier = Modifier.height(19.dp))

            // Main sign-in button
            Button(
                onClick = { onSignIn(email.trim(), password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                shape = RoundedCornerShape(17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Text(
                    text = "Sign in",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Registration link
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCreateAccount() },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New to Neighbourly? ",
                    color = MutedGreen,
                    fontSize = 14.sp
                )

                Text(
                    text = "Create an account",
                    color = Orange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun inputFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = DarkGreen,
        unfocusedTextColor = DarkGreen,
        focusedBorderColor = BorderGreen,
        unfocusedBorderColor = BorderGreen,
        cursorColor = Orange,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedLeadingIconColor = MutedGreen,
        unfocusedLeadingIconColor = MutedGreen
    )

@Composable
private fun GoogleLetter() {
    // A simple Google-style G placeholder for the frontend.
    Text(
        text = "G",
        fontSize = 25.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFF4285F4)
    )
}
