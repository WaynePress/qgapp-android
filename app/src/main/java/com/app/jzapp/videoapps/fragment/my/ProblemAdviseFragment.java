package com.app.jzapp.videoapps.fragment.my;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

public class ProblemAdviseFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //    @BindView(R.id.refreshLayout)
//    TwinklingRefreshLayout refreshLayout;
//    @BindView(R.id.recycler)
//    RecyclerView recycler;
    @BindView(R.id.tv_content)
    TextView tvContent;

    public static ProblemAdviseFragment newInstance() {
        ProblemAdviseFragment fragment = new ProblemAdviseFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_problem_advise;
    }

    @Override
    protected View setStatusBarView() {
        return paddingView;
    }

    @Override
    protected int statusBarColor() {
        return R.color.white;
    }

    @Override
    protected void initView() {
        initToolbarNav(toolbar);

        toolbar.setTitle("");
        title.setText(getString(R.string.problem_advise));

        new Thread() {
            @Override
            public void run() {
                readAssetsTxt(_mActivity, "3");
            }
        }.start();


//        setRefreshLayout();

//        recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
//
//        ArrayList<MultiItemEntity> datas = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            ProblemAdviseLv0Bean bean = new ProblemAdviseLv0Bean();
//            ProblemAdviseLv1Bean lv1Bean = new ProblemAdviseLv1Bean();
//            bean.addSubItem(lv1Bean);
//            datas.add(bean);
//        }
//        ProblemAdviseFragmentAdapter adapter = new ProblemAdviseFragmentAdapter(datas);
//        recycler.setAdapter(adapter);
    }

    /**
     * 读取assets下的txt文件，返回utf-8 String
     *
     * @param context
     * @param fileName 不包括后缀
     * @return
     */
    public void readAssetsTxt(Activity context, String fileName) {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName + ".txt");
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            final String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvContent.setText(text);
                }
            });
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }
//
//    private void setRefreshLayout() {
//        refreshLayout.setEnableLoadmore(false);
//        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setEnableOverScroll(false);
//        refreshLayout.setHeaderHeight(55);
//    }

}
