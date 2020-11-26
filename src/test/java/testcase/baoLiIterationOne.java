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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置和基础信息
 */
public class baoLiIterationOne extends BaseTestCase {

    //excel测试用例
    private static String fileName = "宝利基础数据自动化.xls";
    //执行地址
    private static String platformUrl = ConfigFile.getValue("platform.url");
    //登录请求
    private static String loginUrl = ConfigFile.getUrl("login.url");
    //用从文件获取到的需要登录的用户名到配置文件获取该用户登录账号密码
    private String username = ConfigFile.getValue( "login.username");
    private String password = ConfigFile.getValue( "login.password");
    //用户管理
    private static final String USER = "user";
    //角色管理
    private static final String ROLE = "role";
    //用户字典
    private static final String DICTIONARIES = "dictionaries";
    //部门管理
    private static final String DEPARTMENT = "department";
    //车船箱信息
    private static final String VEHICLE = "vehicle";
    //司机信息
    private static final String DRIVER = "driver";
    //客户信息
    private static final String CLIENT = "client";
    //供应商信息
    private static final String SUPPLIER = "supplier";
    //地址信息
    private static final String ADDRESS = "address";
    //线路信息
    private static final String CIRCUIT = "circuit";
    //物料信息
    private static final String MATERIAL = "material";
    //合同信息
    private static final String CONTRACT = "contract";
    //线路价格信息
    private static final String LINEPRICE = "linePrice";
    //承运商信息
    private static final String CARRIER = "carrier";
    //车辆定位
    private static final String LOCATION = "location";
    //物流地图
    private static final String GEOGRAPHICALMAP= "geographicalMap";
    //轨迹回放
    private static final String TRACK = "track";
    //视频监控
    private static final String MONITORING = "monitoring";
    //视频回放
    private static final String PLAYBACK = "playback";
    //视频下载
    private static final String DOWNLOAD = "download";
    //系统设置
    private static final String SYSTEMSETTINGS = "systemsettings";


    private static String clientId = "406"; //自动化使用客户
    private static String mainPartId = "407"; //自动化测试承运商
    private static String clientOrgId = "-8070"; //自动化使用客户的部门id
    private static String mainPartOrgId = "-8071"; //自动化测试承运商的部门id
    private static String userId = "5555706"; //客户的用户 gzx
    private static String userkgId = "5555707"; //库管用户  gzx库管
    private static String addressId1 = "210"; //自动化使用地址1
    private static String addressId2 = "211"; //自动化使用地址2
    private static String lineBaseId = "167"; //自动化测试线路
    private static String vehicleId = "5555705"; //自动化测试长租车
    private static String driverId = "96"; //自动化测试司机
    private static String areaId1 = "12"; //自动化使用区域1
    private static String areaId2 = "13"; //自动化使用区域2
    private static String lineId = "null"; //自动化使用线路
    private static String contractId = "98"; //自动化测试合同


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
     * 新增用户
     */
    @Test(description = "新增用户", groups = {USER})
    private void test_001_saveUser() {
        randomCurrentTimeBodyParam("userName", "userCode");
        startPerformTest();
    }

    /**
     * 查询用户
     */
    @Test(description = "查询用户", groups = {USER})
    private void test_002_findByPage() {
        replaceBodyParam("userName");
        startPerformTest();
        saveResponseParameters("result.data[0].userId","result.data[0].webgisUserId");
    }

    /**
     * 修改用户
     */
    @Test(description = "修改用户", groups = {USER})
    private void test_003_saveUser() {
        replaceBodyParam("userId", "webgisUserId");
        startPerformTest();
    }

    /**
     * 修改密码
     */
    @Test(description = "修改密码", groups = {USER})
    private void test_003_modifySubUserPassWord() {
        replaceBodyParam("userId");
        fileEntity.getBodyParam().put("webgisPassword", "123456cs");
        startPerformTest();
    }

    /**
     * 微信解绑，因无绑定，提示未绑定微信
     */
    @Test(description = "微信解绑", groups = {USER})
    private void test_003_unbindWeChat() {
        replaceBodyParam("userId", "webgisUserId");
        startPerformTest();
    }

    /**
     * 停用用户
     */
    @Test(description = "停用用户", groups = {USER})
    private void test_004_enableUser() {
        String url = fileEntity.getUrl();
        fileEntity.setUrl(url + parameterMap.get("userId") +  "/2");
        startPerformTest();
    }

