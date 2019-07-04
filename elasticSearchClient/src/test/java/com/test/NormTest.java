package com.test;

import com.common.base.BaseEntity;
import com.common.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.xc.elastic.client.ElasticClient;
import com.xc.elastic.client.ElasticEntity;
import com.xc.elastic.client.SearchPage;
import com.xc.elastic.client.SearchResult;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NormTest {
    ElasticClient client = ElasticClient.create("127.0.0.1",9200);

    public static class ArticleTestModel extends BaseEntity {
        public static final TypeToken<List<ArticleTestModel>> LIST_TYPE_TOKEN = new TypeToken<List<ArticleTestModel>>(){};
        private String title;
        private String content;
        private String createTime;
        private String author;
        private String description;
        private String keyWord;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }
    }

    @Test
    public void testCreateIndex() throws IOException {
        Map<String,Object> map = new HashMap<>();
        for(int i=1; i<99; i++) {
            ElasticEntity elasticEntity = ElasticEntity.of("test","id" + i);
            map.put("name", "姓名" + i);
            map.put("age", i);
            client.createIndex(elasticEntity, GsonUtils.toJson(map));
        }
    }

    @Test
    public void testCreateIndex2() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("name", "【商务部：物价涨势总体是温和的】商务部市场运行司副司长王斌在发布会上表示，" +
                "我们的消费的基本面非常稳固的，前一阶段水果蔬菜价格出现一定这个幅度的上涨，但是近期时令水果上市价格都是逐步回落的，" +
                "物价涨势总体是温和的。\n" +
                "【商务部：消费市场平稳运行 上半年增速有望达8.2%】 商务部市场运行司副司长王斌今日在发布会上表示，" +
                "消费市场总体呈现平稳运行的态势，6月份消费市场有望继续平稳回升，上半年，乐观预计，" +
                "消费增速有望达到8.2%，仍处于中高速的增长区间。下一步我们将根据消费市场的变化，" +
                "进一步加强政策的跟踪评估。中国中等收入群体达到一亿人左右，各种新消费、新业态、" +
                "新模式，创新不断涌现，消费市场潜力大，韧性强，活力足，成长性好。我们相信，" +
                "消费稳健发展的基本面不会变，消费体制升级的总势头不会变，消费长期向好的大趋势不会改变。");
        client.createIndex(ElasticEntity.of("test","idt1"), GsonUtils.toJson(map));
        map.put("name", "近期，三伏天虽没到，但高温已成为全国多地天气的主旋律。此外，" +
                "1日生成的南海热带低压，预计或于未来24小时内发展为今年第4号台风，并将于2日夜间到3日凌晨在海南岛沿海登陆，" +
                "广东、海南、广西等地将迎来强降雨天气。北方多地高温持续，南方将有台风侵扰、暴雨来袭......你家那儿天气还好吗？\n" +
                "北方高温热浪滚滚   局地将达40℃\n" +
                "这几天，高温占据了北方多地天气的主旋律，不仅持续多天，而且范围还不断扩大、强度逐步增强。中央气象台预计，" +
                "从今天至5日，北京、天津、河北、山西、河南、山东的部分地区最高气温将达到35～37℃，其中，3日至4日，" +
                "天津、河北、河南、山东部分地区的最高气温甚至能达到38～40℃。");
        client.createIndex(ElasticEntity.of("test","idt2"), GsonUtils.toJson(map));
    }

    @Test
    public void get() throws IOException {
        ElasticEntity elasticEntity = ElasticEntity.of("article","251");
        String result = client.get(elasticEntity);
        System.out.println(result);
    }

    @Test
    public void update() throws IOException {
        ElasticEntity elasticEntity = ElasticEntity.of("test","id1");
        System.out.println("原始数据："+client.get(elasticEntity));
        Map<String,Object> map = new HashMap<>();
        map.put("name", "我修改了" );
        map.put("age", 999);
        client.update(elasticEntity, GsonUtils.toJson(map));
        System.out.println("修改后：" + client.get(elasticEntity));
    }

    @Test
    public void del() throws  IOException {
        ElasticEntity elasticEntity = ElasticEntity.of("test","id1");
        client.delete(elasticEntity);
        System.out.println(client.get(elasticEntity));
    }


    @Test
    public void searchPage() throws IOException {
        for(int page = 0; page <11; page ++) {
            System.out.println("第"+ (page + 1) + "页");
            String result = GsonUtils.anotherToJson(client.search(new String[]{"name"}, "姓名", new String[]{"test"},SearchPage.of(page)).getResultList());
            System.out.println(result);
        }
    }

    @Test
    public void addInfo() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("title", "中国农科院：基因编辑助大豆在南方丰产");
        map.put("content","科技日报北京7月2日电 （记者瞿剑）据中国农科院最新消息，" +
                "该院作物科学研究所植物转基因技术研究中心、大豆育种技术创新与新品种选育创新团队，" +
                "利用CRISPR/Cas9基因组编辑技术定点敲除大豆开花调控关键基因GmFT2a和GmFT5a，" +
                "创制出更适合低纬度地区种植的突变体材料；同时系统解析了GmFT2a和GmFT5a基因在大豆花期调控中的遗传效应，" +
                "为大豆品种的区域适应性改良提供了新技术、新材料。相关研究成果新近在线发表于《植物生物技术杂志（Plant Biotechnology Journal）》。\n" +
                "团队首席、中国农科院作科所研究员韩天富介绍，大豆对光周期反应敏感，绝大多数品种只有在日照长度缩短到一定限度后，" +
                "才能从营养生长转入生殖生长，进而开花结荚；导致大豆品种北移种植时往往晚花晚熟、生长期延长，甚至不能开花或正常成熟；" +
                "南移种植时则一般表现为过早开花、生长期缩短，产量降低甚至不能正常生长。这种光周期反应特性使得大豆品种适宜种植区域普遍比较狭小。" +
                "已有研究表明，GmFT2a和GmFT5a基因是大豆的重要开花促进因子。是否有条件在全国推广");
        map.put("createTime","2019-07-03 11:45:00");
        map.put("author","央广网");
        map.put("keyWord","起向 基因");
        map.put("description","基因编辑助大豆在南方丰产");
        client.createIndex(ElasticEntity.of("article","a1"), GsonUtils.toJson(map));

        map.put("title", "江苏省支持探索“2.5天小长假”");
        map.put("content","10余省份出台鼓励2.5天休假意见；专家表示：拉动作用将主要体现在家庭消费和服务性消费方面\n" +
                "7月1日，江苏省委办公厅印发激发居民消费潜能的实施意见，促进十大领域消费。政策支持部分提出探索在有条件的地区实施“2.5天小长假”。\n" +
                "在这份名为《关于完善促进消费体制机制进一步激发居民消费潜力的实施意见》中，江苏省提出探索“2.5天小长假”成为舆论焦点。但记者注意到，此举是作为“政策支持”出现在该份意见中的。\n" +
                "起向“2.5天小长假”一般是指周五下午加上双休日的休假方式。\n" +
                "“放假”能够支持消费实现增长吗？能够拉动多大规模的增长？是否有条件在全国推广？是否会对生产力产生负面影响？\n" +
                "中央财经大学中国公共财政与政策研究院院长乔宝云认为，国内消费不足是我们未来面临的主要挑战，江苏作为经济较发达省份，提出探索2.5天小长假支持扩大消费有一定道理，但没有全国推广的必要。交通银行首席经济学家连平也有此观点，认为通过“放假”来促进消费，效果有限。");
        map.put("createTime","2019-07-03 11:45:00");
        map.put("author","新京报");
        map.put("keyWord","小长假、江苏省");
        map.put("description","10余省份出台鼓励2.5天休假意见");
        client.createIndex(ElasticEntity.of("article","a2"), GsonUtils.toJson(map));

        map.put("title", "中国江苏省支持探索“2.5天小长假”");
        map.put("content","10余省份出台鼓励2.5天休假意见；");
        map.put("createTime","2019-07-03 11:45:00");
        map.put("author","新京报中国");
        map.put("keyWord","小长假、江苏省");
        map.put("description","10余省份出台鼓励2.5天休假意见");
        client.createIndex(ElasticEntity.of("article","a3"), GsonUtils.toJson(map));
    }

    @Test
    public void search2() throws IOException {
       SearchResult<String> result = client.search(new String[]{"title","keyWord","author","description","content"}, "起向", new String[]{"article"},SearchPage.NULL_PAGE);
       result.getResultList().forEach(System.out::println);
        System.out.println(result.toString());
    }

    @Test
    public void search3() throws IOException {
        SearchResult<ArticleTestModel> result = client.search(new String[]{"title","keyWord","author","description","content"},
                "起向", new String[]{"article"},SearchPage.NULL_PAGE, ArticleTestModel.LIST_TYPE_TOKEN);
        result.getResultList().forEach( r -> System.out.println(r.getContent()));
    }
}
