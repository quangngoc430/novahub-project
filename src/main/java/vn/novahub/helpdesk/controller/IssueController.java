package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.service.*;

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class IssueController {

    @Autowired
    private AdminIssueService adminIssueService;

    @Autowired
    private AccountIssueService accountIssueService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Issue>> getAllByAdmin(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                                     @RequestParam(name = "status", required = false, defaultValue = "") String status,
                                                     Pageable pageable){
        Page<Issue> issuePage = adminIssueService.getAllByKeywordAndStatus(keyword, status, pageable);

        return new ResponseEntity<>(issuePage, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> findOneByAdmin(@PathVariable(name = "issueId") long issueId) throws IssueNotFoundException {
        return new ResponseEntity<>(adminIssueService.findOne(issueId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> updateForAdmin(@PathVariable(name = "issueId") long issueId,
                                                @RequestBody Issue issue) throws IssueValidationException, IssueNotFoundException, MessagingException {
        Issue issueUpdated = adminIssueService.update(issueId, issue);

        return new ResponseEntity<>(issueUpdated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteByAdmin(@PathVariable(name = "issueId") long issueId) throws IssueNotFoundException {
        adminIssueService.delete(issueId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/me/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Issue>> getAll(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                              @RequestParam(name = "status", required = false, defaultValue = "") String status,
                                              Pageable pageable){
        Page<Issue> issuePage = accountIssueService.getAllByKeywordAndStatus(keyword, status, pageable);

        return new ResponseEntity<>(issuePage, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/me/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> findOne(@PathVariable("issueId") long issueId) throws IssueNotFoundException {
        return new ResponseEntity<>(accountIssueService.findOne(issueId), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/users/me/issues", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> create(@RequestBody Issue issue) throws IssueValidationException, MessagingException {
        issue = accountIssueService.create(issue);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/users/me/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Issue> update(HttpServletRequest request,
                                        @RequestBody Issue issue,
                                        @PathVariable(name = "issueId") long issueId) throws IssueNotFoundException, IssueValidationException, MessagingException {
        issue = accountIssueService.update(issueId, issue);

        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "/users/me/issues/{issueId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delete(@PathVariable(name = "issueId") long issueId) throws IssueNotFoundException {
        accountIssueService.delete(issueId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PermitAll
    @GetMapping(path = "/issues/{issueId}/approve", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> approve(@RequestParam(name = "token", required = false, defaultValue = "") String token,
                                        @PathVariable(name = "issueId") long issueId) throws IssueNotFoundException, IssueIsClosedException, MessagingException {
        adminIssueService.approve(issueId, token);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PermitAll
    @GetMapping(path = "/issues/{issueId}/deny", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deny(@RequestParam(name = "token", required = false, defaultValue = "") String token,
                                     @PathVariable(name = "issueId") long issueId) throws IssueNotFoundException, IssueIsClosedException, MessagingException {
        adminIssueService.deny(issueId, token);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