    /**
     * 启用用户
     */
    @Test(description = "启用用户", groups = {USER})
    private void test_005_enableUser() {
        String url = fileEntity.getUrl();
        fileEntity.setUrl(url + parameterMap.get("userId") + "/1");
        startPerformTest();
    }

    /**
     * 根据用户名查询用户
     */
    @Test(description = "根据用户名查询用户", groups = {USER})
    private void test_006_findByPage() {
        replaceBodyParam("userName");
        startPerformTest();
    }

    /**
     * 新增角色
     */
    @Test(description = "新增角色", groups = {ROLE})
    private void test_007_saveRole() {
        randomCurrentTimeBodyParam("roleName");
        startPerformTest();
    }


    /**
     * 查找角色
     */
    @Test(description = "根据用户名查询角色", groups = {ROLE})
    private void test_008_findByPage() {
        replaceBodyParam("roleName");
        startPerformTest();
        saveResponseParameters("result.data[0].roleId");
    }

    /**
     * 修改角色
     */
    @Test(description = "修改角色", groups = {ROLE})
    private void test_009_saveRole() {
        randomCurrentTimeBodyParam("roleName");
        replaceBodyParam("roleId");
        startPerformTest();
    }

    /**
     * 停用角色
     */
    @Test(description = "停用角色", groups = {ROLE})
    private void test_010_enableUser() {
        String url = fileEntity.getUrl();
        fileEntity.setUrl(url + parameterMap.get("roleId") + "/2");
        startPerformTest();
    }

    /**
     * 启用角色
     */
    @Test(description = "启用角色", groups = {ROLE})
    private void test_011_enableUser() {
        String url = fileEntity.getUrl();
        fileEntity.setUrl(url + parameterMap.get("roleId") + "/1");
        startPerformTest();
    }


    /**
     * 新增用户字典
     */
    @Test(description = "新增用户字典", groups = {DICTIONARIES})
    private void test_012_add() {
        randomCurrentTimeBodyParam("dicName");
        startPerformTest();
    }

    /**
     * 查询用户字典
     */
    @Test(description = "查询用户字典", groups = {DICTIONARIES})
    private void test_013_list() {
        replaceBodyParam("dicName");
        startPerformTest();
        saveResponseParameters("result.result[0].createdTime", "result.result[0].dicId",
                "result.result[0].id", "result.result[0].modifiedTime", "result.result[0].orderNum",
                "result.result[0].corpId");
    }

    /**
     * 修改用户字典
     */
    @Test(description = "修改用户字典", groups = {DICTIONARIES})
    private void test_014_01_update() {
        replaceBodyParam("corpId", "dicName", "id", "modifiedTime", "createdTime", "dicId", "modifiedTime", "orderNum");
        startPerformTest();
    }

    /**
     * 停用用户字典
     */
    @Test(description = "停用用户字典", groups = {DICTIONARIES}, enabled = false)
    private void test_014_02_del() {
        createBodyParamList();
        startPerformTest();

    }
    /**
     * 创建BodyParamList，用于test_014_02_del 停用用户字典
     */
    private void createBodyParamList() {
        fileEntity.getBodyParamList().get(0).put("corpId", parameterMap.get("corpId"));
        fileEntity.getBodyParamList().get(0).put("dicName", parameterMap.get("dicName"));
        fileEntity.getBodyParamList().get(0).put("id", parameterMap.get("id"));
        fileEntity.getBodyParamList().get(0).put("modifiedTime", parameterMap.get("modifiedTime"));
        fileEntity.getBodyParamList().get(0).put("createdTime", parameterMap.get("createdTime"));
        fileEntity.getBodyParamList().get(0).put("dicId", parameterMap.get("dicId"));
        fileEntity.getBodyParamList().get(0).put("orderNum", parameterMap.get("orderNum"));
        fileEntity.getBodyParamList().get(0).put("pageParamVO", null);
        fileEntity.getBodyParamList().get(0).put("pcorpId", null);
    }

    /**
     * 新增部门管理
     */
    @Test(description = "新增部门管理", groups = {DEPARTMENT})
    private void test_015_saveOrganization() {
        randomCurrentTimeBodyParam("orgName", "orgCode");
        startPerformTest();
    }

