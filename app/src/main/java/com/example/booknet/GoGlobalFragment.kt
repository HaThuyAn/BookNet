package com.example.booknet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GoGlobalFragment : Fragment(), OnUserNameClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var goGlobal: Button
    private lateinit var goGlobalAdapter: GoGlobalAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goGlobal = view.findViewById(R.id.go_global)
        goGlobal.setOnClickListener {
            findNavController().navigate(R.id.action_nav_go_global_to_publishPostFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_go_global, container, false)

        goGlobalAdapter = GoGlobalAdapter()
        goGlobalAdapter.setOnUsernameClickListener(this)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = goGlobalAdapter
        return view
    }

    override fun onUsernameClick(position: Int) {
        findNavController().navigate(R.id.action_nav_go_global_to_postCollectionFragment)
    }
}