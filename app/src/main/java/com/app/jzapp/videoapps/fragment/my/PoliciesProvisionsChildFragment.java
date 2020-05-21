package com.app.jzapp.videoapps.fragment.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.MySupportFragment;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PoliciesProvisionsChildFragment extends MySupportFragment {
    private static final String ARG_FROM = "arg_from";
    @BindView(R.id.tv_content)
    TextView tvContent;
    private int mFrom;
    private Unbinder unbinder;

    public static PoliciesProvisionsChildFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_FROM, position);
        PoliciesProvisionsChildFragment fragment = new PoliciesProvisionsChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mFrom = args.getInt(ARG_FROM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_policies_provisions_child, container, false);
            unbinder = ButterKnife.bind(this, mRootView);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, mRootView);
        }

        return mRootView;
    }

    private void initView() {
        new Thread() {
            @Override
            public void run() {
                if (mFrom == 0) {
                    readAssetsTxt(_mActivity, "1");
                } else {
                    readAssetsTxt(_mActivity, "2");
                }

            }
        }.start();

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
