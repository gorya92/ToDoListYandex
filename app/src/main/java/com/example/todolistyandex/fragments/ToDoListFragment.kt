package com.example.todolistyandex.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistyandex.MainActivity
import com.example.todolistyandex.R
import com.example.todolistyandex.model.TodoItemsRepository
import com.example.yandextask.adapter.TodoitemAdapter
import com.example.yandextask.model.TodoItem
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ToDoListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        requireActivity().setTheme(R.style.Theme_YandexTask);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_to_do_list, container, false)
    }

    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var appBarLayout: AppBarLayout
    lateinit var visibilityBtn: ImageButton
    private lateinit var collapsedMenu: Menu

    var dessertAdapter: TodoitemAdapter = TodoitemAdapter();

    var todoItemsRepositorys = TodoItemsRepository()
    var todoItemsRepositoryVisible = TodoItemsRepository()

    private var appBarExpanded = true
    private var visibility = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        var fab: FloatingActionButton = view.findViewById(R.id.fab)



        visibilityBtn = view.findViewById(R.id.visibilityBtn)



        getActivity()?.invalidateOptionsMenu()

        val toolbar: Toolbar = view.findViewById(R.id.anim_toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)

        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(false);

        appBarLayout = view.findViewById(R.id.appbar)

        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar)
        val dateTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        Log.d("Date", dateTime)


        var todoItemsRepositoryFromNew = arguments?.getParcelable<TodoItemsRepository>("Change")
        Log.d(
            "PARCELABLECHANGE",
            arguments?.getParcelable<TodoItemsRepository>("Change").toString()
        )


        var todoItemsRepository = TodoItemsRepository()
        /** Если запускается первый раз то рандом
         * иначе используются значения
         * данные нам при запуске **/
        if (todoItemsRepositoryFromNew == null) {
            var todoItem = TodoItem(
                id = "1",
                "blablabla",
                "important",
                done = false,
                deadline = "22 июля 2022",
                created_at = "22 июля 2022"
            )
            var todoItem2 = TodoItem(
                id = "1",
                "dasdasdasd",
                "low",
                done = true,
                deadline = "22 июля 2022",
                created_at = "22 июля 2022"
            )
            var todoItem3 = TodoItem(
                id = "0",
                "большой текст большой текстм большой текст большой т",
                "basic",
                deadline = "27/07/2022",
                done = false,
                created_at = dateTime
            )




            todoItemsRepository.add(todoItem3)

            for (i in 0 until 30) {
                var importance = "basic"
                var done = false
                if ((0..8).random() == 1) {
                    importance = "important"
                } else
                    if ((0..6).random() == 1) {
                        importance = "low"
                    }
                if ((0..3).random() == 1) {
                    done = true
                }
                var deadline = ""
                if (i % 3 == 0) {
                    deadline = "27/09/2022"
                }
                todoItemsRepository.add(
                    TodoItem(
                        id = (i + 1).toString(),
                        "blablabla",
                        importance = importance,
                        done = done,
                        deadline = deadline,
                        created_at = dateTime
                    )
                )

                todoItemsRepository?.get(i)?.let { Log.d("tag", it.id) }
            }

            initialize(todoItemsRepository)
        } else {
            initialize(todoItemsRepositoryFromNew)

        }

        /** Кнопка для добавления новго дела **/
        fab.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("MyArgChange", dessertAdapter.todoItemsRepository)
            bundle.putBoolean("new", true)
            findNavController().navigate(R.id.newItemFragment, bundle)
        }
        /** Кнопка для изменение папраметра видеть/не видеть
         * выполненные значения **/
        visibilityBtn.setOnClickListener {
            setVisibility(todoItemsRepositorys, todoItemsRepositoryVisible)
        }

        /** Обработка скрола appBarа **/
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            Log.d(
                MainActivity::class.java.getSimpleName(),
                "onOffsetChanged: verticalOffset: $verticalOffset"
            )

            //  Vertical offset == 0 indicates appBar is fully expanded.
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

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (collapsedMenu != null
            && (!appBarExpanded || collapsedMenu.size() != 0)
        ) {
            //collapsed
            collapsingToolbar.expandedTitleMarginStart = 16

            collapsingToolbar.setTitle("Мои дела")
            collapsingToolbar.setCollapsedTitleTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.black
                )
            )
            collapsingToolbar.setExpandedTitleColor(
                ContextCompat.getColor(
                    requireActivity(),
                    com.google.android.material.R.color.mtrl_btn_transparent_bg_color
                )
            )

            if (!visibility) {
                collapsedMenu.add("visibility")
                    .setIcon(R.drawable.ic_baseline_visibility_24)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            } else {

                collapsedMenu.add("visibility")
                    .setIcon(R.drawable.ic_baseline_visibility_off_24)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            }

        } else {
            //expanded

        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        collapsedMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        }
        if (item.title === "visibility") {
            setVisibility(todoItemsRepositorys, todoItemsRepositoryVisible)
        }
        return super.onOptionsItemSelected(item)
    }

    fun setVisibility(
        todoItemsRepository: TodoItemsRepository,
        todoItemsRepositoryVisible: TodoItemsRepository
    ) {
        if (!visibility) {
            visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        } else {
            visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_24)
        }
        visibility = !visibility
        dessertAdapter.visibility = visibility
        if (visibility) {
            Log.d("news", todoItemsRepositorys.size.toString() + " VISIBILITY")
            val productDiffUtilCallback =
                TodoitemAdapter.todoItemDiffCallBack(
                    dessertAdapter.todoItemsRepositoryvisible,
                    todoItemsRepositorys
                )
            val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
            dessertAdapter.submitRepository(todoItemsRepositorys)
            productDiffResult.dispatchUpdatesTo(dessertAdapter);
        } else {
            Log.d("news", todoItemsRepositoryVisible.size.toString())

            val productDiffUtilCallback =
                TodoitemAdapter.todoItemDiffCallBack(
                    dessertAdapter.todoItemsRepositoryvisible,
                    todoItemsRepositoryVisible
                )
            val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
            dessertAdapter.submitRepository(todoItemsRepositoryVisible)
            productDiffResult.dispatchUpdatesTo(dessertAdapter);
        }


    }

    fun initialize(todoItemsRepositoryFromNew: TodoItemsRepository) {
        /** Список только с невыполненными делами **/
        todoItemsRepositoryVisible.clear()
        if (todoItemsRepositoryFromNew != null) {
            for (i in 0 until todoItemsRepositoryFromNew.size) {
                if (!todoItemsRepositoryFromNew[i].done) {
                    todoItemsRepositoryVisible.add(todoItemsRepositoryFromNew[i])
                }
            }

        }
        /** Изменение текста колличества выполненных дел взят **/
        if (todoItemsRepositoryFromNew != null) {
            requireView().findViewById<TextView?>(R.id.performed).text = "Выполнено-" +
                    (todoItemsRepositoryFromNew.size - todoItemsRepositoryVisible.size).toString()
        }

        /** Инициализация RecyclerView **/
        var recyclerView: RecyclerView = requireView().findViewById(R.id.scrollableview);
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity);
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(dessertAdapter);
        var todoItems = todoItemsRepositoryFromNew.reversed()
        dessertAdapter.todoItemsRepository = todoItemsRepositoryFromNew
        dessertAdapter.todoItemsRepositoryvisible = todoItemsRepositoryVisible
        dessertAdapter.activity = requireActivity()
        dessertAdapter.notifyDataSetChanged()
        todoItemsRepositorys = dessertAdapter.todoItemsRepository
    }

    companion object {


    }
}