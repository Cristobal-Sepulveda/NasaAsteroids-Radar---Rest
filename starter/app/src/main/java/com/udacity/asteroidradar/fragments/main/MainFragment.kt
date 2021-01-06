package com.udacity.asteroidradar.fragments.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.Adapter.AsteroidAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.network.Asteroid

class MainFragment : Fragment() {
    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onViewCreated(), which we
     * do in this Fragment.
     */
    /*private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, MainViewModelFactory(activity.application))
                .get(MainViewModel::class.java)
    }*/
    lateinit var viewModel: MainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application
        val viewModelFactory = MainViewModelFactory(application)
        viewModel= ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.asteroidRecycler.adapter = AsteroidAdapter(
                AsteroidAdapter.OnClickListener{
                viewModel.displayAsteroidDetails(it)
                })
        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if (null != it){
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsComplete()
            }
        })
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_today_asteroids ->
                viewModel.todayAsteroids.observe(viewLifecycleOwner, Observer {
                    viewModel.domainAsteroidsInScreen.value = it
                })

            R.id.show_week_asteroids ->
                viewModel.weekAsteroids.observe(viewLifecycleOwner, Observer {
                    viewModel.domainAsteroidsInScreen.value = it
                })
        }
        return true
    }
}


