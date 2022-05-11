package ty.learns.android.ui.first

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ty.learns.android.R
import ty.learns.android.database.FirstDB
import ty.learns.android.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var viewModel: FirstViewModel
    private lateinit var viewModelFactory: FirstViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(layoutInflater, container, false)
        viewModelFactory = FirstViewModelFactory("Hey, Android!", R.drawable.dice_1)
        viewModel = ViewModelProvider(this, viewModelFactory)[FirstViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupDice()

        return binding.root
    }

    private fun setupDice() {
        updateDice()
        binding.diceImage.setOnClickListener {
            val drawableImage = when((1..6).random()) {
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }
            viewModel.updateTapId(drawableImage)
            updateDice()
        }
        viewModel.userTapId.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, "You Tap Dice Image", Toast.LENGTH_SHORT).show()
        })
    }

    private fun updateDice() {
        viewModel.userTapId.value?.let { binding.diceImage.setImageResource(it) }
    }
}