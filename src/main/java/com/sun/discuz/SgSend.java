package com.sun.discuz;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

public class SgSend {

	public static void main(String[] args) {
		autoReply("cookie");
	}

	public static void autoReply(String cookie) {
		try {
			while (true) {
				// 获取第一页
				String html = WebClient.sgSendGet("http://bbs.sgamer.com/forum-44-1.html", null);
				// 获取html代码后按照格式分割
				String[] arrs = html
						.split("forum.php\\?mod\\=forumdisplay\\&fid\\=44\\&amp\\;filter\\=typeid&amp\\;typeid\\=95");
				// 所有当前页的帖子
				for (int i = 1; i < arrs.length; i++) {
					// 地址
					String index = arrs[i].substring(31, 39);
					// 所有回复过的帖子
					InputStream is = new FileInputStream("F://html.txt");
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					int tag = 0;
					while (true) {
						String str = reader.readLine();
						if (str != null && !str.equals("")) {
							if (str.equals(index)) {
								tag++;
							}
						} else {
							if (tag == 0) {
								// 获取1楼的回复
								String htmlMain = WebClient
										.sgSendGet("http://bbs.sgamer.com/thread-" + index + "-1-1.html", null);
								String[] arrss = htmlMain
										.split("<table cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"t_f\"");
								String sub = "";
								if (arrss.length > 1) {
									for (int x = 1; x < arrss.length; x++) {
										int endIndex = arrss[x].indexOf("</td>");
										sub = arrss[x].substring(27, endIndex);
										if (sub.contains("<") || sub.contains("&nbsp;")) {
											sub = "";
											continue;
										} else
											break;
									}
								}
								if (sub.equals("")) {
									reader.close();
									break;
								}
								HashMap<String, Object> param = new HashMap<String, Object>();
								param.put("message", sub);
								param.put("posttime", new Date().getTime());
								param.put("formhash", "300e7f93");
								param.put("usesig", "1");
								param.put("subject", "");
								WebClient.sgSendPost(
										"http://bbs.sgamer.com/forum.php?mod=post&action=reply&fid=44&tid=" + index
												+ "&extra=page%3D1&replysubmit=yes&infloat=yes&handlekey=fastpost&inajax=1",
										param, cookie);
								PrintWriter pw = new PrintWriter(new FileWriter("F://html.txt", true), true);
								pw.println(index);
								pw.close();
								Thread.sleep(40000);
							}
							reader.close();
							break;
						}

					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
