package lab.zlren.security.core.validate.code.image;

import lab.zlren.security.core.properties.SecurityProperties;
import lab.zlren.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码生成器的默认实现
 * 这里不要加@Component，而要以@Bean的形式注入，结合@conditionmissingbean就可以支持覆盖
 *
 * @author zlren
 * @date 17/10/15
 */
public class DefaultImageCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 生成一张验证码图片并将正确值一并返回
     *
     * @param servletWebRequest
     * @return
     */
    @Override
    public ImageCode create(ServletWebRequest servletWebRequest) {

        // 从请求中取值，取不到就从配置中去取（用户配置优于默认配置）
        int width = ServletRequestUtils.getIntParameter(
                servletWebRequest.getRequest(),
                "width",
                securityProperties.getCode().getImage().getWidth());

        int height = ServletRequestUtils.getIntParameter(
                servletWebRequest.getRequest(),
                "height",
                securityProperties.getCode().getImage().getHeight());

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));

        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < securityProperties.getCode().getImage().getLength(); i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }

        g.dispose();

        return new ImageCode(image, sRand.toString(), securityProperties.getCode().getImage().getExpiredIn());
    }

    /**
     * 得到一个随机的颜色
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {

        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);

        return new Color(r, g, b);
    }
}
