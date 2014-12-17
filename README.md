UyghurKeyboard
==============
<img src="https://raw.githubusercontent.com/Sabirjan/UyghurKeyboard/master/img/20141217154207.png" />
<p></p>
很多维吾尔文Android应用都需要应用内输入维吾尔文，比如：搜索、意见反馈、填写维吾尔文名字等等。

在这个项目中你可以不需要让用户安装任何第三方输入法，完全可以达到输入维吾尔文的目标。

你可以把com.zerak.keyboard 做成一个AndroidLib项目
-----------------------------------------------------------------
如果用户点击【返回】后 隐藏输入法，想再次点击输入框的时候弹出输入法，你就添加下面的代码就可以了

				medit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			
				if(keyboard!=null&&!keyboard.isShowing())
				{
					keyboard.showKeyboard();
				}
			}
		});
		


</br>
</br>



<p>新疆精灵通电子科技有限公司[2015]</p>
<p>XinJiang Zerak Electronics Eechnology[2015]</p>

