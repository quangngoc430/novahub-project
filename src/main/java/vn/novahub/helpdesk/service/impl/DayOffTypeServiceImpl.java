package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeExistException;
import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.model.DayOffAccount;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.DayOffAccountRepository;
import vn.novahub.helpdesk.repository.DayOffRepository;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
import vn.novahub.helpdesk.service.DayOffTypeService;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class DayOffTypeServiceImpl implements DayOffTypeService {

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    private DayOffRepository dayOffRepository;

    @Autowired
    private DayOffAccountRepository dayOffAccountRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<DayOffType> getAllDayOffType() {
        return dayOffTypeRepository.findAll();
    }

    @Override
    public DayOffType getById(int id) throws DayOffTypeNotFoundException {
        Optional<DayOffType> dayOffTypeOptional = dayOffTypeRepository.findById(id);

        if (!dayOffTypeOptional.isPresent()) {
            throw new DayOffTypeNotFoundException();
        }
        return dayOffTypeOptional.get();
    }

    @Override
    public DayOffType create(DayOffType dayOffType) throws DayOffTypeExistException {
        DayOffType existingDayOffType = dayOffTypeRepository.findByType(dayOffType.getType());
        if (existingDayOffType != null) {
            throw new DayOffTypeExistException();
        }

        existingDayOffType = dayOffTypeRepository.save(dayOffType);

        generateDayOffAccountList(existingDayOffType);

        return existingDayOffType;
    }

    @Override
    public DayOffType update(DayOffType dayOffType)
                                   throws DayOffTypeNotFoundException {

        Optional<DayOffType> currentDayOffType
                = dayOffTypeRepository.findById(dayOffType.getId());

        if (!currentDayOffType.isPresent()) {
            throw new DayOffTypeNotFoundException();
        }

        if (dayOffType.getType() != null) {
            currentDayOffType.get().setType(dayOffType.getType());
        }

        if (dayOffType.getDefaultQuota() > 0) {
            currentDayOffType.get().setDefaultQuota(dayOffType.getDefaultQuota());
        }

        return dayOffTypeRepository.save(currentDayOffType.get());
    }

    @Override
    public DayOffType delete(int id) throws DayOffTypeNotFoundException, DayOffTypeExistException {

        Optional<DayOffType> currentDayOffType
                = dayOffTypeRepository.findById(id);

        if (!currentDayOffType.isPresent()) {
            throw new DayOffTypeNotFoundException();
        }

        Page<DayOff> dayOffs = dayOffRepository.findByType(
                currentDayOffType.get().getType(), PageRequest.of(0,10));

        if (dayOffs.getTotalElements() != 0) {
            throw new DayOffTypeExistException("This dayOffType can not be deleted because some dayOff reference to it");
        }

        List<DayOffAccount> dayOffAccounts = dayOffAccountRepository.findAllByDayOffTypeId(id);

        dayOffAccountRepository.deleteAll(dayOffAccounts);

        dayOffTypeRepository.delete(currentDayOffType.get());

        return currentDayOffType.get();
    }

    private void generateDayOffAccountList(DayOffType dayOffType) {
        Iterable<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            generateDayOffAccount(account, dayOffType);
        }
    }

    private void generateDayOffAccount(Account account, DayOffType dayOffType) {

        DayOffAccount dayOffAccount = new DayOffAccount();

        dayOffAccount.setDayOffTypeId(dayOffType.getId());
        dayOffAccount.setAccountId(account.getId());
        dayOffAccount.setDayOffType(dayOffType);
        dayOffAccount.setAccount(account);
        dayOffAccount.setPrivateQuota(dayOffType.getDefaultQuota());
        dayOffAccount.setRemainingTime(dayOffAccount.getPrivateQuota());
        dayOffAccount.setYear(Calendar.getInstance().get(Calendar.YEAR));

        dayOffAccountRepository.save(dayOffAccount);
    }
}
