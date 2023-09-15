package com.icezhg.athena.generator;

import com.icezhg.generator.config.Config;
import com.icezhg.generator.data.IdStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zhongjibing on 2023/09/15.
 */
@Component
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {
    /**
     * 代码作者
     */
    private String author = "Author";
    /**
     * 顶级包名
     */
    private String packageName = "generator";

    /**
     * 启用lombok
     */
    private boolean lombokEnable = true;

    /**
     * 启用swagger
     */
    private boolean swaggerEnable = false;

    /**
     * jpa模式
     */
    private boolean jpaEnable = false;

    /**
     * id策略（auto：数据库自增，uuid：生成uuid）
     */
    private IdStrategy idStrategy = IdStrategy.AUTO;

    /**
     * 代码生成路径
     */
    private Config.Path path = new Config.Path();
    /**
     * 数据库配置
     */
    private Config.DataSource dataSource = new Config.DataSource();
    /**
     * 代码文件后缀
     */
    private Config.Name name = new Config.Name();

    private List<String> tables;
    private String ignoreTablePrefix;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isLombokEnable() {
        return lombokEnable;
    }

    public void setLombokEnable(boolean lombokEnable) {
        this.lombokEnable = lombokEnable;
    }

    public boolean isSwaggerEnable() {
        return swaggerEnable;
    }

    public void setSwaggerEnable(boolean swaggerEnable) {
        this.swaggerEnable = swaggerEnable;
    }

    public boolean isJpaEnable() {
        return jpaEnable;
    }

    public void setJpaEnable(boolean jpaEnable) {
        this.jpaEnable = jpaEnable;
    }

    public IdStrategy getIdStrategy() {
        return idStrategy;
    }

    public void setIdStrategy(IdStrategy idStrategy) {
        this.idStrategy = idStrategy;
    }

    public Config.Path getPath() {
        return path;
    }

    public void setPath(Config.Path path) {
        this.path = path;
    }

    public Config.DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(Config.DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Config.Name getName() {
        return name;
    }

    public void setName(Config.Name name) {
        this.name = name;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public String getIgnoreTablePrefix() {
        return ignoreTablePrefix;
    }

    public void setIgnoreTablePrefix(String ignoreTablePrefix) {
        this.ignoreTablePrefix = ignoreTablePrefix;
    }
}
