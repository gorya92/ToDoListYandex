package com.example.todolistyandex.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bignerdranch.android.testing.network.NetworkUtil
import com.bignerdranch.android.testing.retrofitConnect.RetrofitConstants
import com.bignerdranch.android.testing.retrofitConnect.RetrofitViewModel
import com.bignerdranch.android.testing.retrofitConnect.repository.Repository
import com.example.todolistyandex.MyWorker
import com.example.todolistyandex.R
import com.example.todolistyandex.databinding.FragmentToDoListBinding
import com.example.todolistyandex.views.TodoListView
import com.example.todolistyandex.model.TodoItemRepository
import com.example.todolistyandex.presenters.TodoListPresenter
import com.example.todolistyandex.retrofitConnect.repository.RetrofitViewModelFactory
import com.example.yandextask.adapter.TodoitemAdapter
import com.example.yandextask.model.TodoItem
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class ToDoListFragment : MvpAppCompatFragment(), TodoListView {

    private var appBarExpanded = true

    val bundle = Bundle()

    private lateinit var viewBinding: FragmentToDoListBinding


    private lateinit var collapsedMenu: Menu

    var dessertAdapter: TodoitemAdapter = TodoitemAdapter();

    @InjectPresenter()
    lateinit var todoListPresenter: TodoListPresenter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        requireActivity().setTheme(R.style.Theme_YandexTask);
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_to_do_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding = FragmentToDoListBinding.bind(view)
        todoListPresenter.start = arguments?.getBoolean("start")!!

        var retrofitViewModel: RetrofitViewModel

        /** Retrofit connection to project **/
        val repository = Repository()
        val viewModelFactory = RetrofitViewModelFactory(repository)
        retrofitViewModel = ViewModelProvider(this, viewModelFactory)[RetrofitViewModel::class.java]


        /** Добавление workManagera **/
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myWorkRequest =
            PeriodicWorkRequestBuilder<MyWorker>(480, TimeUnit.MINUTES, 25, TimeUnit.MINUTES)
                .setConstraints(constraints).build()

        WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)


        if (NetworkUtil.getConnectivityStatus(requireActivity())) {

            getList(retrofitViewModel)
        } else {
            viewBinding.fab.isEnabled = false
            Snackbar.make(
                viewBinding.coordinatorTodo,
                R.string.noconnection,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.retry, View.OnClickListener {
                    if (NetworkUtil.getConnectivityStatus(requireActivity())) {
                        getList(retrofitViewModel)
                        viewBinding.fab.isEnabled = true
                    } else
                        Snackbar.make(
                            viewBinding.coordinatorTodo,
                            R.string.noconnection2,
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction(R.string.ok, View.OnClickListener {

                            }).setActionTextColor(getColor(requireActivity(), R.color.white))
                            .setTextColor(getColor(requireActivity(), R.color.white))
                            .show()

                }).setActionTextColor(getColor(requireActivity(), R.color.white))
                .setTextColor(getColor(requireActivity(), R.color.white))
                .show()
        }

        val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

        (activity as AppCompatActivity?)!!.setSupportActionBar(viewBinding.animToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(false);

        /** Кнопка для изменение папраметра видеть/не видеть
         * выполненные значения **/
        viewBinding.visibilityBtn.setOnClickListener { todoListPresenter.changeVisibility() }

        /** Кнопка для добавления новго дела **/
        viewBinding.fab.setOnClickListener {

            bundle.putBoolean("new", true)
            patchRequestToList(retrofitViewModel)

        }

        /** Обработка скрола appBarа **/
        viewBinding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            if (Math.abs(verticalOffset) > 200) {
                appBarExpanded = false
                activity?.invalidateOptionsMenu()
            } else {
                appBarExpanded = true
                activity?.invalidateOptionsMenu()
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    fun internetCheck(retrofitViewModel: RetrofitViewModel) {
        if (NetworkUtil.getConnectivityStatus(requireActivity())) {
            getList(retrofitViewModel)
        }
    }

    override fun startApp() {

        /** Инициализация RecyclerView **/

        val recyclerView: RecyclerView = viewBinding.scrollableview
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity);
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager;
        recyclerView.adapter = dessertAdapter;
        changeRv(todoListPresenter.todoListVisible, visibility = todoListPresenter.visibility)
    }

    override fun setCompletedText(i: Int) {
        viewBinding.performed.text = getString(R.string.performed) + i.toString()
    }

    override fun setVisibility() {
        viewBinding.visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        Log.d("visibility", todoListPresenter.visibility.toString())
        todoListPresenter.initializeAllLists()
        todoListPresenter.changeCompleted()
        changeRv(TodoItemRepository.todoList, visibility = todoListPresenter.visibility)
    }

    override fun setNoVisibility() {

        viewBinding.visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_24)
        Log.d("visibility", todoListPresenter.visibility.toString())
        todoListPresenter.initializeAllLists()
        todoListPresenter.changeCompleted()
        changeRv(todoListPresenter.todoListVisible, todoListPresenter.visibility)

    }

    override fun changeMenuVisibility(visibility: Boolean) {
        if (visibility) {
            collapsedMenu.add("visibility")
                .setIcon(R.drawable.ic_baseline_visibility_off_24)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        } else {
            collapsedMenu.add("visibility")
                .setIcon(R.drawable.ic_baseline_visibility_24)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)


        }
    }


    fun changeRv(todo: ArrayList<TodoItem>, visibility: Boolean) {
        dessertAdapter.visibility = visibility
        dessertAdapter.recyclerList = todo

        dessertAdapter.notifyDataSetChanged()
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        if (collapsedMenu != null
            && !appBarExpanded || collapsedMenu.size() != 0
        ) {
            //collapsed

            viewBinding.collapsingToolbar.expandedTitleMarginStart = 16
            viewBinding.collapsingToolbar.title = getString(R.string.my_deal)

            todoListPresenter.changeMenuVisibility()

            viewBinding.collapsingToolbar.setCollapsedTitleTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.black
                )
            )
            viewBinding.collapsingToolbar.setExpandedTitleColor(
                ContextCompat.getColor(
                    requireActivity(),
                    com.google.android.material.R.color.mtrl_btn_transparent_bg_color
                )
            )
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
            todoListPresenter.changeVisibility()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getList(retrofitViewModel: RetrofitViewModel) {

        retrofitViewModel.getList()
        retrofitViewModel.ListResponse.observe(viewLifecycleOwner) { res ->
            if (res.isSuccessful) {
                RetrofitConstants.REVISION = res.body()!!.revision
                TodoItemRepository.todoListRequest = res.body()!!
                TodoItemRepository.todoList = res.body()?.list?.let {
                    res.body()?.todoListToRepository(
                        it
                    )
                } as ArrayList<TodoItem>
                todoListPresenter.initializeAllLists()
                todoListPresenter.changeCompleted()

            } else {
                if (res.code().toString()[0] == '4') {
                    Snackbar.make(
                        viewBinding.coordinatorTodo,
                        R.string.error400,
                        Snackbar.LENGTH_SHORT
                    )
                        .setAction(R.string.ok, View.OnClickListener {

                        }).setActionTextColor(getColor(requireActivity(), R.color.white))
                        .setTextColor(getColor(requireActivity(), R.color.white))
                        .show()
                }
                if (res.code().toString()[0] == '5') {
                    Snackbar.make(
                        viewBinding.coordinatorTodo,
                        R.string.error500,
                        Snackbar.LENGTH_SHORT
                    )
                        .setAction(R.string.ok, View.OnClickListener {

                        }).setActionTextColor(getColor(requireActivity(), R.color.white))
                        .setTextColor(getColor(requireActivity(), R.color.white))
                        .show()
                }

            }
        }
    }

    private fun patchRequestToList(retrofitViewModel: RetrofitViewModel) {

        retrofitViewModel.patchList(todoListPresenter.toTodoList())

        retrofitViewModel.patchListResponse.observe(viewLifecycleOwner) { res ->
            if (res.isSuccessful) {
                Log.d("bilion", todoListPresenter.toTodoList().toString())
                findNavController().navigate(R.id.newItemFragment, bundle)
            } else {
                snackbarShow(res.code().toString())
            }
        }
    }

    fun snackbarShow(code: String) {
        if (code[0] == '4') {
            Snackbar.make(
                viewBinding.coordinatorTodo,
                R.string.error400,
                Snackbar.LENGTH_SHORT
            )
                .setAction(R.string.ok, View.OnClickListener {

                }).setActionTextColor(getColor(requireActivity(), R.color.white))
                .setTextColor(getColor(requireActivity(), R.color.white))
                .show()
        }
        if (code[0] == '5') {
            Snackbar.make(
                viewBinding.coordinatorTodo,
                R.string.error500,
                Snackbar.LENGTH_SHORT
            )
                .setAction(R.string.ok, View.OnClickListener {

                }).setActionTextColor(getColor(requireActivity(), R.color.white))
                .setTextColor(getColor(requireActivity(), R.color.white))
                .show()
        }

    }


}


