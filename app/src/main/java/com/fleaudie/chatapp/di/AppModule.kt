package com.fleaudie.chatapp.di

import android.content.Context
import com.fleaudie.chatapp.data.datasource.AuthDataSource
import com.fleaudie.chatapp.data.datasource.ChatDataSource
import com.fleaudie.chatapp.data.datasource.UserProfileDataSource
import com.fleaudie.chatapp.data.repository.AuthRepository
import com.fleaudie.chatapp.data.repository.ChatRepository
import com.fleaudie.chatapp.data.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun providePhoneAuthProvider(): PhoneAuthProvider {
        return PhoneAuthProvider.getInstance()
    }
    @Provides
    fun provideAuthDataSource(@ApplicationContext context: Context) : AuthDataSource{
        return AuthDataSource(context)
    }
    @Provides
    fun provideAuthRepository(dataSource: AuthDataSource): AuthRepository {
        return AuthRepository(dataSource)
    }
    @Provides
    fun provideChatDataSource(): ChatDataSource {
        return ChatDataSource()
    }
    @Provides
    fun provideChatRepository(dataSource: ChatDataSource): ChatRepository {
        return ChatRepository(dataSource)
    }
    @Provides
    fun provideUserProfileDataSource(): UserProfileDataSource {
        return UserProfileDataSource()
    }
    @Provides
    fun provideUserProfileRepository(dataSource: UserProfileDataSource): UserProfileRepository {
        return UserProfileRepository(dataSource)
    }
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
}