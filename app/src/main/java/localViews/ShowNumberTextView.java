package localViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class ShowNumberTextView extends TextView {
    public ShowNumberTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/main_number.ttf");
        setTypeface(typeface);
    }

    public ShowNumberTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/main_number.ttf");
        setTypeface(typeface);
    }

    public ShowNumberTextView(Context context) {
        super(context);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/main_number.ttf");
        setTypeface(typeface);
    }
}
