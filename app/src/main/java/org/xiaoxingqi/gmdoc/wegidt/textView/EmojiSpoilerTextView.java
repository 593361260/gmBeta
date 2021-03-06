package org.xiaoxingqi.gmdoc.wegidt.textView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.xiaoxingqi.gmdoc.impl.IConstant;
import org.xiaoxingqi.gmdoc.tools.AppTools;
import org.xiaoxingqi.gmdoc.tools.FaceData;
import org.xiaoxingqi.gmdoc.tools.SPUtils;
import org.xiaoxingqi.gmdoc.wegidt.gifTools.GlideImageGetter;
import org.xiaoxingqi.gmdoc.wegidt.imagespan.VerticalImageSpan;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.xiaoxingqi.gmdoc.tools.AppConfig.EMOJI;
import static org.xiaoxingqi.gmdoc.tools.AppConfig.SPOLIER;


/**
 * Created by yzm on 2017/12/23.
 * 功能描述
 * 显示剧透 表情
 * __ _剧透不能点击
 * 不可点击
 */

public class EmojiSpoilerTextView extends AppCompatTextView {
    private Context mContext;
    private List<Point> arrays = new ArrayList<>();
    private boolean isAttchWindows = false;
    private Paint mPaint;

    public EmojiSpoilerTextView(Context context) {
        this(context, null, 0);
    }

    public EmojiSpoilerTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiSpoilerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#6d6d6d"));
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isAttchWindows)
            getMeasureCoondiration(canvas);

    }

    private void getMeasureCoondiration(Canvas canvas) {
        if (!SPUtils.getBoolean(mContext, IConstant.IS_SPOLIER, false)) {
            return;
        }
        Layout layout = getLayout();
        if (arrays.size() > 0) {
            for (int a = 0; a < arrays.size(); a++) {
                if (!isAttchWindows)
                    break;
                /**
                 * 获取一行的字数
                 */
                Rect bound = new Rect();
                /**
                 *当前文本所在行数
                 */
                int startLine = layout.getLineForOffset(arrays.get(a).x);
                /**
                 * 文本结束的行数
                 */
                int endLine = layout.getLineForOffset(arrays.get(a).y);
                int lineCount = layout.getLineCount();
                layout.getLineBounds(startLine, bound);
                //                int yAxisTop = bound.top;//字符顶部y坐标
                int yAxisBottom = bound.bottom;//字符底部y坐标
                int lineHeight = (int) ((getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) * 1f / lineCount + 0.5f);//计算单行的高度
                int yAxisTop = startLine * lineHeight;//字符顶部y坐标
                int xAxisLeft = (int) layout.getPrimaryHorizontal(arrays.get(a).x);//字符左边x坐标
                int xAxisRight = (int) layout.getSecondaryHorizontal(arrays.get(a).y);//偏移量
                if (false) {
                    continue;
                }
                if (startLine == endLine) {//只有一行的时候
                    canvas.drawRect(new RectF(xAxisLeft, lineHeight * startLine + 3, (int) layout.getSecondaryHorizontal(arrays.get(a).y), yAxisTop + lineHeight - 3), mPaint);
                } else {//多行
                    /**
                     * 换行绘制 需要绘制两行 黑色区域
                     */
                    canvas.drawRect(new RectF(xAxisLeft, yAxisTop + 3, bound.right, yAxisTop + lineHeight - 3), mPaint);
                    /**
                     * 循环绘制存在剧透的行数
                     */
                    if (endLine - startLine > 1) {
                        for (int i = startLine + 1; i < endLine; i++) {
                            if (!isAttchWindows)
                                return;
                            canvas.drawRect(new RectF(0, lineHeight * i + 3, bound.right, lineHeight * (i + 1) - 3), mPaint);
                        }
                    }
                    canvas.drawRect(new RectF(0, lineHeight * endLine + 3, xAxisRight, lineHeight * (endLine + 1) - 3), mPaint);
                }
            }
        }
    }

    public void setData(String text) {
        arrays.clear();
        if (TextUtils.isEmpty(text)) {
            setText(text);
            return;
        }
        SpannableStringBuilder htmlStr = (SpannableStringBuilder) Html.fromHtml(text.toString());
        Pattern pattern = Pattern.compile(SPOLIER);
        Matcher matcher = pattern.matcher(htmlStr);
        while (matcher.find()) {//替换需要更改的文本
            final String at = matcher.group();
            if (at != null) {
                int start = matcher.start();
                int end = start + at.length();
                int orignalX = start - 5 * arrays.size();
                int orignalY = end - 5 * (arrays.size() + 1);
                arrays.add(new Point(orignalX, orignalY));
                htmlStr.delete(orignalX, orignalX + "[剧透:".length());
                htmlStr.delete(orignalY, orignalY + 1);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            //            setTextIsSelectable(true);
        }
        setText(htmlStr);
        setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString tmep = (SpannableString) getText();
        if (tmep instanceof Spannable) {
            final int end = tmep.length();
            final Spannable sp = (Spannable) getText();
            ImageSpan[] imgs = tmep.getSpans(0, end, ImageSpan.class);
            for (ImageSpan url : imgs) {
                VerticalImageSpan span = new VerticalImageSpan(getUrlDrawable(url.getSource(), this), url.getSource());
                tmep.setSpan(span, tmep.getSpanStart(url), tmep.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        pattern = Pattern.compile(EMOJI);
        matcher.reset();
        matcher = pattern.matcher(tmep);
        while (matcher.find()) {
            String emoji = matcher.group();
            if (emoji != null) {
                int start = matcher.start();
                int emojiEnd = start + emoji.length();
                String emojiPath = null;
                if ((emojiPath = FaceData.staticFaceInfo.get(emoji)) != null) {
                    try {
                        InputStream open = mContext.getAssets().open(emojiPath);
                        BitmapDrawable drawable = new BitmapDrawable(open);
                        drawable.setBounds(0, 0, AppTools.dp2px(mContext, 16), AppTools.dp2px(mContext, 16));
                        VerticalImageSpan span = new VerticalImageSpan(drawable);
                        tmep.setSpan(span, start, emojiEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        setText(tmep);
        setFocusable(false);
    }

    public static Drawable getUrlDrawable(String source, TextView mTextView) {
        GlideImageGetter imageGetter = new GlideImageGetter(mTextView.getContext(), mTextView);
        return imageGetter.getDrawable(source);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttchWindows = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttchWindows = false;
    }
}
