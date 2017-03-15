package com.ciazhar.resource.server.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ResourceServerController {
	@RequestMapping("/halo")
    public void halo(Model m, @RequestParam(required = false) String nama){
        m.addAttribute("waktu", new Date());
        if(nama != null && !nama.isEmpty()){
            m.addAttribute("pesan", "Halo "+nama);
        }
    }

    @CrossOrigin
    @RequestMapping("/api/halo")
    @ResponseBody
    public Map<String, Object> haloApi(@RequestBody(required = false) Map<String, String> input){
        Map<String, Object> data = new HashMap<>();
        data.put("waktu", new Date());

        if(input != null){
            String nama = input.get("nama");
            if(nama != null && !nama.isEmpty()){
                data.put("pesan", "Halo "+nama);
            }
        }
        return data;
    }

    @CrossOrigin
    @RequestMapping("/api/waktu")
    @ResponseBody
    public String waktu(){
			 return "{\"waktu\":\""+new Date().toString()+"\"}";
		}
}
