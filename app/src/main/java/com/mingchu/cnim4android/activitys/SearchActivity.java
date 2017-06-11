package com.mingchu.cnim4android.activitys;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.fragment.search.SearchGroupFragment;
import com.mingchu.cnim4android.fragment.search.SearchUserFragment;
import com.mingchu.common.app.ToolbarActivity;

public class SearchActivity extends ToolbarActivity {

    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int TYPE_USER = 1; //搜索人
    public static final int TYPE_GROUP = 2;//搜索群

    //具体需要显示的类型
    private int type;
    private SearchFragment mSearchFragment;

    /**
     * 显示搜索界面
     *
     * @param context 上下文
     * @param type    显示的类型 是用户还是群
     */
    public static void show(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        super.initView();
        Fragment fragment = null;
        if (type == TYPE_USER) {
            SearchUserFragment searchUserFragment = new SearchUserFragment();
            fragment = searchUserFragment;
            mSearchFragment = searchUserFragment;
        } else if (type == TYPE_GROUP) {
            SearchGroupFragment searchGroupFragment = new SearchGroupFragment();
            fragment = searchGroupFragment;
            mSearchFragment = searchGroupFragment;
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, fragment)
                .commit();
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        type = bundle.getInt(EXTRA_TYPE);
        return type == TYPE_USER || type == TYPE_GROUP;
    }

    @Override
    protected void initData() {
        super.initData();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);
        //找到搜索
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            //拿到搜索的管理器
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            //添加搜素监听
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //当点击了提交按钮的时候
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //当文字改变的时候  咱们不会及时搜素  只有为null的时候进行搜素
                    if (TextUtils.isEmpty(newText)) {
                        search("");
                    }
                    return false;
                }
            });

        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 搜索发起点
     *
     * @param query 搜索关键字
     */
    private void search(String query) {
        if (mSearchFragment != null) {
            mSearchFragment.search(query);
        }

    }


    /**
     * 搜索的Fragment  必须继承的接口
     */
    public interface SearchFragment {
        void search(String content);
    }

}
