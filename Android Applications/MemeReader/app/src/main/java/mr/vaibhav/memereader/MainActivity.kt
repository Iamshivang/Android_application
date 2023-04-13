package mr.vaibhav.memereader

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.activity_main.*
import mr.vaibhav.memereader.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var currentImageUrl :String ?= null
    val url : String = "https://meme-api.com/gimme"
    private var progressBar: ProgressBar? = null

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val typeface: Typeface =
            Typeface.createFromAsset(assets, "Sweets Smile.ttf")
        NextButton.typeface = typeface
        ShareButton.typeface= typeface

        progressBar = findViewById<ProgressBar>(R.id.progressBar) as ProgressBar

        loadMeme()

    }

    private fun loadMeme(){

        progressBar?.visibility = View.VISIBLE
//        var progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Please wait while MEME is Fetching")
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,url,
            {
                response ->


                Log.e("ResponseToMayank" , "loadMeme: "+ response.toString())
                var responseObject = JSONObject(response)
//                responseObject.get("url")
            //  binding.memeImageView
                Glide.with(this).load( responseObject.get("url")).into(binding.memeImageView)
//               progressDialog.dismiss()
                progressBar?.visibility = View.INVISIBLE
               currentImageUrl=  responseObject.getString("url")


//             responseObject.get("title")
//             responseObject.get("author")
//             responseObject.get("upvotes")
            },
            {
                error ->
//                progressDialog.dismiss()
                progressBar?.visibility = View.INVISIBLE
                Toast.makeText(this , "$(error.localizedMessage)" , Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(stringRequest)

    }

    fun shareMeme(view: View) {
        val intent= Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme:->\n\n $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share this meme")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.action_refresh ->{
                Constants.FLAG= false
                startActivity(Intent(this@MainActivity, IntroActivity::class.java))
                this@MainActivity.finish()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}