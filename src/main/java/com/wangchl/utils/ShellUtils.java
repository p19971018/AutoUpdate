package com.wangchl.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Shell文件创建
 * @author Administrator
 *
 */
public class ShellUtils {

	//创建shell
	public static File createShell(String path,String fileName,StringBuffer strs) throws Exception {

		if (strs == null) {
			System.out.println("strs is null");
			return null;
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		File sh = new File(path+fileName);
		if (sh.exists()) {
			sh.delete();
		}
		sh.createNewFile();
		sh.setExecutable(true);
		FileWriter fw = new FileWriter(sh);
		BufferedWriter bf = new BufferedWriter(fw);

		bf.write(strs.toString());
		bf.flush();
		bf.close();
		return sh;
	}

	//执行shell
	public static String runShell(String shpath) throws Exception {

		if (shpath == null || shpath.equals("")) {
			return "shpath is empty";
		}
		Process ps = Runtime.getRuntime().exec(shpath);
		ps.waitFor();

		BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		String result = sb.toString();
		return result;
	}

}
