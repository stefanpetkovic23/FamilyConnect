package com.example.familyconnectv2.sp

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FCMNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Obrada pristigle FCM poruke
        Log.d(TAG, "Pristigla FCM poruka: " + remoteMessage.data)

        // Implementirajte vašu logiku za prikazivanje notifikacije ili izvršavanje određenih radnji
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Obrada novog uređajskog tokena
        Log.d(TAG, "Novi uređajski token: $token")

        // Implementirajte svoju logiku za ažuriranje uređajskog tokena na serveru ili druge radnje
    }

    companion object {
        private const val TAG = "FCMNotificationService"
    }
}