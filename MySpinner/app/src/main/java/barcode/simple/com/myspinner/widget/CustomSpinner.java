package barcode.simple.com.myspinner.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import barcode.simple.com.myspinner.R;

/**
 * Created by sarath on 24/2/16.
 */
public class CustomSpinner<T> extends RelativeLayout {

    private static final int DEFAULT_TEXT_SIZE = 12;
    private TextView mTextView;
    private ListView mListView;
    private LayoutInflater mInflater;
    private ArrayList<T> mListItems;
    private View mExpandedList;
    private T mSelectedItem;
    private String mHintText = null;
    private int mHintTextSize;
    private int mItemTextSize;

    private enum SpinnerState {
        COLLAPSED,
        EXPANDED
    }

    private SpinnerState mSpinnerState = SpinnerState.COLLAPSED;

    public interface ItemClickListener<T> {
        void onItemSelected(CustomSpinner customSpinner ,T selectedItem);
    }

    private ItemClickListener<T> mItemClickListener;

    public CustomSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        init(attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        View view = mInflater.inflate(R.layout.custom_view, this, true);
        View listHintHeader = view.findViewById(R.id.list_header);
        View spinner = view.findViewById(R.id.spinner);
        TextView expandHintTextView = (TextView) view.findViewById(R.id.list_hint_text);
        mTextView = (TextView) view.findViewById(R.id.textview);
        mListView = (ListView) findViewById(R.id.drop_down);
        mExpandedList = view.findViewById(R.id.expaned_list);
        try {
            TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSpinnerAttrs);
            mHintText = styledAttrs.getString(R.styleable.CustomSpinnerAttrs_hintText);
            mHintTextSize = styledAttrs.getInt(R.styleable.CustomSpinnerAttrs_hintText, DEFAULT_TEXT_SIZE);
            mItemTextSize = styledAttrs.getInt(R.styleable.CustomSpinnerAttrs_itemTextSize, DEFAULT_TEXT_SIZE);
            styledAttrs.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTextView.setHint(mHintText);
        expandHintTextView.setHint(mHintText);
       /* mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mHintTextSize);
        expandHintTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mHintTextSize);
*/

        mListView.setDivider(null);
        if (mListItems != null) {
            setDropDownList(mListItems);
        }
        collapse();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T selectedItem = mListItems.get(position);
                mTextView.setText(selectedItem.toString());
                mSelectedItem = selectedItem;
                if (mItemClickListener != null) {
                    mItemClickListener.onItemSelected(CustomSpinner.this, selectedItem);
                }
                collapse();
            }
        });

        OnClickListener clickListener = itemClickListener();
        mTextView.setOnClickListener(clickListener);
        listHintHeader.setOnClickListener(clickListener);
        spinner.setOnClickListener(clickListener);

    }

    @NonNull
    private OnClickListener itemClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                perFormCollapseExpand();
            }
        };
    }

    private void perFormCollapseExpand() {
        if (mSpinnerState.equals(SpinnerState.COLLAPSED)) {
            expand();
        } else {
            collapse();
        }
    }

    private void collapse() {
        fadeOutAndHideSpinner(mExpandedList);
        mSpinnerState = SpinnerState.COLLAPSED;
    }

    private void fadeOutAndHideSpinner(final View img) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(100);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {

            }
        });

        img.startAnimation(fadeOut);
    }

    private void fadeInAndShowSpinner(final View img) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(100);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {

            }
        });

        img.startAnimation(fadeIn);
    }

    public void setDropDownListItems(ArrayList<T> listItems) {
        mListItems = listItems;
        setDropDownList(listItems);
    }

    public void expand() {
        mExpandedList.setVisibility(VISIBLE);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.your_fade_in_anim);
        mExpandedList.startAnimation(fadeInAnimation);
        //fadeInAndShowSpinner(mExpandedList);
        mSpinnerState = SpinnerState.EXPANDED;

    }

    private void setDropDownList(ArrayList<T> mListItems) {
        ArrayAdapter<T> adapter = new ArrayAdapter<T>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mListItems);
        mListView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(mListView);
    }

    public T getSelectedItem() {
        return mSelectedItem;
    }

    public void setItemClickListener(ItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
