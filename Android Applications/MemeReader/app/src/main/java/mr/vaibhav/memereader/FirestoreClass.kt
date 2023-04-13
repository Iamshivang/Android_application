package mr.vaibhav.memereader

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass{

    private val mFireStore= FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener { e-> Log.e(activity.javaClass.simpleName, "Error writing document", e)
            Toast.makeText(activity, "Registation Failed", Toast.LENGTH_SHORT).show()}
    }


    fun signInUser(activity: SignInActivity){

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {document ->

                Log.e(activity.javaClass.simpleName, document.toString())

                val loggedInUser= document.toObject(User::class.java)
                if (loggedInUser != null) {
                    activity.signedInSuccess(loggedInUser)
                }
            }.addOnFailureListener { e-> Log.e(activity.javaClass.simpleName, "Error while getting loggedIn user details", e)
                activity.signedInFailed()}
    }

    fun getCurrentUserID(): String{

        val currentUser= FirebaseAuth.getInstance().currentUser
        var currentUserID: String= ""
        if(currentUser!= null)
        {
            currentUserID= currentUser.uid
        }
        return currentUserID
    }
}