package testcase;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ConfigFile;
import utils.FilesUtils;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 小程序  重写是因为对于平台地址和其他地址不一致
 */
public class baoLiApplet extends BaseTestCase {

    //excel测试用例
    private static String fileName = "小程序自动化.xls";
    //执行地址
    private static String platformUrl = ConfigFile.getValue("applet.url");
    //小程序只司机操作
    private static final String APPLETDRIVER = "appletdriver";
    //小程序客户操作
    private static final String APPLETCLIENT = "appletclient";
    //库管操作
    private static final String STOREKEEPER = "storekeeper";
    private static String userId = "5555701"; //客户的用户 gzx
    private static String driverId = "96"; //自动化测试司机
    private static String userkgId = "5555707"; //库管用户  gzx库管
    /**
     * 每个用例之前拿excel文件
     * @param method
     */
    @BeforeMethod(alwaysRun = true)
    private void getFileRow(Method method) {
        HashMap<String,String> fileRow = new HashMap<String,String>();
        fileRow = FilesUtils.readExcelGetRow(fileName, method.getName());
        setFileEntity(platformUrl, fileRow);
        log.info("**************开始执行测试用例：" + fileEntity.getTestCaseName() + "**************");
    }


    /**
     * 打印结束执行
     */
    @AfterMethod(alwaysRun = true)
    private void printLog() {
        log.info("**************结束执行测试用例：" + fileEntity.getTestCaseName() + "**************");
    }

    /**************************小程序********************************/

    /**
     * 小程序列表
     */
    @Test(description = "小程序列表", groups = {APPLETDRIVER, APPLETCLIENT})
    private void test_001_waybillTaskDetail() {
        //todo 假数据
        fileEntity.getBodyParam().put("userId", "109");
        //fileEntity.getBodyParam().put("userId", driverId);
        startPerformTest();
//        saveResponseParameters("result.data[0].waybillId");
    }

    /**
     * 接受任务
     */
    @Test(description = "接受任务", groups = {APPLETDRIVER, APPLETCLIENT})
    private void test_002_isAcceptWaybill() {
        //todo 假数据
        fileEntity.getBodyParam().put("userId", "110");
        fileEntity.getBodyParam().put("waybillId", "865");
        //fileEntity.getBodyParam().put("userId", driverId);
        //replaceBodyParam("waybillId");
        startPerformTest();
    }

    /**
     * 进场
     */
    @Test(description = "进厂", groups = {APPLETDRIVER, APPLETCLIENT})
    private void test_003_isPassInOrOut() {
        //todo 假数据
        fileEntity.getBodyParam().put("userId", "110");
        fileEntity.getBodyParam().put("waybillId", "714");
        /*fileEntity.getBodyParam().put("userId", driverId);
        replaceBodyParam("waybillId");*/
        startPerformTest();
    }

    /**
     * 司机装货录入
     */
    @Test(description = "司机装货录入", groups = {APPLETDRIVER, APPLETCLIENT})
    private void test_004_poundEntry() {
       /* fileEntity.getBodyParam().put("userId", driverId);
        replaceBodyParam("waybillId");*/
        //todo 假数据
        fileEntity.getBodyParam().put("userId", "110");
        fileEntity.getBodyParam().put("waybillId", "717");
        startPerformTest();
    }

    /**
     * 异常上报
     */
    @Test(description = "异常上报", groups = {APPLETDRIVER})
    private void test_005_exceptReport() {
        fileEntity.getBodyParam().put("userId", driverId);
        replaceBodyParam("waybillId");
        startPerformTest();
    }

    /**
     * 出厂
     */
    @Test(description = "出厂", groups = {APPLETDRIVER})
    private void test_006_isPassInOrOut() {
        //todo 假数据
        fileEntity.getBodyParam().put("userId", "109");
        fileEntity.getBodyParam().put("waybillId", "698");
        /*fileEntity.getBodyParam().put("userId", driverId);
        replaceBodyParam("waybillId");*/
        startPerformTest();
    }

    /**
     * 出客户
     */
    @Test(description = "出客户", groups = {APPLETDRIVER})
    private void test_007_isPassInOrOut() {
        //todo 假数据
        fileEntity.getBodyParam().put("userId", "109");
        fileEntity.getBodyParam().put("waybillId", "698");
        /*fileEntity.getBodyParam().put("userId", driverId);
        replaceBodyParam("waybillId");*/
        startPerformTest();
    }

    /**
     * 单据打印
     */
    @Test(description = "单据打印", groups = {APPLETDRIVER})
    private void test_008_waybillPrint() {
        /*fileEntity.getBodyParam().put("userId", driverId);
        replaceBodyParam("waybillId");*/
        //todo 假数据
        fileEntity.getBodyParam().put("userId", "5555765");
        fileEntity.getBodyParam().put("waybillId", "859");
        startPerformTest();
    }

    /**
     * 司机卸货录入
     */
    @Test(description = "司机卸货录入", groups = {APPLETDRIVER})
    private void test_009_poundEntry() {

        /*fileEntity.getBodyParam().put("userId", driverId);
        replaceBodyParam("waybillId");*/
        //todo 假数据
        fileEntity.getBodyParam().put("userId", "109");
        fileEntity.getBodyParam().put("waybillId", "698");
        startPerformTest();
    }

    /********************客户操作******************/
    /**
     * 客户签收详情
     */
    @Test(description = "客户签收详情", groups = {APPLETCLIENT})
    private void test_010_waybillTaskDetail() {
        fileEntity.getBodyParam().put("userId", userId);
        startPerformTest();
    }

    /**
     * 客户签收提交
     */
    @Test(description = "客户签收提交", groups = {APPLETCLIENT})
    private void test_011_signSubmit() {
        fileEntity.getBodyParam().put("userId", userId);
        replaceBodyParam("waybillId");
        startPerformTest();
    }

    /**
     * 司机签收确认
     */
    @Test(description = "司机签收确认", groups = {APPLETCLIENT})
    private void test_012_confirmSign() {
        fileEntity.getBodyParam().put("userId", driverId);
        replaceBodyParam("waybillId");
        startPerformTest();
    }


    /*****************库管***********************/
    /**
     * 库管获取小程序列表
     */
    @Test(description = "库管获取小程序列表", groups = {STOREKEEPER})
    private void test_013_waybillTaskDetail() {
        fileEntity.getBodyParam().put("userId", userkgId);
        fileEntity.getBodyParam().put("phone", "15594668868");
        startPerformTest();
        saveResponseParameters("result.data[0].waybillId");
    }

    /**
     * 单据打印
     */
    @Test(description = "单据打印", groups = {STOREKEEPER})
    private void test_014_waybillPrint() {
        fileEntity.getBodyParam().put("userId", userkgId);
        replaceBodyParam("waybillId");
        startPerformTest();
    }

    /**
     * 库管单据录入
     */
    @Test(description = "库管单据录入", groups = {STOREKEEPER})
    private void test_015_poundEntry() {
        fileEntity.getBodyParam().put("userId", userkgId);
        replaceBodyParam("waybillId");
        startPerformTest();
    }
}
