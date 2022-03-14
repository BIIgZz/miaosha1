package com.geekq.miaosha.controller;

import com.geekq.miaosha.common.resultbean.ResultGeekQ;
import com.geekq.miaosha.service.MiaoShaUserService;
import com.geekq.miaosha.service.MiaoshaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Random;

import static com.geekq.miaosha.common.enums.ResultStatus.CODE_FAIL;
import static com.geekq.miaosha.common.enums.ResultStatus.RESIGETER_FAIL;

@Controller
//@RequestMapping("/user")
public class RegisterController {

    private static Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private MiaoShaUserService miaoShaUserService;
    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private HttpServletRequest httpServletRequest;
    @RequestMapping("/do_register")
    public String registerIndex() {
        return "register";
    }

    /**
     * 注册网站
     *
     * @param userName
     * @param passWord
     * @param salt
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public ResultGeekQ<String> register(@RequestParam("username") String userName,
                                        @RequestParam("password") String passWord,
                                        @RequestParam("verificationCode") String verifyCode,
                                        @RequestParam("salt") String salt,
                                        @RequestParam("tel") String tel,
                                        @RequestParam("realName") String realName,
                                        @RequestParam("idNum") String idNum,
                                        HttpServletResponse response) {
        ResultGeekQ<String> result = ResultGeekQ.build();
        //前端这里得传个盐过来
        /**
         * 校验验证码
         */
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(tel);
        if(!com.alibaba.druid.util.StringUtils.equals(verifyCode,inSessionOtpCode)){
            result.withError(CODE_FAIL.getCode(), CODE_FAIL.getMessage());
            return result;
        }
//        boolean check = miaoshaService.checkVerifyCodeRegister(Integer.valueOf(verifyCode));
//        if (!check) {
//            result.withError(CODE_FAIL.getCode(), CODE_FAIL.getMessage());
//            return result;
//        }
        boolean registerInfo = miaoShaUserService.register(response, userName, passWord,salt,tel,realName,idNum);
        if (!registerInfo) {
            result.withError(RESIGETER_FAIL.getCode(), RESIGETER_FAIL.getMessage());
            return result;
        }
        return result;
    }
    //用户获取otp短信接口
    @RequestMapping(value = "/getVerificationCode",method = {RequestMethod.POST})
    @ResponseBody
    public ResultGeekQ<String> getOtp(@RequestParam(name="tel")String telphone){
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        ResultGeekQ<String> result = ResultGeekQ.build();
        int randomInt =  random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        //将OTP验证码同对应用户的手机号关联，使用httpsession的方式绑定他的手机号与OTPCODE
        httpServletRequest.getSession().setAttribute(telphone,otpCode);


        //将OTP验证码通过短信通道发送给用户,省略
        System.out.println("telphone = " + telphone + " & otpCode = "+otpCode);

        return result;
    }
}
