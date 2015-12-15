package getAllWebsiteStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvWriter;

public class MainProg extends JFrame {

	static HttpURLConnection urlconnection;
	static ArrayList<String> weblist = new ArrayList<String>();

	public static void read_weblist(String ReadPath) {
		File file = new File(ReadPath);
		BufferedReader reader = null;
		String tmpString = null;
		int i = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((tmpString = reader.readLine()) != null) {
				weblist.add(tmpString);
				System.out.println(weblist.get(i));
				i++;
			}
			reader.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static String isValid(String strLink) {
		URL url;
		try {
			url = new URL(strLink);
			HttpURLConnection connt = (HttpURLConnection) url.openConnection();
			connt.setConnectTimeout(3000);
			connt.setReadTimeout(3000);
			connt.setRequestMethod("GET");
			connt.setRequestProperty("User-agent",
					"Mozilla/5.0 (Linux; Android 4.4.4; en-us; Nexus 5 Build/JOP40D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2307.2 Mobile Safari/537.36");
			String strMessage = connt.getResponseMessage();
			// System.out.println(strMessage);

			if (strMessage.compareTo("Not Found") == 0) {
				return "access error";
			}
			connt.disconnect();
		} catch (Exception e) {
			return "GET METHOD ERROR";
		}
		Document document;
		try {
			document = Jsoup.connect(strLink)
					.userAgent(
							"Mozilla/5.0 (Linux; Android 4.4.4; en-us; Nexus 5 Build/JOP40D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2307.2 Mobile Safari/537.36")
					.timeout(5000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
		return document.title();
	}

	public static int isWebsiteCanAccess(String netAddr) {

		int returnvalue = 0;
		try {
			URL url = new URL(netAddr + "/");
			urlconnection = (HttpURLConnection) url.openConnection();
			int returncode = urlconnection.getResponseCode();
			returnvalue = returncode;
			urlconnection.disconnect();

		} catch (Exception e) {
			returnvalue = -1;
		}
		return returnvalue;
	}

	public static void main(String args[]) {

		try {
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeStart = time.format(new java.util.Date());
			CsvWriter writer = new CsvWriter("./export.csv", ',', Charset.forName("GBK"));

			Document document = Jsoup.connect("http://h5.mse.360.cn")
					.userAgent(
							"Mozilla/5.0 (Linux; Android 4.4.4; en-us; Nexus 5 Build/JOP40D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2307.2 Mobile Safari/537.36")
					.timeout(5000).get();

			// 获取标题
			String title = document.title();
			System.out.println(title);

			Elements links = document.select("a[href]");
			Elements imports = document.select("link[href]");
			read_weblist("./AddrList.txt");

			writer.write("360网址导航链接:");
			writer.endRecord();
			int t = 0;
			for (Element link : links) {
				String linkaddr = link.attr("abs:href");
				if (linkaddr.contains("haosou") || linkaddr.contains("360") || linkaddr.contains("https"))
					continue;
				writer.write(String.valueOf(t));
				writer.write(linkaddr);
				writer.write(isValid(linkaddr));
				writer.endRecord();
				System.out.println(linkaddr + " " + isValid(linkaddr));
				t++;
			}
			// 以上为所有360网址导航的内容
			writer.write("txt导入链接:");
			writer.endRecord();
			for (int i = 0; i < weblist.size(); i++) {
				String linkaddr = weblist.get(i);
				if (linkaddr.contains("https"))
					continue;
				writer.write(String.valueOf(i));
				writer.write(linkaddr);
				writer.write(isValid(linkaddr));
				writer.endRecord();
				System.out.println(linkaddr + " " + isValid(linkaddr));
			}

			String TimeEnd = time.format(new java.util.Date());
			writer.write("开始时间:" + timeStart + "结束时间:" + TimeEnd);
			writer.close();

		} catch (Exception e)

		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
