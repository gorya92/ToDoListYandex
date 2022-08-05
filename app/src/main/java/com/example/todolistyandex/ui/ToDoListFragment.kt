package com.example.todolistyandex.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistyandex.R
import com.example.todolistyandex.databinding.FragmentToDoListBinding
import com.example.todolistyandex.model.TodoItemRepository
import com.example.todolistyandex.presenters.TodoListPresenter
import com.example.todolistyandex.views.TodoListView
import com.example.yandextask.adapter.TodoitemAdapter
import com.example.yandextask.model.TodoItem
import com.google.android.material.appbar.AppBarLayout
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenterTag


class ToDoListFragment : MvpAppCompatFragment(), TodoListView {

    private var appBarExpanded = true

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
        todoListPresenter.initializeAllLists()
        todoListPresenter.changeCompleted()

        (activity as AppCompatActivity?)!!.setSupportActionBar(viewBinding.animToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(false);

        /** Кнопка для изменение папраметра видеть/не видеть
         * выполненные значения **/
        viewBinding.visibilityBtn.setOnClickListener { todoListPresenter.changeVisibility() }

        /** Кнопка для добавления новго дела **/
        viewBinding.fab.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("new", true)
            findNavController().navigate(R.id.newItemFragment, bundle)
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


    override fun startApp() {

        /** Инициализация RecyclerView **/

        val recyclerView: RecyclerView = viewBinding.scrollableview
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity);
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager;
        recyclerView.adapter = dessertAdapter;
        changeRv(TodoItemRepository.todoListVisible)
    }

    override fun setCompletedText(i: Int) {
        viewBinding.performed.text = getString(R.string.performed) + i.toString()
    }

    override fun setVisibility() {
        viewBinding.visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        changeRv(TodoItemRepository.todoList)
    }

    override fun setNoVisibility() {

        viewBinding.visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_24)
        changeRv(TodoItemRepository.todoListVisible)

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

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (collapsedMenu != null
            && !appBarExpanded || collapsedMenu.size() != 0
        ) {
            //collapsed
            Log.d("menus", "menus")
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

    fun changeRv(todo: ArrayList<TodoItem>) {
        val productDiffUtilCallback =
            TodoitemAdapter.todoItemDiffCallBack(
                dessertAdapter.recyclerList,
                todo
            )
        val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
        dessertAdapter.submitRepository(todo)
        productDiffResult.dispatchUpdatesTo(dessertAdapter);
    }

}