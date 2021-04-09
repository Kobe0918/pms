package com.mjrj.lzh.pms;


import cc.ebatis.impl.Init;
import cc.ebatis.pojo.ActionContext;
import cc.ebatis.pojo.SheetInfo;
import com.google.common.collect.Lists;
import com.mjrj.lzh.pms.dao.*;
import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import com.mjrj.lzh.pms.dto.LeaveDTO;
import com.mjrj.lzh.pms.dto.Lw_familyPayMentDTO;
import com.mjrj.lzh.pms.dto.pagedto.*;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.entity.*;
import com.mjrj.lzh.pms.redis.RedisComponent;
import com.mjrj.lzh.pms.service.*;
import com.mjrj.lzh.pms.springsecurity.jwt.JwtTokenUtils;
import com.mjrj.lzh.pms.util.CommonUtil;
import com.sun.rowset.internal.Row;
import javafx.scene.control.Cell;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class PmsApplicationTests {
    @Autowired
    private PmsConfigDOMapper mapper;

//    @Test
//    public void test(){
//        PmsConfigDO configDO = (PmsConfigDO) mapper.selectConfig(1);
//        System.out.println(configDO.getCaigoSpiuserId());
//    }


    @Test
    public void  test123655(){
        if("".equals("   ".toString().trim())){
            System.out.println("1");
            System.out.println(" ss ".toString().trim());
        }
    }
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    RedisComponent redisComponent;
//    @Test
//    public void test789(){
//
//        redisComponent.addStringAndExpire(CommonUtil.PHONE_PRE + "18760572441","518834",120);
//    }
//
//    @Autowired
//    private PermissionService permissionService;
//    @Test
//    public void test123412(){
//        List<SysPermissionDO> list = permissionService.selectAll();
//        for(SysPermissionDO s : list){
//            System.out.println(s.getName());
//        }
//    }
//    @Autowired
//    private CaiGoService caiGoService;
//
//
//    @Test
//    public void test154(){
//        CaigoPageDTO dfto =new CaigoPageDTO();
//        dfto.setStart(0);
//
//        dfto.setLength(10);
//        dfto.setCaigoUserId(8);
//        CaigoPageDTO mySelfWithPage = caiGoService.selectCaiGoWithPage(dfto);
//
//        System.out.println(mySelfWithPage.getData().toString());
//    }
//
//    @Autowired
//    private SysRoleService roleService;
//    @Test
//    public void test23(){
//        RolePageDTO dto = new RolePageDTO();
//        dto.setRoleName("员工");
//        dto.setDraw(1);
//        dto.setLength(10);
//        dto.setStart(0);
//        RolePageDTO roleByPage = roleService.getRoleByPage(dto);
//        for(Object d : roleByPage.getData()){
//            SysRoleDO m = (SysRoleDO)d;
//            List<Integer> permissionIds = m.getIds();
//            for(Integer i : permissionIds){
//                System.out.println(i);
//            }
//        }
//    }
//
//    @Test
//    public void test33() {
//        List <String> staticPath = new ArrayList <>();
//        staticPath.add("/webjars");
//        staticPath.add("/asserts");
//        staticPath.add("/login");
//        staticPath.add("/register");
////    stringRedisTemplate.opsForValue().set("static_path", JSON.toJSON(staticPath.toString()).toString());
////    String static_path = stringRedisTemplate.opsForValue().get("static_path");
////    List <String> strings = JSONObject.parseArray(static_path, String.class);
////    strings.forEach(s ->{
////        System.out.println(s);
////    });
//        redisTemplate.opsForList().rightPushAll("static_path", staticPath);
////    List static_path = redisTemplate.opsForList().range("static_path", 0, -1);
////    static_path.forEach(s -> {
////        System.out.println(s);
////    });
//    }
//
//    @Test
//    void contextLoads() {
//        redisTemplate.boundValueOps("Test:123456").set("nihao", 7200, TimeUnit.SECONDS);
//    }
//
//@Autowired
//private LeaveDOMapper l
//        ;
//    @Test
//    public void test74(){
//        Integer[] list = new Integer[]{60,61,73,74};
//        l.updateleaveStatusByIds((byte)5,Arrays.asList(list));
//    }
//
//    @Test
//    void testRedis() {
//        // stringRedisTemplate.opsForValue().append("lzh","897456");
//        //String value = stringRedisTemplate.opsForValue().get("lzh");
//
////        AliSendMessage.SmsResponse smsResponse = new AliSendMessage.SmsResponse();
////        smsResponse.setBizId("1");
////        redisTemplate.opsForValue().set("36",smsResponse);
////        AliSendMessage.SmsResponse smsResponse1  = (AliSendMessage.SmsResponse)redisTemplate.opsForValue().get("36");
////        System.out.println(smsResponse1.toString());
//
//        String phone = "18850917735";
//        redisTemplate.opsForHash().putAll(phone, new HashMap <String, Object>() {{
//            put("phone", phone);
//            put("code", "123456");
//            put("event", "forgot");
//        }});
//        redisTemplate.expire(phone, 60, TimeUnit.SECONDS);
//
//    }
//
//    @Test
//    public void test3() {
//
//        String prefix = "phone::" + "18850917735";
//
//        redisTemplate.opsForValue().set(prefix, "1232");
//    }
//
//
//    @Autowired
//    private UserDOMapper userDOMapper;
//
//    @Test
//    public void test4() {
//        short a = 4;
//        System.out.println(a);
////        Date idByPhone = userDOMapper.getIdByPhone("18760572441");
////        if(idByPhone == null){
////            System.out.println("weik");
////        }
////        if("".equals(idByPhone)){
////            System.out.println("ak");
////        }
////        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        System.out.println(df.format(idByPhone));
//
//    }
//
//
////    @Test
////    public void test55() {
////
////        CurrentUserDTO o = (CurrentUserDTO) redisTemplate.boundValueOps("token:50e0ab5b-842e-4c57-a872-dd4549850595").get();
////        System.out.println(o.toString());
////    }
//
//    @Test
//    public void test56() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String up_time = "2019-10-19 7:30:60";
//        String down_time = "2019-10-19 7:32:50";
//        try {
//            Date up = simpleDateFormat.parse(up_time);
//            Date down = simpleDateFormat.parse(down_time);
//            long workTime = down.getTime() - up.getTime();
//            long workHours = workTime / 1000 / 60 / 60;
//            System.out.println(workHours);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    @Autowired
//    private AttendanceDOMapper attendanceDOMapper;
//
//    @Test
//    public void test() {
////        String resourceHandler= "/uploadFiles/**";
////        String f =File.separator;
////        System.out.println(resourceHandler.lastIndexOf(f));
////        System.out.println(resourceHandler.substring(0, resourceHandler.lastIndexOf(File.separator) + 1) );
//
//        Boolean t = getName("201111-123");
//        System.out.println(t);
//    }
//
//    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    public boolean getName(String s) {
//        if (s != null) {
//            try {
//                dateFormat.parse(s);
//                return true;
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//
//    @Test
//    public void test28() {
//        LeaveDTO dto = new LeaveDTO();
//        LeaveDO ldo = new LeaveDO();
////             dto.setBeginTime("2020-04-05 12:30:10");
////             dto.setLeaveStyle(1);
//        BeanUtils.copyProperties(ldo, dto);
//        System.out.println(ldo.toString());
//    }
//
//    @Autowired
//    private LeaveDOMapper mapper;
//
//    @Test
//    public void test100() {
//
//        System.out.println(mapper.selectByLeaveId(37));
//    }
//
//    private SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    @Autowired
//    JwtTokenUtils jwtTokenUtils;
//
//    @Test
//    public void test00() {
//        CurrentUserDTO dto = new CurrentUserDTO();
////        dto.setTelephone("linzhenag");
////        String id = UUID.randomUUID().toString();
////        System.out.println(id + " :id");  //6c3da585-d182-407f-a881-bce97e59b889
////        dto.setToken(id);
////        SysPermissionDO sysPermissionDO1 = new SysPermissionDO();
////        sysPermissionDO1.setPermission("save");
////        SysPermissionDO sysPermissionDO = new SysPermissionDO();
////        sysPermissionDO.setPermission("update");
////       dto.setSysPermission(new ArrayList <SysPermissionDO>(){{add(sysPermissionDO);add(sysPermissionDO1);}});
////        String token = jwtTokenUtils.generateToken(dto);
////        System.out.println(token+ "token");
//        //eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaW56aGVuYWciLCJjcmVhdGVkIjoxNTg2NjIyMjYzMzU4LCJyb2xlcyI6W3siYXV0aG9yaXR5Ijoic2F2ZSJ9LHsiYXV0aG9yaXR5IjoidXBkYXRlIn1dLCJpZCI6IjZjM2RhNTg1LWQxODItNDA3Zi1hODgxLWJjZTk3ZTU5Yjg4OSIsImV4cCI6MTU4NzIyMjI2M30.I6VDIrTxGFaxUf991H6GfKPF6DuCm0eBBhED0bJSAP7ZfP1in6cXkUkiz0CMThQgWFm-VaxrOnrLnhD9cR3KPQ
//
//        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaW56aGVuYWciLCJjcmVhdGVkIjoxNTg2NjU1Mjg0NzI3LCJyb2xlcyI6W3siYXV0aG9yaXR5Ijoic2F2ZSJ9LHsiYXV0aG9yaXR5IjoidXBkYXRlIn1dLCJpZCI6ImQ2YjYxZDYzLWVlMWMtNGFkZC05MTE2LTc3MmU1NDc2MTUzOSIsImV4cCI6MTU4NjY1NTM0NH0.rWv5P6uDiU2w-cZGfQZJPLSbzRQkxwsXBwWtJMAZ_aVCMh7ZPBhEDiX5jNMbdO4KWneY5Q7t77RpjjbTDczL0w";
//        String usernameFromToken = jwtTokenUtils.getUsernameFromToken(token);
//        System.out.println(usernameFromToken + ": username");
////        Boolean aBoolean = jwtTokenUtils.canTokenBeRefreshed(usernameFromToken);
////        System.out.println(aBoolean + ": canTokenBeRefreshed");
////        Date expirationDateFromToken = jwtTokenUtils.getExpirationDateFromToken(token);
////        System.out.println(sim.format(expirationDateFromToken) + ": getExpirationDateFromToken");
//        dto.setToken("d6b61d63-ee1c-4add-9116-772e54761539");
//        Boolean aBoolean1 = jwtTokenUtils.validateToken(token, dto);
//        System.out.println(aBoolean1 + ": validateToken");
//        String redist = jwtTokenUtils.getTokenKeyFromToken(token);
//        System.out.println(redist + ": reidst");
//
////        d6b61d63-ee1c-4add-9116-772e54761539
//
//    }
//
//    @Test
//    public void getToken() {
//        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODg1MDkxNzczNSIsImNyZWF0ZWQiOjE1ODY2MjMwNzA2NjAsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJkZXB0In0seyJhdXRob3JpdHkiOiJhdHRlbmRhbmNlOnNhdmUifSx7ImF1dGhvcml0eSI6InVwZGF0ZTpmaWxlIn1dLCJpZCI6ImQxODUwNTBiLTAyMDAtNDA2ZC1hOWYxLWExZjE5MmMwMTlmMyIsImV4cCI6MTU4NzIyMzA3MH0.Bqce5mH34z00QATR9lL8wIgXVSeYPm1tYiqtpJ3lQ7OTMqrOQ-XVkJjOJhNyho58SOr7-T3VYih89MrQVUU25Q";
////        Date create = jwtTokenUtils.getCreatedDateFromToken(token);
////        Date expira = jwtTokenUtils.getExpirationDateFromToken(token);
////        System.out.println(sim.format(create) +"\n"+sim.format(expira));
////        System.out.println(
////                expira.getTime()+ "\n" + System.currentTimeMillis()
////        );
////        Date date = jwtTokenUtils.generateExpirationDate();
////        String s = sim.format(date);
////        System.out.println(s);
//    }
//
//    @Autowired
//    private SysPermissionDOMapper s;
//
//    @Test
//    public void testPer() {
//        List <SysPermissionDO> sysPermissionDOS = s.selectPermissionByRoleId(1);
//
//        final List <SysPermissionDO> sider = sysPermissionDOS.stream().filter(l -> l.getType().equals(true)).collect(Collectors.toList());
//        System.out.println(sider.size() + " 首节点");
//    }
//
//    @Autowired
//    private SysUserService sysUserService;
//
//    @Test
//    public void testUpdateLogin() {
//        sysUserService.loginSuccessProcedure(3);
//    }
//
//    @Autowired
//    private LeaveService leaveService;
//    @Autowired
//    private DeptService deptService;
//
//    @Test
//    public void test233() {
//        LeavePageDTO dto = new LeavePageDTO();
//        dto.setUserName("2");
//        dto.setStart(0);
//        dto.setLength(10);
//        PageDTO leaveByUserWithPage = leaveService.getLeaveByUserWithPage(dto);
//        List <LeaveDO> data = leaveByUserWithPage.getData();
//        if (data != null) {
//            System.out.println(data.size());
//        } else {
//            System.out.println("为空");
//        }
//    }
//
//    @Test
//    public void test2333() {
//        DeptPageDTO dto = new DeptPageDTO();
//        dto.setStart(0);
//        dto.setLength(10);
//        dto.setDeptName("销售部");
//        DeptPageDTO deptByPage = deptService.getDeptByPage(dto);
//        List <DeptDO> data = dto.getData();
//        if (data != null) {
//            for (DeptDO d : data) {
//                System.out.println(d.getConnectUser().getTelephone());
//            }
//        } else {
//            System.out.println("为空");
//        }
//    }
//
//    @Autowired
//    DeptDOMapper deptDOMapper;
//
//    @Test
//    public void test123456() {
////        List <DeptDO> dos = deptDOMapper.selectTest();
////        if(dos != null){
////            for(DeptDO d : dos){
////                System.out.println(d.getConnectUser().getTelephone());
////            }
////        }
//    }
//
//    @Test
//    public void tesrt12345122() {
//        ResponseResult userSelect2 = sysUserService.getUserSelect2(null);
//        List <SysUserDO> data = (List <SysUserDO>) userSelect2.getData();
//        System.out.println(data.size());
//    }
//
//    @Autowired
//    private PositionService positionService;
//
//    @Test
//    public void test232() {
//        ResponseResult responseResult = positionService.selectAllBySelect2();
//        List <PositionDO> data = (List <PositionDO>) responseResult.getData();
//        System.out.println(data.size());
//    }
//
////    @Test
////    public void test4567() {
////        List <DeptDO> update_time = deptDOMapper.selectDeptByPage(null);
////        for (DeptDO d : update_time) {
////            System.out.println(d.toString());
////        }
////    }
//
//    @Autowired
//    private SysDeptPositionRelationDOMapper sysDeptPositionRelationDOMapper;
//
//    @Test
//    public void test11() {
////        sysDeptPositionRelationDOMapper.deleteByDeptIdAndAnyPositionId(90,new ArrayList <Integer>(){{add(1);
////        add(2);add(3);}});
//
//
//        sysDeptPositionRelationDOMapper.insertByDeptIdAndAnyPositionId(new ArrayList <SysDeptPositionRelationDO>() {
//            {
//                add(new SysDeptPositionRelationDO(1, 2));
//                add(new SysDeptPositionRelationDO(1, 3));
//                add(new SysDeptPositionRelationDO(1, 4));
//            }
//        });
//    }
//
//    @Test
//    public void test1123456() {
//        PositionPageDTO pageDTO = new PositionPageDTO();
//        pageDTO.setDraw(1);
//        pageDTO.setStart(0);
//        pageDTO.setLength(10);
//        pageDTO.setPositionName("员工");
//        PositionPageDTO positionByPage = positionService.getPositionByPage(pageDTO);
//        for (Object p : positionByPage.getData()) {
//            PositionDO m = (PositionDO) p;
//            System.out.println(m.toString());
//        }
//    }
//
//
//    @Test
//    public void test123456656() {
////        PositionDO po = new PositionDO(null,"秘书",true,"总经理秘书",null,null,null,null,
////                (byte)2,new ArrayList <Integer>(){{add(1);
////            add(2);}},null);
////        positionService.savePosition(po);
//
//        positionService.deletePosition(15);
//    }

//    @Test
//    public void test456(){
//
//        List<String> list = Lists.newArrayList(
//                "bcd", "cde", "def", "ab");
//        List<String> result = list.stream()
//                //.parallel()
//                .filter(e -> e.length() >= 3)
//                .map(e -> e.charAt(0))
//                //.peek(System.out :: println)
//                //.sorted()
//                //.peek(e -> System.out.println("++++" + e))
//                .map(e -> String.valueOf(e))
//                .collect(Collectors.toList());
//        System.out.println("----------------------------");
//        System.out.println(result);
//
//    }
//
//
//    @Test
//    public void test789(){
//
//        List<String> list = Lists.newArrayList(
//                "bcd", "cde", "def", "abc");
//
//        List<String> result = Lists.newArrayListWithCapacity(list.size());
//        for (String str : list) {
//            if (str.length() >= 3) {
//                char e = str.charAt(0);
//                String tempStr = String.valueOf(e);
//                result.add(tempStr);
//            }
//        }
//        System.out.println("----------------------------");
//        System.out.println(result);
//
//    }
//
//    private static Map<String,Integer> map = new HashMap <String,Integer>(){
//        {
//            put("跨省异地就医登记信息",1);
//            put("特殊病种登记",2);
//        }
//    };
//
//
//@Test
//public void test2023() throws IOException {
//    parseExcelFile("F:/ErrorData");
//}
//
//    /**
//     * 1.读取指定路径下的文件夹(1.不存在：日志打印无数据 2.存在：xlsx文件和xls文件区分开读取)
//     * 2.根据文件名判断读取文件对应的类型，循环读取文件夹下的文件（100条插入一次数据库）
//     * 3.新建错误数据存放的表，用这个表关联数据获取对应的最新的数据继续推送
//     */
//    private static int VALUE =0;
//    public void parseExcelFile(String path ) throws IOException {
//        List <File> files = new ArrayList <>();
//        File file = new File(path);
//        if(file.exists()){
//            File[] listFiles = file.listFiles();
////            log.info(String.valueOf(listFiles.length));
//            if(null == listFiles | listFiles.length == 0){
////               log.info("文件夹下没有文件");
//            }else{
//                for(File f : listFiles){
//
//                    if(f.isDirectory()){
////                        根据文件夹名称判断类别
//                        String tableName = f.getName();
//                        VALUE = map.get(tableName);
//                        parseExcelFile(f.getPath());
//                    }
//                    if(VALUE == 0){
//                       log.info("文件需要放在相应的表名文件夹下，才可识别");
//                       return;
//                    }
//                     switch (VALUE){
//                        case 1: ;break;
//                        case 2: zwKa08(f.getPath());break;
//                     }
////                       String fileName = f.getName();
////                       log.info(f.getPath() + " : f.getPath()");
////                        if(fileName.endsWith(".xlsx")){
////                             xlsxFile(f.getPath(),value);
////                        }
////                        if(fileName.endsWith(".xls")){
////                            xlsFile(f.getPath(),value);
////                        }
//                }
//            }
//        }else{
//            log.info("不存在该目录");
//        }
//    }
//
//
//    public void xlsxFile(String path ,int value) throws IOException {
//        //用流的方式先读取到你想要的excel的文件
//        FileInputStream fis=new FileInputStream(new File(path));
//        //获取整个excel
//        XSSFWorkbook xb = new XSSFWorkbook(fis);
//        //获取第一个表单sheet
//        XSSFSheet sheet = xb.getSheetAt(0);
//        //获取第一行
//        int firstrow=    sheet.getFirstRowNum();
//        log.info(firstrow + " : 第一行");
//        //获取最后一行
//        int lastrow=    sheet.getLastRowNum();
//        log.info(lastrow + " : 最后一行");
//        //循环行数依次获取列数
//        for (int i = firstrow; i < lastrow+1; i++) {
//            //获取哪一行i
//            XSSFRow row = sheet.getRow(i);
//            if (row!=null) {
//                //获取这一行的第一列
//                int firstcell=    row.getFirstCellNum();
//                //获取这一行的最后一列
//                int lastcell=    row.getLastCellNum();
//                //创建一个集合，用处将每一行的每一列数据都存入集合中
//                List<String> list=new ArrayList<>();
//                for (int j = firstcell; j <lastcell; j++) {
//                    //获取第j列
//                    XSSFCell cell = row.getCell(j);
//
//                    if (cell!=null) {
//                        log.info(cell+"\t");
//                        list.add(cell.toString());
//                    }
//                }
//
//                System.out.println(list.toString());
//            }
//        }
//        fis.close();
//
//    }
//
//
//    public void xlsFile(String path ,int value) throws IOException {
//        //用流的方式先读取到你想要的excel的文件
//        FileInputStream fis=new FileInputStream(new File(path));
//        //解析excel
//        POIFSFileSystem pSystem=new POIFSFileSystem(fis);
//        //获取整个excel
//        HSSFWorkbook hb=new HSSFWorkbook(pSystem);
//        System.out.println(hb.getNumCellStyles());
//        //获取第一个表单sheet
//        HSSFSheet sheet=hb.getSheetAt(0);
//        //获取第一行
//        int firstrow=    sheet.getFirstRowNum();
//        log.info(firstrow + " : 第一行");
//        //获取最后一行
//        int lastrow=    sheet.getLastRowNum();
//        log.info(lastrow + " : 最后一行");
//        //循环行数依次获取列数
//        for (int i = firstrow; i < lastrow+1; i++) {
//            //获取哪一行i
//            HSSFRow row = sheet.getRow(i);
//            if (row!=null) {
//                //获取这一行的第一列
//                int firstcell=    row.getFirstCellNum();
//                //获取这一行的最后一列
//                int lastcell=    row.getLastCellNum();
//                //创建一个集合，用处将每一行的每一列数据都存入集合中
//                List<String> list=new ArrayList<>();
//                for (int j = firstcell; j <lastcell; j++) {
//                    //获取第j列
//                    HSSFCell cell = row.getCell(j);
//
//                    if (cell!=null) {
//                        log.info(cell+"\t");
//                        list.add(cell.toString());
//                    }
//                }
//
//                System.out.println(list.toString());
//            }
//        }
//        fis.close();
//
//    }
//
//    @Test
//    public void test33(){
//        String s = "姓名(必填)";
//        if(s.contains("必填")){
//            System.out.println("可判断");
//        }else{
//            System.out.println("不可判断");
//        }
//    }
//
//
//
//    public void zwKa08(String path){
//        File file = new File(path);
//        Init<ZwKa08Temp> init = new Init<ZwKa08Temp>(file, ZwKa08Temp.class, false);
//        ActionContext<ZwKa08Temp> act = init.start();
//        List <SheetInfo <ZwKa08Temp>> sheets = act.getSheets();
//        log.info(String.valueOf(sheets.size())+":  sheets 长度");//2
//        for(SheetInfo <ZwKa08Temp> s :sheets){
//            List <ZwKa08Temp> info = s.getInfo();
//            log.info(String.valueOf(info.size())+":  info 长度");
//            for(ZwKa08Temp zw : info){
//                   log.info(zw.toString());
//            }
//        }
//    }
//
//    public void zwKslw(String path){
//        File file = new File(path);
//        Init<ZwKslwTemp> init = new Init<ZwKslwTemp>(file, ZwKslwTemp.class, false);
//        ActionContext<ZwKslwTemp> act = init.start();
//        List <SheetInfo <ZwKslwTemp>> sheets = act.getSheets();
//        log.info(String.valueOf(sheets.size())+":  sheets 长度");//2
//        for(SheetInfo <ZwKslwTemp> s :sheets){
//            List <ZwKslwTemp> info = s.getInfo();
//            log.info(String.valueOf(info.size())+":  info 长度");
//            for(ZwKslwTemp zw : info){
//                log.info(zw.toString());
//            }
//        }
//    }
//
//    @Autowired
//    private SysLogsService sysLogsService;
//
//    @Test
//    public void test3236(){
//     List<String> strings  = Arrays.asList("I","love","you");
//        List <String> list = strings.stream().filter(s -> !s.equalsIgnoreCase("i")).collect(Collectors.toList());
//        Random random = new Random();
//        random.nextInt(10);
//        random.ints().limit(10).map(i -> i*i).sorted().forEach(s->{
//            log.info(String
//                    .valueOf(s));
//        });
//        strings.parallelStream().forEach(log::info);
//        log.info(list.toString());
////        sysLogsService.deleteLogs();
//    }
//
//
//    @Test
//    public void test165(){
//        Lw_familyPayMentDTO dto = new Lw_familyPayMentDTO();
//        int aka078 = 60;
//        switch(Integer.valueOf(aka078)){
//            case 10: dto.setBaz105("10");dto.setBaz106("普通门诊收费"); break;
//            case 50: dto.setBaz105("12");dto.setBaz106("特殊门诊收费"); break;
//            case 60: dto.setBaz105("80");dto.setBaz106("健康体检"); break;
//            default : dto.setBaz105("99");dto.setBaz106("其他"); break;
//        }
//        log.info(dto.getBaz105());
//        log.info(dto.getBaz106());
//    }


    @Test
    public void yesy1(){
        try{
            test7();
        }catch(Exception e){
            System.out.println("在这里解决");
            e.printStackTrace();
        }
    }


    public void test7 (){

        test8();

        int i = 1 / 0;

    }


    public void test8 (){

        List list= new ArrayList();
        list.get(2);
    }
}

