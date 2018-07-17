package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.DayOffIsAnsweredException;
import vn.novahub.helpdesk.exception.DayOffOverdueException;
import vn.novahub.helpdesk.exception.DayOffTokenIsNotMatchException;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotValidException;
import vn.novahub.helpdesk.model.DayOff;

import javax.mail.MessagingException;

public interface DayOffService {

    DayOff add(DayOff dayOff)
            throws MessagingException,
            DayOffTypeIsNotValidException;

    Page<DayOff> getAll(Pageable pageable);

    void delete(DayOff dayOff)
            throws MessagingException,
                   DayOffOverdueException;

    void approve(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            MessagingException;

    void deny(long dayOffId, String token)
            throws DayOffIsAnsweredException,
            DayOffTokenIsNotMatchException,
            MessagingException;
}
