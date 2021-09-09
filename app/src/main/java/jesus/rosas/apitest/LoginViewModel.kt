package jesus.rosas.apitest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(
    private val authenticationService: AuthenticationService,
) : ViewModel() {
    private val _signInWithGoogle = MutableLiveData<Boolean>()
    val signInWithGoogle: LiveData<Boolean>
        get() = _signInWithGoogle

    fun signInWithGoogle() {
        _signInWithGoogle.value = true
    }

    fun doOnSignInWithGoogle(result: Result<FirebaseUser>) {
        if (result is Result.Success) {
            // navigate to main page
        } else {
            // your error handling
        }
    }
}