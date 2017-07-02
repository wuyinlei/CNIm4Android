package com.mingchu.factory.presenter.group;

import com.mingchu.common.factory.model.Author;
import com.mingchu.common.factory.presenter.BaseContract;

/**
 * Created by wuyinlei on 2017/7/2.
 *
 * @function 群组创建契约
 */

public interface GroupCreateContract {

    interface Presenter extends BaseContract.Presenter {

        /**
         * 创建
         *
         * @param name    群组名字
         * @param desc    群组描述
         * @param picture 群组头像
         */
        void create(String name, String desc, String picture);

        /**
         * 更改一个model的选中状态
         *
         * @param model      更改的model
         * @param isSelected 是否被选中
         */
        void changeSelect(ViewModel model, boolean isSelected);

    }

    interface View extends BaseContract.RecyclerView<Presenter, ViewModel> {


        //创建成功
        void onCreateSuccessed();


    }


    class ViewModel {

        public Author mAuthor;
        //是否被选中
        public boolean isSelected;

    }
}
