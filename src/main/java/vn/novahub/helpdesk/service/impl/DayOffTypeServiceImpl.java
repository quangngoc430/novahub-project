package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.DayOffTypeExistException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
import vn.novahub.helpdesk.service.DayOffTypeService;

import java.util.List;
import java.util.Optional;

@Service
public class DayOffTypeServiceImpl implements DayOffTypeService {

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

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
        return dayOffTypeRepository.save(dayOffType);
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
    public DayOffType delete(int id) throws DayOffTypeNotFoundException {

        Optional<DayOffType> currentDayOffType
                = dayOffTypeRepository.findById(id);

        if (!currentDayOffType.isPresent()) {
            throw new DayOffTypeNotFoundException();
        }

        dayOffTypeRepository.delete(currentDayOffType.get());

        return currentDayOffType.get();
    }
}
