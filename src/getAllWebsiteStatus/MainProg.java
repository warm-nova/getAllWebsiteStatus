package getAllWebsiteStatus;

import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainProg {

	static HttpURLConnection urlconnection;

	public static boolean isValid(String strLink) 
	{
		URL url;
		try {
			url = new URL(strLink);
			HttpURLConnection connt = (HttpURLConnection)url.openConnection();
			connt.setConnectTimeout(3000);
			connt.setReadTimeout(3000);
			connt.setRequestMethod("GET");
			connt.setRequestProperty("User-agent", "Mozilla/5.0 (Linux; Android 4.4.4; en-us; Nexus 5 Build/JOP40D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2307.2 Mobile Safari/537.36");
			String strMessage = connt.getResponseMessage();
			System.out.println(strMessage);
			if (strMessage.compareTo("Not Found") == 0) {
				return false;
			}
			connt.disconnect();
		} catch (Exception e) {
		return false;
		}
		return true;
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
			Document document = Jsoup.connect("http://h5.mse.360.cn")
					.userAgent(
							"Mozilla/5.0 (Linux; Android 4.4.4; en-us; Nexus 5 Build/JOP40D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2307.2 Mobile Safari/537.36")
					.timeout(5000).get();

			// 获取标题
			String title = document.title();
			System.out.println(title);

			Elements links = document.select("a[href]");
			Elements imports = document.select("link[href]");

			for (Element link : links) {
				String linkaddr = link.attr("abs:href");
				if (linkaddr.contains("haosou") || linkaddr.contains("360"))
					continue;
				System.out.println(linkaddr + "     " + link.text()+"   "+isValid(linkaddr));
			}

		} catch (Exception e)

		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
