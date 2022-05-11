package ty.learns.android

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class HomeTableFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home_list, container, false)
        (view as RecyclerView).adapter = HomeRecyclerViewAdapter()
        return view
    }

    private fun showMenu() {
        (activity as HomeActivity).showMenu()
    }

    private fun hideShow() {
        (activity as HomeActivity).hideMenu()
    }

    // 生命周期
    // https://developer.android.com/codelabs/kotlin-android-training-lifecycles-logging?index=..%2F..android-kotlin-fundamentals#5
    override fun onStart() {
        super.onStart()
        showMenu()
    }

    override fun onStop() {
        super.onStop()
        hideShow()
    }
}