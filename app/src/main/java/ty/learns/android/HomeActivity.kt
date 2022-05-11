package ty.learns.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ty.learns.android.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.homeNavHostFragment)

        setupNavBar()
        setupMenu()
    }

    private fun setupNavBar() {
        // 如果设置 binding.topAppBar.setNavigationOnClickListener，会导致导航上层无效，因为覆盖
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupMenu() {
        binding.topAppBar.inflateMenu(R.menu.activity_home)

        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.act_home_favorite -> {
                    navController.navigate(R.id.action_contentFragment_to_firstFragment)
                    true
                }
                R.id.act_home_about -> {
                    navController.navigate(R.id.action_contentFragment_to_blankFragment)
                    true
                }
                else -> false
            }
        }
    }

    // 在这里，我用生命周期的方法显示和隐藏
    // 这样做虽然特殊性，但从需求的角度，是可以的，此页面始终完全不可见。
    fun showMenu() {
        // 采用分组的形式进行显示和隐藏
        binding.topAppBar.menu.setGroupVisible(R.id.menu_group_home, true)
    }

    fun hideMenu() {
        binding.topAppBar.menu.setGroupVisible(R.id.menu_group_home, false)
    }
}