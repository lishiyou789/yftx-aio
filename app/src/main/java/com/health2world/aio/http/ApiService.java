package com.health2world.aio.http;


import com.health2world.aio.bean.AgreementBean;
import com.health2world.aio.bean.SignService;
import com.health2world.aio.bean.UpgradeInfo;

import java.util.List;
import java.util.Map;

import aio.health2world.http.HttpResult;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lishiyou on 2017/6/26.
 */
interface ApiService {
    /**
     * 登录
     */
    @POST("/api/machine/userAccount/login")
    Observable<HttpResult> login(@Body Map<String, Object> map);

    /**
     * 扫码登录时的二维码
     */
    @POST("/api/machine/userAccount/getQrCode")
    Observable<HttpResult> QRLogin(@Body Map<String, Object> map);

    /**
     * 获取手机验证码
     */
    @POST("/api/sms/send")
    Observable<HttpResult> securityCode(@Body Map<String, Object> map);

    /**
     * 重置密码
     */
    @POST("/api/machine/userAccount/forgetPassWord")
    Observable<HttpResult> resetPwd(@Body Map<String, Object> map);

    /**
     * 注销登录
     */
    @POST("/api/machine/userAccount/logout")
    Observable<HttpResult> logout(@Body Map<String, Object> map);

    /**
     * 版本检验
     */
    @POST("/api/sys/app/validateVersion")
    Observable<HttpResult<UpgradeInfo>> validateVersion(@Body Map<String, Object> map);

    /**
     * 任务列表
     */
    @POST("/machine/task/list")
    Observable<HttpResult> taskList(@Body Map<String, Object> map);

    /**
     * 任务详情
     */
    @POST("/machine/task/detail")
    Observable<HttpResult> taskDetail(@Body Map<String, Object> map);

    /**
     * 任务处理
     */
    @POST("/api/machine/task/addTaskAdvise")
    Observable<HttpResult> taskExecute(@Body Map<String, Object> map);

    /**
     * 获取居民的待处理任务
     *
     * @param map
     * @return
     */
    @POST("/machine/task/pendingTask")
    Observable<HttpResult> getResidentTask(@Body Map<String, Object> map);

    /**
     * 获取标签
     */
    @POST("/api/machine/patientInfo/getTagInfos")
    Observable<HttpResult> getTagList(@Body Map<String, Object> map);


    /**
     * 测量数据的上传（包含了所有的测量项目）
     */
    @POST("/machine/medical/uploadMedicalData")
    Observable<HttpResult> uploadMedicalData(@Body Map<String, Object> map);

    /***
     * 一体机门诊开方
     * @param map
     * @return
     */
    @POST("/api/machine/patientMedicalRecord/add")
    Observable<HttpResult> phoneClinic(@Body Map<String, Object> map);

    /***
     * 添加居民
     * @param map
     * @return
     */
    @POST("/api/machine/patientInfo/addInfo")
    Observable<ResponseBody> addPatientInfo(@Body Map<String, Object> map);

    /**
     * 关联平台已存在的居民
     */
    @POST("machine/patient/familyMember/bindingPatientRelationInfo")
    Observable<HttpResult> relevancyPatientInfo(@Body Map<String, Object> map);

    /***
     * 获取居民个人信息
     * @param map
     * @return
     */
    @POST("/api/machine/patientInfo/info")
    Observable<HttpResult> getPatientInfo(@Body Map<String, Object> map);

    /***
     * 获取家庭信息
     * @param map
     * @return
     */
    @POST("machine/patient/familyMember/getFamilyMember")
    Observable<HttpResult> getFamilyMember(@Body Map<String, Object> map);

    /***
     * 添加家人
     * @param map
     * @return
     */
    @POST("machine/patient/familyMember/addFamilyMember")
    Observable<ResponseBody> addFamilyMember(@Body Map<String, Object> map);

    /***
     * 解绑家人
     */
    @POST("machine/patient/familyMember/relieveFamilyMember")
    Observable<HttpResult> unBindFamilyMember(@Body Map<String, Object> map);

    /***
     * 更新家庭信息
     * @param map
     * @return
     */
    @POST("/api/machine/patientInfo/updateInfo")
    Observable<HttpResult<String>> updatePatientInfo(@Body Map<String, Object> map);

