package org.springframework.samples.petclinic.owner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owners")
public class OwnerRestController {

    private final OwnerService ownerService;

    public OwnerRestController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/count")
    public long getOwnerCount() {
        return ownerService.getOwnerCount();
    }
}
