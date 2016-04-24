package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;
/**
 * 使用GreenDao框架生成Dao文件
 */
public class MyDaoGenerator {
    //辅助文件生成的相对路径
    public static final String DAO_PATH = "E:/android/ZhiHuDaily/app/src/main/java-gen";
    //辅助文件的包名
    public static final String PACKAGE_NAME = "com.example.greendao";
    //数据库的版本号
    public static final int DATA_VERSION_CODE = 1;

    public static void main(String[] args)throws Exception {
        // 创建一个用于添加实体（Entity）的模式（Schema）对象。两个参数分别代表：数据库版本号与自动生成代码的包路径。
        Schema schema = new Schema(DATA_VERSION_CODE, PACKAGE_NAME);
        //可以使用 Schema 对象添加实体（Entities）
        addCache(schema, "NewsContent");
        //生成Dao文件路径,使用 DAOGenerator 类的 generateAll() 方法自动生成代码
        new DaoGenerator().generateAll(schema, DAO_PATH);
    }

    /**
     * 添加不同的缓存表
     * @param schema
     * @param tableName
     */
    private static void addCache(Schema schema, String tableName) {

        Entity joke = schema.addEntity(tableName);

        //主键id自增长
        joke.addIdProperty().primaryKey().autoincrement();
        //
        joke.addStringProperty("newsId");
        //内容
        joke.addStringProperty("content");

    }
}
