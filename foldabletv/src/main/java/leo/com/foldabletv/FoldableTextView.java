package leo.com.foldabletv;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by liuguanli on 2016/10/21.
 */

public class FoldableTextView extends LinearLayout implements View.OnClickListener {

    private static final boolean DEBUG = true;

    private static final String TAG = "FoldableTextView";

    protected TextView mContentTxv; //文本正文
    protected ImageView mFoldImage; //展开按钮

    protected Drawable mFoldedDrawable;
    protected Drawable mExpandedDrawable;
    protected String text;

    protected int visibleLine;
    protected float textSize;
    protected int textColor;

    private static final int DEFAULT_VISIBLE_LINE = 4;

    private boolean isFloadAble = false;
    private boolean isFloaded = false;

    private Context mContext = getContext();

    // 默认的属性

    @Override
    protected void onFinishInflate() {
        Log.i(TAG, "onFinishInflate");
        initView();
        initListener();
    }

    public FoldableTextView(Context context) {
        this(context, null);
        Log.i(TAG, "FoldableTextView -- 1");
    }

    public FoldableTextView(Context context, AttributeSet attrs) {
        super(context);
        Log.i(TAG, "FoldableTextView -- 2");
        initWithAttrs(attrs);
    }

    protected void initView() {
        mContentTxv = (TextView) findViewById(R.id.foldable_textview);
        mFoldImage = (ImageView) findViewById(R.id.foldable_imageview);
        mFoldImage.setImageDrawable(mExpandedDrawable);
        Log.i(TAG, "initView " + mContentTxv.getText());
        if (!TextUtils.isEmpty(mContentTxv.getText())) {
            mContentTxv.post(new Runnable() {
                @Override
                public void run() {
                    if (mContentTxv.getLineCount() > visibleLine) {
                        mFoldImage.setVisibility(VISIBLE);
                        mContentTxv.setMaxLines(visibleLine);
                        isFloadAble = true;
                        isFloaded = true;
                    } else {
                        //                        mFoldImage.setVisibility(GONE);
                    }
                }
            });
        }
    }

    protected void initListener() {
        mFoldImage.setOnClickListener(this);
    }

    protected void initWithAttrs(AttributeSet attrs) {
        Log.i(TAG, "initWithAttrs");
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.FoldableTextView);
        visibleLine = typedArray.getInt(R.styleable.FoldableTextView_visibleLines, DEFAULT_VISIBLE_LINE);
        mFoldedDrawable = typedArray.getDrawable(R.styleable.FoldableTextView_flodedDrawable);
        mExpandedDrawable = typedArray.getDrawable(R.styleable.FoldableTextView_expandedDrawable);
        Log.i(TAG, "initWithAttrs:: mFoldedDrawable-> " + mFoldedDrawable);
        Log.i(TAG, "initWithAttrs:: mExpandedDrawable-> " + mExpandedDrawable);
        if (mFoldedDrawable == null) {
            mFoldedDrawable = getDrawable(R.drawable.expanded);
        }

        if (mExpandedDrawable == null) {
            mExpandedDrawable = getDrawable(R.drawable.folded);
        }
        typedArray.recycle();

        setOrientation(LinearLayout.VERTICAL);
    }

    protected Drawable getDrawable(int resId) {
        Resources resources = mContext.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return resources.getDrawable(resId, mContext.getTheme());
        } else {
            return resources.getDrawable(resId);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.foldable_imageview) {
            if (isFloaded) {
                mContentTxv.setMaxLines(Integer.MAX_VALUE);
                mFoldImage.setImageDrawable(mFoldedDrawable);
            } else {
                mContentTxv.setMaxLines(visibleLine);
                mFoldImage.setImageDrawable(mExpandedDrawable);
            }
            isFloaded = !isFloaded;

        }
    }
}
