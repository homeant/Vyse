package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.service.impl.BaseServiceImpl;
import fun.vyse.cloud.define.entity.ConnectionEO;
import fun.vyse.cloud.define.repository.IConnectionRepository;
import fun.vyse.cloud.define.service.IConnectionService;
import org.springframework.stereotype.Service;

/**
 * ConnectionServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 13:17
 */
@Service
public class ConnectionServiceImpl extends BaseServiceImpl<ConnectionEO,Long, IConnectionRepository> implements IConnectionService {

}
