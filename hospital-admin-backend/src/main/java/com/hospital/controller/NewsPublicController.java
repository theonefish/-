package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.entity.News;
import com.hospital.service.NewsService;
import com.hospital.vo.PageVo;
import com.hospital.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 公开新闻接口 - 供小程序游客访问
 * 支持公告(notice)和健康科普(health)两种类型
 */
@RestController
@RequestMapping("/news")
public class NewsPublicController {

    @Autowired
    private NewsService newsService;

    /**
     * 模拟新闻数据 - 当数据库中没有新闻数据时使用
     * type: notice = 医院公告, health = 健康科普
     */
    private List<Map<String, Object>> getMockNews() {
        List<Map<String, Object>> list = new ArrayList<>();

        // ========== 医院公告 (notice) ==========
        Map<String, Object> n1 = new HashMap<>();
        n1.put("newsId", 1);
        n1.put("id", 1);
        n1.put("title", "关于调整部分门诊挂号费用的通知");
        n1.put("textDesc", "根据上级部门要求，我院将于下月起调整部分科室挂号费用，具体调整方案请查看详情。");
        n1.put("textContent", "尊敬的各位患者：根据上级部门要求，我院将于2024年6月1日起调整部分科室挂号费用。具体调整方案如下：普通门诊挂号费由10元调整为12元，专家门诊挂号费由30元调整为35元。特此通知，感谢您的理解与支持。");
        n1.put("createTime", LocalDateTime.now().minusDays(0));
        n1.put("image", "/static/news1.jpg");
        n1.put("toIndex", "1");
        n1.put("type", "notice");
        n1.put("content", n1.get("textContent"));
        n1.put("cover", n1.get("image"));
        n1.put("source", "康华医院院办");
        n1.put("time", "刚刚");
        list.add(n1);

        Map<String, Object> n2 = new HashMap<>();
        n2.put("newsId", 2);
        n2.put("id", 2);
        n2.put("title", "我院引进最新核磁共振设备，检查更精准");
        n2.put("textDesc", "为提升诊疗水平，我院最新引进3.0T核磁共振设备，成像更清晰，检查时间更短。");
        n2.put("textContent", "我院最新引进西门子3.0T核磁共振设备，具有超高场强、超快速成像等特点，可为神经系统、骨关节等疾病的诊断提供更精准的影像支持。欢迎有需要的患者前来咨询预约。");
        n2.put("createTime", LocalDateTime.now().minusDays(2));
        n2.put("image", "/static/news3.jpg");
        n2.put("toIndex", "1");
        n2.put("type", "notice");
        n2.put("content", n2.get("textContent"));
        n2.put("cover", n2.get("image"));
        n2.put("source", "康华医院院办");
        n2.put("time", "2天前");
        list.add(n2);

        Map<String, Object> n3 = new HashMap<>();
        n3.put("newsId", 3);
        n3.put("id", 3);
        n3.put("title", "儿科专家联合坐诊，守护儿童健康");
        n3.put("textDesc", "本周六，我院儿科将邀请三甲医院专家联合坐诊，为儿童提供专业的诊疗服务。");
        n3.put("textContent", "本周六（上午8:00-12:00），我院儿科将邀请市儿童医院主任医师张教授联合坐诊。擅长儿童呼吸系统、消化系统疾病的诊治。名额有限，请提前预约。");
        n3.put("createTime", LocalDateTime.now().minusDays(3));
        n3.put("image", "/static/news4.jpg");
        n3.put("toIndex", "0");
        n3.put("type", "notice");
        n3.put("content", n3.get("textContent"));
        n3.put("cover", n3.get("image"));
        n3.put("source", "康华医院院办");
        n3.put("time", "3天前");
        list.add(n3);

        Map<String, Object> n4 = new HashMap<>();
        n4.put("newsId", 4);
        n4.put("id", 4);
        n4.put("title", "健康体检套餐优惠活动开始啦");
        n4.put("textDesc", "为回馈广大市民，我院推出年度健康体检优惠套餐，包含全面体检项目。");
        n4.put("textContent", "为回馈广大市民，我院推出年度健康体检优惠套餐，包含血常规、尿常规、肝功能、肾功能、心电图、B超等全面体检项目。活动期间预约可享受8折优惠，欢迎前来咨询。");
        n4.put("createTime", LocalDateTime.now().minusDays(5));
        n4.put("image", "/static/news1.jpg");
        n4.put("toIndex", "0");
        n4.put("type", "notice");
        n4.put("content", n4.get("textContent"));
        n4.put("cover", n4.get("image"));
        n4.put("source", "康华医院院办");
        n4.put("time", "5天前");
        list.add(n4);

        // ========== 健康科普 (health) ==========
        Map<String, Object> h1 = new HashMap<>();
        h1.put("newsId", 5);
        h1.put("id", 5);
        h1.put("title", "春季流感高发期，这份防护指南请收好！");
        h1.put("textDesc", "春季是流感高发季节，呼吸内科王医生为您整理了详细的防护指南，帮助您和家人远离流感。");
        h1.put("textContent", "春季流感高发期防护指南：\n\n1. 勤洗手，保持手部卫生\n2. 戴口罩，避免去人群密集场所\n3. 多喝水，保持充足睡眠\n4. 如出现发热、咳嗽等症状，请及时就医\n\n此外，建议老年人和儿童接种流感疫苗，增强免疫力。");
        h1.put("createTime", LocalDateTime.now().minusDays(1));
        h1.put("image", "/static/news2.jpg");
        h1.put("toIndex", "1");
        h1.put("type", "health");
        h1.put("content", h1.get("textContent"));
        h1.put("cover", h1.get("image"));
        h1.put("source", "呼吸内科 王医生");
        h1.put("time", "1天前");
        list.add(h1);

        Map<String, Object> h2 = new HashMap<>();
        h2.put("newsId", 6);
        h2.put("id", 6);
        h2.put("title", "夏季防暑降温小常识");
        h2.put("textDesc", "夏季高温来临，如何预防中暑？我院急诊科医生为您支招。");
        h2.put("textContent", "夏季防暑降温小常识：\n\n1. 避免在烈日下长时间活动\n2. 多喝水，补充电解质\n3. 穿着宽松透气的衣物\n4. 如出现头晕、恶心等中暑症状，请立即到阴凉处休息并就医\n\n特别提醒：高温天气下，户外工作者应注意劳逸结合。");
        h2.put("createTime", LocalDateTime.now().minusDays(7));
        h2.put("image", "/static/news2.jpg");
        h2.put("toIndex", "0");
        h2.put("type", "health");
        h2.put("content", h2.get("textContent"));
        h2.put("cover", h2.get("image"));
        h2.put("source", "急诊科 李医生");
        h2.put("time", "7天前");
        list.add(h2);

        Map<String, Object> h3 = new HashMap<>();
        h3.put("newsId", 7);
        h3.put("id", 7);
        h3.put("title", "高血压患者日常饮食注意事项");
        h3.put("textDesc", "高血压是常见慢性病，合理饮食对控制血压至关重要。心内科专家为您详细讲解。");
        h3.put("textContent", "高血压患者饮食注意事项：\n\n1. 低盐饮食，每日食盐不超过6克\n2. 多吃新鲜蔬菜水果\n3. 限制高脂肪、高胆固醇食物\n4. 适量补充钾、钙、镁\n5. 戒烟限酒\n\n建议定期监测血压，遵医嘱服药。");
        h3.put("createTime", LocalDateTime.now().minusDays(4));
        h3.put("image", "/static/news3.jpg");
        h3.put("toIndex", "0");
        h3.put("type", "health");
        h3.put("content", h3.get("textContent"));
        h3.put("cover", h3.get("image"));
        h3.put("source", "心内科 张主任");
        h3.put("time", "4天前");
        list.add(h3);

        Map<String, Object> h4 = new HashMap<>();
        h4.put("newsId", 8);
        h4.put("id", 8);
        h4.put("title", "儿童近视防控，家长必看！");
        h4.put("textDesc", "近年来儿童近视率逐年上升，眼科专家教您如何保护孩子的视力。");
        h4.put("textContent", "儿童近视防控指南：\n\n1. 控制电子产品使用时间，每次不超过20分钟\n2. 保证每天2小时以上户外活动\n3. 保持正确读写姿势，眼睛离书本一尺远\n4. 定期检查视力，建立视力档案\n5. 保证充足睡眠，均衡饮食\n\n如发现孩子眯眼、歪头看东西，请及时就医。");
        h4.put("createTime", LocalDateTime.now().minusDays(6));
        h4.put("image", "/static/news4.jpg");
        h4.put("toIndex", "0");
        h4.put("type", "health");
        h4.put("content", h4.get("textContent"));
        h4.put("cover", h4.get("image"));
        h4.put("source", "眼科 赵医生");
        h4.put("time", "6天前");
        list.add(h4);

        Map<String, Object> h5 = new HashMap<>();
        h5.put("newsId", 9);
        h5.put("id", 9);
        h5.put("title", "糖尿病患者如何科学运动");
        h5.put("textDesc", "运动是糖尿病管理的重要环节，但需要注意方法和强度。内分泌科医生为您指导。");
        h5.put("textContent", "糖尿病患者运动建议：\n\n1. 选择中等强度有氧运动，如快走、游泳\n2. 每周至少150分钟，分5次进行\n3. 运动前后监测血糖\n4. 避免空腹运动，随身携带糖果\n5. 足部保护，选择合适的运动鞋\n\n运动计划应在医生指导下制定。");
        h5.put("createTime", LocalDateTime.now().minusDays(8));
        h5.put("image", "/static/news1.jpg");
        h5.put("toIndex", "0");
        h5.put("type", "health");
        h5.put("content", h5.get("textContent"));
        h5.put("cover", h5.get("image"));
        h5.put("source", "内分泌科 刘医生");
        h5.put("time", "8天前");
        list.add(h5);

        Map<String, Object> h6 = new HashMap<>();
        h6.put("newsId", 10);
        h6.put("id", 10);
        h6.put("title", "颈椎病的预防与康复锻炼");
        h6.put("textDesc", "长期伏案工作容易导致颈椎病，骨科专家教您几个简单有效的预防动作。");
        h6.put("textContent", "颈椎病预防与康复：\n\n1. 保持正确坐姿，电脑屏幕与眼睛平齐\n2. 每工作1小时起身活动5-10分钟\n3. 做颈部保健操：前后左右缓慢转动\n4. 选择合适的枕头，避免过高或过低\n5. 注意颈部保暖，避免受凉\n\n如已出现手麻、头晕等症状，请及时就诊。");
        h6.put("createTime", LocalDateTime.now().minusDays(10));
        h6.put("image", "/static/news3.jpg");
        h6.put("toIndex", "0");
        h6.put("type", "health");
        h6.put("content", h6.get("textContent"));
        h6.put("cover", h6.get("image"));
        h6.put("source", "骨科 陈医生");
        h6.put("time", "10天前");
        list.add(h6);

        return list;
    }

