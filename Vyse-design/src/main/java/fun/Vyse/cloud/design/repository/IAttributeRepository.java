package fun.vyse.cloud.design.repository;

import fun.vyse.cloud.design.entity.AttributeMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAttributeRepository extends JpaRepository<AttributeMeta,Long> {

}
