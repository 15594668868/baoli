package testcase;

import com.alibaba.fastjson.JSONArray;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

/**
 * 业务管理
 */
public class baoLiIterationTwo extends BaseTestCase {

    //excel测试用例
    private static String fileName = "宝利业务报表自动化.xls";
    //执行地址
    private static String platformUrl = ConfigFile.getValue("platform.url");
    //登录请求
    private static String loginUrl = ConfigFile.getUrl("login.url");
    //用从文件获取到的需要登录的用户名到配置文件获取该用户登录账号密码
    private String username = ConfigFile.getValue( "login.username");
    private String password = ConfigFile.getValue( "login.password");
    //订单管理
    private static final String ORDER = "order";
    //任务单管理
    private static final String WORKORDER = "workOrder";
    //运单管理
    private static final String WAYBILL = "waybill";
    //运单监控
    private static final String MONITORING = "monitoring";
    //询价
    private static final String ENQUIRY = "enquiry";
    //报表
    private static final String REPORT = "report";
    //财务中心
    private static final String FINANCE = "finance";


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



    /*********************************************订单管理****************************************/
    /**
     * 新增一条订单信息
     */
    //@Test(description = "新增订单", groups = {ORDER})  //新增订单增加了过滤器，无法调用
    private void test_001_uploadOrder() {
        String appKey = "b9c76dfd-2d14-4153-9068-51618dfed199";
        String appSecret = "42075852-6232-4432-b9b0-c0aa564f8f8a";
        long timestamp = System.currentTimeMillis();
        //获取解析了的数组
        Map<String, Object> bodyParam = fileEntity.getBodyParamList().get(0);
        //给orderCode随机数
        String orderCode = "DD" + DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        parameterMap.put("orderCode", orderCode);
        bodyParam.put("orderCode", orderCode);
        String data = bodyParam.toString();
        String encode = URLEncoder.encode(data);
        String sign = Md5Util.md5( appKey + appSecret + timestamp + appKey + data);

        //拼接一个pathParam并放入fileEntity中
        Map<String, Object> pathParam = new HashMap<>();
        pathParam.put("data", encode);
        pathParam.put("timestamp", timestamp);
        pathParam.put("appKey", appKey);
        pathParam.put("sign", sign);
        fileEntity.setPathParam(pathParam);
        //将原list变为空
        fileEntity.setBodyParamList(null);
        startPerformTest();
    }

    /*******************采购订单无库管******************/
    /**
     * 查询订单
     */
    @Test(description = "查询订单管理", groups = {ORDER})
    private void test_002_findByPage() {
        Long[] timeRange = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("startTime", timeRange[0]);
        fileEntity.getBodyParam().put("endTime", timeRange[1]);
        fileEntity.getBodyParam().put("timeRange", timeRange);
        getCgOrderId("orderId");
        //未查找到符合得orderId数据则新增
        if(parameterMap.get("orderId") == null) {
            String data = "[{\"orderCode\":\"cggzx\",\"invoiceTime\":\"2020-05-29\",\"orderType\":1,\"operateType\":1,\"inOutWarehouseOrderCode\":\"crk123456\",\"purchaseName\":\"物流组织\",\"deliverType\":\"四川宝利沥青有限公司\",\"consignerName\":\"张三\",\"deliverOrgName\":\"重庆东琪实业集团有限公司\",\"transportMode\":1,\"isSingleBillSystem\":1,\"transportType\":1,\"selfTakeVehicleNo\":\"苏U889966\",\"planMainTotalNum\":2689,\"planTotalWeight\":256.89,\"planTotalVolume\":25,\"planTotalNumOfPackages\":123,\"remark\":\"备注\",\"orderMateriallInfoPOList\":[{\"rowNumber\":1,\"deliverOrgName\":\"0612测试重复问题1\",\"deliverAddress\":\"软件新城\",\"planDeliverDate\":\"2020-05-29\",\"receiveOrgName\":\"山口巨大\",\"receiveAddress\":\"软件新城\",\"planArrivedDate\":\"2020-06-05\",\"materialCode\":\"W589651\",\"materialName\":\"沥青\",\"specification\":\"正常\",\"type\":\"小\",\"materialUnit\":\"吨\",\"amount\":250}]}]";
            createOrderForm(data);
            getCgOrderId("orderId");
        }
    }

    /**
     * 点击订单拆分获取信息
     *
     */
    @Test(description = "点击订单拆分获取信息", groups = {ORDER})
    private void test_003_getOrderInfoById() {
        fileEntity.getPathParam().put("orderId", parameterMap.get("orderId"));
        startPerformTest();
        saveResponseParameters("result", "result.orderRowIdStr");
    }

