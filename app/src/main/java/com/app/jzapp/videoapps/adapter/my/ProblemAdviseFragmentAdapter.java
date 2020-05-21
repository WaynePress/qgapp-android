package com.app.jzapp.videoapps.adapter.my;

import android.view.View;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.bean.ProblemAdviseLv0Bean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class ProblemAdviseFragmentAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 1;
    public static final int TYPE_LEVEL_1 = 2;


    public ProblemAdviseFragmentAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_problem_advise_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_problem_advise_lv1);

    }

    @Override
    protected void convert(final BaseViewHolder holder, MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final ProblemAdviseLv0Bean lv0 = (ProblemAdviseLv0Bean) item;
                holder.setImageResource(R.id.iv, lv0.isExpanded() ? R.drawable.ic_expand_less_black_24dp : R.drawable.ic_expand_more_black_24dp);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:


                break;

        }
    }

}
