package cn.edu.usts.SchoolApplication.utils;

import java.io.InputStream;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.io.IOUtils;

public class MD5Utils {
	public static String calcMD5(String string) throws Exception {
		String str[] = new String[] { string };
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("javascript");
		engine.eval(readJSFile());
		Invocable inv = (Invocable) engine;
		String res = (String) inv.invokeFunction("calcMD5", str);
		return res;
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	private static String readJSFile() throws Exception {
		ClassLoader cl = MD5Utils.class.getClassLoader();
		InputStream is = cl.getSystemResource("md5.js").openStream();
		String script = IOUtils.toString(is);
		return script;
	}
}