    /**
     * 订单拆分
     */
    @Test(description = "订单拆分", groups = {ORDER})
    private void test_004_splitOrder() {
        createOrderTaskInfoVOList(false);
        startPerformTest();
    }
    /**
     * 传入list和map封装为可用参数，用于test_004_splitOrder、test_053_splitOrder、test_058_splitOrder
     */
    private void createOrderTaskInfoVOList(Boolean warehouseKeeper) {
        //获取到编辑查询到的result
        Map parameter = (Map) parameterMap.get("result");
        //拼接orderTaskInfoVOList和map
        List<Map<String, Object>> orderTaskInfoVOList = new ArrayList<>();
        //获取到存储的orderTaskInfoVOList,拿sort
        List orderTaskInfoVOListOld = (List)parameter.get("orderTaskInfoVOList");
        int listSize = orderTaskInfoVOListOld.size();
        parameterMap.put("listSize", ++ listSize);
        Map orderTaskInfoVOMap = new HashMap();
        //物料id
        orderTaskInfoVOMap.put("orderRowId", parameterMap.get("orderRowIdStr"));
        //任务排序
        orderTaskInfoVOMap.put("sort", listSize);
        //数量
        orderTaskInfoVOMap.put("planAmount", "1");
        //供应商发货部门id   (自动化测试承运商)
        orderTaskInfoVOMap.put("deliverOrgId", mainPartOrgId);
        //发货地址 (中国北京东城自动化使用地址1)
        orderTaskInfoVOMap.put("deliverAddressId", addressId1);
        //收货地址 (中国北京东城自动化使用地址2)
        orderTaskInfoVOMap.put("receiveAddressId", addressId2);
        //供应商收货部门id (自动化使用客户)
        orderTaskInfoVOMap.put("receiveOrgId", clientOrgId);
        //发货地址类型
        orderTaskInfoVOMap.put("deliverAddressType", "1");
        //收货地址类型
        orderTaskInfoVOMap.put("receiveAddressType", "1");
        //发货时间
        orderTaskInfoVOMap.put("planDeliverTime", DateUtil.disposeDate(-1, "yyyy-MM-dd"));
        //收货时间
        orderTaskInfoVOMap.put("planReceiveTime", DateUtil.disposeDate(0, "yyyy-MM-dd"));
        //库管
        if(warehouseKeeper){
            orderTaskInfoVOMap.put("warehouseKeeperId", userkgId);
        }
        orderTaskInfoVOMap.put("isDirectSendBusiness", "0");
        //关联采购
        if(StringUtil.isNotEmpty(parameterMap.get("cgOrderId"))) {
            orderTaskInfoVOMap.put("relPurchaseOrderId", parameterMap.get("cgOrderId"));
            orderTaskInfoVOMap.put("isCheck", true);
            orderTaskInfoVOMap.put("purchaseAddressId", addressId1);
            orderTaskInfoVOMap.put("purchaseAddressName", "自动化使用地址1");
            //是否直发业务为1
            orderTaskInfoVOMap.put("isDirectSendBusiness", 1);
        }

        orderTaskInfoVOMap.put("deliverAddressName", "自动化使用地址1");
        orderTaskInfoVOMap.put("receiveAddressName", "自动化使用地址2");
        orderTaskInfoVOList.add(0, orderTaskInfoVOMap);
        parameter.put("orderTaskInfoVOList", JSONArray.toJSON(orderTaskInfoVOList));
        fileEntity.setBodyParam(parameter);
    }

    /**
     * 获取订单类型
     */
    @Test(description = "获取订单类型", groups = {ORDER})
    private void test_005_dictionaryTypeList() {
        startPerformTest();
    }

    /**
     * 获取订单状态
     */
    @Test(description = "获取订单状态", groups = {ORDER})
    private void test_006_01_dictionaryTypeList() {
        startPerformTest();
    }

    /**
     * 查找订单
     */
    @Test(description = "查找订单", groups = {ORDER})
    private void test_006_02_findByPage() {
        workOrderFindByPage();
        startPerformTest();
        saveResponseParameters("result.data[0]");
    }

    /**
     * test_006_04_findByPage和test_006_02_findByPage此代码相同，所以提取共用, orderCode从test_002_findByPage获取
     */
    private void workOrderFindByPage() {
        Long[] timeRange = DateUtil.getTimeList(-1, 0);
        fileEntity.getBodyParam().put("startTime", timeRange[0]);
        fileEntity.getBodyParam().put("endTime", timeRange[1]);
        fileEntity.getBodyParam().put("timeRange", timeRange);
        fileEntity.getBodyParam().put("orderCode", parameterMap.get("orderCode"));
    }

    @Test(description = "关闭订单", groups = {ORDER})
    private void test_006_03_operateOrder() {
        Map map = (Map) parameterMap.get("data[0]");
        map.put("orderStatus", 3);
        fileEntity.setBodyParam(map);
        startPerformTest();
    }

    /**
     * 查找订单
     */
    @Test(description = "查找订单", groups = {ORDER})
    private void test_006_04_findByPage() {
        workOrderFindByPage();
        startPerformTest();
        saveResponseParameters("result.data[0]");
    }

    @Test(description = "开启订单", groups = {ORDER})
    private void test_006_05_operateOrder() {
        Map map = (Map) parameterMap.get("data[0]");
        map.put("orderStatus", 2);
        fileEntity.setBodyParam(map);
        startPerformTest();
    }


    /**************************任务单*******************/
    /**
     * 查询任务单
     */
    @Test(description = "查询任务单", groups = {WORKORDER})
    protected void test_007_findByPage() {
        Long[] createdTimeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("createdTimeList", createdTimeList);
        startPerformTest();
        List<Map<String, Object>> dataList = response.jsonPath().get("result.data");
        for(Map<String, Object> map : dataList) {
            //3是未处理、5是处理中
            if("5".equals(map.get("taskStatus").toString()) || "3".equals(map.get("taskStatus").toString())){
                parameterMap.put("taskId", map.get("taskId"));
                parameterMap.put("taskCode", map.get("taskCode"));
                break;
            }
        }
    }


    /**
     * 编辑获取任务单
     */
    @Test(description = "编辑获取任务单", groups = {WORKORDER})
    private void test_008_getOrderTaskInfoById() {
        fileEntity.getPathParam().put("taskId", parameterMap.get("taskId"));
        startPerformTest();
        saveResponseParameters("result");
    }


