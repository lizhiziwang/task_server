package com.zsh.task.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.TokenSign;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsh.task.cache.EmailCodeCache;
import com.zsh.task.cache.UserCache;
import com.zsh.task.common.LoginUserThreatContext;
import com.zsh.task.common.Result;
import com.zsh.task.config.ThreadPoolConfig;
import com.zsh.task.entity.*;
import com.zsh.task.handler.EMailClient;
import com.zsh.task.service.*;
import com.zsh.task.utils.HttpRequestUtils;
import com.zsh.task.vo.UnreadVo;
import com.zsh.task.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService us;
    @Resource
    UserCache uc;
    @Resource
    FriendService fs;
    @Resource
    PasswordEncoder encoder;
    @Resource
    MessageService ms;
    @Resource
    UserIdentityService uis;
    @Resource
    FriendRequestService frs;
    @Resource
    ThreadPoolConfig tpc;
    @Value("${game.gaoDe.key}")
    String gaodeKey;

//    final String template = "亲爱的先生/女士：\n  您好！\n 您的登录验证码是：__CODE__，请在5分钟内进行验证。如果验证码不为您本人申请，请忽略本邮件。";
    final String template = "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "\n" +
        "<head>\n" +
        "  <meta charset=\"UTF-8\">\n" +
        "  <title>邮件内容</title>\n" +
        "  <style>\n" +
        "    body {\n" +
        "      font-family: Arial, sans-serif;\n" +
        "      background-color: #f8f8f8;\n" +
        "      margin: 0;\n" +
        "      padding: 0;\n" +
        "    }\n" +
        "\n" +
        "  .email-wrapper {\n" +
        "      width: 100%;\n" +
        "      margin: 0 auto;\n" +
        "      background-color: #fff;\n" +
        "      border-radius: 5px;\n" +
        "      padding: 20px;\n" +
        "    }\n" +
        "    p {\n" +
        "      color: #666;\n" +
        "      line-height: 1.5;\n" +
        "      text-align: justify;\n" +
        "    }\n" +
        "\n" +
        "    a {\n" +
        "      color: #007bff;\n" +
        "      text-decoration: none;\n" +
        "    }\n" +
        "\n" +
        "  .footer {\n" +
        "      margin-top: 20px;\n" +
        "      text-align: center;\n" +
        "      color: #999;\n" +
        "      font-size: 14px;\n" +
        "    }\n" +
        "  </style>\n" +
        "</head>\n" +
        "\n" +
        "<body>\n" +
        "  <div class=\"email-wrapper\">\n" +
        "    <p>亲爱的用户：</p>\n" +
        "    <p style=\"text-indent: 2em;\">您好！</p>\n" +
        "    <p style=\"text-indent: 2em;\">您的登录验证码是：__CODE__，请在5分钟内进行验证。如果验证码不为您本人申请，请忽略本邮件。</p>\n" +
        "  </div>\n" +
        "</body>\n" +
        "\n" +
        "</html>";

    @Resource
    EMailClient email;

    @Resource
    EmailCodeCache ecc;

    @GetMapping("/login")
    public Result<Map<String, Object>> doLogin(@RequestParam(name = "userName")@NotBlank String userName,
                          @RequestParam(name = "password")@NotBlank String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
//        User loginUser = us.getByName(userName);
//        if(loginUser == null){
//            return Result.failed("用户不存在！");
//        }
//        String realPwd = loginUser.getPwd();
//        if (realPwd.equals(password)){
//            StpUtil.login(loginUser.getId());
//            String token = StpUtil.getTokenValue();
//            JSONObject jo = new JSONObject();
//            jo.put("user",loginUser);
//            jo.put("token",token);
//            //设置为在线状态
//            loginUser.setIsOnline(1);
//            us.saveOrUpdate(loginUser);
//            //添加用户登录信息缓存
//            uc.put(token,loginUser);
//            return Result.succeed(jo.toJSONString());
//        }else {
//            return Result.failed("登录失败！");
//        }
        Map<String, Object> re = us.doLogin(userName, password);
        if (re == null){
            return Result.failed("账号或密码错误！");
        }
        return Result.succeed(re);
    }

    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody User user){
        String userName = user.getName();
        if (StringUtils.isBlank(userName)) {
            return Result.failed("用户名不能为空！");
        }
        User byName = us.getByName(userName);
        if (byName != null) {
            return Result.failed("用户名'"+userName+"'已存在，请重新输入！");
        }
//        User re = new User();
        user.setIsOnline(0);
        user.setId(IdUtil.getSnowflakeNextId());//雪花算法生成唯一ID
        user.setPurse(0.0);
//        user.setName(user.getName());
        user.setPwd(encoder.encode(user.getPwd()));
        user.setCreateTime(new Date())
                .setUpdateTime(new Date());
        return Result.succeed(us.save(user));
    }
    // 编辑用户
    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateUser(@RequestBody User user){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.ne("id",user.getId())
                .eq("name",user.getName());

        if (us.getOne(qw)!=null) {
            return Result.failed("该用户名已存在！");
        }
        user.setUpdateTime(new Date());
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(user.getPwd())) {
            user.setPwd(encoder.encode(user.getPwd()));
            //更改密码之后需要重新登录
            StpUtil.logoutByLoginId(user.getId());
        }
        //更新角色
        if (user.getIdentity() != null){
            uis.updateByUserId(user.getIdentity(), user.getId());
        }
        //地理逆编码
        if(user.getLon()!= null&&user.getLat()!=null){
            String url = "https://restapi.amap.com/v3/geocode/regeo"
                    +"?key="+gaodeKey
                    +"&location="+user.getLon()+","+user.getLat();
            JSONObject jo = HttpRequestUtils.get(url);
            String cityCode = jo.getJSONObject("regeocode").getJSONObject("addressComponent").getString("citycode");
            String cityName = jo.getJSONObject("regeocode").getJSONObject("addressComponent").getString("city");
            user.setCityCode(cityCode)
                    .setCityName(cityName)
                    .setLocation(jo.getJSONObject("regeocode").getString("formatted_address"));
        }
        return Result.succeed(us.updateByPrimaryKeySelective(user));
    }
    //更新收货信息
    @PostMapping("/update2")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateUser2(@RequestBody User user){

        user.setUpdateTime(new Date());

        return Result.succeed(us.updateByPrimaryKeySelective(user));
    }
    // 删除用户
    @PostMapping("/rem/{id}")
    public Result<Boolean> removeUser(@PathVariable Long id){
        return Result.succeed(us.removeById(id));
    }
    //用户下线,退出登录
    @GetMapping("/down/{id}")
    public Result<Boolean> downLine(@PathVariable Long id){
        return Result.succeed(us.downLine(id));
    }
    //用户列表查询
    @PostMapping("/search")
    public Result<Page<User>> searchUsers(@RequestBody UserVo vo,
                                          @RequestParam(name = "size")Long size,
                                          @RequestParam(name = "current") Long current){

        Page<User> userPage = us.searchUsers(vo, size, current);
        userPage.getRecords().forEach(e->{
            List<TokenSign> var = StpUtil.getSessionByLoginId(e.getId()).getTokenSignList();
            e.setIsOnline(var.size()>0?1:0)
                    .setStatus(e.getIsOnline()==0?"离线":"在线");
        });
        return Result.succeed(userPage);
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin

    /**
     * @param name 查询用户名,传null时会查询当前用户的所有好友
     * @param userId 当前用户ID--------------获取当前user的所有friend
     * */
    @GetMapping("/friends/{userId}")
    public Result<List<User>> getAllFriend(@PathVariable Long userId,
                                           @RequestParam(name = "name") String name){
        List<User> allFriend = fs.getAllFriend(userId, name);
        //添加未读数
//        List<UnreadVo> vos = ms.selectUnread(userId);
//        for(User user:allFriend){
//            for (UnreadVo vo:vos){
//                if (vo.getSendUser().equals(user.getId())) {
//                    user.setMesCount(vo.getCount());
//                    break;
//                }
//            }
//        }

        return Result.succeed(allFriend);
    }
    // 验证admin
    @GetMapping("/toAdmin/{id}")
    public Result<Boolean> toAdmin(@PathVariable Long id){
        QueryWrapper<UserIdentity> qw = new QueryWrapper<>();

        qw.eq("user_id",id);
        UserIdentity identity = uis.getOne(qw);

        if (identity!=null && identity.getIdentity() == 0) {
            return Result.succeed(true);
        }
        return Result.failed("权限不足，请联系管理员！");
    }
    // 添加申请
    @PostMapping("/fri/{applicant}")
    public Result<Boolean> addRequest(@PathVariable Long applicant,
                                      @RequestParam Long receiver,
                                      @RequestParam(name = "mes") String mes){
        QueryWrapper<FriendRequest> qw = new QueryWrapper<>();
        qw.eq("applicant",applicant)
                .eq("receiver",receiver);
        FriendRequest one = frs.getOne(qw);
        if (one!= null){
            return Result.succeed(false,"已申请，请等待对方同意！");
        }

        return Result.succeed(frs.addRequest(applicant,receiver,mes));
    }

    //同意申请,发生异常回滚
    @PostMapping("/fri/req/{applicant}/{dif}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> agreeRequest(@PathVariable Long applicant, @PathVariable Integer dif){
        Long currentUserId = LoginUserThreatContext.getUser().getId();

        UpdateWrapper<FriendRequest> uw = new UpdateWrapper<>();
        //拒绝
        if(dif == 0){
            uw.set("is_agree",0).
                    set("update_time",new Date());
            uw.eq("applicant",applicant)
                    .eq("receiver",currentUserId);
            return Result.succeed(frs.update(uw));
        }
        //同意
        uw.set("is_agree",1).
                set("update_time",new Date());
        uw.eq("applicant",applicant)
                        .eq("receiver",currentUserId);

        frs.update(uw);

        //添加好友关系
        Friend f = new Friend();
        f.setId(IdUtil.getSnowflakeNextId())
                .setUser1Id(applicant)
                .setUser2Id(currentUserId)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        fs.save(f);

        return Result.succeed(true);
    }

    @GetMapping("/get/fre")
    public Result<?> getFriendRe(){
        Long userId = LoginUserThreatContext.getUser().getId();
        return Result.succeed(frs.getNoAgreeRequest(userId));
    }

    //
    @GetMapping("/ava/{id}")
    public Result<User> getava(@PathVariable Long id){
        User byId = us.getById(id);
        return Result.succeed(byId);
    }
    @GetMapping("/current")
    public Result<User> current(){
        Long id = LoginUserThreatContext.getUser().getId();
        User byId = us.getById(id);
        return Result.succeed(byId);
    }
    //all user info
    @GetMapping("/aui")
    public Result<List<?>> getUserInfo(){

        QueryWrapper wrapper = new QueryWrapper();

        wrapper.select("name,id");

        List<Map<String,Object>> list = us.listMaps(wrapper);
        return Result.succeed(list);
    }
    @GetMapping("/sendEmail")
    public Result<String> sendMail(@RequestParam(name = "target") String target){
        SecureRandom secureRandom = new SecureRandom();
        //生成6位验证码
        int randomSixDigits = secureRandom.nextInt(900000) + 100000;
        String var = template.replaceAll("__CODE__", String.valueOf(randomSixDigits));
        ecc.putCode(target, String.valueOf(randomSixDigits), (long) (60*5*1000));

        email.sendMail(target,"JY店铺邮箱验证码",var);

        return Result.succeed("已发送！");
    }

    // email code login
    @GetMapping("/email")
    public Result<Map<String, Object>> mailLogin(@RequestParam(name = "email",required = true) String email,
                                  @RequestParam(name = "code",required = true) String code){

        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("email",email);

        User one = us.getOne(qw);

        if(one == null){
            return Result.failed("用户邮箱不存在！");
        }

        String s = ecc.getCode(email);

        if(org.apache.commons.lang3.StringUtils.isNoneBlank(code) && code.equals(s)){
            StpUtil.setLoginId(one.getId());
            Map<String,Object> m = new HashMap<>();
            m.put("token",StpUtil.getTokenValue());
            m.put("user",one);
            ecc.delCache(email);
            //缓存用户登录信息
            uc.put(String.valueOf(one.getId()),new LoginUser(one));

            one.setIsOnline(1);
            us.updateByPrimaryKeySelective(one);
            return Result.succeed(m);
        }else {
            //验证码错误，移除缓存，重新发送验证码
            ecc.delCache(email);
            return Result.failed("验证码错误！");
        }
    }

    @PostConstruct
    public void userState(){
//        System.out.println("***********"+StpUtil.getLoginDevice());
        Runnable task = ()->{
            while (true){
                List<User> users = us.list();
                List<User> update = new ArrayList<>();
                users.forEach(e->{
                    if(e.getId() == 0L){
                        return;
                    }
                    String var = StpUtil.getTokenValueByLoginId(e.getId());
                    int var2 = org.apache.commons.lang3.StringUtils.isNoneBlank(var)?1:0;
                    if(e.getIsOnline() != var2){
                        e.setIsOnline(var2).setUpdateTime(new Date());
                        update.add(e);
                    }
                });
                if(update.size()>0){
                    log.info("监测用户状态更新");
                    us.updateBatchById(users);
                }
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        };

        tpc.poolExecutor().execute(task);
    }
}
