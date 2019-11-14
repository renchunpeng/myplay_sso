# 功能配置说明

1. redis配置消息队列
    ````
    // 创建redis.xml文件
    
    // 填写配置选项
      redis:
        database: 14
        host: 172.23.100.162
        port: 6379
        password:
    
    // 启动类导入redis配置文件
        @ImportResource("classpath:redis.xml")
    
    // 方法中向redis频道发送消息
        stringRedisTemplate.convertAndSend("tellRobot", objectMapper.writeValueAsString(Singleton.get(User.class)));
    
    // redis.xml中订阅频道自动处理方法
        public class RedisNotice {
            public void handleMessage(String str) {
                System.out.println("接收到robot频道的消息:" + str);
            }
        }
    ````
    
2. spring配合redis实现的缓存

    ````
    // 填写配置选项
          redis:
            database: 14
            host: 172.23.100.162
            port: 6379
            password:
    
    // 启动类开启缓存注解
        @EnableCaching
    
    // 方法中开启缓存注解，注意启用redis缓存必须要加缓存名称
        @RequestMapping("/singletion")
        @ResponseBody
        @Cacheable("user")
        public User testSingleton() {
            User user = Singleton.get(User.class);
            return user;
        }
    ````

3. pageHelper 使用方式
    
    ````
    // Pagination是自己创建的一个对象
    PageInfo<ExamVO> pageInfo = PageHelper.startPage(examSearchDTO.getPageNum(), examSearchDTO.getPageSize())
            .doSelectPageInfo(() -> examMapper.listExam(examSearchDTO.getData()));
    return new Pagination<T>(pageInfo);
    
    // lucene搜索结果构建分页返回结果
    Pagination<Examquestion> examquestionPagination = PageHelperUtils.pagResult(1, 10, topDocs.totalHits, lists);
    
    // Pagination类
    public class Pagination<T> extends PageSerializable<T> {
    
        private static final long serialVersionUID = 6103720062680505647L;
    
        private int pageNum;
        private int pageSize;
        private int pages;
    
        public Pagination(PageInfo<T> pageInfo) {
            this.total = pageInfo.getTotal();
            this.list = pageInfo.getList();
            this.pageNum = pageInfo.getPageNum();
            this.pageSize = pageInfo.getPageSize();
            this.pages = pageInfo.getPages();
        }
    }
    ````
