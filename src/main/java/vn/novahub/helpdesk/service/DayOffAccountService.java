package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.DayOffAccountNotFoundException;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotExistException;
import vn.novahub.helpdesk.exception.DayOffAccountIsExistException;
import vn.novahub.helpdesk.model.DayOffAccount;

public interface DayOffAccountService {

    DayOffAccount add(DayOffAccount dayOffAccount)
            throws DayOffAccountIsExistException, DayOffTypeIsNotExistException;

    Page<DayOffAccount> findByAccountId(long accountId, Pageable pageable);

    Page<DayOffAccount> getAll(Pageable pageable);

    DayOffAccount update(DayOffAccount dayOffAccount) throws DayOffAccountNotFoundException;

}
