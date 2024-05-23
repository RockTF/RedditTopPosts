import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reddittopposts.R
import com.example.reddittopposts.model.RedditPost
import java.util.concurrent.TimeUnit

class RedditPostAdapter(
    private val context: Context,
    private var posts: List<RedditPost>
) : RecyclerView.Adapter<RedditPostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val authorTextView: TextView = itemView.findViewById(R.id.author)
        private val postDateTextView: TextView = itemView.findViewById(R.id.post_date)
        private val thumbnailImageView: ImageView = itemView.findViewById(R.id.thumbnail)
        private val numCommentsTextView: TextView = itemView.findViewById(R.id.num_comments)

        @SuppressLint("SetTextI18n")
        fun bind(post: RedditPost) {
            authorTextView.text = post.author
            postDateTextView.text = getTimeAgo(post.createdUtc)
            numCommentsTextView.text = "${post.numComments} comments"

            if ((post.thumbnail?.isNotEmpty() == true) && post.thumbnail.startsWith("http")) {
                Glide.with(context).load(post.thumbnail).into(thumbnailImageView)
                thumbnailImageView.visibility = View.VISIBLE
                thumbnailImageView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.url))
                    context.startActivity(intent)
                }
            } else {
                thumbnailImageView.visibility = View.GONE
            }
        }
    }

    private fun getTimeAgo(time: Long): String {
        val now = System.currentTimeMillis() / 1000
        val diff = now - time
        return when {
            diff < 60 -> "Just now"
            diff < 3600 -> "${TimeUnit.SECONDS.toMinutes(diff)} minutes ago"
            diff < 86400 -> "${TimeUnit.SECONDS.toHours(diff)} hours ago"
            else -> "${TimeUnit.SECONDS.toDays(diff)} days ago"
        }
    }
}
