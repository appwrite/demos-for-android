package io.appwrite.tutorialforandroid.services

import android.content.Context
import io.appwrite.Client

object Appwrite {
    private const val ENDPOINT = "https://cloud.appwrite.io/v1"
    private const val PROJECT_ID = "650870066ca28da3d330"

    private lateinit var client: Client
    internal lateinit var ideas: IdeaService
    internal lateinit var users: UserService

    fun init(context: Context) {
        client = Client(context)
            .setEndpoint(ENDPOINT)
            .setProject(PROJECT_ID)

        ideas = IdeaService(client)
        users = UserService(client)
    }
}