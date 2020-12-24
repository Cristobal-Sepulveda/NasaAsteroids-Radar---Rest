package com.udacity.asteroidradar.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.TextItemViewBinding
import com.udacity.asteroidradar.network.Asteroid

class AsteroidAdapter: ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallBack){
    class AsteroidViewHolder(private var binding: TextItemViewBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid){
            binding.asteroidItem = asteroid
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }

            }

    companion object DiffCallBack: DiffUtil.ItemCallback<Asteroid>(){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidAdapter.AsteroidViewHolder {
        return AsteroidViewHolder(TextItemViewBinding.inflate(LayoutInflater.from(parent.context)))
    }
}