    /**
     * 查询部门管理
     */
    @Test(description = "查询部门管理", groups = {DEPARTMENT})
    private void test_016_findOrganizationTree() {
        startPerformTest();
        List<Map<String, Object>> list = response.jsonPath().get("result[0].children");
        for(Map<String, Object> map : list) {
            if(StringUtil.equals(map.get("label").toString(), parameterMap.get("orgName").toString())) {
                parameterMap.put("orgId", map.get("id"));
                break;
            }
        }
    }

    /**
     * 给宝利总公司分配车辆7
     */
    @Test(description = "分配车辆", groups = {DEPARTMENT})
    private void test_017_insertOrgAndVehicle() {
        startPerformTest();
    }


    /**
     * 移出分配给宝利总公司的车辆7
     */
    @Test(description = "取消分配车辆", groups = {DEPARTMENT})
    private void test_018_deleteOrgAndVehicle() {
        startPerformTest();
    }


    /**
     * 编辑组织架构
     */
    @Test(description = "编辑组织架构", groups = {DEPARTMENT})
    private void test_018_01saveOrganization() {
        replaceBodyParam("orgName", "orgCode", "orgId");
        startPerformTest();
    }

    /**
     * 删除组织架构
     */
    @Test(description = "删除组织架构", groups = {DEPARTMENT})
    private void test_018_02deleteOrgAndVehicle() {
        fileEntity.setUrl(fileEntity.getUrl() + parameterMap.get("orgId"));
        startPerformTest();
    }


    /**
     * 新增司机信息（关联承运商,对应mainPartId）
     */
    @Test(description = "新增司机信息", groups = {DRIVER})
    private void test_019_addOrUpdate() {
        randomCurrentTimeBodyParam("driverName");
        fileEntity.getBodyParam().put("mainPartId", mainPartId);
        String phoneNumber = 158 + StringUtil.getRandomNumber(8);
        fileEntity.getBodyParam().put("phoneNumber", phoneNumber);
        parameterMap.put("phoneNumber", phoneNumber);
        startPerformTest();
    }

    /**
     * 查询司机信息
     */
    @Test(description = "查询司机信息", groups = {DRIVER})
    private void test_020_findByPage() {
        replaceBodyParam("driverName");
        startPerformTest();
        saveResponseParameters("result.data[0].id",
                "result.data[0].mainPartId","result.data[0].modifiedTime", "result.data[0].mainPartName");
    }

    /**
     * 修改司机信息
     */
    @Test(description = "修改司机信息", groups = {DRIVER})
    private void test_021_addOrUpdate() {
        replaceBodyParam("id", "mainPartId", "modifiedTime", "phoneNumber", "mainPartName");
        randomCurrentTimeBodyParam("driverName");
        startPerformTest();
    }

    /**
     * 禁用司机信息
     */
    @Test(description = "禁用司机信息", groups = {DRIVER})
    private void test_022_01_enableOrDisableDriver() {
        fileEntity.getPathParam().put("id", parameterMap.get("id"));
        startPerformTest();
    }

    /**
     * 启用司机信息
     */
    @Test(description = "启用司机信息", groups = {DRIVER})
    private void test_022_02_enableOrDisableDriver() {
        fileEntity.getPathParam().put("id", parameterMap.get("id"));
        startPerformTest();
    }

