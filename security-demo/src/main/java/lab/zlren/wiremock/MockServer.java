package lab.zlren.wiremock;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * @author zlren
 * @date 17/10/14
 */
@Slf4j
public class MockServer {

    // public static void main(String[] args) throws IOException {
    //     configureFor("localhost", 8062);
    //     removeAllMappings();
    //
    //     mock("/order/01", "01");
    //     mock("/order/02", "02");
    // }

    private static void mock(String url, String fileName) {

        ClassPathResource classPathResource = new ClassPathResource("mock/response/" + fileName + ".txt");
        String content = null;
        try {
            content = FileUtils.readFileToString(classPathResource.getFile(), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info(content);

        stubFor(
                get(urlPathEqualTo(url))
                        .willReturn(
                                aResponse()
                                        .withBody(content)
                                        .withStatus(200)
                        ));
    }

}
