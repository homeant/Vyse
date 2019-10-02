package fun.vyse.cloud.design.repository;

import fun.vyse.cloud.design.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IModelRepository extends JpaRepository<Model,Long> {

    Model findByName(String name);
}
