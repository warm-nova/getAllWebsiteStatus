package getAllWebsiteStatus;

import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainProg {

	static HttpURLConnection urlconnection;

	public static int isWebsiteCanAccess(String netAddr) {
		int count = 0;
		int returnvalue = 0;
		while (count < 3) {
			try {
				URL url = new URL(netAddr + "/");
				urlconnection = (HttpURLConnection) url.openConnection();
				int returncode = urlconnection.getResponseCode();

				if (returncode == 404) {
					urlconnection.disconnect();
					returnvalue = 404;
				} else {
					returnvalue = returncode;
					urlconnection.disconnect();
					break;
				}

			} catch (Exception e) {
				returnvalue = -1;
				continue;
			}
			count++;
		}
		return returnvalue;
	}

	public static void main(String args[]) {
		try {
			Document document = Jsoup.connect("http://h5.mse.360.cn")
					.userAgent(
							"Mozilla/5.0 (Linux; Android 4.4.4; en-us; Nexus 5 Build/JOP40D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2307.2 Mobile Safari/537.36")
					.timeout(5000).post();

			// 获取标题
			String title = document.title();
			System.out.println(title);

			Elements links = document.select("a[href]");
			Elements imports = document.select("link[href]");

			for (Element link : links) {
				String linkaddr = link.attr("abs:href");
				if (linkaddr.contains("haosou") || linkaddr.contains("360") || linkaddr.contains("https"))
					continue;
				System.out.println(linkaddr + "     " + link.text() + "  RETURN:   " + isWebsiteCanAccess(linkaddr));
			}

		} catch (Exception e)

		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
