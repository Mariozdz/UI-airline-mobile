package una.moviles.textviewadapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:text")
fun setInteger(view: TextView, src: Double) {
    view.text = src.toString()
}

@BindingAdapter("android:src")
fun setImageResource(view: ImageView, src: Int) {
    view.setImageResource(src)
}

@BindingAdapter("android:text")
fun setInteger(view: TextView, src: Int) {
    view.text = src.toString()
}