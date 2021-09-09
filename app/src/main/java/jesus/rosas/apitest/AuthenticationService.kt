package jesus.rosas.apitest

import android.app.Activity
import androidx.annotation.Keep
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthenticationService (private val activity: Activity) {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    init {
        initGoogleAuthentication()
    }
    fun initGoogleAuthentication() {
        val idToken = activity.getString(R.string.id_token)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(idToken)
            .build()
        var googleSignInClient = GoogleSignIn.getClient(activity, gso);
    }
    suspend fun signInWithGoogle(task: Task<GoogleSignInAccount>): Result<FirebaseUser> {
        return try {
            val account: GoogleSignInAccount =
                task.getResult(ApiException::class.java)
            val credential =
                GoogleAuthProvider.getCredential(account.idToken, null)
            val result =
                firebaseAuth.signInWithCredential(credential).await()
            Result.Success(result.user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

sealed class Result<out R> {
    @Keep
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}