package com.missapps.brabet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.missapps.brabet.core.viewmodel.ViewModelFactory
import com.missapps.brabet.firebase.FirebaseRemoteConfigsRepository
import com.missapps.brabet.navigation.Navigation
import com.missapps.brabet.ui.backgroundColor
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var firebaseRemoteConfigsRepository: FirebaseRemoteConfigsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
                Navigation(
                    viewModelFactory = viewModelFactory,
                    url = firebaseRemoteConfigsRepository.url
                )
            }
        }
    }
}
