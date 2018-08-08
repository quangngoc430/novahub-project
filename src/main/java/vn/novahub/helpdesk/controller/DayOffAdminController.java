package vn.novahub.helpdesk.controller;

import org.apache.http.impl.execchain.RequestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.enums.DayOffStatus;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.service.DayOffService;

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api/admin/day-offs", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffAdminController {

    @Autowired
    private DayOffService dayOffService;

    @PermitAll
    @GetMapping("/{id}/answer-token")
    public ResponseEntity<DayOff> answerWithToken(@PathVariable("id") long dayOffId,
                                          @RequestParam("status") String status,
                                          @RequestParam(name = "token") String token)
                                             throws DayOffIsAnsweredException,
                                                    DayOffTokenIsNotMatchException,
                                                    DayOffIsNotExistException,
                                                    MessagingException,
                                                    AccountNotFoundException,
                                                    IOException {
        DayOff dayOff;

        switch (DayOffStatus.valueOf(status)) {
            case APPROVED:
                dayOff = dayOffService.approve(dayOffId, token);
                break;
            case DENIED:
                dayOff = dayOffService.deny(dayOffId, token);
               break;
            default:
                throw new RequestAbortedException("The parameter \'status\' is not valid");
        }
        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/answer")
    public ResponseEntity<DayOff> answerWithoutToken(@PathVariable("id") long dayOffId,
                                                  @RequestParam("status") String status)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            DayOffIsNotExistException,
            MessagingException,
            AccountNotFoundException,
            DayOffOverdueException,
            IOException {
        DayOff dayOff;

        switch (DayOffStatus.valueOf(status)) {
            case APPROVED:
                dayOff = dayOffService.approve(dayOffId);
                break;
            case DENIED:
                dayOff = dayOffService.deny(dayOffId);
                break;
            case CANCELLED:
                dayOff = dayOffService.cancel(dayOffId);
                break;
            default:
                throw new RequestAbortedException("The parameter \'status\' is not valid");
        }
        return new ResponseEntity<>(dayOff, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<DayOff>> getAllDayOffs(
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name= "keyword", required = false, defaultValue = "") String keyword,
            Pageable pageable) {
        return new ResponseEntity<>(
                dayOffService.getAllByStatusAndKeyword(status, keyword ,pageable),
                HttpStatus.OK);
    }

}
