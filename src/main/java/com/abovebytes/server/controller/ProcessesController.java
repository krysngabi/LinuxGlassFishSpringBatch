package com.abovebytes.server.controller;

import com.abovebytes.server.utils.ShellCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping(path = "/processes")
@CrossOrigin
public class ProcessesController {
    private static int DEFAULT_BUFFER_SIZE = 8192;
    private static final Logger LOG = LoggerFactory.getLogger(ProcessesController.class);

    @GetMapping("/file")
    public ResponseEntity<byte[]> getFile(@RequestParam(name = "filename") String filename, @RequestParam(name = "servername") String servername, @RequestParam(name = "app") String app) throws IOException {
        String filePath = "";
        if (servername.equals("zedpay")) {
            if (app.equals("glassfish")) {
                filePath = "/usr/glassfish4/glassfish/domains/domain1/logs/server.log";
            } else if (app.equals("api")) {
                filePath = "/usr/logs/logx.file";
            }
        } else if (servername.equals("abovebytes")) {
            if (app.equals("glassfish")) {
                filePath = "/usr/glassfish5/glassfish/domains/domain1/logs/server.log";
            } else if (app.equals("api")) {
                filePath = "/usr/logs/logx.file";
            }
        } else {
            filePath = filename;
        }

        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(bytes);

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    @PostMapping("/glassfish")
    public ResponseEntity<String> restartServer(@RequestParam(name = "servername") String servername) {
        String response = "";

        if (servername.equals("zedpay")) {
            response = ShellCommands.executeBashCommand("cd /usr/glassfish4/glassfish/bin/ ; ./asadmin restart-domain domain1");
        } else if (servername.equals("abovebytes")) {
            response = ShellCommands.executeBashCommand("kill $(lsof -t -i:4848) ;  cd /usr/glassfish5/glassfish/bin/ ; ./asadmin restart-domain domain1");
        } else {
            response = ShellCommands.executeBashCommand("kill $(lsof -t -i:4848) ; cd /Users/user/Documents/WebServers/glassfish4/bin ; ./asadmin restart-domain domain1");
        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
}
