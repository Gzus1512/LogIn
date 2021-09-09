package jesus.rosas.apitest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    viewModel.signInWithGoogle.observe(viewLifecycleOwner, {
        it?.let {
            if (it) {
                val signInIntent = authenticationService.googleSignInClient.signInIntent
                googleLoginLauncher.launch(signInIntent)
            }
        }
    })

    private var googleLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            lifecycleScope.launch(Dispatchers.IO) {
                val result = authenticationService.signInWithGoogle(task)
                viewModel.doOnSignInWithGoogle(result)
            }
        } catch (e: Exception) {
            Log.e(TAG, e)
        }
    }
}