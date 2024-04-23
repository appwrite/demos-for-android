package io.appwrite.tutorialforandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.appwrite.models.User
import io.appwrite.tutorialforandroid.services.Appwrite
import io.appwrite.tutorialforandroid.services.IdeaService
import io.appwrite.tutorialforandroid.ui.screens.IdeasScreen
import io.appwrite.tutorialforandroid.ui.screens.UserScreen
import io.appwrite.tutorialforandroid.services.UserService
import io.appwrite.tutorialforandroid.ui.theme.TutorialForAndroidTheme

enum class Screen {
    User,
    Ideas
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Appwrite.init(applicationContext)

        setContent {
            TutorialForAndroidTheme {
                AppContent(Appwrite.users, Appwrite.ideas)
            }
        }
    }
}

@Composable
private fun AppBottomBar(screen: MutableState<Screen>) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { screen.value = Screen.Ideas }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.List, contentDescription = "Ideas")
                    Text("Ideas")
                }
            }
            IconButton(onClick = { screen.value = Screen.User }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Person, contentDescription = "User")
                    Text("User")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppContent(userService: UserService, ideasService: IdeaService) {
    val user = remember { mutableStateOf<User<Map<String, Any>>?>(null) }
    val screen = remember { mutableStateOf(Screen.Ideas) }

    LaunchedEffect(screen) {
        user.value = userService.getLoggedIn()
    }

    Scaffold(bottomBar = { AppBottomBar(screen) }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (screen.value) {
                Screen.User -> UserScreen(user, userService)
                else -> IdeasScreen(user.value, ideasService)
            }
        }
    }
}