    /**
     * 编辑任务单
     */
    @Test(description = "编辑任务单", groups = {WORKORDER})
    private void test_009_saveOrderTaskInfo() {
        fileEntity.setBodyParam((Map)parameterMap.get("result"));
        startPerformTest();
    }


    /**
     * 点击拆线路
     */
    @Test(description = "点击拆线路", groups = {WORKORDER})
    private void test_010_goSeparateTask() {
        fileEntity.getPathParam().put("taskId", parameterMap.get("taskId"));
        startPerformTest();
        saveResponseParameters("result");
    }
    /**
     * 查询线路信息
     */
    @Test(description = "查询线路信息", groups = {WORKORDER})
    private void test_011_02_findByPage() {
        //查找线路编号为L2008030001，线路名称为自动化测试线路的这个线路使用
        fileEntity.getBodyParam().put("lineName", "自动化测试线路");
        startPerformTest();
        //保存用于新增修改线路价格信息
        parameterMap.put("lineId", response.jsonPath().get("result.data[0].id"));
        parameterMap.put("lineCode", response.jsonPath().get("result.data[0].lineCode"));
        //起点
        parameterMap.put("fromAddressId", response.jsonPath().get("result.data[0].fromAddressId"));
        parameterMap.put("fromAddressName", response.jsonPath().get("result.data[0].fromAddressName"));
        //线路距离
        parameterMap.put("lineDistance", response.jsonPath().get("result.data[0].lineDistance"));
        //终点
        parameterMap.put("toAddressId", response.jsonPath().get("result.data[0].toAddressId"));
        parameterMap.put("toAddressName", response.jsonPath().get("result.data[0].toAddressName"));
    }

    /**
     * 拆分线路
     */
    @Test(description = "拆分线路", groups = {WORKORDER})
    private void test_011_04_separateTask() {
        create011BodyParam();
        startPerformTest();
    }
    /**
     * 用于test_011_separateTask 拆分线路处理参数
     */
    private void create011BodyParam() {
        //来源于点击拆分订单获取到任务单
        Map map = (Map)parameterMap.get("result");
        //获取拆分线路的sort
        List orderSubTaskInfoVOSOld = (List) map.get("orderSubTaskInfoVOS");
        int sort = orderSubTaskInfoVOSOld.size();
        //拼接orderSubTaskDetailFeePOS参数
        Map orderSubTaskDetailFeePOMap = new HashMap();
        orderSubTaskDetailFeePOMap.put("feeItem", 1);
        //自动化测试承运商
        orderSubTaskDetailFeePOMap.put("drewUpBillSubject", mainPartOrgId);
        //自动化测试承运商
        orderSubTaskDetailFeePOMap.put("feeBearSubject", mainPartOrgId);
        List orderSubTaskDetailFeePOS = new ArrayList();
        orderSubTaskDetailFeePOS.add(0, orderSubTaskDetailFeePOMap);

        //拼接orderSubTaskInfoVOS参数
        //获取到当前时间的日期
        String startDate = DateUtil.disposeDate(0, "yyyy-MM-dd");
        //获取到明天的日期
        String endDate = DateUtil.disposeDate(1, "yyyy-MM-dd");
        List orderSubTaskInfoVOS = new ArrayList();
        Map orderSubTaskInfoVOSMap = new HashMap();
        orderSubTaskInfoVOSMap.put("orderSubTaskDetailFeePOS", orderSubTaskDetailFeePOS);
        orderSubTaskInfoVOSMap.put("sort", ++sort);
        //线路为test_011_02_findByPage查询出的线路
        orderSubTaskInfoVOSMap.put("lineId", parameterMap.get("lineId"));
        //距离
        orderSubTaskInfoVOSMap.put("lineTransportLength", parameterMap.get("lineDistance"));
        orderSubTaskInfoVOSMap.put("deliverAddressName", parameterMap.get("fromAddressName"));
        orderSubTaskInfoVOSMap.put("deliverAddressId", parameterMap.get("fromAddressId"));
        orderSubTaskInfoVOSMap.put("receiveAddressName", parameterMap.get("toAddressName"));
        orderSubTaskInfoVOSMap.put("receiveAddressId", parameterMap.get("toAddressId"));
        orderSubTaskInfoVOSMap.put("transportPhase", "1");
        orderSubTaskInfoVOSMap.put("planAmount", "1");
        //联系人：gzx
        orderSubTaskInfoVOSMap.put("deliverContactsId", userId);
        orderSubTaskInfoVOSMap.put("deliverContactsName", "gzx");
        orderSubTaskInfoVOSMap.put("deliverContactsPhone", null);
        //联系人：gzx库管
        orderSubTaskInfoVOSMap.put("receiveContactsId", userkgId);
        orderSubTaskInfoVOSMap.put("receiveContactsName", "gzx库管");
        orderSubTaskInfoVOSMap.put("receiveContactsPhone", null);

        //自动化测试承运商
        orderSubTaskInfoVOSMap.put("deliverOrgId", mainPartOrgId);
        orderSubTaskInfoVOSMap.put("deliverOrgName", "自动化测试承运商");
        orderSubTaskInfoVOSMap.put("planDeliverTime", startDate);
        orderSubTaskInfoVOSMap.put("deliverAddressType", 1);
        //自动化使用客户
        orderSubTaskInfoVOSMap.put("receiveOrgId", clientOrgId);
        orderSubTaskInfoVOSMap.put("receiveOrgName", "自动化使用客户");
        orderSubTaskInfoVOSMap.put("planReceiveTime", endDate);
        orderSubTaskInfoVOSMap.put("receiveAddressType", 1);
        orderSubTaskInfoVOS.add(0, orderSubTaskInfoVOSMap);
        map.put("orderSubTaskInfoVOS", orderSubTaskInfoVOS);
        fileEntity.setBodyParam(map);
    }