    /**
     * 获取患者二维码
     */
    @POST("/api/machine/patientInfo/getPatientQrCode")
    Observable<HttpResult<String>> getPatientQrCode(@Body Map<String, Object> map);

    /**
     * 获取高级搜索的筛选条件
     */
    @POST("/api/machine/patientInfo/getScreeningLabel")
    Observable<ResponseBody> screeningLabel(@Body Map<String, Object> map);

    /**
     * 居民检索
     */
    @POST("/api/machine/patientInfo/list")
    Observable<HttpResult> residentQuery(@Body Map<String, Object> map);

    /**
     * 居民信息查询
     */
    @POST("/api/machine/patientInfo/info")
    Observable<HttpResult> getPatientInfoById(@Body Map<String, Object> map);

    /**
     * 医生添加居民到自己管辖范围
     */
    @POST("/api/machine/patientInfo/doctorAddPatientInfo")
    Observable<HttpResult> doctorAddPatientInfo(@Body Map<String, Object> map);

    /**
     * 获取服务包详情
     */
    @POST("/api/machine/servicepack/detail")
    Observable<HttpResult<SignService>> getServicePackage(@Body Map<String, Object> map);


    /**
     * 获取服务包（家庭医生  私人诊所）
     *
     * @param map
     * @return
     */
    @POST("/api/machine/servicepack/servicePages")
    Observable<HttpResult<List<SignService>>> getServicePackageList(@Body Map<String, Object> map);

    /**
     * 查询居民公卫是否有档案
     */
    @POST("/api/machine/patientInfo/isRecord")
    Observable<HttpResult> queryHealthFile(@Body Map<String, Object> map);

    /**
     * 履约界面
     */
    @POST("/api/machine/servicepack/patientSignDetail")
    Observable<HttpResult<List<SignService>>> patientSignDetail(@Body Map<String, Object> map);

    /**
     * 获取服务协议内容
     *
     * @param map tokenId patientId
     * @return
     */
    @POST("/api/machine/servicepack/getPatientProtocol")
    Observable<HttpResult<List<AgreementBean>>> patientAgreement(@Body Map<String, Object> map);

    /**
     * 解约服务包
     */
    @POST("/api/machine/servicepack/removeSign")
    Observable<HttpResult> removeSign(@Body Map<String, Object> map);

    /**
     * 家庭签约
     */
    @POST("/api/machine/servicepack/familySign")
    Observable<HttpResult> familySign(@Body Map<String, Object> map);

    /**
     * 校验身份证是否存在平台
     */
    @POST("/api/machine/servicepack/validateIdCard")
    Observable<HttpResult> validateIdCard(@Body Map<String, Object> map);

    /**
     * 校验身份证是否存在平台 (新版本)
     */
    @POST("machine/patient/familyMember/validateIdCard")
    Observable<HttpResult> validateIdentityCard(@Body Map<String, Object> map);

    /**
     * 履约操作 添加服务记录
     */
    @POST("/api/machine/servicepack/addRecord")
    Observable<HttpResult> addServiceRecord(@Body Map<String, Object> map);

    /**
     * 获取居民的健康报告
     */
    @POST("/machine/medical/reportList")
    Observable<HttpResult> healthReport(@Body Map<String, Object> map);

    /***
     * 履约服务记录列表(新接口)
     * @param map
     * @return
     */
    @POST("/machine/servicepack/performanceList")
    Observable<HttpResult> getPerformanceRecord(@Body Map<String, Object> map);

    /**
     * 历史记录数据（新接口）
     */
    @POST("/machine/medical/historyMemory")
    Observable<HttpResult> historyData(@Body Map<String, Object> map);

    /**
     * 标记公卫建档任务已经完成
     */
    @POST("/api/machine/patientInfo/completePhInfo")
    Observable<HttpResult> completePhInfo(@Body Map<String, Object> map);

    /**
     * 验证手机号码是否存在
     */
    @POST("/api/machine/patientInfo/isValidDatePhoneAccount")
    Observable<HttpResult> isValidDatePhoneAccount(@Body Map<String, Object> map);

    /**
     * 指标解读
     */
    @POST("/machine/machine/report/interpret")
    Observable<HttpResult> interpret(@Body Map<String, Object> map);

}
