EmojiDemo
=========

最近在做Android的评论加上表情，挺郁闷的，刚开始在网上找了下资源，发现大部分都是本地APP表情的展示,很少有通过网络之后在手机上显示表情的源码，后来在网上找了几份关于表情的代码，在此基础上进行了改进，最终解决了这个问题。

服务器是PHP,网络数据传输格式是json字符串，下面的简要的贴一下主要的代码：

需要注意的地方: 服务器端接收到的字段先解析不要直接使用json中的字段，然后再SetText:

ArrayList<String> contentSpanArr = new ArrayList<String>();

	public void transText(String s,TextView tv){
		contentSpanArr.clear();
		tv.setText("");
		sortClassify(s);
		for(int i=0;i<contentSpanArr.size();i++){
			String sa = contentSpanArr.get(i);
			if(sa.startsWith("[")){
				boolean hasEmo = false;
				int j;
				for(j=0;j<MsgFaceUtils.faceImgNames.length;j++){
					if(sa.equals(MsgFaceUtils.faceImgNames[j].trim())){
						hasEmo=true;
						SpannableString spannableString = EmojiParser.getInstance(context).addFace(context, MsgFaceUtils.faceImgs[j],
								MsgFaceUtils.faceImgNames[j]);
						tv.append(spannableString);
					}
				}
				if(!hasEmo){
					tv.append(sa);
				}
			}else{
				tv.append(sa);
			}
		}
	}

	public  void sortClassify(String s){
		if (s.contains("[")) {
			String a1 = s.substring(0,s.indexOf("["));
			if(a1!=null&&a1.length()>0){
				contentSpanArr.add(a1);
			}
			if(s.contains("]")){
				String z = s.substring(s.indexOf("["), s.indexOf("]")+1);
				contentSpanArr.add(z);
				String z1 = s.substring(s.indexOf("]")+1,s.length());
				sortClassify(z1);
			}else{
				contentSpanArr.add(s);
				return;
			}

		} else {
			contentSpanArr.add(s);
			return;
		}
	}

If you have issue ,please by email: wenbohtone@gmail.com
