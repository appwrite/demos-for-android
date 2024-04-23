package io.appwrite.tutorialforandroid.services

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.models.Document
import io.appwrite.services.Databases

class IdeaService(client: Client) {
    private val ideaDatabaseId = "6508783c5dc784d544dd"
    private val ideaCollectionId = "65087840ab307cb06883"
    private val databases = Databases(client)

    suspend fun fetch(): List<Document<Map<String, Any>>> {
        return databases.listDocuments(
            ideaDatabaseId,
            ideaCollectionId,
            listOf(Query.limit(10))
        ).documents
    }

    suspend fun add(
        userId: String,
        title: String,
        description: String
    ): Document<Map<String, Any>> {
        return databases.createDocument(
            ideaDatabaseId,
            ideaCollectionId,
            ID.unique(),
            mapOf(
                "userId" to userId,
                "title" to title,
                "description" to description
            )
        )
    }

    suspend fun remove(id: String) {
        databases.deleteDocument(
            ideaDatabaseId,
            ideaCollectionId,
            id
        )
    }
}