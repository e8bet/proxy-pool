package com.chenerzhu.crawler.proxy.pool.job.crawler;

import com.chenerzhu.crawler.proxy.pool.entity.ProxyIp;
import com.chenerzhu.crawler.proxy.pool.entity.WebPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author vincent
 * @create 2019-11-11
 * https://list.proxylistplus.com/Fresh-HTTP-Proxy-List-1 Slow sometimes
 **/
@Slf4j
public class ProxyListCrawlerJob extends AbstractCrawler {
    public ProxyListCrawlerJob(ConcurrentLinkedQueue<ProxyIp> proxyIpQueue, String pageUrl) {
        super(proxyIpQueue, pageUrl);
    }

    public ProxyListCrawlerJob(ConcurrentLinkedQueue<ProxyIp> proxyIpQueue, String pageUrl, int pageCount) {
        super(proxyIpQueue, pageUrl, pageCount);
    }

    @Override
    public void parsePage(WebPage webPage) {
        Elements elements = webPage.getDocument().getElementsByTag("tr");
        Element element;
        ProxyIp proxyIp;
        for (int i = 1; i < elements.size(); i++) {
            try {
                element = elements.get(i);
                proxyIp = new ProxyIp();

                proxyIp.setIp(element.child(1).text());
                proxyIp.setPort(Integer.parseInt(element.child(2).text()));
                proxyIp.setLocation(element.child(4).text());
                proxyIp.setType(element.child(3).text());
                proxyIp.setAvailable(true);
                proxyIp.setCreateTime(new Date());
                proxyIp.setLastValidateTime(new Date());
                proxyIp.setValidateCount(0);
                proxyIpQueue.offer(proxyIp);
            } catch (Exception e) {
                log.error("ProxyListCrawlerJob error:{0}",e);
            }
        }
    }

    public static void main(String[] args) {
        ConcurrentLinkedQueue<ProxyIp> proxyIpQueue = new ConcurrentLinkedQueue<>();

        ProxyListCrawlerJob proxyListCrawlerJob = new ProxyListCrawlerJob(proxyIpQueue, "https://list.proxylistplus.com/Fresh-HTTP-Proxy-List-1");

        proxyListCrawlerJob.run();
    }


}