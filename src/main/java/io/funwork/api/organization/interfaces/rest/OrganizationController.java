package io.funwork.api.organization.interfaces.rest;

import io.funwork.api.organization.domain.support.dto.OrganizationTreeDto;
import io.funwork.api.organization.application.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @RequestMapping("/tree/{id}")
    public ResponseEntity tree(@PathVariable Long id) {
        OrganizationTreeDto tree = organizationService.getTreeByPerson(id);
        if( tree != null) {
            return new ResponseEntity<>(tree, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
