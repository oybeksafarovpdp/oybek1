package oybek.pdp.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import oybek.pdp.demo.entity.Attachment;
import oybek.pdp.demo.entity.AttachmentContent;
import oybek.pdp.demo.repository.AttachentContentRepository;
import oybek.pdp.demo.repository.AttachentRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    AttachentRepository attachentRepository;

    @Autowired
    AttachentContentRepository attachentContentRepository;


    private static final String yol = "oybekkk";
    // File System saqlash
    @PostMapping("/upload")
    public String uploadToFileSystem(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        if (file != null) {
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();
            Attachment attachment = new Attachment();
            attachment.setSize(size);
            attachment.setFileOriginalName(originalFilename);
            attachment.setContentType(contentType);

            String[] split = originalFilename.split("\\.");
            String s = split[split.length - 1];
            String name = UUID.randomUUID().toString() + "." + s;
            attachment.setName(name);
            attachentRepository.save(attachment);

            Path path = Paths.get(yol + "/" + name);
            Files.copy(file.getInputStream(), path);

            return "Qo'shildi. ID si - " + attachment.getId();
        }
        return "Saqlanmadi!";
    }

    // File Systemdan ma'lumotni olish
    @GetMapping("/download/{id}")
    public void getFileFromSystem(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Optional<Attachment> byId = attachentRepository.findById(id);
        if (byId.isPresent()) {
            Attachment attachment = byId.get();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getFileOriginalName() + "\"");
            response.setContentType(attachment.getContentType());
            FileInputStream fileInputStream = new FileInputStream(yol + "/" + attachment.getName());
            FileCopyUtils.copy(fileInputStream, response.getOutputStream());


        }

    }
    // Database dan attachementlarni olish
    @GetMapping("/attachment")
    public List<Attachment> getFile(HttpServletResponse response) throws IOException {
        List<Attachment> all = attachentRepository.findAll();
        return all;
    }



/*

    // Databasega Faylni saqlash
    @PostMapping("/upload")
    public String addAttachment(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        if (file != null) {
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();
            Attachment attachment = new Attachment();
            attachment.setSize(size);
            attachment.setFileOriginalName(originalFilename);
            attachment.setContentType(contentType);
            Attachment saveAttachment = attachentRepository.save(attachment);

            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setAsosiyContent(file.getBytes());
            attachmentContent.setAttachment(saveAttachment);

            attachentContentRepository.save(attachmentContent);

            return "File saqlandi. ID si - " + saveAttachment.getId();
        } else {
            return "File yo'q";
        }
    }



    // Database dan attachementni id si bo'yicha olish
    @GetMapping("/info/{id}")
    public Attachment getFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Optional<Attachment> byId = attachentRepository.findById(id);
        if (byId.isPresent()) {
            Attachment attachment = byId.get();
            return attachment;
        } else return null;
    }

    // Database dan attachement Contentni id si bo'yicha olish
    @GetMapping("/download/{id}")
    public void getFileByContent(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Optional<Attachment> byId = attachentRepository.findById(id);
        if (byId.isPresent()) {
            Attachment attachment = byId.get();
            Optional<AttachmentContent> byAttachment_id = attachentContentRepository.findByAttachment_Id(id);
            if (byAttachment_id.isPresent()) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getFileOriginalName() + "\"");
                response.setContentType(attachment.getContentType());
                AttachmentContent attachmentContent = byAttachment_id.get();
                FileCopyUtils.copy(attachmentContent.getAsosiyContent(), response.getOutputStream());
            }
        }
    }
*/


}