    /**
     * 公开的新闻列表接口 - 无需登录即可访问
     * @param type 类型：notice = 医院公告, health = 健康科普, 不传则返回全部
     */
    @GetMapping("/list")
    public ResultVo<PageVo<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type) {

        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(News::getTitle, keyword);
        }
        wrapper.orderByDesc(News::getCreateTime);
        Page<News> result = newsService.page(new Page<>(page, pageSize), wrapper);

        // 如果数据库中没有新闻数据，返回模拟数据
        if (result.getTotal() == 0) {
            List<Map<String, Object>> mockList = getMockNews();

            // 按类型过滤
            if (type != null && !type.isEmpty()) {
                mockList = mockList.stream()
                        .filter(n -> type.equals(n.get("type")))
                        .collect(Collectors.toList());
            }

            // 关键词过滤
            if (keyword != null && !keyword.isEmpty()) {
                String kw = keyword.toLowerCase();
                mockList = mockList.stream()
                        .filter(n -> {
                            String title = (String) n.get("title");
                            return title != null && title.toLowerCase().contains(kw);
                        })
                        .collect(Collectors.toList());
            }

            // 分页处理
            int start = (int) ((page - 1) * pageSize);
            int end = (int) Math.min(start + pageSize, mockList.size());
            List<Map<String, Object>> pageRecords = start < mockList.size()
                    ? mockList.subList(start, end)
                    : new ArrayList<>();
            return ResultVo.success(PageVo.of(pageRecords, (long) mockList.size(), page, pageSize));
        }

        List<Map<String, Object>> records = result.getRecords().stream().map(n -> {
            Map<String, Object> m = new HashMap<>();
            m.put("newsId", n.getId());
            m.put("id", n.getId());
            m.put("title", n.getTitle());
            m.put("textDesc", n.getTextDesc());
            m.put("textContent", n.getTextContent());
            m.put("createTime", n.getCreateTime());
            m.put("image", n.getImage());
            m.put("toIndex", n.getToIndex());
            // 兼容字段 - 给小程序用
            m.put("content", n.getTextContent());
            m.put("cover", n.getImage());
            m.put("source", "康华医院院办");
            m.put("time", formatTime(n.getCreateTime()));
            return m;
        }).collect(Collectors.toList());

        return ResultVo.success(PageVo.of(records, result.getTotal(), page, pageSize));
    }

    /**
     * 公开的新闻详情接口 - 无需登录即可访问
     */
    @GetMapping("/detail/{id}")
    public ResultVo<Map<String, Object>> detail(@PathVariable Integer id) {
        News news = newsService.getById(id);
        // 如果数据库中没有，尝试从模拟数据中查找
        if (news == null) {
            List<Map<String, Object>> mockList = getMockNews();
            for (Map<String, Object> m : mockList) {
                if (id.equals(m.get("id"))) {
                    return ResultVo.success(m);
                }
            }
            return ResultVo.error("资讯不存在");
        }
        Map<String, Object> m = new HashMap<>();
        m.put("newsId", news.getId());
        m.put("id", news.getId());
        m.put("title", news.getTitle());
        m.put("textDesc", news.getTextDesc());
        m.put("textContent", news.getTextContent());
        m.put("createTime", news.getCreateTime());
        m.put("image", news.getImage());
        m.put("toIndex", news.getToIndex());
        m.put("content", news.getTextContent());
        m.put("cover", news.getImage());
        m.put("source", "康华医院院办");
        m.put("time", formatTime(news.getCreateTime()));
        return ResultVo.success(m);
    }

    /**
     * 格式化时间显示
     */
    private String formatTime(java.time.LocalDateTime time) {
        if (time == null) return "";
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.Duration duration = java.time.Duration.between(time, now);
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < 60) {
            return minutes + "分钟前";
        } else if (hours < 24) {
            return hours + "小时前";
        } else if (days < 30) {
            return days + "天前";
        } else {
            return time.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }
}
