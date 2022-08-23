package com.android.kotlinmvvmtodolist.ui.task

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.kotlinmvvmtodolist.R
import com.android.kotlinmvvmtodolist.databinding.FragmentTaskBinding
import com.android.kotlinmvvmtodolist.util.NetworkUtil
import com.android.kotlinmvvmtodolist.viewmodel.TaskViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar


class TaskFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter
    lateinit var binding: FragmentTaskBinding
    private lateinit var collapsedMenu: Menu
    private var appBarExpanded = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.setTheme(R.style.Theme_KotlinMVVMToDoList);

        binding = FragmentTaskBinding.inflate(inflater)
        start()
        setObserveAndVM()
        setAdapter()
        fabClick()
        swiped()
        colapsingSettings()
        setActionBarProperties()
        appbarExpanded()
        setHasOptionsMenu(true)
        visibilityClick()
        hideKeyboard(requireActivity())

        viewModel.getList()
        return binding.root
    }
    fun start(){
        if (!NetworkUtil.getConnectivityStatus(context = requireActivity())) {
            Snackbar.make(
                binding.root, "Нет связи с cервером будет показана локальная " +
                        "инфомация", Snackbar.LENGTH_LONG
            ).apply {
                setAction("ok") {

                }
                show()
            }
        }
    }

    private fun visibilityClick() {
        binding.visibilityBtn.setOnClickListener {
            changeVisibility()
        }
    }

    private fun changeVisibility() {

        viewModel.visibility = !viewModel.visibility
        if (!viewModel.visibility) {

            setNoVisibility()
        } else {

            setVisibility()
        }
    }

    private fun setVisibility() =
        binding.visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_off_24)

    private fun setNoVisibility() =
        binding.visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_24)

    private fun changeMenuVisibility() {
        if (viewModel.visibility) {
            collapsedMenu.add("visibility")
                .setIcon(R.drawable.ic_baseline_visibility_off_24)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        } else {
            collapsedMenu.add("visibility")
                .setIcon(R.drawable.ic_baseline_visibility_24)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
    }

    private fun setActionBarProperties() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.animToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(false);
    }

    private fun appbarExpanded() {
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            if (Math.abs(verticalOffset) > 200) {
                appBarExpanded = false
                activity?.invalidateOptionsMenu()
            } else {
                appBarExpanded = true
                activity?.invalidateOptionsMenu()
            }

        })

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (!appBarExpanded || collapsedMenu.size() != 0
        ) {
            changeMenuVisibility()
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        collapsedMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title === "visibility") {
            changeVisibility()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun colapsingSettings() {
        binding.collapsingToolbar.setCollapsedTitleTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.black
            )
        )
        binding.collapsingToolbar.setExpandedTitleColor(
            ContextCompat.getColor(
                requireActivity(),
                com.google.android.material.R.color.mtrl_btn_transparent_bg_color
            )
        )
        binding.collapsingToolbar.expandedTitleMarginStart = 16
        binding.collapsingToolbar.title = getString(R.string.my_deal)
    }

    private fun setObserveAndVM() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getAllDoneTasks.observe(viewLifecycleOwner) {
            binding.performed.text = getString( R.string.performed) + it.size
        }
        viewModel.getAllTasks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            viewModel.deleteRepeat()
            viewModel.patchList(it)
        }
    }

    private fun setAdapter() {
        adapter = TaskAdapter(TaskClickListener { taskEntry ->

            findNavController().navigate(
                TaskFragmentDirections.actionTaskFragmentToUpdateFragment(
                    taskEntry
                )
            )
        })
        binding.recyclerView.adapter = adapter
    }

    private fun fabClick() {
        binding.fab.setOnClickListener {
            startRotate(binding.fab)
        }
    }

    private fun startRotate(view: View) {
        // подключаем файл анимации
       var mEnlargeAnimation = AnimationUtils.loadAnimation(activity, R.anim.enlarge);
        // при запуске начинаем с эффекта увеличения
        binding.fab.startAnimation(mEnlargeAnimation);
        val rotateAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        rotateAnimator.duration = 500
        rotateAnimator.start()
        rotateAnimator.doOnEnd { findNavController().navigate(R.id.action_taskFragment_to_addFragment) }
    }

    private fun swiped() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskEntry = adapter.currentList[position]
                viewModel.delete(taskEntry)
                Snackbar.make(binding.root, "Deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.insert(taskEntry)
                    }
                    show()
                }
            }
        }).attachToRecyclerView(binding.recyclerView)
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        currentFocusedView.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

    }

}