    /**
     * 新增客户信息
     */
    @Test(description = "新增客户信息", groups = {CLIENT})
    private void test_023_addOrUpdate() {
        randomCurrentTimeBodyParam("mainPartCode", "mainPartName");
        //处理存储在datailList下的地址
        List list = JSON.parseArray(fileEntity.getBodyParam().get("detailList").toString());
        Map<String, Object> map = JSON.parseObject(list.get(0).toString());
        map.put("addressDetail", "自动化随机数" + DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS"));
        map.put("addressName", "自动化随机数" + DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS"));
        list.set(0, map);
        fileEntity.getBodyParam().put("detailList", list);
        startPerformTest();
    }

    /**
     * 查询客户信息
     */
    @Test(description = "查询客户信息", groups = {CLIENT})
    private void test_024_01_findByPage() {
        replaceBodyParam("mainPartCode", "mainPartName");
        startPerformTest();
        saveResponseParameters("result.data[0].id");
    }

    /**
     * 编辑客户信息获取数据
     */
    @Test(description = "编辑客户信息获取数据", groups = {CLIENT})
    private void test_024_02_getByMainPartId() {
        fileEntity.getPathParam().put("id", parameterMap.get("id"));
        startPerformTest();
        saveResponseParameters("result.id", "result.detailList",
                "result.orgId","result.createdTime", "result.modifiedTime");
    }

    /**
     * 修改客户信息
     */
    @Test(description = "修改客户信息", groups = {CLIENT})
    private void test_025_addOrUpdate() {
        replaceBodyParam("id", "orgId", "createdTime", "detailList",
                "modifiedTime", "mainPartCode", "mainPartName");
        startPerformTest();
    }

    /**
     * 禁用客户信息
     */
    @Test(description = "禁用客户信息", groups = {CLIENT})
    private void test_026_01_enableOrDisableMainPart() {
        fileEntity.getPathParam().put("id", parameterMap.get("id"));
        startPerformTest();
    }

    /**
     * 启用客户信息
     */
    @Test(description = "启用客户信息", groups = {CLIENT})
    private void test_026_02_enableOrDisableMainPart() {
        fileEntity.getPathParam().put("id", parameterMap.get("id"));
        startPerformTest();
    }

    /**
     * 查看客户详情
     */
    @Test(description = "查看客户详情", groups = {CLIENT})
    private void test_026_03_getByMainPartId() {
        fileEntity.getPathParam().put("id", parameterMap.get("id"));
        startPerformTest();
    }

    /**DateUtil.getDate
     * 新增供应商信息
     */
    @Test(description = "新增供应商信息", groups = {SUPPLIER})
    private void test_027_addOrUpdate() {
        randomCurrentTimeBodyParam("mainPartCode", "mainPartName", "mainPartSimpleName");
        //处理存储在datailList下的地址
        List list = JSON.parseArray(fileEntity.getBodyParam().get("detailList").toString());
        Map<String, Object> map = JSON.parseObject(list.get(0).toString());
        map.put("addressDetail", "自动化" + DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS"));
        map.put("addressName", "自动化" + DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS"));
        list.set(0, map);
        fileEntity.getBodyParam().put("detailList", list);
        startPerformTest();
    }

    /**
     * 查询供应商信息
     */
    @Test(description = "查询供应商信息", groups = {SUPPLIER})
    private void test_028_01_findByPage() {
        replaceBodyParam("mainPartCode", "mainPartName");
        startPerformTest();
        saveResponseParameters("result.data[0].id");
    }

    /**
     * 编辑获取供应商信息
     */
    @Test(description = "编辑获取供应商信息", groups = {SUPPLIER})
    private void test_028_02_getByMainPartId() {
        fileEntity.getPathParam().put("id", parameterMap.get("id"));
        startPerformTest();
        saveResponseParameters("result.detailList",
                "result.orgId","result.createdTime", "result.modifiedTime");
    }

    /**
     * 修改供应商信息
     */
    @Test(description = "修改供应商信息", groups = {SUPPLIER})
    private void test_029_addOrUpdate() {
        replaceBodyParam("id", "orgId", "createdTime", "detailList",
                "modifiedTime", "mainPartCode", "mainPartName", "mainPartSimpleName");
        startPerformTest();
    }

    /**
     * 禁用供应商信息
     */
    @Test(description = "禁用供应商信息", groups = {SUPPLIER})
    private void test_030_01_enableOrDisableMainPart() {
        fileEntity.getPathParam().put("id", parameterMap.get("id"));
        startPerformTest();
    }

    /**
     * 获取供应商详情
     */
    @Test(description = "获取供应商详情", groups = {SUPPLIER})
    private void test_030_02_getByMainPartId() {
        fileEntity.getPathParam().put("id", parameterMap.get("id"));
        startPerformTest();
    }



    /**
     * 新增地址信息 电子围栏：自动化使用区域2
     */
    @Test(description = "新增地址信息", groups = {ADDRESS})
    private void test_031_addOrUpdate() {
        fileEntity.getBodyParam().put("customAreaId", areaId2);
        randomCurrentTimeBodyParam("addressName", "addressDetail");
        startPerformTest();
    }

    /**
     * 查询地址信息
     */
    @Test(description = "查询地址信息", groups = {ADDRESS})
    private void test_032_findByPage() {
        replaceBodyParam("addressName");
        startPerformTest();
        saveResponseParameters("result.data[0].id",
                "result.data[0].modifiedTime","result.data[0].createdTime", "result.data[0].addressCode");
    }

    /**
     * 修改地址信息
     */
    @Test(description = "修改地址信息", groups = {ADDRESS})
    private void test_033_addOrUpdate() {
        replaceBodyParam("id", "modifiedTime", "createdTime",
                "addressCode", "addressName", "addressDetail");
        startPerformTest();
    }


    /**
     * 新增线路信息 起点：自动化开始地址，终点：自动化结束地址，电子线路：自动化测试线路
     */
    @Test(description = "新增线路信息", groups = {CIRCUIT})
    private void test_034_addOrUpdate() {
        randomCurrentTimeBodyParam("lineName", "lineDescription");
        fileEntity.getBodyParam().put("fromAddressId", addressId1);
        fileEntity.getBodyParam().put("toAddressId", addressId2);
        fileEntity.getBodyParam().put("customLineId", lineId);
        startPerformTest();
    }

    /**
     * 查询线路信息
     */
    @Test(description = "查询线路信息", groups = {CIRCUIT})
    private void test_035_findByPage() {
        replaceBodyParam("lineName");
        startPerformTest();
        saveResponseParameters("result.data[0].id",
                "result.data[0].modifiedTime","result.data[0].createdTime", "result.data[0].lineCode");
        //保存用于新增修改线路价格信息
        parameterMap.put("lineId", response.jsonPath().get("result.data[0].id"));
        parameterMap.put("lineCode", response.jsonPath().get("result.data[0].lineCode"));
        parameterMap.put("lineDescription", response.jsonPath().get("result.data[0].lineDescription"));
        parameterMap.put("lineName", response.jsonPath().get("result.data[0].lineName"));
    }

    /**
     * 修改线路信息
     */
    @Test(description = "修改线路信息", groups = {CIRCUIT})
    private void test_036_addOrUpdate() {
        replaceBodyParam("id", "modifiedTime", "createdTime",
                "lineCode", "lineName", "lineDescription");
        startPerformTest();
    }

    /**
     * 新增物料信息
     */
    @Test(description = "新增物料信息", groups = {MATERIAL})
    private void test_037_addOrUpdate() {
        randomCurrentTimeBodyParam("materialCode", "materialName");
        startPerformTest();
    }

    /**
     * 查询物料信息
     */
    @Test(description = "查询物料信息", groups = {MATERIAL})
    private void test_038_findByPage() {
        replaceBodyParam("materialCode");
        startPerformTest();
        saveResponseParameters("result.data[0].id",
                "result.data[0].modifiedTime","result.data[0].createdTime");
    }

    /**
     * 修改物料信息
     */
    @Test(description = "修改物料信息", groups = {MATERIAL})
    private void test_039_addOrUpdate() {
        replaceBodyParam("id", "modifiedTime", "createdTime", "materialCode", "materialName");
        startPerformTest();
    }


    /**
     * 新增合同信息
     */
    @Test(description = "新增合同信息", groups = {CONTRACT})
    private void test_040_addOrUpdate() {
        randomCurrentTimeBodyParam("contractCode", "contractName");
        String startDate = DateUtil.disposeDate(-1, "yyyy-MM-dd");
        String endDate = DateUtil.getDate(new Date(), "yyyy-MM-dd");
        fileEntity.getBodyParam().put("startTime", startDate);
        fileEntity.getBodyParam().put("endTime", endDate);
        String[] date = new String[]{startDate, endDate};
        fileEntity.getBodyParam().put("timeRange", date);
        fileEntity.getBodyParam().put("mainPartId", mainPartId);
        startPerformTest();
    }

    /**
     * 查询合同信息
     */
    @Test(description = "查询合同信息", groups = {CONTRACT})
    private void test_041_findByPage() {
        replaceBodyParam("contractCode");
        startPerformTest();
        saveResponseParameters("result.data[0].id",
                "result.data[0].modifiedTime","result.data[0].createdTime");
    }

    /**
     * 修改合同信息
     */
    @Test(description = "修改合同信息", groups = {CONTRACT})
    private void test_042_addOrUpdate() {
        String startDate = DateUtil.disposeDate(-1, "yyyy-MM-dd");
        String endDate = DateUtil.getDate(new Date(), "yyyy-MM-dd");
        replaceBodyParam("id", "modifiedTime", "createdTime", "contractCode", "contractName");
        fileEntity.getBodyParam().put("startTime", startDate);
        fileEntity.getBodyParam().put("endTime", endDate);
        String[] date = new String[]{startDate, endDate};
        fileEntity.getBodyParam().put("timeRange", date);
        startPerformTest();
    }


    /**
     * 新增线路价格信息(依赖于新增线路信息)
     */
    @Test(description = "新增线路价格信息", groups = {LINEPRICE})
    private void test_043_addOrUpdate() {
        //给出上方新加的线路id
        fileEntity.getBodyParam().put("lineId", parameterMap.get("lineId"));
        List priceList = JSON.parseArray(fileEntity.getBodyParam().get("priceList").toString());
        String startDate = DateUtil.disposeDate(-30, "yyyy-MM-dd");
        String endDate = DateUtil.disposeDate(90, "yyyy-MM-dd");
        Map map = JSON.parseObject(priceList.get(0).toString());
        map.put("contractId", contractId);
        map.put("carrierName", "自动化测试承运商");
        map.put("carrierId", mainPartId);
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
    @Test(description = "查询线路价格信息", groups = {LINEPRICE})
    private void test_044_findByPage() {
        replaceBodyParam("lineId");
        startPerformTest();
        saveResponseParameters("result.data[0].id",
                "result.data[0].modifiedTime","result.data[0].createdTime", "result.data[0].lineCode",
                "result.data[0].vehicleList[0].linePriceId");
        parameterMap.put("vehicleListId", response.jsonPath().get("result.data[0].vehicleList[0].id"));
    }


    /**
     * 修改线路价格信息
     */
    @Test(description = "修改线路价格信息", groups = {LINEPRICE})
    private void test_045_01_addOrUpdate() {
        //修改为线路id
        replaceBodyParam("lineId");

        //处理priceList参数
        List priceList = JSON.parseArray(fileEntity.getBodyParam().get("priceList").toString());
        Map map = JSON.parseObject(priceList.get(0).toString());
        //将新增线路时拿到的数据保存
        map.put("lineCode", parameterMap.get("lineCode"));
        map.put("lineDescription", parameterMap.get("lineDescription"));
        map.put("lineId", parameterMap.get("lineId"));
        map.put("lineName", parameterMap.get("lineName"));
        //处理vehicle中参数
        List vehicleList = JSON.parseArray(map.get("vehicleList").toString());
        Map vehicleMap = JSON.parseObject(vehicleList.get(0).toString());
        vehicleMap.put("linePriceId", parameterMap.get("linePriceId"));
        vehicleMap.put("id", parameterMap.get("vehicleListId"));
        //将参数重新封装起来
        vehicleList.set(0, vehicleMap);
        map.put("vehicleList", vehicleList);
        map.put("id", parameterMap.get("id"));
        map.put("modifiedTime", parameterMap.get("modifiedTime"));
        map.put("createdTime", parameterMap.get("createdTime"));
        map.put("lineCode", parameterMap.get("lineCode"));
        priceList.set(0, map);
        fileEntity.getBodyParam().put("priceList", priceList);
        startPerformTest();
    }


    @Test(description = "价格历史", groups = {LINEPRICE})
    private void test_045_02_getHistoryPriceList() {
        fileEntity.getPathParam().put("id", parameterMap.get("id"));
        startPerformTest();
    }

    /**
     * 新增承运商信息
     */
    @Test(description = "新增承运商信息", groups = {CARRIER}, priority = 1)
    private void test_046_addOrUpdate() {
        randomCurrentTimeBodyParam("mainPartName", "mainPartCode");
        startPerformTest();
    }

    /**
     * 查询承运商信息
     */
    @Test(description = "查询承运商信息", groups = {CARRIER}, priority = 1)
    private void test_047_findByPage() {
        replaceBodyParam("mainPartCode");
        startPerformTest();
        saveResponseParameters("result.data[0].modifiedTime","result.data[0].createdTime");
        //获取承运商id
        parameterMap.put("mainPartId", response.jsonPath().get("result.data[0].id"));
    }

    /**
     * 修改承运商信息
     */
    @Test(description = "修改承运商信息", groups = {CARRIER}, priority = 1)
    private void test_048_addOrUpdate() {
        replaceBodyParam("modifiedTime", "createdTime", "mainPartName", "mainPartCode");
        fileEntity.getBodyParam().put("id", parameterMap.get("mainPartId"));
        startPerformTest();
    }

    /**
     * 禁用承运商信息
     */
    @Test(description = "禁用承运商信息", groups = {CARRIER}, priority = 1)
    private void test_048_01_enableOrDisableMainPart() {
        fileEntity.getPathParam().put("id", parameterMap.get("mainPartId"));
        startPerformTest();
    }

    /**
     * 禁用承运商信息
     */
    @Test(description = "启用承运商信息", groups = {CARRIER}, priority = 1)
    private void test_048_02_enableOrDisableMainPart() {
        fileEntity.getPathParam().put("id", parameterMap.get("mainPartId"));
        startPerformTest();
    }


    /**
     * 新增车船箱信息
     */
    @Test(description = "新增车船箱信息", groups = {VEHICLE})
    private void test_049_vehicle() {
        randomCurrentTimeBodyParam("vehicleNo");
        fileEntity.getBodyParam().put("carrierId", mainPartId);
        startPerformTest();
    }

    /**
     * 查询车船箱信息
     */
    @Test(description = "查询车船箱信息", groups = {VEHICLE})
    private void test_050_findVehicleByPage() {
        replaceBodyParam("vehicleNo");
        startPerformTest();
        saveResponseParameters("result.data[0].id",
                "result.data[0].modifiedTime", "result.data[0].vehicleId");
    }

    /**
     * 修改车船箱信息
     */
    @Test(description = "修改车船箱信息", groups = {VEHICLE})
    private void test_051_vehicle() {
        replaceBodyParam("id", "modifiedTime", "vehicleId", "vehicleNo");
        startPerformTest();
    }

    /**
     * 禁用车船箱信息
     */
    @Test(description = "禁用车船箱信息", groups = {VEHICLE})
    private void test_052_enabledVehicle() {
        fileEntity.getPathParam().put("vehicleId", parameterMap.get("vehicleId"));
        startPerformTest();
    }

    /********************************车辆定位*********************************/
    /**
     * 定位苏EUJ363车辆
     */
    @Test(description = "定位苏EUJ363车辆", groups = {LOCATION})
    private void test_053_getMonitorData() {
        startPerformTest();
    }

    /**
     * 获取天气信息
     */
    @Test(description = "获取天气信息", groups = {LOCATION})
    private void test_053_cityWeather() {
        startPerformTest();
    }


    /*****************************物流地图*******************************/

    /**
     * 新增区域
     */
    @Test(description = "新增区域", groups = {GEOGRAPHICALMAP})
    private void test_054_saveOrEditArea() {
        randomCurrentTimeBodyParam("areaName");
        startPerformTest();
        saveResponseParameters("result.areaId", "result.createdTimeStr", "result.modifiedTimeStr", "result.createdTime");
    }

    /**
     * 修改区域
     */
    @Test(description = "修改区域", groups = {GEOGRAPHICALMAP})
    private void test_055_saveOrEditArea() {
        replaceBodyParam("areaId", "createdTimeStr", "modifiedTimeStr", "createdTime", "areaName");
        startPerformTest();
    }

    /**
     * 获取价格历史
     */
    @Test(description = "获取价格历史", groups = {GEOGRAPHICALMAP})
    private void test_056_selPointAreaHistory() {
        fileEntity.setUrl(fileEntity.getUrl() + parameterMap.get("areaId"));
        startPerformTest();
    }

    /**
     * 新增线路信息
     */
    @Test(description = "新增线路信息", groups = {GEOGRAPHICALMAP})
    private void test_057_addOrEditLine() {
        randomCurrentTimeBodyParam("lineName");
        startPerformTest();
        saveResponseParameters("result.lineId", "result.createdTime", "result.createdTimeStr",
                "result.modifiedTime", "result.modifiedTimeStr");
    }

    /**
     * 编辑获取线路信息
     */
    @Test(description = "编辑获取线路信息", groups = {GEOGRAPHICALMAP})
    private void test_058_selLineById() {
        fileEntity.setUrl(fileEntity.getUrl() + parameterMap.get("lineId"));
        startPerformTest();
    }

    /**
     * 编辑线路信息
     */
    @Test(description = "编辑线路信息", groups = {GEOGRAPHICALMAP})
    private void test_059_addOrEditLine() {
        replaceBodyParam("lineId", "lineName");
        startPerformTest();
    }

    /*********************************轨迹回放*************************/
    /**
     * 查询轨迹回放
     */
    @Test(description = "查询轨迹回放", groups = {TRACK})
    private void test_060_getTrackInfoList() {
        Long[] time = DateUtil.getTimeList(-1, 0);
        fileEntity.getBodyParam().put("startTime", time[0]);
        fileEntity.getBodyParam().put("endTime", time[1]);
        startPerformTest();
    }

    /*********************************视频监控*************************/

    /**
     * 视频监控
     */
    @Test(description = "视频监控", groups = {MONITORING})
    private void test_061_openVideo() {
        startPerformTest();
    }

    /**
     * 视频监控
     */
    @Test(description = "视频监控", groups = {MONITORING})
    private void test_062_getVideoUrl() {
        startPerformTest();
    }
    /*********************************视频回放*************************/

    /**
     * 视频回放
     */
    @Test(description = "视频回放", groups = {PLAYBACK})
    private void test_063_getVehicleACCStatusAndOnLineStatus() {
        startPerformTest();
    }
    /*********************************视频下载*************************/

    /**
     * 视频下载
     */
    @Test(description = "视频下载", groups = {DOWNLOAD})
    private void test_064_getVideoDownInfoList() {
        startPerformTest();
    }

    /*********************************系统设置*************************/
    /**
     * 新增常用报警设置
     */
    @Test(description = "新增常用报警设置", groups = {SYSTEMSETTINGS})
    private void test_065_saveOverSpeedAlarmSet() {
        startPerformTest();
    }

    /**
     * 修改常用报警设置
     */
    @Test(description = "修改常用报警设置", groups = {SYSTEMSETTINGS})
    private void test_066_updateOverSpeedAlarmSet() {
        startPerformTest();
    }

    /**
     * 修改车辆推荐设置
     */
    @Test(description = "修改车辆推荐设置", groups = {SYSTEMSETTINGS})
    private void test_067_addOrUpdate() {
        startPerformTest();
    }

    /**
     * 修改磅单误差设置
     */
    @Test(description = "修改磅单误差设置", groups = {SYSTEMSETTINGS})
    private void test_068_addOrUpdate() {
        startPerformTest();
    }

    /**
     * 派车更改设置
     */
    @Test(description = "派车更改设置", groups = {SYSTEMSETTINGS})
    private void test_069_addOrUpdate() {
        startPerformTest();
    }

    /**
     * 新增"gzx0723线路"的报警设置
     */
    @Test(description = "新增\"gzx0723线路\"的报警设置", groups = {SYSTEMSETTINGS})
    private void test_070_saveLineAlarmSet() {
        startPerformTest();
    }

    /**
     * 查找"gzx0723线路"的报警设置
     */
    @Test(description = "查找\"gzx0723线路\"的报警设置", groups = {SYSTEMSETTINGS})
    private void test_071_findPage() {
        startPerformTest();
        saveResponseParameters("result.content[0].alarmSetId");
    }

    /**
     * 修改获取刚才新增的设置
     */
    @Test(description = "修改获取刚才新增的设置", groups = {SYSTEMSETTINGS})
    private void test_072_getLineAlarmDetail() {
        fileEntity.getPathParam().put("alarmSetId", parameterMap.get("alarmSetId"));
        startPerformTest();
    }
    /**
     * 修改设置
     */
    @Test(description = "修改设置", groups = {SYSTEMSETTINGS})
    private void test_073_updateLineAlarmSet() {
        replaceBodyParam("alarmSetId");
        startPerformTest();
    }

    /**
     * 删除设置
     */
    @Test(description = "删除设置", groups = {SYSTEMSETTINGS})
    private void test_074_deleteLineAlarmSetById() {
        fileEntity.getPathParam().put("alarmSetId", parameterMap.get("alarmSetId"));
        startPerformTest();
    }

}