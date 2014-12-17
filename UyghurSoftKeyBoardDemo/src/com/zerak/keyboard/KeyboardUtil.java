package com.zerak.keyboard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.zerak.keyboarddemo.R;
import com.zerak.keyboard.LatinKeyboard.LatinKey;

public class KeyboardUtil {

	private static final List<String> EMPTY_LIST = new ArrayList<String>();
	List<String> listSuggestions = new ArrayList<String>();
	// private CandidateView mCandidateView=null;
	EditText mEtc;
	private Context ctx = null;
	private SearchInterface inter = null;
	private LatinKeyboardView keyboardView;
	private static LatinKeyboard k0;// 维文小写
	private static LatinKeyboard k1;// 维文大写
	private static LatinKeyboard k2;// 英文小写
	private static LatinKeyboard k3;// 英文大写
	private static LatinKeyboard k4;// 数字
	private static LatinKeyboard k6;// 符号小写
	private static LatinKeyboard k7;// 符号大写
	// public static Typeface myTypeface;
	public boolean isnun = false;// 是否数据键盘
	public boolean isupper = false;// 是否大写
	public static int pressedCode;
	public static int INPUT_TYPE = 0;
	public static int TARGET_INPUT_TYPE = 0;
	public static final int INPUT_UYGHUR_SMALL = 0;// 维文小写
	public static final int INPUT_UYGHUR_LARGE = 1;// 维文大写
	public static final int INPUT_ENGLISH_SMALL = 2;// 英文小写
	public static final int INPUT_ENGLISH_LARGE = 3;// 英文大写
	public static final int INPUT_NUMBER = 4;// 数字
	public static final int INPUT_CHINA = 5;// 中文
	public static final int INPUT_SYMBOL = 6;// 符号小写
	public static final int INPUT_SYMBOL_LARGE = 7;// 符号大写
	public static final int ENTER_TYPE_SEARCH = 1001;// 搜索
	public static final int ENTER_TYPE_RETURN = 1002;// 换行
	private int enter_type = ENTER_TYPE_SEARCH;// 默认
	private String m_strpinyin = "";