    /**
     * 点击拆线路，，，，此处存在问题，无法进行派车
     */
    /*@Test(description = "点击拆线路", groups = {WORKORDER})
    private void test_011_05_goSeparateTask() {
        //todo
        parameterMap.put("taskId", 782);
        fileEntity.getPathParam().put("taskId", parameterMap.get("taskId"));
        startPerformTest();
        saveResponseParameters("result");
    }*/

    /**
     * 拆分线路直接派车
     */
    /*@Test(description = "拆分线路直接派车", groups = {WORKORDER})
    private void test_011_06_dispatchCarOrCarrier() {
        Map result = (Map) parameterMap.get("result");
        String planDeliverTime = DateUtil.disposeDate(-1, "yyyy-MM-dd");
        String planReceiveTime = DateUtil.disposeDate(0, "yyyy-MM-dd");
        fileEntity.getBodyParam().put("planDeliverTime", planDeliverTime);
        fileEntity.getBodyParam().put("planReceiveTime", planReceiveTime);
        fileEntity.getBodyParam().put("taskRversion", result.get("rversion"));
        fileEntity.getBodyParam().put("lineId", lineBaseId);
        fileEntity.getBodyParam().put("sort", ((List)result.get("orderSubTaskInfoVOS")).size() + 1);
        fileEntity.getBodyParam().put("lineTransportLength", parameterMap.get("lineDistance"));
        fileEntity.getBodyParam().put("deliverAddressName", parameterMap.get("fromAddressName"));
        fileEntity.getBodyParam().put("deliverAddressId", parameterMap.get("fromAddressId"));
        fileEntity.getBodyParam().put("receiveAddressName", parameterMap.get("toAddressName"));
        fileEntity.getBodyParam().put("receiveAddressId", parameterMap.get("toAddressId"));
        fileEntity.getBodyParam().put("taskCode", result.get("taskCode"));
        fileEntity.getBodyParam().put("taskId", result.get("taskId"));
        fileEntity.getBodyParam().put("orderCode", result.get("orderCode"));
        //自动化测试长租车id
        fileEntity.getBodyParam().put("mainVehicleId", vehicleId);
        startPerformTest();
    }*/
    /**
     * 关闭主任务单
     */
    @Test(description = "关闭任务单", groups = {WORKORDER})
    private void test_012_confirmTaskInfo() {
        fileEntity.getPathParam().put("taskId", parameterMap.get("taskId"));
        startPerformTest();
    }

    /**
     * 开启主任务单
     */
    @Test(description = "开启任务单", groups = {WORKORDER})
    private void test_013_confirmTaskInfo() {
        fileEntity.getPathParam().put("taskId", parameterMap.get("taskId"));
        startPerformTest();
    }



    /**
     * 根据任务编号查找拿到子任务单id
     */
    @Test(description = "根据任务编号查找拿到子任务单id", groups = {WORKORDER})
    private void test_014_findByPage() {
        replaceBodyParam("taskCode");
        Long[] createdTimeList = DateUtil.getTimeList(-1, 0);
        fileEntity.getBodyParam().put("createdTimeList", createdTimeList);
        startPerformTest();
        saveResponseParameters("result.data[0].orderSubTaskInfoVOS[0].subTaskId");
        //将直接派车的任务单号存取，用于后面运单管理取消
        parameterMap.put("taskCodeTwo", response.jsonPath().get("result.data[0].orderSubTaskInfoVOS[1].taskCode"));
    }

    /**
     * 关闭子任务单
     */
    @Test(description = "关闭子任务单", groups = {WORKORDER})
    private void test_015_confirmTaskInfo() {
        fileEntity.getPathParam().put("subTaskId", parameterMap.get("subTaskId"));
        startPerformTest();
    }


    /**
     * 开启子任务单
     */
    @Test(description = "开启子任务单", groups = {WORKORDER})
    private void test_016_confirmTaskInfo() {
        fileEntity.getPathParam().put("subTaskId", parameterMap.get("subTaskId"));
        startPerformTest();
    }


