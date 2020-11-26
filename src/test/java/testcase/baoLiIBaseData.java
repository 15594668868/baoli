package testcase;

import com.alibaba.fastjson.JSON;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ConfigFile;
import utils.DateUtil;
import utils.FilesUtils;
import utils.StringUtil;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 宝利基础依赖数据创建
 */
public class baoLiIBaseData extends BaseTestCase {

    //excel测试用例
    private static String fileName = "宝利基础依赖数据创建.xls";
    //执行地址
    private static String platformUrl = ConfigFile.getValue("platform.url");
    //登录请求
    private static String loginUrl = ConfigFile.getUrl("login.url");
    //用从文件获取到的需要登录的用户名到配置文件获取该用户登录账号密码
    private String username = ConfigFile.getValue( "login.username");
    private String password = ConfigFile.getValue( "login.password");
    //基础数据
    private static final String BASEDATA = "basedata";




    /**
     * 登录拿到cookie
     */
    @BeforeClass(alwaysRun = true)
    private void loginGetCookie() {
        login(loginUrl, username, password);
    }

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

    /**
     * 新增角色
     */
    @Test(description = "新增角色", groups = {BASEDATA})
    private void test_001_saveRole() {
        fileEntity.getBodyParam().put("roleName", "自动化使用角色（勿删）");
        startPerformTest();
    }

    /**
     * 查找角色
     */
    @Test(description = "根据用户名查询角色", groups = {BASEDATA})
    private void test_002_findByPage() {
        replaceBodyParam("roleName");
        startPerformTest();
        saveResponseParameters("result.data[0].roleId");
    }