	public KeyboardUtil(SearchInterface inter, Context ctx, EditText edit,
			CandidateView candidateView) {

		if (Global.typeface == null) {
			Global.typeface = Typeface.createFromAsset(ctx.getAssets(),
					"UkijTuzTom.ttf");
		}
		// myTypeface=Global.typeface;
		this.inter = inter;
		this.ctx = ctx;
		// this.mCandidateView=candidateView;
		this.mEtc = edit;
		// 键盘布局文件的初始化
		k0 = new LatinKeyboard(ctx, R.xml.uyghur);
		k1 = new LatinKeyboard(ctx, R.xml.uyghur_large);
		k2 = new LatinKeyboard(ctx, R.xml.qwerty);
		k3 = new LatinKeyboard(ctx, R.xml.qwerty_large);
		k4 = new LatinKeyboard(ctx, R.xml.number);
		keyboardView = (LatinKeyboardView) ((Activity) ctx)
				.findViewById(R.id.uykeyboard_view);
		keyboardView.setKeyboard(k0);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(true);
		keyboardView.setOnKeyboardActionListener(listener);

	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
			pressedCode = primaryCode;
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {

			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				/**
				 * Enter键处理
				 * 
				 */
				// mEtc.OnInsertEnter();
				// 2012 -12-19 edit sabirjan
				if (enter_type == ENTER_TYPE_SEARCH) {
					if (KeyboardUtil.this.inter != null) {
						SearchInterface activity = (SearchInterface) KeyboardUtil.this.inter;
						activity.ActivitySearch();
					}
					hideKeyboard();
				} else {
					int index = mEtc.getSelectionStart();
					Editable editable = mEtc.getText();
					editable.insert(index,"\n");
				}
				// hideKeyboard();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				/*
				 * 如果是输入的是中文则先不要删除输入框的，先把拼音删除完，然后删除输入框的
				 */
				if (INPUT_TYPE == INPUT_CHINA && m_strpinyin.length() > 0) {
					m_strpinyin = m_strpinyin.substring(0,
							m_strpinyin.length() - 1);
					chuliCH();
					return;
				}
				if (mEtc.getText() != null && mEtc.getSelectionStart() > 0) {
					int index = mEtc.getSelectionStart();
					Editable editable = mEtc.getText();
					editable.delete(index - 1, index);
				}

			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换

				onDownShift();

			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换

				changeKey();

			} else if (primaryCode == 57419) { // go left

			} else if (primaryCode == 57421) { // go right

			} else {
				if (INPUT_TYPE == INPUT_CHINA
						&& isword(Character.toString((char) primaryCode))) {
					chuliCH((char) primaryCode);
				} else {
						int index = mEtc.getSelectionStart();
						Editable editable = mEtc.getText();
						editable.insert(index,
								Character.toString((char) primaryCode));
				}

			}
		}
	};

	/**
	 * 设置输入法Enter键的行为
	 * 
	 * @param enterType
	 */
	public void setEnterType(int enterType) {
		if (enterType == ENTER_TYPE_RETURN || enterType == ENTER_TYPE_SEARCH) {
			enter_type = enterType;
		} else {
			enter_type = ENTER_TYPE_SEARCH;
		}
	}
	/**
	 * 设置enter键的默认图标
	 * @param res_id
	 */
	public void setEnterDrwable(int res_id)
	{
		
	}
	/**
	 * 
	 * 处理汉语输入 主函数 如果输入法的状态是INPUT-CHINA时调用此函数
	 */
	private void chuliCH(char primaryCode) {
		// mCandidateView.clear();
		listSuggestions.clear();
		m_strpinyin += Character.toString((char) primaryCode);
		if (PinyinParser.isSinglePinyin(m_strpinyin)) {
			// 获取单个词
			getCHWords();
		} else if (m_strpinyin.length() > 0) {
			List<String> listpinyin = PinyinParser.getPinyinList(m_strpinyin);
			if (listpinyin.size() == 1) {
				getCHWords();
			} else if (listpinyin.size() > 1) {
				String tmp = "";
				for (String pinyin : listpinyin) {
					tmp += pinyin + "'";

				}
				listSuggestions.add(0, tmp);
				// mCandidateView.setSuggestions(listSuggestions, true, false);

			}
		}

	}

	/**
	 * 点击删除的时候处理（中文输入）
	 * 
	 */
	private void chuliCH() {
		// mCandidateView.clear();
		listSuggestions.clear();
		if (PinyinParser.isSinglePinyin(m_strpinyin)) {
			// 获取单个词
			getCHWords();
		} else if (m_strpinyin.length() > 0) {
			List<String> listpinyin = PinyinParser.getPinyinList(m_strpinyin);
			if (listpinyin.size() == 1) {
				getCHWords();
			} else if (listpinyin.size() > 1) {
				System.out.println("pinyin 原始:" + m_strpinyin);
				String tmp = "";
				for (String pinyin : listpinyin) {
					System.out.println("pinyin:" + pinyin);
					tmp += pinyin + "'";

				}
				listSuggestions.add(0, tmp);
				// mCandidateView.setSuggestions(listSuggestions, true, false);

			}
		}

	}

	/*
	 * 根据输入的拼音来获取汉字，暂时的函数
	 */
	private void getCHWords() {
		// mCandidateView.clear();
		// listSuggestions=HuoquPinYin.getWords(m_strpinyin);
		if (listSuggestions == null) {
			listSuggestions = new ArrayList<String>();

		}
		listSuggestions.add(0, m_strpinyin);
		// mCandidateView.setSuggestions(listSuggestions, true, false);
	}

	public int getCharFromKey(int primaryCode, int direction) {
		if (direction == -1 || primaryCode == '\n')
			return primaryCode; // hardware key

		if (primaryCode == '*' || (primaryCode >= '0' && primaryCode <= '9')) {
			List<Key> listKeys = keyboardView.getKeyboard().getKeys();
			for (Key k : listKeys) {
				LatinKey lk = (LatinKey) k;
				if (primaryCode == k.codes[0])
					return lk.fancyLabel.charAt(direction);
			}

		}

		return primaryCode;
	}

	/**
	 * 点击Shift 键的处理 根据TARGET_INPUT_TYPE 来判断
	 */
	private void onDownShift() {
		if (TARGET_INPUT_TYPE == INPUT_UYGHUR_SMALL) {
			TARGET_INPUT_TYPE = INPUT_UYGHUR_LARGE;
			keyboardView.setKeyboard(k1);
			LatinKeyboardView.sShiftState = true;
		} else if (TARGET_INPUT_TYPE == INPUT_UYGHUR_LARGE) {
			TARGET_INPUT_TYPE = INPUT_UYGHUR_SMALL;
			keyboardView.setKeyboard(k0);
			LatinKeyboardView.sShiftState = false;
		} else if (TARGET_INPUT_TYPE == INPUT_ENGLISH_SMALL) {
			TARGET_INPUT_TYPE = INPUT_ENGLISH_LARGE;
			keyboardView.setKeyboard(k3);
			LatinKeyboardView.sShiftState = true;
		} else if (TARGET_INPUT_TYPE == INPUT_ENGLISH_LARGE) {
			TARGET_INPUT_TYPE = INPUT_ENGLISH_SMALL;
			keyboardView.setKeyboard(k2);
			LatinKeyboardView.sShiftState = false;
		} else if (TARGET_INPUT_TYPE == INPUT_SYMBOL) {

		} else if (TARGET_INPUT_TYPE == INPUT_SYMBOL_LARGE) {

		}

	}

	/**
	 * 键盘大小写切换
	 */
	private void changeKey() {

		/**
		 * 判断当前的输入布局，比如：如果维吾尔文布局则转换英文的，如果是英文的则转换中文的
		 * 
		 */
		switch (INPUT_TYPE) {
		case INPUT_UYGHUR_SMALL:
		case INPUT_UYGHUR_LARGE:
			// 转换英文小写
			keyboardView.setKeyboard(k2);
			TARGET_INPUT_TYPE = INPUT_ENGLISH_SMALL;
			INPUT_TYPE = INPUT_ENGLISH_SMALL;
			break;

		case INPUT_ENGLISH_SMALL:
		case INPUT_ENGLISH_LARGE:

			keyboardView.setKeyboard(k4);
			TARGET_INPUT_TYPE = INPUT_NUMBER;
			INPUT_TYPE = INPUT_NUMBER;
			break;
		case INPUT_NUMBER:
			keyboardView.setKeyboard(k0);
			TARGET_INPUT_TYPE = INPUT_UYGHUR_SMALL;
			INPUT_TYPE = INPUT_UYGHUR_SMALL;
			break;
		case INPUT_SYMBOL:

			break;
		case INPUT_SYMBOL_LARGE:
			break;
		default:
			break;
		}
	}

	/**
	 * 显示键盘
	 * 
	 */
	public void showKeyboard() {
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA"
				+ keyboardView.getVisibility());
		int visibility = keyboardView.getVisibility();

		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
			System.out.println("BBBBBBBBBBBBBBBBBBB");
		}
	}

	/**
	 * 隐藏键盘
	 * 
	 */
	public void hideKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.setVisibility(View.GONE);
		}
	}

	private boolean isword(String str) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		if (wordstr.indexOf(str.toLowerCase()) > -1) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 方法描述 :向EditText添加字符串
	 * 
	 * @param text
	 *            要添加的字符串
	 */
	public void setEditText(EditText edittext) {
		try {
			mEtc = edittext;
		} catch (Exception ex) {
			System.out.println("setEditText Error:" + ex);
		}
	}

	/**
	 * 选择自动完成的词语后添加到制定的位置
	 * 
	 */
	public void pickSuggestionManually(int index) {

		if (listSuggestions != null && listSuggestions.size() > 0
				&& listSuggestions.size() > index && index >= 0) {

		}
		m_strpinyin = "";
	}

	public void setNullPopupWindow() {
		try {

			this.inter = null;
			this.ctx = null;

			keyboardView = null;
			k0 = null;
			k1 = null;
			k2 = null;
			k3 = null;
			k4 = null;
			System.gc();
		} catch (Exception ex) {
			System.out.println("setNullPopupWindow error:" + ex);
		}
	}

	public boolean isShowing() {
		if (keyboardView != null) {
			if (keyboardView.getVisibility() == View.GONE
					|| keyboardView.getVisibility() == View.INVISIBLE) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

}