    /**
     * 编辑获取子任务单
     */
    @Test(description = "编辑获取子任务单", groups = {WORKORDER})
    private void test_017_getOrderSubTaskInfoById() {
        fileEntity.getPathParam().put("subTaskId", parameterMap.get("subTaskId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑子任务单，并且选择所属承运商为测试自动化勿删
     */
    @Test(description = "保存子任务单", groups = {WORKORDER})
    private void test_018_saveOrderSubTaskInfo() {
        Map map = ((Map)parameterMap.get("result"));
        //自动化测试承运商
        map.put("carrierId", mainPartId);
        //处理orderSubTaskDetailFeePOS为最新的版本号
        String rversion = map.get("rversion").toString();
        List orderSubTaskDetailFeePOS = (List) map.get("orderSubTaskDetailFeePOS");
        if(orderSubTaskDetailFeePOS.size() != 0) {
            Map orderSubTaskDetailFeePOSMap = (Map) orderSubTaskDetailFeePOS.get(0);
            orderSubTaskDetailFeePOSMap.put("rversion", rversion);
            orderSubTaskDetailFeePOS.add(0, orderSubTaskDetailFeePOSMap);
            map.put("orderSubTaskDetailFeePOS", orderSubTaskDetailFeePOS);
        }
        fileEntity.setBodyParam(map);
        startPerformTest();
    }

    /**
     * 拒绝
     */
    @Test(description = "拒绝",  groups = {WORKORDER})
    private void test_019_confirmTaskInfo() {
        fileEntity.getPathParam().put("subTaskId", parameterMap.get("subTaskId"));
        startPerformTest();
    }
    /**
     * 编辑获取拒绝后的子任务单
     */
    @Test(description = "编辑获取拒绝后的子任务单", groups = {WORKORDER})
    private void test_020_getOrderSubTaskInfoById() {
        fileEntity.getPathParam().put("subTaskId", parameterMap.get("subTaskId"));
        startPerformTest();
        saveResponseParameters("result");
    }


    /**
     * 编辑子任务单，并且选择所属承运商为测试自动化勿删
     */
    @Test(description = "保存子任务单", groups = {WORKORDER})
    private void test_021_saveOrderSubTaskInfo() {
        Map map = ((Map)parameterMap.get("result"));
        //处理orderSubTaskDetailFeePOS为最新的版本号
        String rversion = map.get("rversion").toString();
        //自动化测试承运商id
        map.put("carrierId", mainPartId);
        List orderSubTaskDetailFeePOS = (List) map.get("orderSubTaskDetailFeePOS");
        if(orderSubTaskDetailFeePOS.size() != 0) {
            Map orderSubTaskDetailFeePOSMap = (Map) orderSubTaskDetailFeePOS.get(0);
            orderSubTaskDetailFeePOSMap.put("rversion", rversion);
            orderSubTaskDetailFeePOS.add(0, orderSubTaskDetailFeePOSMap);
            map.put("orderSubTaskDetailFeePOS", orderSubTaskDetailFeePOS);
        }
        fileEntity.setBodyParam(map);
        startPerformTest();
    }

    /**
     * 确认
     */
    @Test(description = "确认",  groups = {WORKORDER})
    private void test_022_confirmTaskInfo() {
        fileEntity.getPathParam().put("subTaskId", parameterMap.get("subTaskId"));
        startPerformTest();
    }

    /**
     * 获取拆运单数据
     */
    @Test(description = "获取拆运单数据", groups = {WORKORDER})
    private void test_023_goSeparateSubTask() {
        fileEntity.getPathParam().put("subTaskId", parameterMap.get("subTaskId"));
        startPerformTest();
        saveResponseParameters("result.orderSubTaskList");
    }

    /**
     * 拆运单
     */
    @Test(description = "拆运单", groups = {WORKORDER})
    private void test_024_createWaybillInfo() {
        List orderSubTaskList = (List) parameterMap.get("orderSubTaskList");
        Map map = (Map) orderSubTaskList.get(0);
        //拆分订单数量
        map.put("planSendAmount", 1);
        map.put("planInStartPlaceTime", map.get("planDeliverTime"));
        map.put("planArrivedEndPlaceTime", map.get("planReceiveTime"));
        map.put("mainPartId", mainPartId);
        orderSubTaskList.set(0, map);
        fileEntity.setBodyParamList(orderSubTaskList);
        startPerformTest();
    }

    /*********************************************运单管理****************************************/

    /**
     * 根据运单号模糊查询运单
     */
    @Test(description = "根据运单号模糊查询运单", groups = {WAYBILL})
    private void test_025_findByPage() {
        //查找状态为1的也就是未处理状态运单
        fileEntity.getBodyParam().put("handleStatus", 1);
        fileEntity.getBodyParam().put("createdTimeList", DateUtil.getTimeList(-1, 0));
        startPerformTest();
        saveResponseParameters("result.data[0]");
    }

    /**
     * 编辑运单
     */
    @Test(description = "编辑运单", groups = {WAYBILL})
    private void test_026_saveOrderWaybill() {
        fileEntity.setBodyParam((Map) parameterMap.get("data[0]"));
        startPerformTest();
    }

    /**
     * 依赖于test_025_findByPage获取data[0]
     */
    @Test(description = "根据运单号精确查询运单", groups = {WAYBILL})
    private void test_027_findByPage() {
        Map data = (Map)parameterMap.get("data[0]");
        fileEntity.getBodyParam().put("waybillCode", data.get("waybillCode"));
        fileEntity.getBodyParam().put("createdTimeList", DateUtil.getTimeList(-1, 0));
        startPerformTest();
        saveResponseParameters("result.data[0]");
    }
    /**
     * 派车选择自动化测试司机（勿删）和自动化测试车牌（勿删）
     */
    @Test(description = "派车", groups = {WAYBILL})
    private void test_028_dispatchCar() {
        Map data = (Map)parameterMap.get("data[0]");
        //自动化使用司机信息
        data.put("mainDriverId", driverId);
        data.put("mainDriverName", "自动化测试司机");
        data.put("mainDriverPhone", "15594668868");
        //自动化测试长租车信息
        data.put("mainVehicleId", vehicleId);
        data.put("mainVehicleNo", "自动化测试长租车");
        fileEntity.setBodyParam(data);
        startPerformTest();
    }


    /**
     * 依赖于test_014_findByPage获取taskCode
     */
    /*@Test(description = "根据运单号模糊查询运单", groups = {WAYBILL})
    private void test_029_findByPage() {
        fileEntity.getBodyParam().put("waybillCode", parameterMap.get("taskCodeTwo"));
        fileEntity.getBodyParam().put("createdTimeList", DateUtil.getTimeList(-1, 0));
        startPerformTest();
        saveResponseParameters("result.data[0].waybillId");
    }*/

    /**
     * 取消直接派车的订单
     */
    /*@Test(description = "取消直接派车的运单", groups = {WAYBILL})
    private void test_030_cancelWaybillByWaybillId() {
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        startPerformTest();
    }*/


    /******************************************运单监控****************************************/


    /**
     * 获取运单监控接口
     */
    @Test(description = "获取运单监控", groups = {MONITORING})
    private void test_031_findWaybillMonitor() {
        Long[] createdTimeList = DateUtil.getTimeList(-1, 1);
        fileEntity.getBodyParam().put("createdTimeList", createdTimeList);
        startPerformTest();
    }



    /***********************************************询价*******************************************/
    /**
     * 新增询价 ,依赖于 test_011_02_findByPage test_011_01_addOrUpdate 的数据
     */
    @Test(description = "新增询价", groups = {ENQUIRY})
    private void test_032_saveQuiryLinePrice() {
        Map map = fileEntity.getBodyParamList().get(0);
        map.put("lineName", parameterMap.get("lineName"));
        map.put("lineCode", parameterMap.get("lineCode"));
        map.put("lineId", parameterMap.get("lineId"));
        map.put("toAddressId", parameterMap.get("toAddressId"));
        map.put("toAddressName", parameterMap.get("toAddressName"));
        map.put("fromAddressId", parameterMap.get("fromAddressId"));
        map.put("fromAddressName", parameterMap.get("fromAddressName"));
        map.put("createdTimeList", DateUtil.getTimeList(-1, 0));
        startPerformTest();
    }

    /**
     * 查询询价管理
     */
    @Test(description = "查询询价管理", groups = {ENQUIRY})
    private void test_033_findByPage() {
        replaceBodyParam("lineName");
        fileEntity.getBodyParam().put("createdTimeList", DateUtil.getTimeList(-1, 0));
        startPerformTest();
        saveResponseParameters("result.data[0].id");
    }

    /**
     * 发起询价
     */
    @Test(description = "发起询价", groups = {ENQUIRY})
    private void test_034_quiryLinePrice() {
        fileEntity.getPathParam().put("id", parameterMap.get("id"));
        startPerformTest();
    }





    /************************统计报表*******************************/

    /**
     * 班航次报表查询
     */
    @Test(description = "班航次报表查询", groups = {REPORT})
    private void test_035_findByPage() {
        Long[] timeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("timeList", timeList);
        startPerformTest();
    }

    /**
     * 承运商里程查询
     */
    @Test(description = "承运商里程查询", groups = {REPORT})
    private void test_036_findByPage() {
        Long[] timeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("timeList", timeList);
        startPerformTest();
    }

    /**
     * 车辆运输里程查询
     */
    @Test(description = "车辆运输里程查询", groups = {REPORT})
    private void test_037_findByPage() {
        Long[] timeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("timeList", timeList);
        startPerformTest();
    }

    /**
     * 运单报表查询
     */
    @Test(description = "运单报表查询", groups = {REPORT})
    private void test_038_findWaybillInfoByPage() {
        Long[] planTimList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("planTimList", planTimList);
        startPerformTest();
    }

    /**
     * 盈亏量查询
     */
    @Test(description = "盈亏量查询", groups = {REPORT})
    private void test_039_findProfitAndLossByPage() {
        Long[] createdTimeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("createdTimeList", createdTimeList);
        startPerformTest();
    }

    /**
     * 派车执行率
     */
    @Test(description = "派车执行率", groups = {REPORT})
    private void test_040_findByPage() {
        Long[] timeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("timeList", timeList);
        startPerformTest();
    }


    /**
     * 进厂及时率
     */
    @Test(description = "进厂及时率", groups = {REPORT})
    private void test_041_findInTimelyRateByPage() {
        Long[] createdTimeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("createdTimeList", createdTimeList);
        startPerformTest();
    }


    /**
     * 送达及时率
     */
    @Test(description = "送达及时率", groups = {REPORT})
    private void test_042_findInTimelyRateByPage() {
        Long[] createdTimeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("createdTimeList", createdTimeList);
        startPerformTest();
    }

    /**
     * 承运商运费
     */
    @Test(description = "承运商运费", groups = {REPORT})
    private void test_043_findSupplierCost() {
        Long[] timeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("timeList", timeList);
        startPerformTest();
    }


    /**
     * 订单运费
     */
    @Test(description = "订单运费", groups = {REPORT})
    private void test_044_findOrderCost() {
        Long[] timeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("timeList", timeList);
        startPerformTest();
    }


    /**
     * 财务发票费用
     */
    @Test(description = "财务发票费用", groups = {REPORT})
    private void test_045_findInvoiceCost() {
        Long[] timeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("timeList", timeList);
        startPerformTest();
    }

    /**
     * 线路偏移报表
     */
    @Test(description = "线路偏移报表", groups = {REPORT})
    private void test_046_findLineOffByPage() {
        Long[] createdTimeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("createdTimeList", createdTimeList);
        startPerformTest();
    }

    /**
     * 异常运单
     */
    @Test(description = "异常运单", groups = {REPORT})
    private void test_047_findExceptionWaybillByPage() {
        Long[] exceptionProduceTimeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("exceptionProduceTimeList", exceptionProduceTimeList);
        startPerformTest();
    }

    /**
     * 订单同步地址修改
     */
    @Test(description = "订单同步地址修改", groups = {REPORT})
    private void test_048_findByPage() {
        Long[] timeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("timeList", timeList);
        startPerformTest();
    }

    /**
     * 车辆报警处理
     */
    @Test(description = "车辆报警处理", groups = {REPORT})
    private void test_049_findPage() {
        Long[] timeRange = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("bTime", timeRange[0]);
        fileEntity.getBodyParam().put("eTime", timeRange[1]);
        startPerformTest();
    }










    /**
     * 下载运单excel
     */
    //@Test(description = "下载运单excel", groups = {REPORT})
    private void test_050_exportWaybillInfo() {
        startPerformTest();
        downloadSaveFile("运单.xls");
    }


    /*******************销售订单有库管  销售订单，且其是客户自提，自己拆自己并且完成******************/

    /**
     * 查询订单  销售订单，且其是客户自提
     */
    @Test(description = "查询订单管理", groups = {ORDER})
    private void test_051_findByPage() {
        Long[] timeRange = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("startTime", timeRange[0]);
        fileEntity.getBodyParam().put("endTime", timeRange[1]);
        fileEntity.getBodyParam().put("timeRange", timeRange);
        //先删除orderId，然后查找新的，如果未找到新得则新增
        parameterMap.remove("orderId");
        getOrderId();
        //未查找到符合得orderId数据则新增
        if(parameterMap.get("orderId") == null) {
            String data = "[{\"orderCode\":\"xsgzx\",\"invoiceTime\":\"2020-05-29\",\"orderType\":2,\"operateType\":1,\"inOutWarehouseOrderCode\":\"crk123456\",\"purchaseName\":\"物流组织\",\"deliverType\":\"四川宝利沥青有限公司\",\"consignerName\":\"张三\",\"deliverOrgName\":\"重庆东琪实业集团有限公司\",\"transportMode\":2,\"isSingleBillSystem\":1,\"transportType\":1,\"selfTakeVehicleNo\":\"苏U889966\",\"planMainTotalNum\":2689,\"planTotalWeight\":256.89,\"planTotalVolume\":25,\"planTotalNumOfPackages\":123,\"remark\":\"备注\",\"orderMateriallInfoPOList\":[{\"rowNumber\":1,\"deliverOrgName\":\"0612测试重复问题1\",\"deliverAddress\":\"软件新城\",\"planDeliverDate\":\"2020-05-29\",\"receiveOrgName\":\"山口巨大\",\"receiveAddress\":\"软件新城\",\"planArrivedDate\":\"2020-06-05\",\"materialCode\":\"W589651\",\"materialName\":\"沥青\",\"specification\":\"正常\",\"type\":\"小\",\"materialUnit\":\"吨\",\"amount\":250}]}]";
            createOrderForm(data);
            getOrderId();
        }
    }

    /**
     * 点击订单拆分获取信息
     *
     */
    @Test(description = "点击订单拆分获取信息", groups = {ORDER})
    private void test_052_getOrderInfoById() {
        fileEntity.getPathParam().put("orderId", parameterMap.get("orderId"));
        startPerformTest();
        saveResponseParameters("result", "result.orderRowIdStr");
    }

    /**
     * 订单拆分
     */
    @Test(description = "订单拆分", groups = {ORDER})
    private void test_053_splitOrder() {
        createOrderTaskInfoVOList(true);
        startPerformTest();
    }

    /**
     * 查找运单
     */
    @Test(description = "查找运单", groups = {WAYBILL})
    private void test_054_findByPage() {
        //查找状态为3的也就是运输中状态运单
        fileEntity.getBodyParam().put("handleStatus", 3);
        fileEntity.getBodyParam().put("createdTimeList", DateUtil.getTimeList(-1, 0));
        startPerformTest();
        saveResponseParameters("result.data[0]");
    }

    /**
     * 完成
     */
    @Test(description = "完成", groups = {WAYBILL})
    private void test_055_finishWaybill() {
        Map map = (Map)parameterMap.get("data[0]");
        fileEntity.setBodyParam(map);
        startPerformTest();
    }

/**********************销售订单+采购+直发*******************/
    /**
     * 查询订单
     */
    @Test(description = "查询订单管理", groups = {ORDER})
    private void test_056_findByPage() {
        Long[] timeRange = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("startTime", timeRange[0]);
        fileEntity.getBodyParam().put("endTime", timeRange[1]);
        fileEntity.getBodyParam().put("timeRange", timeRange);
        parameterMap.remove("orderId");
        getOrderId();
        getCgOrderId("cgOrderId");
        //未查找到符合得销售订单客户自提orderId数据则新增
        if(parameterMap.get("orderId") == null) {
            String data = "[{\"orderCode\":\"xsgzx\",\"invoiceTime\":\"2020-05-29\",\"orderType\":2,\"operateType\":1,\"inOutWarehouseOrderCode\":\"crk123456\",\"purchaseName\":\"物流组织\",\"deliverType\":\"四川宝利沥青有限公司\",\"consignerName\":\"张三\",\"deliverOrgName\":\"重庆东琪实业集团有限公司\",\"transportMode\":2,\"isSingleBillSystem\":1,\"transportType\":1,\"selfTakeVehicleNo\":\"苏U889966\",\"planMainTotalNum\":2689,\"planTotalWeight\":256.89,\"planTotalVolume\":25,\"planTotalNumOfPackages\":123,\"remark\":\"备注\",\"orderMateriallInfoPOList\":[{\"rowNumber\":1,\"deliverOrgName\":\"0612测试重复问题1\",\"deliverAddress\":\"软件新城\",\"planDeliverDate\":\"2020-05-29\",\"receiveOrgName\":\"山口巨大\",\"receiveAddress\":\"软件新城\",\"planArrivedDate\":\"2020-06-05\",\"materialCode\":\"W589651\",\"materialName\":\"沥青\",\"specification\":\"正常\",\"type\":\"小\",\"materialUnit\":\"吨\",\"amount\":250}]}]";
            createOrderForm(data);
            getOrderId();
        }
        //未查找到符合得采购订单cgOrderId数据则新增
        if(parameterMap.get("cgOrderId") == null) {
            String data = "[{\"orderCode\":\"cggzx\",\"invoiceTime\":\"2020-05-29\",\"orderType\":1,\"operateType\":1,\"inOutWarehouseOrderCode\":\"crk123456\",\"purchaseName\":\"物流组织\",\"deliverType\":\"四川宝利沥青有限公司\",\"consignerName\":\"张三\",\"deliverOrgName\":\"重庆东琪实业集团有限公司\",\"transportMode\":1,\"isSingleBillSystem\":1,\"transportType\":1,\"selfTakeVehicleNo\":\"苏U889966\",\"planMainTotalNum\":2689,\"planTotalWeight\":256.89,\"planTotalVolume\":25,\"planTotalNumOfPackages\":123,\"remark\":\"备注\",\"orderMateriallInfoPOList\":[{\"rowNumber\":1,\"deliverOrgName\":\"0612测试重复问题1\",\"deliverAddress\":\"软件新城\",\"planDeliverDate\":\"2020-05-29\",\"receiveOrgName\":\"山口巨大\",\"receiveAddress\":\"软件新城\",\"planArrivedDate\":\"2020-06-05\",\"materialCode\":\"W589651\",\"materialName\":\"沥青\",\"specification\":\"正常\",\"type\":\"小\",\"materialUnit\":\"吨\",\"amount\":250}]}]";
            createOrderForm(data);
            getCgOrderId("cgOrderId");
        }
    }

    /**
     * 获取getOrderId
     */
    private void getOrderId() {
        startPerformTest();
        //寻找第一条处理中或者未处理的订单
        List dataList = response.jsonPath().get("result.data");
        for(Object object : dataList) {
            Map dataMap = (Map)object;
            //只取销售订单，且其是客户自提，1是采购订单，2是销售订单
            if("2".equals(dataMap.get("orderType").toString()) && "2".equals(dataMap.get("transportMode").toString())) {
                //如果该订单处于未处理或者处理中状态，说明可以操作,则获取其orderId并存储
                if(("1".equals(dataMap.get("orderStatus").toString()) && "未处理".equals(dataMap.get("orderStatusName")))
                        || ("2".equals(dataMap.get("orderStatus").toString()) && "处理中".equals(dataMap.get("orderStatusName")))) {
                    parameterMap.put("orderId", dataMap.get("orderId"));
                }
            }
        }
    }
    //获取cgOrderId
    private void getCgOrderId(String savaKet) {
        startPerformTest();
        //寻找第一条处理中或者未处理的订单
        List dataList = response.jsonPath().get("result.data");
        for(Object object : dataList) {
            Map dataMap = (Map)object;
            //只取采购订单，1是采购订单，2是销售订单
            if("1".equals(dataMap.get("orderType").toString())) {
                //如果该订单处于未处理或者处理中状态，说明可以操作,则获取其orderId并存储
                if(("1".equals(dataMap.get("orderStatus").toString()) && "未处理".equals(dataMap.get("orderStatusName")))
                        || ("2".equals(dataMap.get("orderStatus").toString()) && "处理中".equals(dataMap.get("orderStatusName")))) {
                    parameterMap.put(savaKet, dataMap.get("orderId"));
                    parameterMap.put("orderCode", dataMap.get("orderCode"));
                }
            }
        }
    }

    /**
     * 点击订单拆分获取信息
     *
     */
    @Test(description = "点击订单拆分获取信息", groups = {ORDER})
    private void test_057_getOrderInfoById() {
        fileEntity.getPathParam().put("orderId", parameterMap.get("orderId"));
        startPerformTest();
        saveResponseParameters("result", "result.orderRowIdStr");
    }

    /**
     * 订单拆分
     */
    @Test(description = "订单拆分", groups = {ORDER})
    private void test_058_splitOrder() {
        createOrderTaskInfoVOList(true);
        startPerformTest();
    }


    /************************************c财务中心********************************/
    /**
     * 费用管理查询
     */
    @Test(description = "费用管理查询", groups = {FINANCE})
    private void test_059_findByPage() {
        Long[] timeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("timeList", timeList);
        startPerformTest();
    }

    /**
     * 对账结算单
     */
    @Test(description = "对账结算单", groups = {FINANCE})
    private void test_060_findByPage() {
        Long[] timeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("timeList", timeList);
        startPerformTest();
    }

    /**
     * 财务账单
     */
    @Test(description = "财务账单", groups = {FINANCE})
    private void test_061_findByPage() {
        Long[] timeList = DateUtil.getTimeList(-10, 0);
        fileEntity.getBodyParam().put("timeList", timeList);
        startPerformTest();
    }

    /**
     * 新增订单接口
     * @param data
     */
    public static void createOrderForm(String data) {
        String appKey = "b9c76dfd-2d14-4153-9068-51618dfed199";
        String appSecret = "42075852-6232-4432-b9b0-c0aa564f8f8a";
        String url="http://58.214.0.26:9000/api/orderModule/uploadOrder";
        long timestamp = System.currentTimeMillis();
        String encode = URLEncoder.encode(data);
        String sign = Md5Util.md5( appKey + appSecret + timestamp + appKey + data);
        String s = sendPost(url, "timestamp=" + timestamp + "&appKey=" + appKey + "&sign=" + sign + "&data=" + encode);
         System.out.println(s);
    }
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }



}