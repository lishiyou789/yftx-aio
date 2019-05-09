package com.health2world.aio.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.RequiresApi;

/**
 * 公卫业务扩展内容提供者
 * 所有需要对接公卫的应用都需要提供并实现该内容提供者
 */
public abstract class ExtraProvider extends ContentProvider {

    /**
     * 权限
     */
//    private static final String AUTHORITY = "com.konsung.provider.publichealth.extra";
    private static final String AUTHORITY = "com.healthworld.aio.provider.publichealth";

    /**
     * ExtraProvider 版本
     */
    private static final String PATH_VERSION = "version";
    /**
     * 当前居民信息共享
     */
    private static final String PATH_CITIZEN_SHARE = "currentPatient";

    /**
     * 当前居民最新测量信息共享
     */
    private static final String PATH_MEASURE_DATA_SHARE = "queryMeasureData";

    /**
     * 医生信息共享
     */
    private static final String PATH_USER_DATA_SHARE = "currentDoctor";

    /**
     * 健康一体机管理系统服务器地址共享
     */
    private static final String PATH_IP_ADDRESS_SHARE = "path";

    /**
     * 同步公卫用户过来
     */
    private static final String PATH_ADD_CITIZEN = "insertCitizen";

    /**
     * 皮肤样式
     */
    private static final String PATH_SKIN_STYLE = "skin";

    /**
     * 列名
     */
    private static final String COLUMN_CONFIG = "config";

    /**
     * 匹配器
     */
    private static final UriMatcher MATCHER_URI = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * 当前居民数据共享代码
     */
    private static final int CODE_CITIZEN_SHARE = 1;

    /**
     * 当前居民测量数据共享代码
     */
    private static final int CODE_MEASURE_DATA_SHARE = 2;

    /**
     * 用户、医生信息共享代码
     */
    private static final int CODE_USER_DATA_SHARE = 3;

    /**
     * 服务器地址共享代码
     */
    private static final int CODE_IP_ADDRESS_SHARE = 4;

    /**
     * 公卫居民信息同步到一体机代码
     */
    private static final int CODE_ADD_CITIZEN = 5;

    /**
     * 接口版本代码
     */
    private static final int CODE_VERSION = 6;

    /**
     * 皮肤色系
     *
     * @see SkinStyle
     */
    private static final int CODE_SKIN = 7;

    /**
     * 应用类型
     */
    private static final int CODE_APP_TYPE = 8;

    static {
        MATCHER_URI.addURI(AUTHORITY, PATH_CITIZEN_SHARE, CODE_CITIZEN_SHARE);
        MATCHER_URI.addURI(AUTHORITY, PATH_MEASURE_DATA_SHARE, CODE_MEASURE_DATA_SHARE);
        MATCHER_URI.addURI(AUTHORITY, PATH_USER_DATA_SHARE, CODE_USER_DATA_SHARE);
        MATCHER_URI.addURI(AUTHORITY, PATH_IP_ADDRESS_SHARE, CODE_IP_ADDRESS_SHARE);
        MATCHER_URI.addURI(AUTHORITY, PATH_ADD_CITIZEN, CODE_ADD_CITIZEN);
        MATCHER_URI.addURI(AUTHORITY, PATH_VERSION, CODE_VERSION);
        MATCHER_URI.addURI(AUTHORITY, PATH_SKIN_STYLE, CODE_SKIN);
    }

    /**
     * 构造
     */
    public ExtraProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return -1;
    }

    @Override
    public String getType(Uri uri) {
        switch (MATCHER_URI.match(uri)) {
            case CODE_VERSION: // 协议版本号
                return getVersion();
            case CODE_SKIN: // 公卫皮肤
                return getSkinStyle();
            default:
                break;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (MATCHER_URI.match(uri)) {
            case CODE_ADD_CITIZEN: // 同步居民信息
                return insertCitizen(uri, values);
            default:
                break;
        }
        return uri;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        MatrixCursor cursor = new MatrixCursor(new String[]{COLUMN_CONFIG});
        switch (MATCHER_URI.match(uri)) {
            case CODE_CITIZEN_SHARE: // 共享当前居民
                return queryCurrentCitizen(cursor);
            case CODE_MEASURE_DATA_SHARE: // 测量数据
                return queryCurrentMeasureData(cursor);
            case CODE_USER_DATA_SHARE: // 共享用户数据
                return queryCurrentUser(cursor);
            case CODE_IP_ADDRESS_SHARE: // 共享当前服务器配置
                return queryIpAddress(cursor);
            default:
                return cursor;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return -1;
    }

    /**
     * 向本地插入居民档案
     *
     * @param uri    uri
     * @param values 数据
     * @return 插入后的uri
     */
    abstract Uri insertCitizen(Uri uri, ContentValues values);

    /**
     * 查询当前居民，请返回当前居民信息
     *
     * @param cursor 空数据
     * @return 需要返回的数据
     */
    abstract Cursor queryCurrentCitizen(MatrixCursor cursor);

    /**
     * 查询当前测量数据
     *
     * @param cursor 空数据
     * @return 需要返回的数据
     */
    abstract Cursor queryCurrentMeasureData(MatrixCursor cursor);

    /**
     * 查询当前用户
     *
     * @param cursor 空数据
     * @return 需要返回的数据
     */
    abstract Cursor queryCurrentUser(MatrixCursor cursor);

    /**
     * 查询服务器地址
     *
     * @param cursor 空数据
     * @return 需要返回的数据
     */
    abstract Cursor queryIpAddress(MatrixCursor cursor);

    /**
     * 考虑到后期协议变更，添加协议版本获取逻辑
     * 当前协议版本
     *
     * @return 协议版本
     */
    abstract String getVersion();

    /**
     * 获取皮肤类型
     *
     * @return 皮肤类型
     */
    abstract String getSkinStyle();

    /**
     * 皮肤类型
     */
    public static class SkinStyle {
        /**
         * 天蓝色系
         */
        public static final String SKIN_SKY_BLUE = "0";

        /**
         * LOLLIPOP系统软件色系
         * 使用了LOLLIPOP的特性，需要Api 21
         */
        @RequiresApi(21)
        public static final String SKIN_GREY_LOLLIPOP = "1";

        /**
         * 康尚绿
         */
        public static final String SKIN_KONSUNG_GREEN = "2";
    }

    /**
     * 应用类型
     */
    public static class AppType {
        /**
         * 应用类型-单应用
         */
        public static final String TYPE_SINGLE = "0";

        /**
         * 应用类型-多个应用组合
         */
        public static final String TYPE_COALITION = "1";
    }

    /**
     * 协议版本
     */
    public static class AppVersion {

        public static final String VERSION_1 = "1";

        public static final String VERSION_2 = "2";
    }
}
