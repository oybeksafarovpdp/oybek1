package oybek.pdp.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oybek.pdp.demo.entity.Attachment;
import oybek.pdp.demo.entity.AttachmentContent;

import java.util.Optional;

public interface AttachentContentRepository extends JpaRepository<AttachmentContent,Integer> {

    Optional<AttachmentContent> findByAttachment_Id(Integer attachment_id);

}
