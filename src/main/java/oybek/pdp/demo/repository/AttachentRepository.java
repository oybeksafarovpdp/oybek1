package oybek.pdp.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oybek.pdp.demo.entity.Attachment;

public interface AttachentRepository extends JpaRepository<Attachment,Integer> {
}
