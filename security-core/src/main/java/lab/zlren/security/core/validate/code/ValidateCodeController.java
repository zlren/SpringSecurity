package lab.zlren.security.core.validate.code;

import lab.zlren.security.core.validate.code.image.ImageCode;
import lab.zlren.security.core.validate.code.sms.SmsCode;
import lab.zlren.security.core.validate.code.sms.SmsCodeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static lab.zlren.security.core.properties.SecurityConstant.SESSION_KEY_IMAGE_CODE;
import static lab.zlren.security.core.properties.SecurityConstant.SESSION_KEY_SMS_CODE;

/**
 * 生成验证码图片并将正确结果保存起来用户后续的校验
 *
 * @author zlren
 * @date 17/10/15
 */
@RestController
@Slf4j
public class ValidateCodeController {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();


    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    @Autowired
    private ValidateCodeGenerator smsCodeGenerator;

    @Autowired
    private SmsCodeSender smsCodeSender;

    /**
     * 请求生成图片验证码
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws IOException
     */
    @GetMapping("/code/image")
    public void createCodeImage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws
            IOException {

        // 生成一张验证码图片
        ImageCode imageCode = (ImageCode) imageCodeGenerator.create(new ServletWebRequest
                (httpServletRequest));
        // 将正确的答案保存在session中
        sessionStrategy.setAttribute(new ServletWebRequest(httpServletRequest), SESSION_KEY_IMAGE_CODE, imageCode);
        // 将图片传给前端
        ImageIO.write(imageCode.getImage(), "JPEG", httpServletResponse.getOutputStream());
    }

    /**
     * 请求生成短信验证码
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws IOException
     */
    @GetMapping("/code/sms")
    public void createCodeSMS(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws
            IOException, ServletRequestBindingException {

        // 生成短信验证码
        SmsCode smsCode = (SmsCode) smsCodeGenerator.create(new ServletWebRequest(httpServletRequest));
        // 将正确的答案保存在session中！！注意session中是正确的验证码
        sessionStrategy.setAttribute(new ServletWebRequest(httpServletRequest), SESSION_KEY_SMS_CODE, smsCode);

        // 调用短信供应商把短信发出去
        String phoneNumber = ServletRequestUtils.getRequiredStringParameter(httpServletRequest, "mobile");
        smsCodeSender.send(phoneNumber, smsCode.getCode());
    }
}
