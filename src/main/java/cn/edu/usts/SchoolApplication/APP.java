package cn.edu.usts.SchoolApplication;

/**
 * Hello world!
 *
 */
import java.io.IOException;

public class APP {
	public static void main(String[] args) throws IOException, InterruptedException, Exception {
		/**
		 * 填写你的上网账号，密码以及上网连接时长
		 */
		String user = "", passwd = "", timelimit = "180"; // timelimit应当设置10-180之间，单位为分钟
		Login.doLogin(user, passwd, timelimit);
	}
}
