package com.rvrjc.colorido.controller;

import com.rvrjc.colorido.entity.ContactInfo;
import com.rvrjc.colorido.repository.ContactInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    @GetMapping
    public List<ContactInfo> getContacts(@RequestParam(required = false) String type) {
        if (type != null) {
            return contactInfoRepository.findByTypeOrderByDisplayOrderAsc(type);
        }
        return contactInfoRepository.findAllByOrderByDisplayOrderAsc();
    }

    @PostMapping
    public ResponseEntity<ContactInfo> createContact(@RequestBody ContactInfo contact) {
        ContactInfo saved = contactInfoRepository.save(contact);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactInfo> updateContact(@PathVariable Long id, @RequestBody ContactInfo updated) {
        Optional<ContactInfo> opt = contactInfoRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        ContactInfo c = opt.get();
        if (updated.getName() != null && !updated.getName().trim().isEmpty()) c.setName(updated.getName());
        if (updated.getRole() != null) c.setRole(updated.getRole());
        if (updated.getDepartment() != null) c.setDepartment(updated.getDepartment());
        if (updated.getPhone() != null) c.setPhone(updated.getPhone());
        if (updated.getEmail() != null) c.setEmail(updated.getEmail());
        if (updated.getType() != null) c.setType(updated.getType());
        if (updated.getDisplayOrder() != null) c.setDisplayOrder(updated.getDisplayOrder());

        return ResponseEntity.ok(contactInfoRepository.save(c));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        if (!contactInfoRepository.existsById(id)) return ResponseEntity.notFound().build();
        contactInfoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
