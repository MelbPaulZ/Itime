package org.unimelb.itime.widget.popupmenu;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.PopupMenuBinding;

import java.util.List;

public class PopupMenu {

	private LinearLayoutCompat popView;
	private Context context;
	private List<Item> items;
	private List<View> itemViews;
	private OnItemClickListener onItemClickListener;
	private PopupMenuBinding binding;
	private PopupMenuViewModel viewModel;
	private PopupWindow popupWindow;
	private PopupContentView contentView;
	private ModalPopupView modalPopupView;

	public PopupMenu(Context context) {
		this.context = context;
		initLinearLayout();
	}


	private void initLinearLayout(){
		popView = new LinearLayoutCompat(context);
		popView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		popView.setBackground(context.getResources().getDrawable(R.drawable.bg_shadow));
		popView.setOrientation(LinearLayoutCompat.VERTICAL);
		modalPopupView = new ModalPopupView(context);
		modalPopupView.setContentView(popView);
	}

	private int measureContentWidth(LinearLayoutCompat listView) {
		ViewGroup mMeasureParent = null;
		int maxWidth = 0;
		View itemView = null;
		int itemType = 0;

		final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int count = listView.getChildCount();
		for (int i = 0; i < count; i++) {
			itemView = listView.getChildAt(i);
			itemView.measure(widthMeasureSpec, heightMeasureSpec);
			final int itemWidth = itemView.getMeasuredWidth();
			if (itemWidth > maxWidth) {
				maxWidth = itemWidth;
			}
		}

		return maxWidth;
	}

	private boolean isIn(View view, MotionEvent ev){
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		RectF rectF =  new RectF(location[0], location[1], location[0] + view.getWidth(),
				location[1] + view.getHeight());

		return rectF.contains(ev.getRawX(), ev.getRawY());
	}

	/**
	 * 设置显示的位置
	 *
	 *
	 */
	public void showLocation(View view) {
		modalPopupView.showAtLocation(view, 0, 0);

	}

	// dip转换为px
	public int dip2px(float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	// 点击监听接口
	public interface OnItemClickListener {
		void onClick(int position, Item item);
	}

	// 设置监听
	public void setOnItemClickListener(OnItemClickListener onItemClick) {
		this.onItemClickListener = onItemClick;
	}

	public void setItems(List<Item> items) {
		this.items = items;
//		viewModel.setItems(items);
		createMenu();
	}

	private void createMenu(){
		popView.removeAllViews();

		for(int i=0;i<items.size();i++){
			View v = generateItemView(items.get(i));
			v.setTag(i);
			final int position = i;
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(onItemClickListener!=null){
						onItemClickListener.onClick(position, items.get(position));
					}
					modalPopupView.dismiss();
				}
			});
			popView.addView(v);
//			itemViews.add(v);
		}
	}

	public void setBackground(Drawable drawable){
//		contentView.setBackground(new ColorDrawable(color));
		modalPopupView.setBackground(drawable);
	}

	private View generateItemView(Item item){
		View itemView = LayoutInflater.from(context).inflate(R.layout.item_popup_menu_item, popView, false);

		TextView textView = (TextView) itemView.findViewById(R.id.item_name);
		textView.setText(item.name);

		ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);
		if(item.icon!=-1) {
			imageView.setImageResource(item.icon);
		}else{
			imageView.setVisibility(View.GONE);
		}
		return itemView;
	}

	public static class Item{
		// R.id of icon
		public int icon = -1;
		public String name;

		public Item(int icon, String name){
			this.icon = icon;
			this.name = name;
		}

		public Item(String name){
			this.name = name;
		}
	}

}
