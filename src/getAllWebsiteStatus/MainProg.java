package getAllWebsiteStatus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainProg {
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
				System.out.println(link.attr("abs:href") + "     " + link.text());
			}

		} catch (Exception e)

		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
