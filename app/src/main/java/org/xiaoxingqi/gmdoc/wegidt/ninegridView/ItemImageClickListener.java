package org.xiaoxingqi.gmdoc.wegidt.ninegridView;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by Jaeger on 2016/12/29.
 * <p>
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */

public interface ItemImageClickListener<T> {
    void onItemImageClick(Context context, View imageView, int index, List<T> list);
}
