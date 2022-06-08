package com.myplaygroup.app.feature_main.presentation.admin.users.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme

@Composable
fun ShowAlertDialog(
    createErrorMessage: String?,
    createUser: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .width(300.dp)
            .padding(5.dp),
        shape = RoundedCornerShape(5.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding(5.dp))

            Text(
                text = "Create User",
                style = MaterialTheme.typography.h4
            )

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_person_pin_24),
                tint = MaterialTheme.colors.primary,
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.padding(10.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Username") },
                placeholder = { Text(text = "Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                isError = createErrorMessage != null
            )

            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(25.dp)
            ) {
                if(createErrorMessage != null){
                    Text(
                        text = createErrorMessage,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.TopCenter)
                    )
                }
            }

            Button(
                onClick = {
                    createUser(username)
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(60.dp)
                    .padding(10.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary),
            ) {
                Text(
                    text = "Create",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun ShowAlertDialogPreview2() {
    MyPlaygroupTheme {
        ShowAlertDialog("Username is not valid"){

        }
    }
}
