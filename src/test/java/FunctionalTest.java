import org.stj.config.BuildConfig;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author godBless 2022/06/01
 */
public class FunctionalTest {
    
    private String userSql = "CREATE TABLE `user` (\n" +
        "  `id` int(11) NOT NULL COMMENT '用户id',\n" +
        "  `username` varchar(255) NOT NULL COMMENT '用户名',\n" +
        "  `password` varchar(255) NOT NULL COMMENT '密码',\n" +
        "  PRIMARY KEY (`id`)\n" +
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';";
    
    private String sourceCodePath = "src/test/java";
    private String basePackage = "org.stj.test";
    private String resourcePath = "src/test/resources";
    
    /**
     * 删除已存在的源码
     */
    @Before
    public void removeSourceCode() throws Exception {
        Path sourceCode = Paths.get(sourceCodePath + "/" + basePackage.replace(".", "/"));
        Path resource = Paths.get(resourcePath);
        SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            // 先去遍历删除文件
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        
            // 再去遍历删除目录
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(sourceCode, visitor);
        Files.walkFileTree(resource, visitor);
    }
    
    /**
     * 测试全部功能
     */
    @Test
    public void testAll() {
        String sql = userSql;
        BuildConfig config = new BuildConfig();
        config.setSql(sql);
        config.setUseLombok(false);
        // 全局java源码输出路径
        config.setBaseClassPath(sourceCodePath);
        // 业务名字
        config.setBusinessName("User");
        // DO包名
        config.setDoPackageName(basePackage + ".domain");
        // DO的源码输出路径=doBaseClassPath/packageName
        config.setDoBaseClassPath(sourceCodePath);
        // DO的类名
        config.setDoClassName("UserDO");
        // DTO包名
        config.setDtoPackageName(basePackage + ".dto");
        // 如果不设置dtoBaseClasPath则输出路径=baseClassPath/dtoPackageName
//        config.setDtoBaseClassPath(sourceCodePath + "/dto");
        // 如果不设置dtoClassName则类名根据业务名字自动生成
//        config.setDtoClassName("UserDTO");
        // mapper包名
        config.setMapperClassPackageName(basePackage + ".mapper");
        // mapperXml文件路径
        config.setMapperXmlBaseClassPath(resourcePath);
        // service包名
        config.setServiceInterfacePackageName(basePackage + ".service");
        // serviceImpl包名
        config.setServiceImplPackageName(basePackage + ".service.impl");
        // controller包名
        config.setControllerPackageName(basePackage + ".controller");
        SqlToJava.parse(config);
    }
    
    /**
     * 测试联合主键
     */
    @Test
    public void testUnionPrimaryKey() {
        String sql = "CREATE TABLE `user` (\n" +
            "  `id` int(11) NOT NULL COMMENT '用户id',\n" +
            "  `id2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'id1',\n" +
            "  `username` varchar(255) NOT NULL COMMENT '用户名',\n" +
            "  `password` varchar(255) NOT NULL COMMENT '密码',\n" +
            "  PRIMARY KEY (`id`,`id2`) USING BTREE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';";
        BuildConfig config = new BuildConfig();
        config.setSql(sql);
        config.setUseLombok(false);
        config.setBaseClassPath(sourceCodePath);
        config.setBusinessName("User");
    
        config.setDoPackageName(basePackage + ".domain");
    
        config.setDtoPackageName(basePackage + ".dto");
    
        config.setMapperClassPackageName(basePackage + ".mapper");
    
        config.setMapperXmlBaseClassPath(resourcePath);
    
        config.setServiceInterfacePackageName(basePackage + ".service");
    
        config.setServiceImplPackageName(basePackage + ".service.impl");
    
        config.setControllerPackageName(basePackage + ".controller");
        SqlToJava.parse(config);
    }
    
}
