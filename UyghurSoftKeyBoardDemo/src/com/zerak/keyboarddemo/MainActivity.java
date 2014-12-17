package com.zerak.keyboarddemo;

import com.zerak.keyboarddemo.R;
import com.zerak.keyboard.CandidateView;
import com.zerak.keyboard.Global;
import com.zerak.keyboard.KeyboardUtil;
import com.zerak.keyboard.SearchInterface;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

public class MainActivity extends Activity implements SearchInterface {
	KeyboardUtil keyboard;
	CandidateView mCandidateView;
	EditText medit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		if (Global.typeface == null) {
			Global.typeface = Typeface.createFromAsset(this.getAssets(),
					"UkijTuzTom.ttf");
		}
		medit = (EditText) findViewById(R.id.edit_fankui);
		medit.setTypeface(Global.typeface);
		keyboard = new KeyboardUtil(this, this, medit, mCandidateView);
		keyboard.setEnterType(KeyboardUtil.ENTER_TYPE_RETURN);
		keyboard.showKeyboard();
	}

	@Override
	public void ActivitySearch() {

	}
}