    /**
     * 新增客户信息
     */
    @Test(description = "新增客户信息", groups = {BASEDATA})
    private void test_003_addOrUpdate() {
        fileEntity.getBodyParam().put("mainPartCode", "自动化使用客户");
        fileEntity.getBodyParam().put("mainPartName", "自动化使用客户");
        //社会统一信用代码
        fileEntity.getBodyParam().put("thirdPartyUserId", "610324199603152032");
        //联系人手机号
        fileEntity.getBodyParam().put("esignOrgPhone", "15594668868");
        //处理存储在datailList下的地址
        List list = JSON.parseArray(fileEntity.getBodyParam().get("detailList").toString());
        Map<String, Object> map = JSON.parseObject(list.get(0).toString());
        map.put("addressDetail", "自动化随机数" + DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS"));
        map.put("addressName", "自动化随机数" + DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS"));
        map.put("phoneNumber1", "15594668868");
        list.set(0, map);
        fileEntity.getBodyParam().put("detailList", list);
        startPerformTest();
    }

    /**
     * 查询客户信息
     */
    @Test(description = "查询客户信息", groups = {BASEDATA})
    private void test_004_findByPage() {
        fileEntity.getBodyParam().put("mainPartCode", "自动化使用客户");
        fileEntity.getBodyParam().put("mainPartName", "自动化使用客户");
        startPerformTest();
        //获取客户id
        parameterMap.put("clientId", response.jsonPath().get("result.data[0].id"));
    }

    /**
     * 新增承运商信息
     */
    @Test(description = "新增承运商信息", groups = {BASEDATA})
    private void test_005_addOrUpdate() {
        fileEntity.getBodyParam().put("mainPartName", "自动化测试承运商");
        fileEntity.getBodyParam().put("mainPartCode", "自动化测试承运商");
        startPerformTest();
    }

    /**
     * 查询承运商信息
     */
    @Test(description = "查询承运商信息", groups = {BASEDATA})
    private void test_006_findByPage() {
        fileEntity.getBodyParam().put("mainPartCode", "自动化测试承运商");
        startPerformTest();
        //获取承运商id
        parameterMap.put("mainPartId", response.jsonPath().get("result.data[0].id"));
    }

    /**
     * 查询部门管理
     */
    @Test(description = "查询部门管理", groups = {BASEDATA})
    private void test_007_findOrganizationTree() {
        startPerformTest();
        parameterMap.put("baoliOrgId", response.jsonPath().get("result[0].id"));
        List<Map<String, Object>> list = response.jsonPath().get("result[0].children");
        for(Map<String, Object> map : list) {
            if(StringUtil.equals(map.get("label").toString(), "客户")){
                List childrenList = (List)map.get("children");
                for(Object object : childrenList){
                    Map childrenMap = (Map)object;
                    if(StringUtil.equals(childrenMap.get("label").toString(), "自动化使用客户")) {
                        parameterMap.put("clientOrgId", childrenMap.get("id"));
                        break;
                    }
                }
            }
            if(StringUtil.equals(map.get("label").toString(), "承运商")){
                List childrenList = (List)map.get("children");
                for(Object object : childrenList){
                    Map childrenMap = (Map)object;
                    if(StringUtil.equals(childrenMap.get("label").toString(), "自动化测试承运商")) {
                        parameterMap.put("mainPartOrgId", childrenMap.get("id"));
                        break;
                    }
                }
            }

        }
    }

    /**
     * 新增客户用户
     */
    @Test(description = "新增用户", groups = {BASEDATA})
    private void test_008_saveUser() {
        fileEntity.getBodyParam().put("userName", "gzx");
        fileEntity.getBodyParam().put("userCode", "gzx");
        fileEntity.getBodyParam().put("orgId", parameterMap.get("clientOrgId"));
        String[] roleIdList = new String[]{parameterMap.get("roleId").toString()};
        fileEntity.getBodyParam().put("roleIdList", roleIdList);
        fileEntity.getBodyParam().put("phone1", "15594668868");
        fileEntity.getBodyParam().put("idCard", "622825199901010101");
        startPerformTest();
    }

    /**
     * 查询客户用户
     */
    @Test(description = "查询用户", groups = {BASEDATA})
    private void test_009_findByPage() {
        fileEntity.getBodyParam().put("userName", "gzx");
        startPerformTest();
        //保存客户用户id
        parameterMap.put("userId", response.jsonPath().get("result.data[0].userId"));
    }

    /**
     * 新增库管用户
     */
    @Test(description = "新增用户", groups = {BASEDATA})
    private void test_010_saveUser() {
        fileEntity.getBodyParam().put("userName", "gzx库管");
        fileEntity.getBodyParam().put("userCode", "gzxkg");
        fileEntity.getBodyParam().put("orgId", parameterMap.get("baoliOrgId")); //江苏宝利组织架构id
        String[] roleIdList = new String[]{parameterMap.get("roleId").toString()};
        fileEntity.getBodyParam().put("roleIdList", roleIdList);
        fileEntity.getBodyParam().put("phone1", "15594668869");
        fileEntity.getBodyParam().put("idCard", "622825199901010102");
        startPerformTest();
    }

    /**
     * 查询库管用户
     */
    @Test(description = "查询用户", groups = {BASEDATA})
    private void test_011_findByPage() {
        fileEntity.getBodyParam().put("userName", "gzx库管");
        startPerformTest();
        //保存客户用户id
        parameterMap.put("userkgId", response.jsonPath().get("result.data[0].userId"));
    }


    /**
     * 新增区域1
     */
    @Test(description = "新增区域1", groups = {BASEDATA})
    private void test_012_saveOrEditArea() {
        fileEntity.getBodyParam().put("areaName", "自动化使用区域1");
        fileEntity.getBodyParam().put("orgId", parameterMap.get("baoliOrgId")); //江苏宝利组织架构id
        startPerformTest();
        parameterMap.put("areaId1", response.jsonPath().get("result.areaId"));
    }
    /**
     * 新增区域2
     */
    @Test(description = "新增区域2", groups = {BASEDATA})
    private void test_013_saveOrEditArea() {
        fileEntity.getBodyParam().put("areaName", "自动化使用区域2");
        fileEntity.getBodyParam().put("orgId", parameterMap.get("baoliOrgId")); //江苏宝利组织架构id
        startPerformTest();
        parameterMap.put("areaId2", response.jsonPath().get("result.areaId"));
    }

    /**
     * 新增电子线路信息
     */
    @Test(description = "新增电子线路信息", groups = {BASEDATA})
    private void test_014_addOrEditLine() {
        fileEntity.getBodyParam().put("lineName", "自动化使用线路");
        fileEntity.getBodyParam().put("orgId", parameterMap.get("baoliOrgId")); //江苏宝利组织架构id
        startPerformTest();
        parameterMap.put("lineId", response.jsonPath().get("result.lineId"));
    }

    /**
     * 新增地址信息1
     */
    @Test(description = "新增地址信息1", groups = {BASEDATA})
    private void test_015_addOrUpdate() {
        fileEntity.getBodyParam().put("addressName", "自动化使用地址1");
        fileEntity.getBodyParam().put("addressDetail", "自动化使用地址1");
        fileEntity.getBodyParam().put("customAreaId", parameterMap.get("areaId1"));
        startPerformTest();
    }

    /**
     * 查询地址信息1
     */
    @Test(description = "查询地址信息1", groups = {BASEDATA})
    private void test_016_findByPage() {
        fileEntity.getBodyParam().put("addressName", "自动化使用地址1");
        startPerformTest();
        parameterMap.put("addressId1", response.jsonPath().get("result.data[0].id"));
    }


    /**
     * 新增地址信息2
     */
    @Test(description = "新增地址信息2", groups = {BASEDATA})
    private void test_017_addOrUpdate() {
        fileEntity.getBodyParam().put("addressName", "自动化使用地址2");
        fileEntity.getBodyParam().put("addressDetail", "自动化使用地址2");
        fileEntity.getBodyParam().put("customAreaId", parameterMap.get("areaId2"));
        startPerformTest();
    }

    /**
     * 查询地址信息2
     */
    @Test(description = "查询地址信息2", groups = {BASEDATA})
    private void test_018_findByPage() {
        fileEntity.getBodyParam().put("addressName", "自动化使用地址2");
        startPerformTest();
        parameterMap.put("addressId2", response.jsonPath().get("result.data[0].id"));
    }

    /**
     * 新增线路信息
     */
    @Test(description = "新增线路信息", groups = {BASEDATA})
    private void test_019_addOrUpdate() {
        fileEntity.getBodyParam().put("lineName", "自动化测试线路");
        fileEntity.getBodyParam().put("lineDescription", "不要删除呀");
        fileEntity.getBodyParam().put("fromAddressId", parameterMap.get("addressId1"));
        fileEntity.getBodyParam().put("toAddressId", parameterMap.get("addressId2"));
        fileEntity.getBodyParam().put("customLineId", parameterMap.get("lineId"));
        startPerformTest();
    }

    /**
     * 查询线路信息
     */
    @Test(description = "查询线路信息", groups = {BASEDATA})
    private void test_020_findByPage() {
        fileEntity.getBodyParam().put("lineName", "自动化测试线路");
        startPerformTest();
        parameterMap.put("lineBaseId", response.jsonPath().get("result.data[0].id"));
    }

    /**
     * 新增合同信息
     */
    @Test(description = "新增合同信息", groups = {BASEDATA})
    private void test_021_addOrUpdate() {
        fileEntity.getBodyParam().put("contractCode", "自动化测试合同");
        fileEntity.getBodyParam().put("contractName", "自动化测试合同");
        fileEntity.getBodyParam().put("mainPartId", parameterMap.get("mainPartId"));
        String startDate = DateUtil.disposeDate(-30, "yyyy-MM-dd");
        String endDate = DateUtil.disposeDate(90, "yyyy-MM-dd");
        fileEntity.getBodyParam().put("startTime", startDate);
        fileEntity.getBodyParam().put("endTime", endDate);
        String[] date = new String[]{startDate, endDate};
        fileEntity.getBodyParam().put("timeRange", date);
        startPerformTest();
    }

    /**
     * 查询合同信息
     */
    @Test(description = "查询合同信息", groups = {BASEDATA})
    private void test_022_findByPage() {
        fileEntity.getBodyParam().put("contractCode", "自动化测试合同");
        startPerformTest();
        parameterMap.put("contractId", response.jsonPath().get("result.data[0].id"));
    }


    /**
     * 新增线路价格信息(依赖于新增线路信息)
     */
    @Test(description = "新增线路价格信息", groups = {BASEDATA})
    private void test_023_addOrUpdate() {
        //给出上方新加的线路id
        fileEntity.getBodyParam().put("lineId", parameterMap.get("lineBaseId"));
        List priceList = JSON.parseArray(fileEntity.getBodyParam().get("priceList").toString());
        String startDate = DateUtil.disposeDate(-30, "yyyy-MM-dd");
        String endDate = DateUtil.disposeDate(90, "yyyy-MM-dd");
        Map map = JSON.parseObject(priceList.get(0).toString());
        map.put("contractId", parameterMap.get("contractId"));
        map.put("carrierName", "自动化测试承运商");
        map.put("carrierId", parameterMap.get("mainPartId"));
        map.put("contractStartTime", startDate);
        map.put("contractEndTime", endDate);
        map.put("timeRange", new String[]{startDate, endDate});
        priceList.set(0, map);
        fileEntity.getBodyParam().put("priceList", priceList);
        startPerformTest();
    }

    /**
     * 查询线路价格信息
     */
    @Test(description = "查询线路价格信息", groups = {BASEDATA})
    private void test_024_findByPage() {
        fileEntity.getBodyParam().put("lineId", parameterMap.get("lineBaseId"));
        startPerformTest();
        parameterMap.put("transportPriceId", response.jsonPath().get("result.data[0].id"));
    }

    /**
     * 新增车船箱信息
     */
    @Test(description = "新增车船箱信息", groups = {BASEDATA})
    private void test_025_vehicle() {
        fileEntity.getBodyParam().put("vehicleNo", "自动化测试长租车");
        fileEntity.getBodyParam().put("carrierId", parameterMap.get("mainPartId"));
        startPerformTest();
    }

    /**
     * 查询车船箱信息
     */
    @Test(description = "查询车船箱信息", groups = {BASEDATA})
    private void test_026_findVehicleByPage() {
        fileEntity.getBodyParam().put("vehicleNo", "自动化测试长租车");
        startPerformTest();
        //车辆id
        parameterMap.put("vehicleId", response.jsonPath().get("result.data[0].id"));
    }


    /**
     * 新增司机信息
     */
    @Test(description = "新增司机信息", groups = {BASEDATA})
    private void test_027_addOrUpdate() {
        fileEntity.getBodyParam().put("driverName", "自动化测试司机");
        fileEntity.getBodyParam().put("phoneNumber", "15594668868");
        fileEntity.getBodyParam().put("mainPartId", parameterMap.get("mainPartId"));
        fileEntity.getBodyParam().put("mainVehicleNo", parameterMap.get("vehicleId"));
        startPerformTest();
    }

    /**
     * 查询司机信息
     */
    @Test(description = "查询司机信息", groups = {BASEDATA})
    private void test_028_findByPage() {
        fileEntity.getBodyParam().put("driverName", "自动化测试司机");
        startPerformTest();
        parameterMap.put("driverId", response.jsonPath().get("result.data[0].id"));
        //自动化使用客户
        System.out.println("private static String clientId = \"" + parameterMap.get("clientId") + "\"; //自动化使用客户" );
        //自动化测试承运商
        System.out.println("private static String mainPartId = \"" + parameterMap.get("mainPartId") + "\"; //自动化测试承运商" );
        //自动化使用客户的部门id
        System.out.println("private static String clientOrgId = \"" + parameterMap.get("clientOrgId") + "\"; //自动化使用客户的部门id" );
        //自动化测试承运商的部门id
        System.out.println("private static String mainPartOrgId = \"" + parameterMap.get("mainPartOrgId") + "\"; //自动化测试承运商的部门id" );
        //客户的用户 gzx
        System.out.println("private static String userId = \"" + parameterMap.get("userId") + "\"; //客户的用户 gzx" );
        //库管用户  gzx库管
        System.out.println("private static String userkgId = \"" + parameterMap.get("userkgId") + "\"; //库管用户  gzx库管" );
        //自动化使用地址1
        System.out.println("private static String addressId1 = \"" + parameterMap.get("addressId1") + "\"; //自动化使用地址1" );
        //自动化使用地址2
        System.out.println("private static String addressId2 = \"" + parameterMap.get("addressId2") + "\"; //自动化使用地址2" );
        //自动化测试线路
        System.out.println("private static String lineBaseId = \"" + parameterMap.get("lineBaseId") + "\"; //自动化测试线路" );
        //自动化测试长租车
        System.out.println("private static String vehicleId = \"" + parameterMap.get("vehicleId") + "\"; //自动化测试长租车" );
        //自动化测试司机
        System.out.println("private static String driverId = \"" + parameterMap.get("driverId") + "\"; //自动化测试司机" );
        //自动化使用区域1
        System.out.println("private static String areaId1 = \"" + parameterMap.get("areaId1") + "\"; //自动化使用区域1" );
        //自动化使用区域2
        System.out.println("private static String areaId2 = \"" + parameterMap.get("areaId2") + "\"; //自动化使用区域2" );
        //自动化使用线路
        System.out.println("private static String lineId = \"" + parameterMap.get("lineId") + "\"; //自动化使用线路" );
        //自动化测试合同
        System.out.println("private static String contractId = \"" + parameterMap.get("contractId") + "\"; //自动化测试合同" );
    }

}