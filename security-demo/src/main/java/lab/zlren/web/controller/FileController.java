package lab.zlren.web.controller;

import lab.zlren.dto.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * Created by zlren on 17/10/13.
 */
@RestController
@RequestMapping("file")
@Slf4j
public class FileController {

    @PostMapping
    public FileInfo upload(MultipartFile file) throws IOException {

        log.info(file.getName());
        log.info(file.getOriginalFilename());
        log.info(String.valueOf(file.getSize()));

        String folder = "/Users/zlren/Documents/work_space/security/security-demo/src/main/resources/upload";
        File localFile = new File(folder, new Date().getTime() + ".txt");

        file.transferTo(localFile);

        return new FileInfo(localFile.getAbsolutePath());
    }

    @GetMapping("{id}")
    public void download(@PathVariable String id, HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse) {

        String folder = "/Users/zlren/Documents/work_space/security/security-demo/src/main/resources/upload";

        try (
                // 在try的括号中开启的流会自动关闭
                InputStream inputStream = new FileInputStream(new File(folder, id + ".txt"));
                OutputStream outputStream = httpServletResponse.getOutputStream()
        ) {

            httpServletResponse.setContentType("application/x-download");
            httpServletResponse.addHeader("Content-Disposition", "attachment;filename=test.txt");

            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
