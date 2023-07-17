package com.brvsk.rasphousemate.info;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("api/v1/info")
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;


    @GetMapping(path = "/platform", produces = "application/json")
    public Map<String, String> getPlatformInfo() {
        return infoService.getPlatformInfo();
    }

    @GetMapping(path = "/hardware", produces = "application/json")
    public Map<String, String> getHardwareInfo() {
        return infoService.getHardwareInfo();
    }

    @GetMapping(path = "/memory", produces = "application/json")
    public Map<String, Float> getMemoryInfo() {
        return infoService.getMemoryInfo();
    }


    @GetMapping(path = "/os", produces = "application/json")
    public Map<String, String> getOsInfo() {
        return infoService.getOsInfo();
    }

    @GetMapping(path = "/java", produces = "application/json")
    public Map<String, String> getJavaInfo() {
        return infoService.getJavaInfo();
    }


    @GetMapping(path = "/network", produces = "application/json")
    public Map<String, String> getSystemInfo() {
        return infoService.getSystemInfo();
    }


    @GetMapping(path = "/codec", produces = "application/json")
    public Map<String, Boolean> getCodecInfo() {
        return infoService.getCodecInfo();
    }

    @GetMapping(path = "frequencies", produces = "application/json")
    public Map<String, Long> getClockInfo() {
        return infoService.getClockInfo();
    }
}
