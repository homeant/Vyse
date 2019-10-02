package fun.vyse.cloud.design.service.impl;

import fun.vyse.cloud.design.entity.Model;
import fun.vyse.cloud.design.repository.IModelRepository;
import fun.vyse.cloud.design.service.IModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelServiceImpl implements IModeService {

    @Autowired
    private IModelRepository modelRepository;

    @Override
    public Model queryModel(String name) {
        return modelRepository.findByName(name);
    }
}
