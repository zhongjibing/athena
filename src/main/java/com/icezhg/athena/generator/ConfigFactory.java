package com.icezhg.athena.generator;

import com.icezhg.generator.config.Config;
import com.icezhg.generator.data.IdStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by zhongjibing on 2023/09/15.
 */
@Component
public class ConfigFactory {

    private GeneratorProperties properties;

    @Autowired
    public void setProperties(GeneratorProperties properties) {
        this.properties = properties;
    }

    public static ConfigBuilder create() {
        return new ConfigBuilder();
    }

    public static ConfigBuilder defaultBuilder() {
       return null;
    }



    public static class ConfigBuilder {
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

        public ConfigBuilder author(String author) {
            this.author = author;
            return this;
        }

        public ConfigBuilder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public ConfigBuilder lombokEnable(boolean lombokEnable) {
            this.lombokEnable = lombokEnable;
            return this;
        }

        public ConfigBuilder swaggerEnable(boolean swaggerEnable) {
            this.swaggerEnable = swaggerEnable;
            return this;
        }

        public ConfigBuilder jpaEnable(boolean jpaEnable) {
            this.jpaEnable = jpaEnable;
            return this;
        }

        public ConfigBuilder idStrategy(IdStrategy idStrategy) {
            this.idStrategy = idStrategy;
            return this;
        }

        public ConfigBuilder path(Config.Path path) {
            this.path = path;
            return this;
        }

        public ConfigBuilder dataSource(Config.DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public ConfigBuilder name(Config.Name name) {
            this.name = name;
            return this;
        }

        public ConfigBuilder tables(List<String> tables) {
            this.tables = tables;
            return this;
        }

        public ConfigBuilder ignoreTablePrefix(String ignoreTablePrefix) {
            this.ignoreTablePrefix = ignoreTablePrefix;
            return this;
        }

        public Config build() {
            Config config = new Config();
            setter(author, config::setAuthor);
            setter(packageName, config::setPackageName);
            setter(lombokEnable, config::setLombokEnable);
            setter(swaggerEnable, config::setSwaggerEnable);
            setter(jpaEnable, config::setJpaEnable);
            setter(idStrategy, config::setIdStrategy);
            setter(path, config::setPath);
            setter(dataSource, config::setDataSource);
            setter(name, config::setName);
            setter(tables, config::setTables);
            setter(ignoreTablePrefix, config::setIgnoreTablePrefix);
            return config;
        }

        private <T> void setter(T t, Consumer<T> consumer) {
            if (t != null) {
                consumer.accept(t);
            }
        }

    }
}
