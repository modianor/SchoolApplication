package cn.edu.usts.SchoolApplication;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.edu.usts.SchoolApplication.constant.Constant;
import cn.edu.usts.SchoolApplication.utils.MD5Utils;

public class Login {
	static String username = "", password = "", timelimit = "120";
	static String checkedcode = "", soltcode = "";

	public static void doLogin(String user, String passwd, String time)
			throws IOException, Exception, InterruptedException {
		username = user;
		password = passwd;
		timelimit = time;
		Map<String, String> cookies = getLoginInfo();

		Map<String, String> datas = new TreeMap<String, String>();
		datas.put("Name", username);
		datas.put("rPassword", "");
		datas.put("yianzhengma", checkedcode);
		datas.put("TimeLimit", timelimit);
		datas.put("Password", password);
		Response res = Jsoup.connect(Constant.URL.main_url).referrer(Constant.URL.main_url).timeout(60 * 1000)
				.data(datas).cookies(cookies).ignoreContentType(true).method(Method.POST).execute();
		Document doc = res.parse();
		String case_1 = doc.select(Constant.Selector.case_1).text().trim().toString();
		String case_2 = doc.select(Constant.Selector.case_2).text().trim().toString();

		while (case_1.contains("强制断开") || case_2.contains("被断开帐户名为")) {
			Thread.sleep(1000);
			cookies = getLoginInfo();
			res = Jsoup.connect(Constant.URL.main_url).timeout(60 * 1000).data(datas).cookies(cookies)
					.ignoreContentType(true).referrer(Constant.URL.main_url).method(Method.POST).execute();
			doc = res.parse();
			case_1 = doc.select(Constant.Selector.case_1).text().trim().toString();
			case_2 = doc.select(Constant.Selector.case_2).text().trim().toString();
		}

		String flag_1 = doc.select(Constant.Selector.flag_1).text().trim().toString();
		String flag_2 = doc.select(Constant.Selector.flag_2).text().trim().toString();
		System.out.println("账户：" + username);
		System.out.println("密码：" + password);
		System.out.println("验证码：" + checkedcode);
		System.out.println(doc.html());
		if (flag_1 != null && !flag_1.equals("") && flag_1.contains("免费标准")) {
			System.out.println("恭喜您，登录成功！");
			System.exit(0);
		}

		if (flag_2 != null && !flag_2.equals("") && flag_2.contains("输入有误")) {
			System.out.println("恭喜您，离线成功！");
			System.exit(0);
		}

	}

	public static Map<String, String> getLoginInfo() throws IOException, Exception {
		Response res = Jsoup.connect(Constant.URL.main_url).timeout(60 * 1000).ignoreContentType(true)
				.method(Method.GET).execute();
		Map<String, String> cookies = res.cookies();
		Document doc = res.parse();
		checkedcode = doc.select(Constant.Selector.checkcode).text().trim().toString();
		soltcode = doc.select(Constant.Selector.soltcode).attr("onclick").trim().toString();
		int start = soltcode.indexOf((char) '\'', 0);
		int end = soltcode.indexOf((char) '\'', start + 1);
		soltcode = soltcode.substring(start + 1, end);
		password = MD5Utils.calcMD5(MD5Utils.calcMD5(password + soltcode));
		return cookies;
	}

}
