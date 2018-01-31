package com.zhangyingwei.spiders.konwledge.store;

import com.zhangyingwei.cockroach.executer.response.TaskResponse;
import com.zhangyingwei.cockroach.executer.task.Task;
import com.zhangyingwei.cockroach.store.IStore;
import com.zhangyingwei.spiders.konwledge.cache.CacheMap;
import com.zhangyingwei.spiders.konwledge.config.ItemConfig;
import com.zhangyingwei.spiders.konwledge.config.WebSiteConfig;
import com.zhangyingwei.spiders.konwledge.model.Konwledge;

/**
 * @author: zhangyw
 * @date: 2018/1/30
 * @time: 下午8:26
 * @desc:
 */
public class KonwledgeStore implements IStore {

    private CacheMap cacheMap = CacheMap.getIns();

    @Override
    public void store(TaskResponse response) throws Exception {
        WebSiteConfig config = (WebSiteConfig) response.getTask().getExtr();
        ItemConfig itemConfig = config.getItemConfig();

        System.out.println("获取成功：" + config.getGroup());

        response.select(config.getItemCss()).stream().limit(config.getLimit()).forEach(element -> {
            String title = element.select(itemConfig.getTitle()).text();
            String url = element.select(itemConfig.getUrl()).attr("href");
            String desc = element.select(itemConfig.getDesc()).text();

            Konwledge konwledge = new Konwledge();
            konwledge.setTitle(title);
            konwledge.setUrl(url);
            konwledge.setDesc(desc);
            konwledge.setEmails(config.getEmails());
            konwledge.setGroup(config.getGroup());

            if (konwledge.isEmpty()) {
                System.out.println("|-title-| " + konwledge.getTitle());
                cacheMap.put(config.getGroup(),konwledge);
            }
        });
    }
}