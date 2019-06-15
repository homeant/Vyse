package fun.vyse.cloud.design.web;

import fun.vyse.cloud.design.entity.Model;
import fun.vyse.cloud.design.service.IModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("model")
public class ModelController {

    @Autowired
    private IModeService modeService;

    @GetMapping("{name}")
    public Model model(@PathVariable("name") String name){
        return modeService.queryModel(name);
    }
}
