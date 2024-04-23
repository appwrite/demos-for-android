package io.appwrite.tutorialforandroid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.appwrite.models.Document
import io.appwrite.models.User
import io.appwrite.tutorialforandroid.services.IdeaService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeasScreen(
    user: User<Map<String, Any>>?,
    ideasService: IdeaService
) {
    var ideas by remember { mutableStateOf<List<Document<Map<String, Any>>>>(listOf()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(ideasService) {
        coroutineScope.launch {
            ideas = ideasService.fetch()
        }
    }

    fun onSubmit(title: String, description: String) {
        if (user === null) return
        coroutineScope.launch {
            ideas = ideas.plus(ideasService.add(user.id, title, description))
        }
    }

    fun onRemove(ideaId: String) {
        coroutineScope.launch {
            ideas = ideas.filter { idea -> idea.id !== ideaId }
            ideasService.remove(ideaId)
        }
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user != null) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Button(onClick = {
                onSubmit(title, description)
                title = ""
                description = ""
            }) {
                Text("Submit")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(ideas) { idea ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = idea.data["title"]?.toString() ?: "", fontWeight = FontWeight(700))
                    Text(text = idea.data["description"]?.toString() ?: "")
                    if (user?.id === idea.data["userId"])
                        Button(onClick = { onRemove(idea.id) }) {
                            Text("Remove")
                        }
                }
            }
        }
    }
}

