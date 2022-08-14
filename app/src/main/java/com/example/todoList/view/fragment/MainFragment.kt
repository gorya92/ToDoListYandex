package com.example.todoList.view.fragment


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistyandex.model.Todo
import com.google.android.material.appbar.AppBarLayout
import com.example.todoList.MainApplication
import com.example.todoList.R
import com.example.todoList.adapter.RepoAdapter
import com.example.todoList.presenter.GithubUserPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

class MainFragment : Fragment(), GithubUserPresenter.View {


    @Inject
    lateinit var presenter: GithubUserPresenter

    private lateinit var collapsedMenu: Menu
    private var appBarExpanded = true

    val bundle = Bundle()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as MainApplication).component
            .inject(this)
        presenter.injectView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserInfo()
        fabclick()
        visibilityClick()
        colapsingSettings()
        setActionBarProperties()
        appbarExpanded()

    }

   fun setActionBarProperties(){
       (activity as AppCompatActivity?)!!.setSupportActionBar(anim_toolbar)
       (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(false);
    }

    private fun appbarExpanded() {
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            if (Math.abs(verticalOffset) > 200) {
                appBarExpanded = false
                activity?.invalidateOptionsMenu()
            } else {
                appBarExpanded = true
                activity?.invalidateOptionsMenu()
            }

            presenter.showListChange()
        })

    }

    private fun fabclick() {
        fab.setOnClickListener {
            bundle.putBoolean("new", true)
            findNavController().navigate(R.id.newItemFragment,bundle)
        }
    }

    private fun loadUserInfo() = presenter.getUserInfo()

    override fun loading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun showUserInfo(userInfo: ArrayList<Todo>, visibility: Boolean) {
        scrollableview.apply {
            var githubAdapter: RepoAdapter = RepoAdapter()
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = githubAdapter
            githubAdapter.visibility = visibility
            githubAdapter.arr = userInfo

        }
    }

    override fun showUserInfoError(message: String?) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()


    override fun setVisibility() = visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_off_24)

    fun visibilityClick() {
        visibilityBtn.setOnClickListener {
            presenter.visibleClick()
        }
    }

    override fun setNoVisibility() = visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_24)


    override fun setCompletedText(i: Int) {
        performed.text = getString(R.string.performed) + i.toString()
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

    override fun dismissLoading() {
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (!appBarExpanded || collapsedMenu.size() != 0
        ) {

            presenter.changeMenuVisibility()

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
            presenter.visibleClick()
        }
        return super.onOptionsItemSelected(item)
    }

    fun colapsingSettings() {
        collapsing_toolbar.setCollapsedTitleTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.black
            )
        )
        collapsing_toolbar.setExpandedTitleColor(
            ContextCompat.getColor(
                requireActivity(),
                com.google.android.material.R.color.mtrl_btn_transparent_bg_color
            )
        )
        collapsing_toolbar.expandedTitleMarginStart = 16
        collapsing_toolbar.title = getString(R.string.my_deal)
    }

}
